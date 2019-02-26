package com.example.supera.kamil.quicktravel.mapper;

import com.example.supera.kamil.quicktravel.firebase.mapper.FirebaseMapper;
import com.example.supera.kamil.quicktravel.models.Departure;
import com.example.supera.kamil.quicktravel.models.Stop;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static java.lang.Double.parseDouble;

public class StopMapper extends FirebaseMapper<Stop> {

    @Override
    public List<Stop> mapList(DataSnapshot dataSnapshot) {
        List<Stop> stops = new ArrayList<>();

        for (DataSnapshot ds: dataSnapshot.getChildren()) {
            Stop stop = new Stop();
            stop.setName(ds.getKey());
            stop.setPoint(getGeoPoint(ds));
            stop.setDepartures(getDepartures(ds));
            stops.add(stop);
        }

        return stops;
    }

    /**
     * Gets information about stop position and creates new {@link LatLng} object.
     * @param dataSnapshot One of children from root data snapshot from Firebase.
     * @return {@link LatLng} object.
     */
    private LatLng getGeoPoint(DataSnapshot dataSnapshot) {
        Double longitude = null;
        Double latitude = null;

        // Get geo position of stop.
        for (DataSnapshot geoPoint : dataSnapshot.child("geoPoint").getChildren()) {
            if (Objects.equals(geoPoint.getKey(), "_long")) {
                longitude = parseDouble(geoPoint.getValue().toString());
            } else {
                latitude = parseDouble(geoPoint.getValue().toString());
            }
        }

        return new LatLng(latitude, longitude);
    }

    /**
     * Gets all {@link Departure} belonged to single stop.
     * @param dataSnapshot One of children from root data snapshot from Firebase.
     * @return Departures list.
     */
    private List<Departure> getDepartures(DataSnapshot dataSnapshot) {
        List<Departure> departures = new ArrayList<>();

        // Load departures for specific stop.
        for (DataSnapshot departuresS : dataSnapshot.child("departures").getChildren()) {
            Departure departure = new Departure();

            for (DataSnapshot type : departuresS.getChildren()) {
                departure.setType(type.getKey());

                for (DataSnapshot time : type.getChildren()) {
                    departure.setTime(time.getValue().toString());
                }
            }

            departures.add(departure);
        }

        return departures;
    }
}