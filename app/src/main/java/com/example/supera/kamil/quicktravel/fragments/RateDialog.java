package com.example.supera.kamil.quicktravel.fragments;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.v4.app.DialogFragment;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;

import com.example.supera.kamil.quicktravel.R;

public class RateDialog extends DialogFragment {

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        LayoutInflater inflater = requireActivity().getLayoutInflater();

        builder.setView(inflater.inflate(R.layout.rate_dialog, null))
            .setPositiveButton(R.string.rating_text_view, this::rate)
            .setNegativeButton("Cofnij", this::cancelDialog);
        return builder.create();
    }

    private void rate(DialogInterface dialog, int id) {

    }

    private void cancelDialog(DialogInterface dialog, int id) {
        RateDialog.this.getDialog().cancel();
    }
}