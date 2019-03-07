package com.example.supera.kamil.quicktravel.repository;

import com.example.supera.kamil.quicktravel.firebase.repository.FirebaseRepository;
import com.example.supera.kamil.quicktravel.mapper.RouteMapper;
import com.example.supera.kamil.quicktravel.models.Route;

public class RouteRepository extends FirebaseRepository<Route> {

    public RouteRepository() {
        super(new RouteMapper());
    }

    @Override
    protected String getNode() {
        return "routes/";
    }
}
