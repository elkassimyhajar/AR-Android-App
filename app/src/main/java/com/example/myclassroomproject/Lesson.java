package com.example.myclassroomproject;

public class Lesson {

    private int image;
    private String[] nameShuffled;
    private String name, arPath;
    private int sound;

    public Lesson(int image, String name, String[] nameShuffled){
        this.image = image;
        this.name = name;
        this.nameShuffled = nameShuffled;
    }

    public Lesson(int image, String name, int sound, String arPath){
        this.image = image;
        this.name = name;
        this.sound = sound;
        this.arPath = arPath;
    }

    public int getImage() {
        return image;
    }

    public void setImage(int image) {
        this.image = image;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getSound() {
        return sound;
    }

    public void setSound(int sound) {
        this.sound = sound;
    }

    public String getArPath() {
        return arPath;
    }

    public void setArPath(String arPath) {
        this.arPath = arPath;
    }

    public String[] getNameShuffled() {
        return nameShuffled;
    }

    public void setNameShuffled(String[] nameShuffled) {
        this.nameShuffled = nameShuffled;
    }
}
