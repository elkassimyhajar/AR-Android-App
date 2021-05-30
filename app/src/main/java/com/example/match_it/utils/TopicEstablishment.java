package com.example.match_it.utils;

public class TopicEstablishment {
    private String name;
    private int image;
    private int sound;

    public TopicEstablishment(int image, String name, int sound) {
        this.image = image;
        this.name = name;
        this.sound = sound;
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
}
