package com.example.match_it.render;

import android.util.Log;

import com.google.ar.core.Anchor;
import com.google.ar.core.Pose;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.ArSceneView;
import com.google.ar.sceneform.Node;
import com.google.ar.sceneform.rendering.ModelRenderable;

import java.util.Arrays;

public class BoardRenderer {
    protected final int BOARD_CELL_COUNT;
    private final int BOARD_ROW_COUNT;
    private final int BOARD_COL_COUNT;
    protected static float SPACE_BETWEEN_CELLS = 0.25f;

    private final ArSceneView arSceneView;
    private final ModelRenderable[] boardModels;
    private final Pose hitPose;
    private Node[] boardNodes;
    private Anchor[] boardAnchors;
    private AnchorNode[] boardAnchorNodes;

    private void setAnchors() {
        boardAnchors = new Anchor[BOARD_CELL_COUNT];
        if(arSceneView.getSession()==null)
            return ;
        float[] trans = this.hitPose.getTranslation();
        float[] rot = this.hitPose.getRotationQuaternion();
        Pose pose = new Pose(trans, rot);
        for(int j = 0; j < BOARD_ROW_COUNT; j++) {
            for(int i = 0; i < BOARD_COL_COUNT; i++) {
                if(i!= BOARD_COL_COUNT-1) {
                    boardAnchors[i+BOARD_COL_COUNT*j] = arSceneView.getSession().createAnchor(pose);
                    if( j % 2 == 0)
                        trans[2] += SPACE_BETWEEN_CELLS;
                    else
                        trans[2] -= SPACE_BETWEEN_CELLS;
                    pose = new Pose(trans, rot);
                }
                else
                    boardAnchors[i+BOARD_COL_COUNT*j] = arSceneView.getSession().createAnchor(pose);
            }
            trans[0] += SPACE_BETWEEN_CELLS;
            pose = new Pose(trans, rot);
        }
    }

    private void setAnchorNodes() {
        boardAnchorNodes = new AnchorNode[BOARD_CELL_COUNT];
        for(int i = 0; i < BOARD_CELL_COUNT; i++) {
            boardAnchorNodes[i] = new AnchorNode(boardAnchors[i]);
            boardAnchorNodes[i].setParent(arSceneView.getScene());
        }
    }

    private void setNodes() {
        boardNodes = new Node[BOARD_CELL_COUNT];
        for(int i = 0; i < BOARD_CELL_COUNT; i++) {
            boardNodes[i] = new Node();
            boardNodes[i].setRenderable(boardModels[i]);
            boardNodes[i].setParent(boardAnchorNodes[i]);
        }
    }

    public void render() {
        Log.d("__models__", Arrays.toString(boardModels));
        //Log.d("__model__", boardModel.toString());
        setAnchors();
        Log.d("__anchors__", Arrays.toString(boardAnchors));
        setAnchorNodes();
        Log.d("__anchor nodes__", Arrays.toString(boardAnchorNodes));
        setNodes();
        Log.d("__nodes__", Arrays.toString(boardNodes));
    }

   public BoardRenderer(ArSceneView arSceneView, ModelRenderable[] models, Pose pose) {
        BOARD_CELL_COUNT = models.length;
        BOARD_COL_COUNT = (int) Math.sqrt(models.length);
        BOARD_ROW_COUNT = (int) Math.sqrt(models.length);
        Log.d("__dimensions__", BOARD_COL_COUNT + " x " + BOARD_ROW_COUNT);
        this.arSceneView = arSceneView;
        this.boardModels = models;
        this.hitPose = pose;
   }

    public int getBOARD_CELL_COUNT() {
        return BOARD_CELL_COUNT;
    }

    public Node[] getBoardNodes() {
        return boardNodes;
    }
}
