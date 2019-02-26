package com.example.supera.kamil.quicktravel.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;
import android.support.annotation.NonNull;
import android.widget.Toast;

import com.example.supera.kamil.quicktravel.firebase.repository.FirebaseRepository;
import com.example.supera.kamil.quicktravel.models.Departure;
import com.example.supera.kamil.quicktravel.models.Stop;
import com.example.supera.kamil.quicktravel.repository.StopRepository;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static java.lang.Double.parseDouble;

/**
 * Handle all data about stops. Used to store data between more than one
 * activity/fragment which uses stops data.
 */
public class StopViewModel extends ViewModel {
    private MutableLiveData<List<Stop>> stops;
    private StopRepository repository = new StopRepository();

    public LiveData<List<Stop>> getStops() {
        if (stops == null) {
            stops = new MutableLiveData<>();
            loadStops();
        }

        return stops;
    }

    @Override
    protected void onCleared() {
        repository.deleteListener();
    }

    /**
     * Loads all stops from database and sets MutableLiveData with loaded stops.
     */
    private void loadStops() {
        repository.addListener(new FirebaseRepository.FirebaseRepositoryCallback<Stop>() {
            @Override
            public void onSuccess(List<Stop> result) {
                stops.setValue(result);
            }

            @Override
            public void onError(Exception e) {
                // TODO: FIND PROPER WAY TO INFORM USER ABOUT THIS EXCEPTION.
                stops.setValue(null);
            }
        });
    }
}
