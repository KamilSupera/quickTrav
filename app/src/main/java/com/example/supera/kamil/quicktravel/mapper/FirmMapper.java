package com.example.supera.kamil.quicktravel.mapper;

import com.example.supera.kamil.quicktravel.firebase.mapper.FirebaseMapper;
import com.example.supera.kamil.quicktravel.models.Firm;
import com.example.supera.kamil.quicktravel.models.Route;
import com.google.firebase.database.DataSnapshot;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;


public class FirmMapper extends FirebaseMapper<Firm> {

    @Override
    public List<Firm> mapList(DataSnapshot dataSnapshot) {
        Firm firm = new Firm();
        List<Firm> firms = new ArrayList<>();

        for (DataSnapshot ds : dataSnapshot.getChildren()) {
            if (ds.getKey().equals("about")) {
                firm.setAbout(ds.getValue().toString());
            } else if (ds.getKey().equals("name")) {
                firm.setName(ds.getValue().toString());
            } else {
                firm.setAddress(ds.getValue().toString());
            }
        }

        firms.add(firm);
        return firms;
    }

    @Override
    public List<Firm> mapList(Iterable<DataSnapshot> dataSnapshot) {
        return null;
    }
}
