package com.example.supera.kamil.quicktravel.models;

import android.support.annotation.NonNull;

import java.util.List;


public class Departure {
    private String type;
    private List<String> time;

    public Departure() {}

    public String getType() {
        return type;
    }

    public List<String> getTime() {
        return time;
    }

    public void setTime(List<String> time) {
        this.time = time;
    }

    public void setType(String type) {
        this.type = type;
    }

    @NonNull
    @Override
    public String toString() {
        return type;
    }
}
