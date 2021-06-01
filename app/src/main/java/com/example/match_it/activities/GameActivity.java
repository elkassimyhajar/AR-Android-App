package com.example.match_it.activities;

import android.app.Dialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.media.MediaPlayer;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.net.Uri;
import android.os.Bundle;

import android.util.Log;
import android.view.Gravity;
import android.view.ViewManager;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import com.example.myclassroomproject.*;
import com.example.match_it.render.BoardRenderer;
import com.example.match_it.render.ObjectsRenderer;
import com.google.android.material.snackbar.Snackbar;
import com.google.ar.core.Config;
import com.google.ar.core.Pose;
import com.google.ar.core.Session;
import com.google.ar.core.exceptions.CameraNotAvailableException;
import com.google.ar.sceneform.ArSceneView;
import com.google.ar.sceneform.FrameTime;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.assets.RenderableSource;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.ux.ArFragment;

import java.util.Arrays;

public class GameActivity extends AppCompatActivity {
    private ArFragment arFragment;
    private ArSceneView arSceneView;
    private TextView timer;
    private ImageButton pauseButton;
    Snackbar snackbar;

    private String selectedTopic;
    private int level;

    private String[] board_gltf_uris;
    private String[] objects_gltf_uris;
    private int[] board_cells_status;
    private int[] sounds;

    private Pose pose;
    private BoardRenderer boardRenderer;
    private ObjectsRenderer objectsRenderer;

    private ModelRenderable[] boardModels;
    private ModelRenderable[] objectsModels;
    private MediaPlayer mediaPlayer;

    // False once models loading has started
    private boolean shouldLoadModels = true;
    //
    private int nbrObjectsLoaded;
    private int nbrBoardModelsLoaded;
    // True once models have been placed
    private boolean hasPlacedModels = false;
    // True when the game is pause
    private boolean paused = false;
    // True once the game has been ended
    private boolean ended = false;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        if(!checkNetworkAvailability()) {
            Toast toast = Toast.makeText(this, "Check your internet connexion and try again.", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.BOTTOM, 0, 0);
            toast.show();
            finish();
        }

        setContentView(R.layout.activity_game);

        arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.gameArFragment);
        if(arFragment == null)
            return;

        arSceneView = arFragment.getArSceneView();

        // Init
        init();

        // Set a tap listener on the AR fragment to determine on which plane models will be rendered
        arFragment.setOnTapArPlaneListener((hitResult, plane, motionEvent) -> {
            if(shouldLoadModels) {
                shouldLoadModels = false;
                snackbar = Snackbar
                        .make(findViewById(R.id.gameArFragment), "Loading Assets ... Don't move your phone.", Snackbar.LENGTH_INDEFINITE);
                snackbar.show();
                // Load 3d assets
                loadBoardModels();
                loadObjectsModels();
                // Adjust the hit pose
                Pose hitPose = hitResult.getHitPose();
                float[] translation = hitPose.getTranslation();
                float[] rotation = new float[4];
                rotation[0] = 0.0f; rotation[1] = 0.0f; rotation[2] = 0.0f; rotation[3] = 0.0f;
                pose = new Pose(translation, rotation);
                // Set an update listener on the AR Scene
                arSceneView
                        .getScene()
                        .addOnUpdateListener(this::onUpdateFrame);
            }
        });

        // Set a click listener on pause button
        pauseButton = findViewById(R.id.PauseButton);
        pauseButton.setOnClickListener(
                v -> {
                    paused = true;
                    pauseGame();
                }
        );
    }

    private void pauseGame() {
        // Open pause dialog
        Dialog pauseDialog = new Dialog(GameActivity.this);
        pauseDialog.setContentView(R.layout.dialog_pause);
        pauseDialog.setCancelable(false);
        pauseDialog.setCanceledOnTouchOutside(false);
        pauseDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        pauseDialog.show();
        Button continue_game = pauseDialog.findViewById(R.id.continue_button);
        continue_game.setOnClickListener(v -> {
            paused = false;
            pauseDialog.dismiss();
        });
        Button help = pauseDialog.findViewById(R.id.help_button);
        help.setOnClickListener(v -> {
            pauseDialog.dismiss();
            Dialog helpDialog = new Dialog(GameActivity.this);
            helpDialog.setContentView(R.layout.dialog_help);
            helpDialog.show();
            helpDialog.setOnCancelListener(dialog -> paused = false);
        });
        Button restart = pauseDialog.findViewById(R.id.restart_button);
        restart.setOnClickListener(
                v -> {
                    Intent intent = new Intent(GameActivity.this, GameActivity.class);
                    intent.putExtra("topic", selectedTopic);
                    intent.putExtra("level", level);
                    pauseDialog.dismiss();
                    startActivity(intent);
                    pauseDialog.dismiss();
                    finish();
                }
        );
        Button levelsMenu = pauseDialog.findViewById(R.id.levels_menu_button);
        levelsMenu.setOnClickListener(v -> finish());
        Button quit = pauseDialog.findViewById(R.id.quit_game_button);
        quit.setOnClickListener(
                v -> {
                    LevelsActivity.setMustFinish(true);
                    pauseDialog.dismiss();
                    finish();
                }
        );
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (arSceneView == null) {
            return;
        }
        try {
            arSceneView.resume();
            paused = false;
        } catch (CameraNotAvailableException ex) {
            Toast toast = Toast.makeText(this, "Unable to get camera", Toast.LENGTH_LONG);
            toast.setGravity(Gravity.BOTTOM, 0, 0);
            toast.show();
            finish();
        }
    }

    @Override
    public void onPause() {
        super.onPause();
        if (arSceneView != null) {
            arSceneView.pause();
            paused = true;
            if(mediaPlayer!=null)
                mediaPlayer.release();
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (arSceneView != null) {
            arSceneView.destroy();
            ended = true;
            if(mediaPlayer!=null)
                mediaPlayer.release();
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        if(mediaPlayer!=null)
            mediaPlayer.release();
    }

    private boolean checkSuccess() {
        for (int boardPiecesStatus : board_cells_status) {
            if (boardPiecesStatus != 1)
                return false;
        }
        return true;
    }

    private void onTapObject(int tappedObjectIndex) {
        // Get the node that overlaps the tapped node
        Node node = arSceneView.getScene().overlapTest(objectsRenderer.getObjectsNodes()[tappedObjectIndex]);
        if (node != null) {
            // Check if this node is one of the board' nodes
            for (int i = 0; i < boardRenderer.getBoardNodes().length; i++) {
                // The board's cell should be empty <=> status == 0
                if(board_cells_status[i] == 0) {
                    // Calculate the distance between node and the board's cell
                    Vector3 overlapNodeWorldPosition = node.getWorldPosition();
                    Vector3 goalNodeWorldPosition = boardRenderer.getBoardNodes()[i].getWorldPosition();
                    float dx = overlapNodeWorldPosition.x - goalNodeWorldPosition.x;
                    float dy = overlapNodeWorldPosition.y - goalNodeWorldPosition.y;
                    float dz = overlapNodeWorldPosition.z - goalNodeWorldPosition.z;
                    float distMeters = (float) Math.sqrt((dx * dx + dy * dy + dz * dz));
                    // The tapped object overlaps the correct board's cell
                    if (i == tappedObjectIndex && (node == boardRenderer.getBoardNodes()[i] || distMeters <= 0.02 )) {
                        // Change cell's status
                        board_cells_status[i] = 1;
                        // Play object's name sound
                        mediaPlayer.release();
                        mediaPlayer = MediaPlayer.create(this, sounds[i]);
                        mediaPlayer.start();
                        // Put the object at the center of the board's cell
                        objectsRenderer.getObjectsNodes()[i].setWorldPosition(boardRenderer.getBoardNodes()[i].getWorldPosition());
                        // Disable object's translation
                        objectsRenderer.getObjectsNodes()[i].getTranslationController().setEnabled(false);
                        return;
                    }
                    // The tapped object overlaps a wrong board's cell
                    if (i != tappedObjectIndex && ( node == boardRenderer.getBoardNodes()[i] || distMeters <= 0.06 )) {
                        // Play the sound effect
                        mediaPlayer.release();
                        mediaPlayer = MediaPlayer.create(this, R.raw.wrong);
                        mediaPlayer.start();
                        // Put the object at its initial position
                        objectsRenderer.getObjectsNodes()[tappedObjectIndex].setWorldPosition(objectsRenderer.getObjectsInitialPositions()[tappedObjectIndex]);
                        return;
                    }
                }
            }
        }
    }

    private void onUpdateFrame(FrameTime frameTime) {
        Session session = arSceneView.getSession();
        if (session == null) {
            return;
        }
        // Render models if the've been loaded and start the game
        if( !hasPlacedModels && nbrObjectsLoaded == objectsModels.length && nbrBoardModelsLoaded == boardModels.length) {
            Log.d("__FRAME UPDATE__", "putting assets");
            hasPlacedModels = true;
            // Play sound effect
            mediaPlayer = MediaPlayer.create(this, R.raw.playbuttonclick);
            mediaPlayer.start();
            // Remove the snackbar
            snackbar.dismiss();
            // Render the game board
            boardRenderer = new BoardRenderer(arSceneView, boardModels, pose);
            boardRenderer.render();
            // Render the game objects
            objectsRenderer = new ObjectsRenderer(arFragment, objectsModels, pose, boardRenderer.getBOARD_CELL_COUNT());
            objectsRenderer.render();
            // Disable Plane Renderer
            arSceneView.getPlaneRenderer().setEnabled(false);
            // Stop finding planes
            arSceneView.getSession().getConfig().setPlaneFindingMode(Config.PlaneFindingMode.DISABLED);
            // Start the timer
            startTimer();
            // Set a tap listener on every object
            for(int i = 0; i < objectsRenderer.getObjectsNodes().length; i++) {
                int finalI = i;
                objectsRenderer.getObjectsNodes()[i].setOnTapListener(
                        (hitTestResult, motionEvent1) -> onTapObject(finalI)
                );
            }
        }
        if (!ended && !paused && checkSuccess()) {
            ended = true;
            endGame(1);
        }
    }

    private void endGame(int status) {
        // Remove the pause button
        ((ViewManager) pauseButton.getParent()).removeView(pauseButton);
        // Remove the timer text view
        ((ViewManager) timer.getParent()).removeView(timer);
        if(status == 1) {
            // Update the value of last unlocked level
            if(level!=3) {
                SharedPreferences sharedpreferences = getSharedPreferences("matchItPrefs", Context.MODE_PRIVATE);
                sharedpreferences.edit().putInt(selectedTopic+"level", level + 1).apply();
            }
            // Play sound effect
            mediaPlayer.setOnCompletionListener(mp -> {
                mediaPlayer.release();
                mediaPlayer = MediaPlayer.create(GameActivity.this, R.raw.goodjob);
                mediaPlayer.start();
            });
            // Open win dialog
            Dialog winDialog = new Dialog(GameActivity.this);
            winDialog.setContentView(R.layout.dialog_win);
            winDialog.setCancelable(false);
            winDialog.setCanceledOnTouchOutside(false);
            winDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            winDialog.show();
            Button nextLevel = winDialog.findViewById(R.id.nxt_lvl_button);
            nextLevel.setOnClickListener(
                    v -> {
                        if(level != 3) {
                            Intent intent = new Intent(GameActivity.this, GameActivity.class);
                            intent.putExtra("topic", selectedTopic);
                            intent.putExtra("level", level + 1);
                            startActivity(intent);
                        }
                        winDialog.dismiss();
                        LevelsActivity.setMustFinish(true);
                        finish();
                        Intent intent = new Intent(GameActivity.this, LevelsActivity.class);
                        intent.putExtra("topic", selectedTopic);
                        startActivity(intent);
                    }
            );
            Button quit = winDialog.findViewById(R.id.quit_game_button);
            quit.setOnClickListener(
                    v -> {
                        LevelsActivity.setMustFinish(true);
                        finish();
                        winDialog.dismiss();
                    }
            );
        }
        else {
            // Play sound effect
            mediaPlayer.release();
            mediaPlayer = MediaPlayer.create(GameActivity.this, R.raw.timeisup);
            mediaPlayer.start();
            // Open lose dialog
            Dialog loseDialog = new Dialog(GameActivity.this);
            loseDialog.setContentView(R.layout.dialog_lose);
            loseDialog.setCancelable(false);
            loseDialog.setCanceledOnTouchOutside(false);
            loseDialog.getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
            loseDialog.show();
            Button retry = loseDialog.findViewById(R.id.retry_button);
            retry.setOnClickListener(
                    v -> {
                        Intent intent = new Intent(GameActivity.this, GameActivity.class);
                        intent.putExtra("topic", selectedTopic);
                        intent.putExtra("level", level);
                        startActivity(intent);
                        finish();
                        loseDialog.dismiss();
                    }
            );
            Button levelsMenu = loseDialog.findViewById(R.id.levels_menu_button);
            levelsMenu.setOnClickListener(v -> {
                finish();
                loseDialog.dismiss();
            });
            Button quit = loseDialog.findViewById(R.id.quit_game_button);
            quit.setOnClickListener(
                    v -> {
                        LevelsActivity.setMustFinish(true);
                        finish();
                        loseDialog.dismiss();
                    }
            );
        }
    }

    private void startTimer() {
        timer = findViewById(R.id.timerTextView);
        String timerIniTxt = board_cells_status.length * 4 / 60 + ":" + board_cells_status.length * 4 % 60;
        timer.setText(timerIniTxt);
        Thread timerThread = new Thread(() -> {
            int seconds = board_cells_status.length * 4;
            while (!ended && seconds != 0) {
                if (!paused) {
                    try {
                        Thread.sleep(1000);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                    seconds--;
                    int minutesPassed = seconds / 60;
                    int secondsPassed = seconds % 60;
                    String timerTxt = minutesPassed + ":" + secondsPassed;
                    int finalSeconds = seconds;
                    runOnUiThread(() -> {
                        timer.setText(timerTxt);
                        if (finalSeconds <= 5) {
                            timer.setBackgroundColor(Color.RED);
                            if (finalSeconds == 0) {
                                ended = true;
                                endGame(-1);
                            }
                        }
                    });
                }
            }
        });
        timerThread.start();
    }

    public void loadObjectsModels() {
        this.objectsModels = new ModelRenderable[objects_gltf_uris.length];
        for (int i = 0; i < objects_gltf_uris.length; i++) {
            int finalI = i;
            ModelRenderable
                    .builder()
                    .setSource(
                            this,
                            RenderableSource
                                    .builder()
                                    .setSource(this,
                                            Uri.parse(objects_gltf_uris[finalI]),
                                            RenderableSource.SourceType.GLTF2)
                                    .setRecenterMode(RenderableSource.RecenterMode.ROOT)
                                    .build()
                    )
                    .build()
                    .thenAccept(renderable -> {
                        objectsModels[finalI] = renderable;
                        nbrObjectsLoaded++;
                        Log.d("__Loading Objects__", String.valueOf(nbrObjectsLoaded));
                    })
                    .exceptionally(
                            throwable -> {
                                Log.d("__model__", "Unable to load model " + objects_gltf_uris[finalI]);
                                Toast toast = Toast.makeText(
                                        this,
                                        "Unable to load model " + objects_gltf_uris[finalI],
                                        Toast.LENGTH_LONG);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.show();
                                finish();
                                return null;
                            });
        }
    }

    public void loadBoardModels() {
        this.boardModels = new ModelRenderable[board_gltf_uris.length];
        for (int i = 0; i < board_gltf_uris.length; i++) {
            int finalI = i;
            ModelRenderable
                    .builder()
                    .setSource(
                            this,
                            RenderableSource
                                    .builder()
                                    .setSource(this,
                                            Uri.parse(board_gltf_uris[finalI]),
                                            RenderableSource.SourceType.GLTF2)
                                    .setRecenterMode(RenderableSource.RecenterMode.ROOT)
                                    .build()
                    )
                    .build()
                    .thenAccept(renderable -> {
                        boardModels[finalI] = renderable;
                        nbrBoardModelsLoaded++;
                        Log.d("__Loading Board__", String.valueOf(nbrBoardModelsLoaded));
                    })
            .exceptionally(
                            throwable -> {
                                Log.d("__model__", "Unable to load model " + board_gltf_uris[finalI]);
                                Toast toast = Toast.makeText(
                                        this,
                                        "Unable to load model " + board_gltf_uris[finalI],
                                        Toast.LENGTH_LONG);
                                toast.setGravity(Gravity.CENTER, 0, 0);
                                toast.show();
                                finish();
                                return null;
                            });
        }
    }

    private void init() {
        // Get the selected topic and level
        Bundle bundle = getIntent().getExtras();
        selectedTopic = bundle.getString("topic");
        level = bundle.getInt("level");
        Log.d("__TOPIC__ ", selectedTopic);
        Log.d("__LEVEL__", String.valueOf(level));
        switch (selectedTopic) {
            case "alphabet":
                switch (level) {
                    case 1 :
                    case 2 :
                    case 3 :
                    case 0 :
                        board_gltf_uris = new String[]{
                                "https://raw.githubusercontent.com/elkassimyhajar/AR-Android-App/meryem/app/sampledata/A_mold.gltf",
                                "https://raw.githubusercontent.com/elkassimyhajar/AR-Android-App/meryem/app/sampledata/B_mold.gltf",
                                "https://raw.githubusercontent.com/elkassimyhajar/AR-Android-App/meryem/app/sampledata/L_mold.gltf",
                                "https://raw.githubusercontent.com/elkassimyhajar/AR-Android-App/meryem/app/sampledata/M_mold.gltf"
                        };
                        objects_gltf_uris = new String[]{
                                "https://raw.githubusercontent.com/elkassimyhajar/AR-Android-App/meryem/app/sampledata/A.gltf",
                                "https://raw.githubusercontent.com/elkassimyhajar/AR-Android-App/meryem/app/sampledata/B.gltf",
                                "https://raw.githubusercontent.com/elkassimyhajar/AR-Android-App/meryem/app/sampledata/L.gltf",
                                "https://raw.githubusercontent.com/elkassimyhajar/AR-Android-App/meryem/app/sampledata/M.gltf"
                        };
                        board_cells_status = new int[board_gltf_uris.length];
                        sounds = new int[] {
                                R.raw.a,
                                R.raw.b,
                                R.raw.l,
                                R.raw.m
                        };
                        break;
                }
                break;
            case "shapes":
                switch (level) {
                    case 0 :
                        board_gltf_uris = new String[]{
                                "https://raw.githubusercontent.com/elkassimyhajar/AR-Android-App/meryem/app/sampledata/square_mold_red.gltf",
                                "https://raw.githubusercontent.com/elkassimyhajar/AR-Android-App/meryem/app/sampledata/rectangle_mold_brown.gltf",
                                "https://raw.githubusercontent.com/elkassimyhajar/AR-Android-App/meryem/app/sampledata/triangle_mold_orange.gltf",
                                "https://raw.githubusercontent.com/elkassimyhajar/AR-Android-App/meryem/app/sampledata/circle_mold_blue.gltf"
                        };
                        objects_gltf_uris = new String[]{
                                "https://raw.githubusercontent.com/elkassimyhajar/AR-Android-App/meryem/app/sampledata/square_red.gltf",
                                "https://raw.githubusercontent.com/elkassimyhajar/AR-Android-App/meryem/app/sampledata/rectangle_brown.gltf",
                                "https://raw.githubusercontent.com/elkassimyhajar/AR-Android-App/meryem/app/sampledata/triangle_orange.gltf",
                                "https://raw.githubusercontent.com/elkassimyhajar/AR-Android-App/meryem/app/sampledata/circle_blue.gltf"
                        };
                        board_cells_status = new int[board_gltf_uris.length];
                        sounds = new int[] {
                                R.raw.redsquare,
                                R.raw.brownrectangle,
                                R.raw.orangetriangle,
                                R.raw.bluecircle
                        };
                        break;
                    case 1 :
                        board_gltf_uris = new String[]{
                                "https://raw.githubusercontent.com/elkassimyhajar/AR-Android-App/meryem/app/sampledata/triangle_mold_green.gltf",
                                "https://raw.githubusercontent.com/elkassimyhajar/AR-Android-App/meryem/app/sampledata/triangle_mold_orange.gltf",
                                "https://raw.githubusercontent.com/elkassimyhajar/AR-Android-App/meryem/app/sampledata/triangle_mold_purple.gltf",
                                "https://raw.githubusercontent.com/elkassimyhajar/AR-Android-App/meryem/app/sampledata/triangle_mold_yellow.gltf",
                        };
                        objects_gltf_uris = new String[]{
                                "https://raw.githubusercontent.com/elkassimyhajar/AR-Android-App/meryem/app/sampledata/triangle_green.gltf",
                                "https://raw.githubusercontent.com/elkassimyhajar/AR-Android-App/meryem/app/sampledata/triangle_orange.gltf",
                                "https://raw.githubusercontent.com/elkassimyhajar/AR-Android-App/meryem/app/sampledata/triangle_purple.gltf",
                                "https://raw.githubusercontent.com/elkassimyhajar/AR-Android-App/meryem/app/sampledata/triangle_yellow.gltf",
                        };
                        board_cells_status = new int[board_gltf_uris.length];
                        sounds = new int[] {
                                R.raw.greentriangle,
                                R.raw.orangetriangle,
                                R.raw.purpletriangle,
                                R.raw.yellowtriangle
                        };
                        break;
                    case 2 :
                        board_gltf_uris = new String[]{
                                "https://raw.githubusercontent.com/elkassimyhajar/AR-Android-App/meryem/app/sampledata/square_mold_red.gltf",
                                "https://raw.githubusercontent.com/elkassimyhajar/AR-Android-App/meryem/app/sampledata/rectangle_mold_black.gltf",
                                "https://raw.githubusercontent.com/elkassimyhajar/AR-Android-App/meryem/app/sampledata/triangle_mold_yellow.gltf",
                                "https://raw.githubusercontent.com/elkassimyhajar/AR-Android-App/meryem/app/sampledata/hexagon_mold_white.gltf",
                                "https://raw.githubusercontent.com/elkassimyhajar/AR-Android-App/meryem/app/sampledata/circle_mold_blue.gltf",
                                "https://raw.githubusercontent.com/elkassimyhajar/AR-Android-App/meryem/app/sampledata/rectangle_mold_brown.gltf",
                                "https://raw.githubusercontent.com/elkassimyhajar/AR-Android-App/meryem/app/sampledata/circle_mold_pink.gltf",
                                "https://raw.githubusercontent.com/elkassimyhajar/AR-Android-App/meryem/app/sampledata/triangle_mold_orange.gltf",
                                "https://raw.githubusercontent.com/elkassimyhajar/AR-Android-App/meryem/app/sampledata/square_mold_grey.gltf",
                        };
                        objects_gltf_uris = new String[]{
                                "https://raw.githubusercontent.com/elkassimyhajar/AR-Android-App/meryem/app/sampledata/square_red.gltf",
                                "https://raw.githubusercontent.com/elkassimyhajar/AR-Android-App/meryem/app/sampledata/rectangle_black.gltf",
                                "https://raw.githubusercontent.com/elkassimyhajar/AR-Android-App/meryem/app/sampledata/triangle_yellow.gltf",
                                "https://raw.githubusercontent.com/elkassimyhajar/AR-Android-App/meryem/app/sampledata/hexagon_white.gltf",
                                "https://raw.githubusercontent.com/elkassimyhajar/AR-Android-App/meryem/app/sampledata/circle_blue.gltf",
                                "https://raw.githubusercontent.com/elkassimyhajar/AR-Android-App/meryem/app/sampledata/rectangle_brown.gltf",
                                "https://raw.githubusercontent.com/elkassimyhajar/AR-Android-App/meryem/app/sampledata/circle_pink.gltf",
                                "https://raw.githubusercontent.com/elkassimyhajar/AR-Android-App/meryem/app/sampledata/triangle_orange.gltf",
                                "https://raw.githubusercontent.com/elkassimyhajar/AR-Android-App/meryem/app/sampledata/square_grey.gltf"
                        };
                        board_cells_status = new int[board_gltf_uris.length];
                        sounds = new int[] {
                                R.raw.redsquare,
                                R.raw.blackrectangle,
                                R.raw.yellowtriangle,
                                R.raw.whitehexagon,
                                R.raw.bluecircle,
                                R.raw.brownrectangle,
                                R.raw.pinkcircle,
                                R.raw.orangetriangle,
                                R.raw.greysquare
                        };
                        break;
                    case 3 :
                        board_gltf_uris = new String[]{
                                "https://raw.githubusercontent.com/elkassimyhajar/AR-Android-App/meryem/app/sampledata/triangle_mold_green.gltf",
                                "https://raw.githubusercontent.com/elkassimyhajar/AR-Android-App/meryem/app/sampledata/hexagon_mold_yellow.gltf",
                                "https://raw.githubusercontent.com/elkassimyhajar/AR-Android-App/meryem/app/sampledata/square_mold_red.gltf",
                                "https://raw.githubusercontent.com/elkassimyhajar/AR-Android-App/meryem/app/sampledata/circle_mold_blue.gltf",
                                "https://raw.githubusercontent.com/elkassimyhajar/AR-Android-App/meryem/app/sampledata/rectangle_mold_black.gltf",
                                "https://raw.githubusercontent.com/elkassimyhajar/AR-Android-App/meryem/app/sampledata/hexagon_mold_white.gltf",
                                "https://raw.githubusercontent.com/elkassimyhajar/AR-Android-App/meryem/app/sampledata/triangle_mold_purple.gltf",
                                "https://raw.githubusercontent.com/elkassimyhajar/AR-Android-App/meryem/app/sampledata/square_mold_grey.gltf",
                                "https://raw.githubusercontent.com/elkassimyhajar/AR-Android-App/meryem/app/sampledata/rectangle_mold_brown.gltf",
                        };
                        objects_gltf_uris = new String[]{
                                "https://raw.githubusercontent.com/elkassimyhajar/AR-Android-App/meryem/app/sampledata/triangle_green.gltf",
                                "https://raw.githubusercontent.com/elkassimyhajar/AR-Android-App/meryem/app/sampledata/hexagon_yellow.gltf",
                                "https://raw.githubusercontent.com/elkassimyhajar/AR-Android-App/meryem/app/sampledata/square_red.gltf",
                                "https://raw.githubusercontent.com/elkassimyhajar/AR-Android-App/meryem/app/sampledata/circle_blue.gltf",
                                "https://raw.githubusercontent.com/elkassimyhajar/AR-Android-App/meryem/app/sampledata/rectangle_black.gltf",
                                "https://raw.githubusercontent.com/elkassimyhajar/AR-Android-App/meryem/app/sampledata/hexagon_white.gltf",
                                "https://raw.githubusercontent.com/elkassimyhajar/AR-Android-App/meryem/app/sampledata/triangle_purple.gltf",
                                "https://raw.githubusercontent.com/elkassimyhajar/AR-Android-App/meryem/app/sampledata/square_grey.gltf",
                                "https://raw.githubusercontent.com/elkassimyhajar/AR-Android-App/meryem/app/sampledata/rectangle_brown.gltf",
                        };
                        board_cells_status = new int[board_gltf_uris.length];
                        sounds = new int[] {
                                R.raw.greentriangle,
                                R.raw.yellowhexagon,
                                R.raw.redsquare,
                                R.raw.bluecircle,
                                R.raw.blackrectangle,
                                R.raw.whitehexagon,
                                R.raw.purpletriangle,
                                R.raw.greysquare,
                                R.raw.brownrectangle
                        };
                        break;
                }
        }
        Arrays.fill(board_cells_status, 0);
    }

    private boolean checkNetworkAvailability() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetworkInfo = connectivityManager.getActiveNetworkInfo();
        return activeNetworkInfo != null && activeNetworkInfo.isConnected();
    }
}


