package com.example.supera.kamil.quicktravel.models;

import android.support.annotation.NonNull;

/**
 * Model used to read comments with their owner from database.
 */
public class OwnerComment {
    private String owner;
    private Rating rating;
    private String comment;

    public OwnerComment() {}

    public String getOwner() {
        return owner;
    }

    public Rating getRating() {
        return rating;
    }

    public void getComment() {
        return comment;
    }

    public void setRating(Rating rating) {
        this.rating = rating;
    }

    public void setOwner(String owner) {
        this.owner = owner;
    }

    public void setComment(String comment) {
        this.comment = comment;
    }

    @NonNull
    @Override
    public String toString() {
        return owner;
    }
}
