package com.example.supera.kamil.quicktravel.repository;

import com.example.supera.kamil.quicktravel.firebase.repository.FirebaseRepository;
import com.example.supera.kamil.quicktravel.mapper.FirmMapper;
import com.example.supera.kamil.quicktravel.mapper.RouteMapper;
import com.example.supera.kamil.quicktravel.models.Firm;
import com.example.supera.kamil.quicktravel.models.Route;

public class FirmRepository extends FirebaseRepository<Firm> {

    public FirmRepository() {
        super(new FirmMapper());
    }

    @Override
    protected String getNode() {
        return "firm/";
    }
}
