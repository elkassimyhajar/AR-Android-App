package com.example.match_it;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.app.ActivityManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.google.ar.core.Anchor;
import com.google.ar.core.Pose;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.Color;
import com.google.ar.sceneform.rendering.MaterialFactory;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.rendering.ShapeFactory;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.TransformableNode;

import java.util.Arrays;
import java.util.Objects;

public class GameActivity extends AppCompatActivity {

    private ArFragment arFragment;
    private ModelRenderable greenCubeRenderable;
    // helps to render the 3d model only once when we tap the screen
    private int clickNo = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_game);

        SharedPreferences preferences = getApplicationContext().getSharedPreferences("MyPref", Context.MODE_PRIVATE);
        String selectedTopic = preferences.getString("topic", "NOT FOUND");
        Log.i("TOPIC", selectedTopic);

        if(checkSystemSupport(this)) {
            // ArFragment is linked up with its respective id used in the activity_main.xml
            arFragment = (ArFragment) getSupportFragmentManager().findFragmentById(R.id.fragment);

            //create red sphere
            MaterialFactory.makeOpaqueWithColor(this, new Color(android.graphics.Color.GREEN))
                    .thenAccept(
                            material -> greenCubeRenderable = ShapeFactory.makeCube(new Vector3(0.2f, 0.2f, 0.2f), new Vector3(0.0f, 0.15f, 0.0f), material)
                    );


            arFragment.setOnTapArPlaneListener((hitResult, plane, motionEvent) -> {
                //clickNo++;
                //if (clickNo == 1) {
                Anchor anchor = hitResult.createAnchor();
                Pose pose = anchor.getPose();

                Log.i("X ", Arrays.toString(pose.getXAxis()));
                Log.i("Y ", Arrays.toString(pose.getYAxis()));
                Log.i("Z ", Arrays.toString(pose.getZAxis()));

                addModel(anchor, greenCubeRenderable);
                    /*ModelRenderable.builder()
                            .setSource(this, R.raw.tiger)
                            .setIsFilamentGltf(true)
                            .build()
                            .thenAccept(modelRenderable -> addModel(anchor, modelRenderable))
                            .exceptionally(throwable -> {
                                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                                builder.setMessage("Somthing is not right" + throwable.getMessage()).show();
                                return null;
                            });*/
                //}
            });

        }
        else
            return;
        // Enable AR-related functionality on ARCore supported devices only.
        //maybeEnableArButton();
    }

    //check whether the phone’s hardware meets all the systemic requirements to run this AR App
    public static boolean checkSystemSupport(Activity activity) {
        // checking whether the API version of the running Android >= 24
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            String openGlVersion = ((ActivityManager) Objects.requireNonNull(activity.getSystemService(Context.ACTIVITY_SERVICE))).getDeviceConfigurationInfo().getGlEsVersion();
            // checking whether the OpenGL version >= 3.0
            if (Double.parseDouble(openGlVersion) >= 3.0) {
                return true;
            } else {
                Toast.makeText(activity, "App needs OpenGl Version 3.0 or later", Toast.LENGTH_SHORT).show();
                activity.finish();
                return false;
            }
        } else {
            Toast.makeText(activity, "App does not support required Build Version", Toast.LENGTH_SHORT).show();
            activity.finish();
            return false;
        }
    }

    private void addModel(Anchor anchor, ModelRenderable modelRenderable) {
        // Creating a AnchorNode with a specific anchor
        AnchorNode anchorNode = new AnchorNode(anchor);
        // attaching the anchorNode with the ArFragment
        anchorNode.setParent(arFragment.getArSceneView().getScene());
        TransformableNode transform = new TransformableNode(arFragment.getTransformationSystem());
        // attaching the anchorNode with the TransformableNode
        transform.setParent(anchorNode);
        // attaching the 3d model with the TransformableNode that is
        // already attached with the node
        transform.setRenderable(modelRenderable);
        transform.select();
    }
}
