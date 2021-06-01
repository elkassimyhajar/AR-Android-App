package com.example.androidproject;

public class SliderItem {

    //Here we can use String variable to store url
    //If we want to load image from the internet
    private int image;

    public SliderItem(int image) {
        this.image = image;
    }

    public int getImage() {
        return image;
    }
}
