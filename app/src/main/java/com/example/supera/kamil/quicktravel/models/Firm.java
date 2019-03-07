package com.example.supera.kamil.quicktravel.models;

import android.support.annotation.NonNull;

public class Firm {
    private String name;
    private String address;

    public Firm() {}

    public String getAddress() {
        return address;
    }

    public String getName() {
        return name;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public void setName(String name) {
        this.name = name;
    }

    @NonNull
    @Override
    public String toString() {
        return name;
    }
}
