package com.example.supera.kamil.quicktravel.models;

import android.support.annotation.NonNull;


public class Departure {
    private String type;
    private String time;

    public Departure() {}

    public Departure(String type, String time) {
        this.type = type;
        this.time = time;
    }

    public String getType() {
        return type;
    }

    public String getTime() {
        return time;
    }

    public void setTime(String time) {
        this.time = time;
    }

    public void setType(String type) {
        this.type = type;
    }

    @NonNull
    @Override
    public String toString() {
        return time;
    }
}
