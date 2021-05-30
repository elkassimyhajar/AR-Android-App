package com.example.match_it.render;

import android.util.Log;

import com.example.match_it.render.BoardRenderer;
import com.google.ar.core.Anchor;
import com.google.ar.core.Pose;
import com.google.ar.sceneform.AnchorNode;
import com.google.ar.sceneform.ArSceneView;
import com.google.ar.sceneform.math.Vector3;
import com.google.ar.sceneform.rendering.ModelRenderable;
import com.google.ar.sceneform.ux.ArFragment;
import com.google.ar.sceneform.ux.TransformableNode;

import java.util.Arrays;
import java.util.Random;

public class ObjectsRenderer {

    private final int OBJECTS_COUNT;
    private final float SPACE_BETWEEN_OBJECTS = BoardRenderer.SPACE_BETWEEN_CELLS + 0.1f;
    private final int BOARD_CELLS_COUNT;

    private final ArFragment arFragment;
    private final ArSceneView arSceneView;

    private final ModelRenderable[] objectsModels;

    private final Pose hitPose;

    private TransformableNode[] objectsNodes;
    private Anchor[] objectsAnchors;
    private AnchorNode[] objectsAnchorNodes;
    private Vector3[] objectsInitialPositions;

    private static Random rand = new Random();

    private void setAnchors() {
        objectsAnchors = new Anchor[OBJECTS_COUNT];
        if(arSceneView.getSession()==null)
            return ;
        float[] trans;
        float[] rot = new float[4];
        for (int i = 0 ; i < 4; i++)
            rot[i] = 0.0f;
        int modelsPerLine = (int) Math.sqrt(BOARD_CELLS_COUNT) - 1;
        Pose pose;
        int j = 0;
        int numTour = 1;
        while (j < OBJECTS_COUNT) {
            Log.d("__j__", String.valueOf(j));
            trans = this.hitPose.getTranslation();
            trans[2] -= SPACE_BETWEEN_OBJECTS * numTour;
            pose = new Pose(trans, rot);
            for (int i = 0; i < modelsPerLine; i++) {
                if(j < OBJECTS_COUNT) {
                    objectsAnchors[j] = arSceneView.getSession().createAnchor(pose);
                    trans[0] += SPACE_BETWEEN_OBJECTS;
                    pose = new Pose(trans, rot);
                    j++;
                }
            }
            trans = this.hitPose.getTranslation();
            trans[0] -= SPACE_BETWEEN_OBJECTS * numTour;
            pose = new Pose(trans, rot);
            for (int i = 0; i < modelsPerLine; i++) {
                if(j < OBJECTS_COUNT) {
                    objectsAnchors[j] = arSceneView.getSession().createAnchor(pose);
                    trans[2] += SPACE_BETWEEN_OBJECTS;
                    pose = new Pose(trans, rot);
                    j++;
                }
            }
            trans = this.hitPose.getTranslation();
            trans[0] += BoardRenderer.SPACE_BETWEEN_CELLS * (Math.sqrt(BOARD_CELLS_COUNT) - 1) + SPACE_BETWEEN_OBJECTS * numTour;
            pose = new Pose(trans, rot);
            for (int i = 0; i < modelsPerLine; i++) {
                if(j < OBJECTS_COUNT) {
                    objectsAnchors[j] = arSceneView.getSession().createAnchor(pose);
                    trans[2] += SPACE_BETWEEN_OBJECTS;
                    pose = new Pose(trans, rot);
                    j++;
                }
            }
            trans = this.hitPose.getTranslation();
            trans[2] += BoardRenderer.SPACE_BETWEEN_CELLS * (Math.sqrt(BOARD_CELLS_COUNT) - 1) + SPACE_BETWEEN_OBJECTS * numTour;
            pose = new Pose(trans, rot);
            for (int i = 0; i < modelsPerLine; i++) {
                if(j < OBJECTS_COUNT) {
                    objectsAnchors[j] = arSceneView.getSession().createAnchor(pose);
                    trans[0] += SPACE_BETWEEN_OBJECTS;
                    pose = new Pose(trans, rot);
                    j++;
                }
            }
            numTour ++;
        }

    }

    private void setAnchorNodes() {
        objectsAnchorNodes = new AnchorNode[OBJECTS_COUNT];
        for(int i = 0; i < OBJECTS_COUNT; i++) {
            objectsAnchorNodes[i] = new AnchorNode(objectsAnchors[i]);
            objectsAnchorNodes[i].setParent(arSceneView.getScene());
        }
    }

    private void setNodes() {
        objectsNodes = new TransformableNode[OBJECTS_COUNT];
        objectsInitialPositions = new Vector3[OBJECTS_COUNT];
        for(int i = 0; i < OBJECTS_COUNT; i++) {
            objectsNodes[i] = new TransformableNode(arFragment.getTransformationSystem());
            objectsNodes[i].getRotationController().setEnabled(false);
            objectsNodes[i].getScaleController().setEnabled(false);
            objectsNodes[i].setRenderable(objectsModels[i]);
            objectsNodes[i].setParent(objectsAnchorNodes[i]);
            objectsInitialPositions[i] = objectsNodes[i].getWorldPosition();
        }
    }

    public void render() {
        Log.d("__models__", Arrays.toString(objectsModels));
        setAnchors();
        Log.d("__anchors__", Arrays.toString(objectsAnchors));
        setAnchorNodes();
        Log.d("__anchor nodes__", Arrays.toString(objectsAnchorNodes));
        setNodes();
        Log.d("__nodes__", Arrays.toString(objectsNodes));
    }

    public ObjectsRenderer(ArFragment arFragment, ModelRenderable[] models, Pose pose, int cellsNbr) {
        OBJECTS_COUNT = models.length;
        BOARD_CELLS_COUNT = cellsNbr;
        this.arFragment = arFragment;
        this.arSceneView = arFragment.getArSceneView();
        this.objectsModels = models;
        this.hitPose = pose;
    }

    public TransformableNode[] getObjectsNodes() {
        return objectsNodes;
    }

    public Vector3[] getObjectsInitialPositions() {
        return objectsInitialPositions;
    }
}

