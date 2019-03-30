package com.example.supera.kamil.quicktravel.fragments;

import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Typeface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.supera.kamil.quicktravel.NoStop;
import com.example.supera.kamil.quicktravel.R;
import com.example.supera.kamil.quicktravel.activities.RouteDetailActivity;
import com.example.supera.kamil.quicktravel.models.Departure;
import com.example.supera.kamil.quicktravel.viewmodels.AppViewModel;

import java.util.HashMap;
import java.util.List;
import java.util.Objects;

/**
 * Displays timetable for stop clicked by user.
 * Requires three arguments which needs to be passed to fragment's bundle when initializing:
 *     * routeName - name of route which stop belong.
 *     * stopName - name of stop clicked by user in {@link RouteDetailActivity}.
 *     * type - type of timetable i.e weekend or worker
 *
 * Basic process of creating timetable look like this:
 *     1. Check which type is sent to fragment(weekend or worker) and show header text depended on that.
 *     2. Get from all departures for stop clicked by user belonged to earlier choosen route.
 *     3. Sort time list in ascending order then create set(distinct values only) with hours of times from database.
 *     4. When set is created map time string list into hashmap looking like this:
 *         10=[20, 50], where 10 is an hour and 20, 50 are minutes. This gives the possibility to divide
 *         timetable into hours and minutes belonging to hours.
 *     5. After all that is done loop every hour in set and create horizontal linear layout for every of them
 *         which contains textviews with hour and minutes.
 */
public class DeparturesWorker extends Fragment {

    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.departures_worker, container, false);

        Bundle bundle = getArguments();

        String stopName = bundle.getString("stopName");
        String type = bundle.getString("type");

        TextView typeView = rootView.findViewById(R.id.type);

        if (type.equals("worker")) {
            typeView.setText(R.string.header_worker);
        } else {
            typeView.setText(R.string.header_weekend);
        }

        TextView stopNameTextView = rootView.findViewById(R.id.stopName);
        stopNameTextView.setText(stopName);

        AppViewModel model = ViewModelProviders.of(Objects.requireNonNull(getActivity()))
            .get(AppViewModel.class);

        model.getRoutes().observe(this, routes -> {
            if (routes != null) {
                routes.forEach(route -> {
                    if (route.getName().equals(bundle.getString("routeName"))) {
                        try {
                            List<Departure> departures = route.getStopDepartures(
                                stopName, type);
                            LinearLayout root = rootView.findViewById(R.id.mainLinear);

                            createTimeTable(departures, root);
                        } catch (NoStop noStop) {
                            Toast.makeText(getContext(), "Brak przystanku. Spróbuj ponownie",
                                Toast.LENGTH_LONG).show();
                        }
                    }
                });
            }
        });

        return rootView;
    }

    private void createTimeTable(List<Departure> departures, LinearLayout root) {
        for (Departure departure : departures) {
            List<String> hours = departure.hoursOnly();
            HashMap<String, List<String>> map = departure.mapToDict();

            for (String hour : hours) {
                List<String> minutes = map.get(hour);
                LinearLayout linearLayout = new LinearLayout(getActivity());
                linearLayout.setOrientation(LinearLayout.HORIZONTAL);
                linearLayout.addView(hourTextView(hour));

                for (String minute : minutes) {
                    linearLayout.addView(minuteTextView(minute));
                }

                root.addView(linearLayout);
            }
        }
    }

    private TextView hourTextView(String hour) {
        TextView textView = new TextView(getActivity());
        textView.setWidth(200);
        textView.setHeight(100);
        textView.setTypeface(null, Typeface.BOLD);
        textView.setText(hour);
        return textView;
    }

    private TextView minuteTextView(String minute) {
        TextView textView = new TextView(getActivity());
        textView.setWidth(100);
        textView.setHeight(100);
        textView.setText(minute);
        return textView;
    }
}
