package com.example.supera.kamil.quicktravel.models;


import android.graphics.Color;
import android.support.annotation.NonNull;
import android.view.SubMenu;

import com.example.supera.kamil.quicktravel.NoStop;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PolylineOptions;

import java.util.ArrayList;
import java.util.List;


public class Route {
    private List<Firm> firms;
    private Double maxTimeOfRide;
    private List<Stop> stops;
    private String name;
    private String totalLength;
    private Float rating;
    private int votes;

    public void setName(String name) {
        this.name = name;
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

    public void setRating(Float rating) {
        this.rating = rating;
    }

    public void setVotes(int votes) {
        this.votes = votes;
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

    public List<Stop> getStops() {
        return stops;
    }

    public String getTotalLength() {
        return totalLength;
    }

    public Float getRating() {
        return rating;
    }

    public int getVotes() {
        return votes;
    }

    /** END GET/SET METHODS */

    public void addStopsToMap(GoogleMap googleMap, String name) {
        if (this.name.contains(name) && stops != null) {
            for (Stop stop : stops) {
                addStopAsMarker(googleMap, stop);
            }
        }
    }

    public void addStopsToDrawer(SubMenu subMenu, String name) {
        if (this.name.contains(name) && stops != null) {
            for (Stop stop : stops) {
                subMenu.add(stop.getName());
            }
        }
    }

    /**
     * Checks if route posses any stop with given name.
     * @param name
     * @return True if posses, false when not.
     */
    public boolean checkIfRoutePossesStop(String name) {
        if (stops != null){
            return stops
                .stream()
                .anyMatch(stop -> stop.getName().equals(name));
        }

        return false;
    }

    /**
     * Add stops to map for specified earlier route. Change color of the clicked one.
     * @param googleMap
     * @param name
     */
    public void addStopsToMapWithOneChangedColor(GoogleMap googleMap, String name) {
        for (Stop stop : stops) {
            if (stop.getName().contains(name)) {
                addStopsAsMarkerWithColorChange(googleMap, stop,
                    BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_ORANGE));
            } else {
                addStopAsMarker(googleMap, stop);
            }
        }
    }

    public void drawRoute(GoogleMap googleMap) {
        PolylineOptions line = new PolylineOptions();
        line.color(Color.RED);
        line.width(6f);
        line.geodesic(true);

        List<LatLng> points = new ArrayList<>();

        for (Stop stop : stops) {
            line.add(stop.getPoint());
        }

        googleMap.addPolyline(line);
    }

    public List<Departure> getStopDepartures(String stopName, String type) throws NoStop {
        for (Stop stop : stops) {
            if (stop.getName().equals(stopName)) {
                return stop.filterDeparturesByType(type);
            }
        }

        throw new NoStop();
    }

    public void changeRating(Float userRating) {
         rating *= votes;
         rating += userRating;
         votes++;
         rating /= votes;
    }

    private void addStopAsMarker(GoogleMap googleMap, Stop stop) {
        googleMap.addMarker(new MarkerOptions()
            .position(stop.getPoint())
            .title(stop.getName()));
    }

    private void addStopsAsMarkerWithColorChange(GoogleMap googleMap, Stop stop,
                                                 BitmapDescriptor descriptor) {
        googleMap.addMarker(new MarkerOptions()
            .position(stop.getPoint())
            .title(stop.getName())
            .icon(descriptor));
    }

    @NonNull
    @Override
    public String toString() {
        return name;
    }
}
