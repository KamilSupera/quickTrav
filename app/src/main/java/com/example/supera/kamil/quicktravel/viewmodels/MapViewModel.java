package com.example.supera.kamil.quicktravel.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.example.supera.kamil.quicktravel.models.Departure;
import com.example.supera.kamil.quicktravel.models.Stop;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static java.lang.Double.parseDouble;

public class MapViewModel extends ViewModel {
    private MutableLiveData<List<Stop>> stops;

    public LiveData<List<Stop>> getStops() {
        if (stops == null) {
            stops = new MutableLiveData<>();
            loadStops();
        }

        return stops;
    }

    private void loadStops() {
        List<Stop> stopsDb = new ArrayList<>();
        // Initialize firebase db and get connection reference
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        DatabaseReference ref = database.getReference("root/stops/");

        // Load data
        ref.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                for (DataSnapshot ds: dataSnapshot.getChildren()) {
                    Stop stop = new Stop();
                    stop.setName(ds.getKey());

                    Double longitude = null;
                    Double latitude = null;

                    for (DataSnapshot geoPoint : ds.child("geoPoint").getChildren()) {
                        if (Objects.equals(geoPoint.getKey(), "_long")) {
                            longitude = parseDouble(geoPoint.getValue().toString());
                        } else {
                            latitude = parseDouble(geoPoint.getValue().toString());
                        }
                    }

                    stop.setPoint(new LatLng(latitude, longitude));
                    List<Departure> departures = new ArrayList<>();

                    for (DataSnapshot departuresS : ds.child("departures").getChildren()) {
                        Departure departure = new Departure();

                        for (DataSnapshot type : departuresS.getChildren()) {
                            departure.setType(type.getKey());

                            for (DataSnapshot time : type.getChildren()) {
                                departure.setTime(time.getValue().toString());
                            }
                        }

                        departures.add(departure);
                    }

                    stop.setDepartures(departures);
                    stopsDb.add(stop);
                }

                stops.setValue(stopsDb);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });
    }
}
