package com.example.supera.kamil.quicktravel.repository;

import com.example.supera.kamil.quicktravel.firebase.repository.FirebaseRepository;
import com.example.supera.kamil.quicktravel.mapper.StopMapper;
import com.example.supera.kamil.quicktravel.models.Stop;

public class StopRepository extends FirebaseRepository<Stop> {

    public StopRepository() {
        super(new StopMapper());
    }

    @Override
    protected String getNode() {
        return "stops/";
    }
}
