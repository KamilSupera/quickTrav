package com.example.supera.kamil.quicktravel.models;


import android.support.annotation.NonNull;

import java.util.List;

public class Route {
    private List<OwnerComment> comments;
    private List<Firm> firms;
    private Double maxTimeOfRide;
    private List<Stop> stops;
    private String name;

    public Route() {
    }

    public Route(List<OwnerComment> comments, List<Firm> firms, Double maxTimeOfRide,
                 List<Stop> stops, String name) {
        this.comments = comments;
        this.firms = firms;
        this.stops = stops;
        this.name = name;
        this.maxTimeOfRide = maxTimeOfRide;
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

    @NonNull
    @Override
    public String toString() {
        return name;
    }
}
