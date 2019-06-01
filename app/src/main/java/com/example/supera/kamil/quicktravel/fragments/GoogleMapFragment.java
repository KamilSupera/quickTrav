package com.example.supera.kamil.quicktravel.fragments;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.supera.kamil.quicktravel.R;
import com.example.supera.kamil.quicktravel.activities.MainActivity;
import com.example.supera.kamil.quicktravel.activities.RoutesActivity;
import com.example.supera.kamil.quicktravel.gps_location.DeviceDisabled;
import com.example.supera.kamil.quicktravel.gps_location.GPSLocation;
import com.example.supera.kamil.quicktravel.utils.AppViewModelActions;
import com.example.supera.kamil.quicktravel.viewmodels.AppViewModel;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.MapsInitializer;
import com.google.android.gms.maps.model.BitmapDescriptor;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class GoogleMapFragment extends Fragment implements GoogleMap.OnMarkerClickListener {
    private MapView mMapView;
    private GoogleMap googleMap;
    private final float defZoom = 15f;
    private final String userPosition = "Twoja pozycja";

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.map_fragment, container, false);

        AppViewModel model = ViewModelProviders.of(Objects.requireNonNull(getActivity()))
            .get(AppViewModel.class);

        Bundle bundle = getArguments();

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

            googleMap.setOnMarkerClickListener(this);

            GPSLocation gpsLocation = new GPSLocation(getContext(), getActivity());
            AppViewModelActions.mapFragmentViewModelUsage(this, model,
                googleMap, bundle, gpsLocation);

            try {
                LatLng location = gpsLocation.getDeviceLocation();

                googleMap.addMarker(new MarkerOptions()
                    .position(location)
                    .title(userPosition)
                    .icon(BitmapDescriptorFactory.defaultMarker(BitmapDescriptorFactory.HUE_MAGENTA)));

                googleMap.moveCamera(CameraUpdateFactory
                    .newLatLngZoom(location, defZoom));
            } catch (DeviceDisabled deviceDisabled) {
                deviceDisabled.printStackTrace();
            }
        });

        return rootView;
    }

    @Override
    public boolean onMarkerClick(Marker marker) {
        String title = marker.getTitle();

        if (!title.equals(userPosition)) {
            if (getActivity().getClass() == MainActivity.class) {
                Intent intent = new Intent(getActivity(), RoutesActivity.class);
                intent.putExtra("title", title);
                startActivity(intent);
            }
        }

        return false;
    }
}
