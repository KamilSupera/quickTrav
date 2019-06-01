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
import java.util.Objects;

public class AboutViewModel extends ViewModel {
    private MutableLiveData<List<Route>> routes;
    private MutableLiveData<List<Firm>> firms;

    private RouteRepository repository = new RouteRepository();
    private FirmRepository firmRepository = new FirmRepository();

    public LiveData<List<Firm>> getFirm() {
        if (firms == null) {
            firms = new MutableLiveData<>();
            loadFirm();
        }

        return firms;
    }

    private void loadFirm() {
        firmRepository.addListener(new FirebaseRepository.FirebaseRepositoryCallback<Firm>() {
            @Override
            public void onSuccess(List<Firm> result) {
                firms.setValue(result);
            }

            @Override
            public void onError(Exception e) {
                // TODO: FIND PROPER WAY TO INFORM USER ABOUT THIS EXCEPTION.
                firms.setValue(null);
            }
        });
    }

    public String getFirmAddress() {
        if (getFirm().getValue() != null) {
            Firm firm = Objects.requireNonNull(getFirm().getValue()).get(0);

            return firm.getAddress();
        } else {
            return "";
        }
    }

    public String getFirmName() {
        if (getFirm().getValue() != null) {
            Firm firm = Objects.requireNonNull(getFirm().getValue()).get(0);

            return firm.getName();
        } else {
            return "";
        }
    }

    public String getFirmAbout() {
        if (getFirm().getValue() != null) {
            Firm firm = Objects.requireNonNull(getFirm().getValue()).get(0);

            return firm.getAbout();
        } else {
            return "";
        }
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
