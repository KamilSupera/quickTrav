package com.example.supera.kamil.quicktravel.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.example.supera.kamil.quicktravel.firebase.repository.FirebaseRepository;
import com.example.supera.kamil.quicktravel.models.Route;
import com.example.supera.kamil.quicktravel.repository.RouteRepository;

import java.util.List;

public class AppViewModel extends ViewModel {
    private MutableLiveData<List<Route>> routes;
    private RouteRepository repository = new RouteRepository();

    public LiveData<List<Route>> getRoutes() {
        if (routes == null) {
            routes = new MutableLiveData<>();
            loadRoutes();
        }

        return routes;
    }

    private void loadRoutes() {
        repository.addListener(new FirebaseRepository.FirebaseRepositoryCallback<Route>() {
            @Override
            public void onSuccess(List<Route> result) {
                routes.setValue(result);
            }

            @Override
            public void onError(Exception e) {
                // TODO: FIND PROPER WAY TO INFORM USER ABOUT THIS EXCEPTION.
                routes.setValue(null);
            }
        });
    }
}
