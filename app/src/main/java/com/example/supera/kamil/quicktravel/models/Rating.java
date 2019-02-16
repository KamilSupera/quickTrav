package com.example.supera.kamil.quicktravel.models;

import android.support.annotation.NonNull;

/**
 * Models to add comments into firebase database.
 */
public class Rating {
    private String comment;
    private Float rating;

    public Rating() {}

    public Rating(String comment, Float rating) {
        this.comment = comment;
        this.rating = rating;
    }

    public Float getRating() {
        return rating;
    }

    public String getComment() {
        return comment;
    }

    public void setRating(Float rating) {
        this.rating = rating;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @NonNull
    @Override
    public String toString() {
        return this.comment;
    }
}
