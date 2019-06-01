package com.example.supera.kamil.quicktravel.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.example.supera.kamil.quicktravel.firebase.repository.FirebaseRepository;
import com.example.supera.kamil.quicktravel.models.Firm;
import com.example.supera.kamil.quicktravel.models.Route;
import com.example.supera.kamil.quicktravel.repository.FirmRepository;
import com.example.supera.kamil.quicktravel.repository.RouteRepository;

import java.util.List;

public class AboutViewModel extends ViewModel {
    private MutableLiveData<List<Route>> routes;
    private MutableLiveData<List<Firm>> firm;

    private RouteRepository repository = new RouteRepository();
    private FirmRepository firmRepository = new FirmRepository();

    public LiveData<List<Firm>> getFirm() {
        if (firm == null) {
            firm = new MutableLiveData<>();
            loadFirm();
        }

        return firm;
    }

    private void loadFirm() {
        firmRepository.addListener(new FirebaseRepository.FirebaseRepositoryCallback<Firm>() {
            @Override
            public void onSuccess(List<Firm> result) {
                firm.setValue(result);
            }

            @Override
            public void onError(Exception e) {
                // TODO: FIND PROPER WAY TO INFORM USER ABOUT THIS EXCEPTION.
                firm.setValue(null);
            }
        });
    }

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
