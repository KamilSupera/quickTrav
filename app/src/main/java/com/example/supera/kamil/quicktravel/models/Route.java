package com.example.supera.kamil.quicktravel.models;


import android.support.annotation.NonNull;
import android.view.SubMenu;

import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.List;

public class Route {
    private List<OwnerComment> comments;
    private List<Firm> firms;
    private Double maxTimeOfRide;
    private List<Stop> stops;
    private String name;
    private String totalLength;

    public Route() {
    }

    public void setName(String name) {
        this.name = name;
    }

    public void setComments(List<OwnerComment> comments) {
        this.comments = comments;
    }

    public void setFirms(List<Firm> firms) {
        this.firms = firms;
    }

    public void setMaxTimeOfRide(Double maxTimeOfRide) {
        this.maxTimeOfRide = maxTimeOfRide;
    }

    public void setStops(List<Stop> stops) {
        this.stops = stops;
    }

    public void setTotalLength(String totalLength) {
        this.totalLength = totalLength;
    }

    public String getName() {
        return name;
    }

    public Double getMaxTimeOfRide() {
        return maxTimeOfRide;
    }

    public List<Firm> getFirms() {
        return firms;
    }

    public List<OwnerComment> getComments() {
        return comments;
    }

    public List<Stop> getStops() {
        return stops;
    }

    public String getTotalLength() {
        return totalLength;
    }

    public void addStopsToMap(GoogleMap googleMap, String name) {
        if (this.name.contains(name)) {
            if (stops != null) {
                for (Stop stop : stops) {
                    googleMap.addMarker(new MarkerOptions()
                        .position(stop.getPoint())
                        .title(stop.getName()));
                }
            }
        }
    }

    public void addStopsToDrawer(SubMenu subMenu, String name) {
        if (this.name.contains(name)) {
            if (stops != null) {
                for (Stop stop : stops) {
                    subMenu.add(stop.getName());
                }
            }
        }
    }

    @NonNull
    @Override
    public String toString() {
        return name;
    }
}
