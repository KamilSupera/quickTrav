package com.example.supera.kamil.quicktravel.firebase.repository;

import com.example.supera.kamil.quicktravel.firebase.listener.FirebaseListener;
import com.example.supera.kamil.quicktravel.firebase.mapper.FirebaseMapper;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;

/**
 * Base class of this whole magic with repositories, mappers and listener.
 * Destiny of this repository is to join everything for one of our model classes.
 * It connects {@link FirebaseListener} class and  {@link FirebaseMapper} with it's child.
 * Also defines {@link FirebaseRepositoryCallback} callbacks for
 * {@link com.google.firebase.database.ValueEventListener#onDataChange(DataSnapshot)} (onSuccess)
 * and {@link com.google.firebase.database.ValueEventListener#onCancelled(DatabaseError)} (onError)
 * to then easily inject it into {@link android.arch.lifecycle.ViewModel} class.
 *
 *
 * Using description:
 * 1. Add Repository class for model extended with {@link FirebaseRepository} class.
 *     Add to constructor mapper used in point 2.
 *     Then override there {@link FirebaseRepository#getNode()} method to specify Firebase root node.
 * 2. Add Mapper class extended with {@link FirebaseMapper} class. Write {@link FirebaseMapper#mapList(DataSnapshot)}
 *     to map Firebase data loaded from listener into model fields.
 * 3. Add repository to ViewModel and give it listener with {@link FirebaseRepositoryCallback} like this:
 *     repository.addListener(new FirebaseRepository.FirebaseRepositoryCallback<Stop>() {});
 *
 *
 * @param <Model> One of our models from models package.
 */
public abstract class FirebaseRepository<Model> {
    protected DatabaseReference ref;
    protected FirebaseRepositoryCallback<Model> callback;

    private String rootNode = "root/";
    private FirebaseListener listener;
    private FirebaseMapper<Model> mapper;

    /**
     * Specifies root node to read data from Firebase database.
     * @return String with root node.
     */
    protected abstract String getNode();

    /**
     * Initialize Firabase database reference and load specific mapper.
     * @param mapper
     */
    public FirebaseRepository(FirebaseMapper<Model> mapper) {
        ref = FirebaseDatabase
            .getInstance()
            .getReference(rootNode + getNode());

        this.mapper = mapper;
    }

    /**
     * Add listener to database reference.
     * @param callback
     */
    public void addListener(FirebaseRepositoryCallback<Model> callback) {
        this.callback = callback;
        listener = new FirebaseListener(mapper, callback);
        ref.addValueEventListener(listener);
    }

    /**
     * Remove listener from database reference.
     */
    public void deleteListener() {
        ref.removeEventListener(listener);
    }

    public interface FirebaseRepositoryCallback<T> {
        void onSuccess(List<T> result);

        void onError(Exception e);
    }
}
