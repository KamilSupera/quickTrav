package com.example.supera.kamil.quicktravel.utils;

import android.app.Activity;
import android.arch.lifecycle.LifecycleOwner;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.SubMenu;

import com.example.supera.kamil.quicktravel.gps_location.CityLookupFail;
import com.example.supera.kamil.quicktravel.gps_location.DeviceDisabled;
import com.example.supera.kamil.quicktravel.gps_location.GPSLocation;
import com.example.supera.kamil.quicktravel.models.Route;
import com.example.supera.kamil.quicktravel.models.Stop;
import com.example.supera.kamil.quicktravel.viewmodels.AppViewModel;
import com.google.android.gms.maps.GoogleMap;

import java.io.IOException;
import java.util.List;

/**
 * Holds all usage of {@link AppViewModel}.
 * I think it's better to make specific class for it
 * and keep everything in one place and then call it in activity or fragment
 * rather than use those observe methods there.
 */
public class AppViewModelActions {

    /**
     * Load stops for routes with specific city name read from gps.
     * Used in {@link com.example.supera.kamil.quicktravel.activities.MainActivity}
     * @param activity - Calling activity.
     * @param owner - Calling owner.
     * @param model - Initialized AppViewModel
     * @param subMenu - SubMenu to add items.
     * @param drawer - DrawerLayout containing SubMenu
     */
    public static void loadStopsToDrawer(Activity activity, LifecycleOwner owner, AppViewModel model,
                                         SubMenu subMenu, DrawerLayout drawer) {
        GPSLocation gps = new GPSLocation(activity.getBaseContext(), activity);

        model.getRoutes().observe(owner, routes -> {
            if (routes != null) {
                routes.forEach(route -> {
                    try {
                        route.addStopsToDrawer(subMenu, gps.getCityFromCurrentLocation());
                    } catch (DeviceDisabled | IOException | CityLookupFail deviceDisabled) {
                        deviceDisabled.printStackTrace();
                        // TODO ADD BETTER EXCEPTION HANDLING
                    }
                });
            }
        });
    }

    /**
     * Load routes for stop clicked by user.
     * Used in {@link com.example.supera.kamil.quicktravel.activities.RoutesActivity}
     * @param owner
     * @param model
     * @param subMenu
     * @param title
     */
    public static void loadRoutesToDrawer(LifecycleOwner owner, AppViewModel model,
                                          SubMenu subMenu, String title) {
        model.getRoutes().observe(owner, routes -> {
            if (routes != null) {
                routes.forEach(route -> {
                    if (route.checkIfRoutePossesStop(title)) {
                        subMenu.add(route.getName());
                    }
                });
            }
        });
    }

    /**
     * All usage of observe in:
     * {@link com.example.supera.kamil.quicktravel.fragments.GoogleMapFragment}
     * @param owner
     * @param model
     * @param googleMap
     * @param bundle
     * @param gpsLocation
     */
    public static void mapFragmentViewModelUsage(LifecycleOwner owner, AppViewModel model,
                                                 GoogleMap googleMap, Bundle bundle, GPSLocation gpsLocation) {
        model.getRoutes().observe(owner, routes -> {
            if (bundle != null) {
                String title = bundle.getString("title");

                if (routes != null) {
                    loadStopsBelongedToRoute(routes, googleMap, title);
                }
            } else {
                if (routes != null) {
                    loadStopsToMap(routes, googleMap, gpsLocation);
                }
            }
        });
    }

    /**
     * Load all stops belonged to routes wchich posses stop clicked by user. Used when moving between
     * {@link com.example.supera.kamil.quicktravel.activities.MainActivity} and
     * {@link com.example.supera.kamil.quicktravel.activities.RoutesActivity}
     * @param routes
     * @param googleMap
     * @param title
     */
    private static void loadStopsBelongedToRoute(List<Route> routes, GoogleMap googleMap, String title) {
        routes.forEach(route -> {
            if (route.checkIfRoutePossesStop(title)) {
                route.addStopsToMapWithOneChangedColor(googleMap, title);
            }
        });
    }

    /**
     * Load all stops from database when routes contains city name read from gps.
     * @param routes
     * @param googleMap
     * @param gpsLocation
     */
    private static void loadStopsToMap(List<Route> routes, GoogleMap googleMap, GPSLocation gpsLocation) {
        routes.forEach(route -> {
            try {
                route.addStopsToMap(googleMap, gpsLocation.getCityFromCurrentLocation());
            } catch (DeviceDisabled | IOException | CityLookupFail deviceDisabled) {
                deviceDisabled.printStackTrace();
                // TODO ADD BETTER EXCEPTION HANDLING
            }
        });
    }
}
