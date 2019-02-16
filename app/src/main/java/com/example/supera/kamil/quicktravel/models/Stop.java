package com.example.supera.kamil.quicktravel.models;

import android.support.annotation.NonNull;

import com.google.android.gms.maps.model.LatLng;

import java.util.List;
import java.util.stream.Collectors;

public class Stop {
    private String name;
    private List<Departure> departures;
    private LatLng point;
    private int numberInRoute;

    public Stop() {}

    public Stop(String name, List<Departure> departures, LatLng point, int numberInRoute) {
        this.name = name;
        this.departures = departures;
        this.point = point;
        this.numberInRoute = numberInRoute;
    }

    public List<Departure> filterDeparturesByType(String type) {
        return departures.stream().filter(departure -> departure.getType().equals(type))
            .collect(Collectors.toList());
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getName() {
        return name;
    }

    public int getNumberInRoute() {
        return numberInRoute;
    }

    public LatLng getPoint() {
        return point;
    }

    public List<Departure> getDepartures() {
        return departures;
    }

    public void setDepartures(List<Departure> departures) {
        this.departures = departures;
    }

    public void setNumberInRoute(int numberInRoute) {
        this.numberInRoute = numberInRoute;
    }

    public void setPoint(LatLng point) {
        this.point = point;
    }

    @NonNull
    @Override
    public String toString() {
        return name;
    }
}
