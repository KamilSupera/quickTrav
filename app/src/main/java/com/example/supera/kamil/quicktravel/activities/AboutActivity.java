package com.example.supera.kamil.quicktravel.activities;

import android.annotation.SuppressLint;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.widget.TextView;

import com.example.supera.kamil.quicktravel.R;
import com.example.supera.kamil.quicktravel.viewmodels.AboutViewModel;

import java.util.stream.Collectors;

public class AboutActivity extends AppCompatActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.about);

        AboutViewModel model = ViewModelProviders.of(this)
            .get(AboutViewModel.class);

        initializeFirmTextViews(model);

        getRoutesInformation(model);
    }

    @SuppressLint("SetTextI18n")
    private void initializeFirmTextViews(AboutViewModel model) {
        TextView addressTv = findViewById(R.id.address);
        TextView nameTv = findViewById(R.id.name);
        TextView aboutTv = findViewById(R.id.about);

        model.getFirm().observe(this, firms -> {
            if (firms != null) {
                addressTv.setText("Adres przewoźnika: " + model.getFirmAddress());
                nameTv.setText("Nazwa przewoźnika: " + model.getFirmName());
                aboutTv.setText("O przewoźniku: " + model.getFirmAbout());
            }
        });
    }

    @SuppressLint("SetTextI18n")
    private void getRoutesInformation(AboutViewModel model) {
        TextView quantityTv = findViewById(R.id.quantity);
        TextView availableRoutesTv = findViewById(R.id.available);

        model.getRoutes().observe(this, routes -> {
            if (routes != null) {
                quantityTv.setText("Liczba tras: " + routes.size());

                availableRoutesTv.setText("Dostępne trasy: \n" + TextUtils.join("\n",
                    routes.stream().map(route -> route.getName()).collect(Collectors.toList())));
            }
        });
    }
}
