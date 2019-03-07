package com.example.supera.kamil.quicktravel.mapper;

import com.example.supera.kamil.quicktravel.firebase.mapper.FirebaseMapper;
import com.example.supera.kamil.quicktravel.models.Firm;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.List;


public class FirmMapper extends FirebaseMapper<Firm> {

    @Override
    public List<Firm> mapList(DataSnapshot dataSnapshot) {
        return null;
    }

    @Override
    public List<Firm> mapList(Iterable<DataSnapshot> dataSnapshot) {
        List<Firm> firms = new ArrayList<>();

        for (DataSnapshot ds : dataSnapshot) {
            Firm firm = new Firm();
            firm.setName(ds.getKey());
            firm.setAddress(ds.child("address").getValue().toString());
            firms.add(firm);
        }

        return firms;
    }
}
