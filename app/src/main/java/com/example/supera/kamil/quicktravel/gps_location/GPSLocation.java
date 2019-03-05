package com.example.supera.kamil.quicktravel.gps_location;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import com.google.android.gms.maps.model.LatLng;


public class GPSLocation implements LocationListener {
    LocationManager locationManager;
    Activity activity;
    Context context;
    private boolean permissionGPS = false;
    private boolean permisionNetwork = false;

    public GPSLocation(Context context, Activity activity) {
        this.context = context;
        this.activity = activity;
    }

    private boolean checkPermission() {
        boolean permissionStatus = true;

        if (permissionChecker(Manifest.permission.ACCESS_FINE_LOCATION)) {
            permissionStatus = false;
        }

        if (permissionChecker(Manifest.permission.ACCESS_COARSE_LOCATION)) {
            permissionStatus = false;
        }

        if (!permissionStatus) {
            String[] permissions = {
                Manifest.permission.ACCESS_FINE_LOCATION,
                Manifest.permission.ACCESS_COARSE_LOCATION
            };

            ActivityCompat.requestPermissions(activity, permissions, 200);
        }

        return permissionStatus;
    }

    private boolean permissionChecker(String permission) {
        return ActivityCompat.checkSelfPermission(context, permission)
            != PackageManager.PERMISSION_GRANTED;
    }

    private void initLocationManager() {
        locationManager = (LocationManager) context.getSystemService(Context.LOCATION_SERVICE);
    }

    private boolean checkDeviceEnabled() {
        initLocationManager();
        permissionGPS = locationManager.isProviderEnabled(LocationManager.GPS_PROVIDER);
        permisionNetwork = locationManager.isProviderEnabled(LocationManager.NETWORK_PROVIDER);

        return permissionGPS || permisionNetwork;
    }

    private LatLng getCurrentLatLng(String provider) {
        Location location = null;
        LatLng latLng = null;
        try {
            if (checkPermission()) {
                locationManager.requestLocationUpdates(
                    provider,
                    50000,
                    10,
                    this
                );
            }

            if (locationManager != null) {
                    latLng =  new LatLng(location.getLatitude(),
                    location.getLongitude()
                );
            }
        }catch (Exception e) {
            e.printStackTrace();
        }

        return latLng;
    }

    private LatLng getCoordinates(String provider) throws DeviceDisabled {
        if (checkPermission()) {
            locationManager.requestLocationUpdates(
                provider,
                60000,
                10,
                this
            );

            Location location = null;

            if (locationManager != null) {
                location = locationManager.getLastKnownLocation(provider);
            }

            if (location != null) {
                return new LatLng(
                    location.getLatitude(),
                    location.getLongitude()
                );
            }
        }

        throw new DeviceDisabled();
    }

    public LatLng getDeviceLocation() throws DeviceDisabled {
        initLocationManager();

        if (checkDeviceEnabled()) {
            if (permisionNetwork) {
                return getCoordinates(LocationManager.NETWORK_PROVIDER);
            }

            if (permissionGPS) {
                return getCoordinates(LocationManager.GPS_PROVIDER);
            }
        }

        throw new DeviceDisabled();
    }


    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }
}