package com.example.supera.kamil.quicktravel.fragments;

import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.supera.kamil.quicktravel.R;
import com.example.supera.kamil.quicktravel.gps_location.DeviceDisabled;
import com.example.supera.kamil.quicktravel.gps_location.GPSLocation;
import com.example.supera.kamil.quicktravel.models.Stop;
import com.example.supera.kamil.quicktravel.viewmodels.StopViewModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.Objects;

public class GoogleMapFragment extends Fragment {
    private MapView mMapView;
    private GoogleMap googleMap;
    private static float defZoom = 15f;

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.map_fragment, container, false);

        StopViewModel model = ViewModelProviders.of(Objects.requireNonNull(getActivity()))
            .get(StopViewModel.class);

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

            // Update map when new stops are returned.
            model.getStops().observe(this, stops -> {
                if (stops != null) {
                    // Mark stops on map.
                    for (Stop stop : stops) {
                        googleMap.addMarker(new MarkerOptions()
                            .position(stop.getPoint())
                            .title(stop.getName()));
                    }
                }
            });

            GPSLocation gpsLocation = new GPSLocation(getContext(), getActivity());

            try {
                googleMap.addMarker(new MarkerOptions()
                    .position(gpsLocation.getDeviceLocation())
                    .title("Twoja pozycja"));

                googleMap.moveCamera(CameraUpdateFactory
                    .newLatLngZoom(gpsLocation.getDeviceLocation(), defZoom));
            } catch (DeviceDisabled deviceDisabled) {
                deviceDisabled.printStackTrace();
            }
        });

        return rootView;
    }
}
