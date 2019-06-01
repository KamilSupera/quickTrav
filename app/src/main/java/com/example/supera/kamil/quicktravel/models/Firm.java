package com.example.supera.kamil.quicktravel.models;

import android.support.annotation.NonNull;

public class Firm {
    private String name;
    private String address;
    private String about;

    public Firm() {}

    public String getAddress() {
        return address;
    }

    public String getName() {
        return name;
    }

    public String getAbout() {
        return about;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setAbout(String about) {
        this.about = about;
    }

    @NonNull
    @Override
    public String toString() {
        return name;
    }
}
