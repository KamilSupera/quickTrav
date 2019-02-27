package com.example.supera.kamil.quicktravel.firebase.mapper;

import com.google.firebase.database.DataSnapshot;

import java.util.List;

/**
 * Inject function for mapping {@link DataSnapshot} object into one of our models class.
 * @param <Model> One of our models from models package.
 */
public abstract class FirebaseMapper<Model> {
    public abstract List<Model> mapList(DataSnapshot dataSnapshot);
}
