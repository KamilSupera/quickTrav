package com.example.supera.kamil.quicktravel.mapper;

import com.example.supera.kamil.quicktravel.firebase.mapper.FirebaseMapper;
import com.example.supera.kamil.quicktravel.models.Bus;
import com.google.android.gms.maps.model.LatLng;
import com.google.firebase.database.DataSnapshot;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

import static java.lang.Double.parseDouble;


public class BusMapper extends FirebaseMapper<Bus> {

    @Override
    public List<Bus> mapList(DataSnapshot dataSnapshot) {
        Bus bus = new Bus();
        List<Bus> buses = new ArrayList<>();
        Double longitude = null;
        Double latitude = null;

        for (DataSnapshot ds : dataSnapshot.getChildren()) {
            if (ds.getKey().equals("_long")) {
                longitude = parseDouble(ds.getValue().toString());
            } else {
                latitude = parseDouble(ds.getValue().toString());
            }
        }

        bus.setCurrentPosition(new LatLng(latitude, longitude));
        buses.add(bus);

        return buses;
    }

    @Override
    public List<Bus> mapList(Iterable<DataSnapshot> dataSnapshot) {
        return null;
    }
}
