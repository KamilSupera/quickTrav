package com.example.supera.kamil.quicktravel.models;

import android.support.annotation.NonNull;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;


public class Departure {
    private String type;
    private List<String> time;

    public Departure() {}

    public String getType() {
        return type;
    }

    public List<String> getTime() {
        return time;
    }

    public void setTime(List<String> time) {
        this.time = time;
    }

    public void setType(String type) {
        this.type = type;
    }

    public HashMap<String, List<String>> mapToDict() {
        Collections.sort(this.time);
        HashMap<String, List<String>> dict = new HashMap<>();

        for (String time : this.time) {
            String[] timeSplit = time.split(":");
            String hour = timeSplit[0];
            String minute = timeSplit[1];

            List<String> minutes = dict.get(hour);

            if (minutes == null) {
                minutes = new ArrayList<>();
            }

            minutes.add(minute);
            dict.put(hour, minutes);
        }

        return dict;
    }

    public List<String> hoursOnly() {
        Collections.sort(this.time);
        return this.time
            .stream()
            .map(time -> time.split(":")[0])
            .distinct()
            .collect(Collectors.toList());
    }

    @NonNull
    @Override
    public String toString() {
        return type;
    }
}
