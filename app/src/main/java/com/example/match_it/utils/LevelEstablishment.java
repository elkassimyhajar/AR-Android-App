package com.example.match_it.utils;

import android.content.Context;

public class LevelEstablishment {
    private String label;
    private int img;
    private int num;
    private Context context;

    public LevelEstablishment(String label, int img, int num, Context context) {
        this.label = label;
        this.img = img;
        this.num = num;
        this.context = context;
    }

    public String getLabel() {
        return label;
    }

    public void setLabel(String label) {
        this.label = label;
    }

    public int getImg() {
        return img;
    }

    public void setImg(int img) {
        this.img = img;
    }

    public int getNum() {
        return num;
    }

    public void setNum(int num) {
        this.num = num;
    }

    public Context getContext() {
        return context;
    }

    public void setContext(Context context) {
        this.context = context;
    }
}
