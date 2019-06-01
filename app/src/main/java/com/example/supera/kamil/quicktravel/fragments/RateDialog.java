package com.example.supera.kamil.quicktravel.fragments;

import android.app.Dialog;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.RatingBar;

import com.example.supera.kamil.quicktravel.R;
import com.example.supera.kamil.quicktravel.viewmodels.AppViewModel;

import java.util.HashSet;
import java.util.Objects;
import java.util.Set;

/**
 * This class is responsible for actions inside rating dialog.
 */
public class RateDialog extends DialogFragment {
    private AppViewModel viewModel;
    private RatingBar ratingBar;

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        viewModel = ViewModelProviders.of(Objects.requireNonNull(getActivity()))
            .get(AppViewModel.class);

        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        View view = inflater.inflate(R.layout.rate_dialog, null);
        ratingBar = view.findViewById(R.id.ratingBar);

        builder.setView(view)
            .setPositiveButton(R.string.rating_text_view, this::rate)
            .setNegativeButton("Cofnij", this::cancelDialog);

        return builder.create();
    }

    /**
     * Starts `rate` action from view model and saves vote to HashSet.
     * @param dialog
     * @param id
     */
    private void rate(DialogInterface dialog, int id) {
        addVote(viewModel.rate(ratingBar.getRating()));
    }

    private void cancelDialog(DialogInterface dialog, int id) {
        RateDialog.this.getDialog().cancel();
    }

    /**
     * Saves vote to votes HashSet to determine later for which route user already voted.
     * @param routeName
     */
    private void addVote(String routeName) {
        Context context = getActivity();
        SharedPreferences preferences = context.getSharedPreferences(
            getString(R.string.preferences_file_key), Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = preferences.edit();
        Set<String> votes = preferences.getStringSet("votes", null);

        if (votes == null) {
            votes = new HashSet<>();
            votes.add(routeName);
            editor.putStringSet("votes", votes);
            editor.commit();
        } else {
            votes.add(routeName);
            editor.putStringSet("votes", votes);
            editor.commit();
        }
    }
}