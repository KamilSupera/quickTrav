package com.example.supera.kamil.quicktravel.models; 

import com.google.android.gms.maps.model.LatLng;


public class Bus {
    private LatLng currentPosition;

    public LatLng getCurrentPosition() {
        return currentPosition;
    }

    public void setCurrentPosition(LatLng position) {
        currentPosition = position;
    }
}
