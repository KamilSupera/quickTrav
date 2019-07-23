package com.example.supera.kamil.quicktravel.utils;

import android.app.Activity;
import android.arch.lifecycle.LifecycleOwner;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.widget.DrawerLayout;
import android.view.SubMenu;

import com.android.volley.Request;
import com.android.volley.toolbox.JsonObjectRequest;
import com.example.supera.kamil.quicktravel.VolleyInstance;
import com.example.supera.kamil.quicktravel.gps_location.CityLookupFail;
import com.example.supera.kamil.quicktravel.gps_location.DeviceDisabled;
import com.example.supera.kamil.quicktravel.gps_location.GPSLocation;
import com.example.supera.kamil.quicktravel.models.Route;
import com.example.supera.kamil.quicktravel.models.Stop;
import com.example.supera.kamil.quicktravel.viewmodels.AppViewModel;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
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
                subMenu.clear();
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
                subMenu.clear();
                routes.forEach(route -> {
                    if (route.checkIfRoutePossesStop(title)) {
                        subMenu.add(route.getName());
                    }
                });
            }
        });
    }

    /**
     * Load route's stop to drawer in RouteDetailsActivity.
     * @param owner
     * @param model
     * @param subMenu
     * @param routeName
     */
    public static void routeDetails(LifecycleOwner owner, AppViewModel model,
                                    SubMenu subMenu, String routeName) {
        model.getRoutes().observe(owner, routes -> {
            if (routes != null) {
                subMenu.clear();
                routes.stream().forEach(route -> {
                    if (route.getName().equals(routeName)) {
                        Collections.sort(route.getStops());
                        route.addStopsToDrawer(subMenu, routeName);
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
                                                 GoogleMap googleMap, Bundle bundle, GPSLocation gpsLocation, Activity activity) {
        model.getRoutes().observe(owner, routes -> {
            if (routes != null) {
                if (bundle != null) {
                    String type = bundle.getString("type");

                    if (type.equals("route_detail")) {
                        String routeName = bundle.getString("route");

                        routes.stream().forEach(route -> {
                            if (route.getName().equals(routeName)) {
                                Collections.sort(route.getStops());
                                route.addStopsToMap(googleMap, routeName);

                                getDirections(route, googleMap, activity);
                            }
                        });
                    } else {
                        String title = bundle.getString("title");
                        loadStopsBelongedToRoute(routes, googleMap, title);
                    }
                } else {
                    loadStopsToMap(routes, googleMap, gpsLocation);
                }
            }
        });
    }

    private static void getDirections(Route route, GoogleMap map, Activity activity) {
        String URL = "https://maps.googleapis.com/maps/api/directions/json?";
        final String API_KEY = "key=API_KEY";

        Collections.sort(route.getStops());

        List<Stop> stops = route.getStops();

        LatLng first = stops.get(0).getPoint();
        LatLng last = stops.get(stops.size() - 1).getPoint();

        final String origin = "origin=" + first.latitude + "," + first.longitude;
        final String destination = "destination=" + last.latitude + "," + last.longitude;

        System.out.println(stops.size());

        if (stops.size() == 2) {
            URL += origin + "&" + destination + "&" + API_KEY;
        } else {
            StringBuilder waypoints = new StringBuilder("waypoints=");

            for (int i = 0; i < stops.size(); i++) {
                if (i != 0 || i != stops.size() - 1) {
                    LatLng point = stops.get(i).getPoint();

                    waypoints.append(point.latitude).append(",").append(point.longitude).append("|");
                }
            }

            URL += origin + "&" + waypoints + "&" + destination + "&" + API_KEY;
        }

        JsonObjectRequest request = new JsonObjectRequest(Request.Method.GET, URL, null, response -> {
            try {
                JSONArray routeArray = response.getJSONArray("routes");
                JSONObject routes = routeArray.getJSONObject(0);
                JSONObject overviewPolylines = routes.getJSONObject("overview_polyline");
                String encodedString = overviewPolylines.getString("points");
                List<LatLng> list = decodePoly(encodedString);

                map.addPolyline(new PolylineOptions()
                    .addAll(list)
                    .width(12)
                    .color(Color.parseColor("#05b1fb"))//Google maps blue color
                    .geodesic(true)
                );
            } catch (JSONException e) {
                System.out.println(e.getMessage());
            }
        }, System.out::println);

        VolleyInstance.getInstance(activity).addToRequestQueue(request, "Directions");
    }

    private static List<LatLng> decodePoly(String encoded) {
        List<LatLng> poly = new ArrayList<LatLng>();
        int index = 0, len = encoded.length();
        int lat = 0, lng = 0;

        while (index < len) {
            int b, shift = 0, result = 0;

            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);

            int dlat = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lat += dlat;

            shift = 0;
            result = 0;

            do {
                b = encoded.charAt(index++) - 63;
                result |= (b & 0x1f) << shift;
                shift += 5;
            } while (b >= 0x20);

            int dlng = ((result & 1) != 0 ? ~(result >> 1) : (result >> 1));
            lng += dlng;

            LatLng p = new LatLng( (((double) lat / 1E5)),
                (((double) lng / 1E5) ));
            poly.add(p);
        }

        return poly;
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
