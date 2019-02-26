package com.example.supera.kamil.quicktravel.firebase.listener;

import android.support.annotation.NonNull;

import com.example.supera.kamil.quicktravel.firebase.repository.FirebaseRepository;
import com.example.supera.kamil.quicktravel.firebase.mapper.FirebaseMapper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.ValueEventListener;

import java.util.List;

/**
 * Used to add {@link ValueEventListener} methods to specific model.
 * @param <Model> One of our models from models package.
 */
public class FirebaseListener<Model> implements ValueEventListener {
    private FirebaseRepository.FirebaseRepositoryCallback<Model> callback;
    private FirebaseMapper<Model> mapper;

    public FirebaseListener(FirebaseMapper<Model> mapper,
                            FirebaseRepository.FirebaseRepositoryCallback<Model> callback) {
        this.mapper = mapper;
        this.callback = callback;
    }

    @Override
    public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
        List<Model> data = mapper.mapList(dataSnapshot);
        callback.onSuccess(data);
    }

    @Override
    public void onCancelled(@NonNull DatabaseError databaseError) {
        callback.onError(databaseError.toException());
    }
}
