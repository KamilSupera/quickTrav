package com.example.supera.kamil.quicktravel.activities;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;


public class SplashScreen extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        startActivity(new Intent(SplashScreen.this, MainActivity.class));

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

            ActivityCompat.requestPermissions(this, permissions, 200);
        }

        finish();
    }

    private boolean permissionChecker(String permission) {
        return ActivityCompat.checkSelfPermission(this, permission)
            != PackageManager.PERMISSION_GRANTED;
    }
}
