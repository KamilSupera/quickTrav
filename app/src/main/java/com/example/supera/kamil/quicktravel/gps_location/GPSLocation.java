package com.example.supera.kamil.quicktravel.gps_location;

import android.Manifest;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;

import com.example.supera.kamil.quicktravel.activities.MainActivity;
import com.google.android.gms.maps.model.LatLng;

import java.io.IOException;
import java.util.List;
import java.util.Locale;


public class GPSLocation implements LocationListener {
    private LocationManager locationManager;
    private Activity activity;
    private Context context;
    private boolean permissionGPS = false;
    private boolean permisionNetwork = false;

    public GPSLocation(Context context, Activity activity) {
        this.context = context;
        this.activity = activity;
    }

    /**
     * Loads city name from current GPS position.
     * @return Name of city of current phone position.
     * @throws DeviceDisabled - When GPS is not working on device.
     * @throws IOException - When couldn't read from latitude or longitude from current location.
     * @throws CityLookupFail - When city was not found.
     */
    public String getCityFromCurrentLocation() throws DeviceDisabled, IOException, CityLookupFail {
        LatLng currentLocation = this.getDeviceLocation();

        Geocoder geocoder = new Geocoder(context, Locale.getDefault());
        List<Address> addresses = geocoder.getFromLocation(currentLocation.latitude,
            currentLocation.longitude, 1);

        if (addresses.size() > 0) {
            return addresses.get(0).getLocality();
        } else {
            throw new CityLookupFail();
        }
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