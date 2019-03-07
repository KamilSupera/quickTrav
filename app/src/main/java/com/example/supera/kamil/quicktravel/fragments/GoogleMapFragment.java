package com.example.supera.kamil.quicktravel.fragments;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.supera.kamil.quicktravel.R;
import com.example.supera.kamil.quicktravel.gps_location.CityLookupFail;
import com.example.supera.kamil.quicktravel.gps_location.DeviceDisabled;
import com.example.supera.kamil.quicktravel.gps_location.GPSLocation;
import com.example.supera.kamil.quicktravel.viewmodels.AppViewModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;

import java.io.IOException;
import java.util.Objects;

public class GoogleMapFragment extends Fragment {
    private MapView mMapView;
    private GoogleMap googleMap;
    private final float defZoom = 15f;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.map_fragment, container, false);

        AppViewModel model = ViewModelProviders.of(Objects.requireNonNull(getActivity()))
            .get(AppViewModel.class);

        mMapView = rootView.findViewById(R.id.mapView);
        mMapView.onCreate(savedInstanceState);

        mMapView.onResume();

        try {
            MapsInitializer.initialize(Objects.requireNonNull(getActivity())
                .getApplicationContext());
        } catch (Exception e) {
            e.printStackTrace();
        }

        // Map is ready to display.
        mMapView.getMapAsync(mMap -> {
            googleMap = mMap;

            GPSLocation gpsLocation = new GPSLocation(getContext(), getActivity());

            model.getRoutes().observe(this, routes -> {
                if (routes != null) {
                    routes.forEach(route -> {
                        try {
                            route.addStopsToMap(googleMap, gpsLocation.getCityFromCurrentLocation());
                        } catch (DeviceDisabled deviceDisabled) {
                            deviceDisabled.printStackTrace();
                            // TODO ADD BETTER EXCEPTION HANDLING
                        } catch (IOException e) {
                            e.printStackTrace();
                        } catch (CityLookupFail cityLookupFail) {
                            cityLookupFail.printStackTrace();
                        }
                    });
                }
            });

            try {
                LatLng location = gpsLocation.getDeviceLocation();

                googleMap.addMarker(new MarkerOptions()
                    .position(location)
                    .title("Twoja pozycja"));

                googleMap.moveCamera(CameraUpdateFactory
                    .newLatLngZoom(location, defZoom));
            } catch (DeviceDisabled deviceDisabled) {
                deviceDisabled.printStackTrace();
            }
        });

        return rootView;
    }
}
