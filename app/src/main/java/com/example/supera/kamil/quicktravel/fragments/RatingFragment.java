package com.example.supera.kamil.quicktravel.fragments;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.example.supera.kamil.quicktravel.R;
import com.example.supera.kamil.quicktravel.models.Route;
import com.example.supera.kamil.quicktravel.viewmodels.AppViewModel;

import java.util.HashSet;
import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

public class RatingFragment extends Fragment implements View.OnClickListener{
    private String routeName;
    private FloatingActionButton likeButton;
    private FloatingActionButton unlike;
    private Button rate;
    private List<Route> allRoutes;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.rating, container, false);

        AppViewModel model = ViewModelProviders.of(Objects.requireNonNull(getActivity()))
            .get(AppViewModel.class);

        model.getRoutes().observe(this, routes -> {
            if (routes != null) {
                allRoutes = routes;
            }
        });

        likeButton = rootView.findViewById(R.id.likeButton);
        unlike = rootView.findViewById(R.id.unlikeButton);
        rate = rootView.findViewById(R.id.rating_button);
        rate.setOnClickListener(this);

        Bundle bundle = getArguments();

        if (bundle != null) {
            routeName = bundle.getString("route");

            likeButton.setOnClickListener(this);
            unlike.setOnClickListener(this);

            Context context = getActivity();
            SharedPreferences preferences = context.getSharedPreferences(
                getString(R.string.preferences_file_key), Context.MODE_PRIVATE);
            Set<String> likes = preferences.getStringSet("likes", null);

            if (likes != null) {
                List<String> exist = likes.stream().filter(like -> like.equals(routeName)).collect(Collectors.toList());

                if (exist.size() > 0) {
                    likeButton.hide();
                } else {
                    unlike.hide();
                }
            } else {
                unlike.hide();
            }
        } else {
            likeButton.hide();
            unlike.hide();
        }

        return rootView;
    }

    @Override
    public void onClick(View v) {
        Context context = getActivity();
        SharedPreferences preferences = context.getSharedPreferences(
            getString(R.string.preferences_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        Set<String> likes = preferences.getStringSet("likes", null);

        switch (v.getId()) {
            case R.id.likeButton:
                if (likes == null) {
                    likes = new HashSet<>();
                    likes.add(routeName);
                    editor.putStringSet("likes", likes);
                    editor.commit();
                    likeButton.hide();
                    unlike.show();
                } else {
                    likes.add(routeName);
                    editor.putStringSet("likes", likes);
                    editor.commit();
                    likeButton.hide();
                    unlike.show();
                }

                break;
            case R.id.unlikeButton:
                likes.remove(routeName);

                if (likes.size() > 0) {
                    editor.putStringSet("likes", likes);
                } else {
                    editor.remove("likes");
                }

                editor.commit();
                likeButton.show();
                unlike.hide();

                break;
            case R.id.rating_button:
                System.out.println(this.allRoutes);
                RateDialog dialog = new RateDialog();
                dialog.show(getActivity().getSupportFragmentManager(), "Test");
                break;
            default:
                break;
        }
    }
}
