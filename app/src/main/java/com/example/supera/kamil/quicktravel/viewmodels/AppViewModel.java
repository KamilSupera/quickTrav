package com.example.supera.kamil.quicktravel.viewmodels;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.MutableLiveData;
import android.arch.lifecycle.ViewModel;

import com.example.supera.kamil.quicktravel.firebase.repository.FirebaseRepository;
import com.example.supera.kamil.quicktravel.models.Bus;
import com.example.supera.kamil.quicktravel.models.Route;
import com.example.supera.kamil.quicktravel.repository.BusRepository;
import com.example.supera.kamil.quicktravel.repository.RouteRepository;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;

import java.util.List;
import java.util.stream.Collectors;

public class AppViewModel extends ViewModel {
    private MutableLiveData<List<Route>> routes;
    private MutableLiveData<List<Bus>> buses;

    private String routeName;
    private RouteRepository repository = new RouteRepository();
    private BusRepository busRep = new BusRepository();

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

    public LiveData<List<Bus>> getBuses() {
        if (buses == null) {
            buses = new MutableLiveData<>();
            loadBuses();
        }

        return buses;
    }

    private void loadBuses() {
        busRep.addListener(new FirebaseRepository.FirebaseRepositoryCallback<Bus>() {
            @Override
            public void onSuccess(List<Bus> result) {
                buses.setValue(result);
            }

            @Override
            public void onError(Exception e) {
                buses.setValue(null);
            }
        });
    }

    public void setRouteName(String name) {
        routeName = name;
    }

    /**
     * Changes `rating` and `votes` properties in firebase database.
     * This action is used inside {@link com.example.supera.kamil.quicktravel.fragments.RateDialog}
     * fragment when user rates {@link Route}.
     * @param userRating - Value from {@link android.widget.RatingBar}.
     * @return Rated route name or null when no route was loaded from firebase earlier.
     */
    public String rate(Float userRating) {
        Route route = getRouteByName();

        if (route != null) {
            DatabaseReference ref = FirebaseDatabase
                .getInstance()
                .getReference("root/routes/" + routeName);

            route.changeRating(userRating);

            ref.child("votes").setValue(route.getVotes());
            ref.child("rating").setValue(route.getRating());

            return routeName;
        }

        return null;
    }

    /**
     * Get route by it's name using property `routeName` inside this class.
     * @return String or null when any route was not found.
     */
    private Route getRouteByName() {
        if (routes.getValue() != null) {
            return routes.getValue()
                .stream()
                .filter(route -> route.getName().equals(routeName))
                .collect(Collectors.toList())
                .get(0);
        } else {
            return null;
        }
    }
}
