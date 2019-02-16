package com.example.supera.kamil.quicktravel.models;

import android.support.annotation.NonNull;

/**
 * Model used to read comments with their owner from database.
 */
public class OwnerComment {
    private String owner;
    private Rating rating;

    public OwnerComment() {}

    public OwnerComment(String owner, Rating rating) {
        this.owner = owner;
        this.rating = rating;
    }

    public String getOwner() {
        return owner;
    }

    public Rating getRating() {
        return rating;
    }

    public void setRating(Rating rating) {
        this.rating = rating;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    @NonNull
    @Override
    public String toString() {
        return owner;
    }
}
