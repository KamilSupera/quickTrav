package com.example.supera.kamil.quicktravel.fragments;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

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
    private AppViewModel viewModel;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.rating, container, false);

        viewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity()))
            .get(AppViewModel.class);

        likeButton = rootView.findViewById(R.id.likeButton);
        unlike = rootView.findViewById(R.id.unlikeButton);
        rootView.findViewById(R.id.rating_button).setOnClickListener(this);

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

        // Make TextView with rating reactive with ViewModel obervable.
        viewModel.getRoutes().observe(getActivity(), routes -> {
            if (routes != null) {
                Route route = routes.stream()
                    .filter(routeFilter -> routeFilter.getName().equals(routeName))
                    .collect(Collectors.toList())
                    .get(0);

                TextView ratingText = rootView.findViewById(R.id.ratingText);
                ratingText.setText(String.format(route.getRating().toString(), "%.2f"));
            }
        });

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

                editor.commit();

                if (likes.size() > 0) {
                    editor.putStringSet("likes", likes);
                } else {
                    editor.remove("likes");
                }
                likeButton.show();
                unlike.hide();

                break;
            case R.id.rating_button:
                // If user didn't vote for this route show dialog, otherwise show toast.
                if (!didUserVote(this.routeName)) {
                    viewModel.setRouteName(this.routeName);
                    RateDialog dialog = new RateDialog();
                    dialog.show(getActivity().getSupportFragmentManager(), "Test");
                } else {
                    Toast.makeText(getContext(), "Już głosowałeś na tę trase", Toast.LENGTH_LONG).show();
                }
                break;
            default:
                break;
        }
    }

    /**
     * Check if user voted for this route.
     * @param name
     * @return
     */
    private boolean didUserVote(String name) {
        Context context = getActivity();
        SharedPreferences preferences = context.getSharedPreferences(
            getString(R.string.preferences_file_key), Context.MODE_PRIVATE);
        Set<String> votes = preferences.getStringSet("votes", null);

        if (votes == null) {
            return false;
        } else {
            return votes.contains(routeName);
        }
    }
}
