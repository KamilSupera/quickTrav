package com.example.supera.kamil.quicktravel.repository;

import com.example.supera.kamil.quicktravel.firebase.repository.FirebaseRepository;
import com.example.supera.kamil.quicktravel.mapper.BusMapper;
import com.example.supera.kamil.quicktravel.models.Bus;

public class BusRepository extends FirebaseRepository<Bus> {
    public BusRepository() {
        super(new BusMapper());
    }

    @Override
    protected String getNode() {
        return "bus/";
    }
}

