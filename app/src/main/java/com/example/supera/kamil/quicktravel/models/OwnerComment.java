package com.example.supera.kamil.quicktravel.models;

import android.support.annotation.NonNull;

/**
 * Model used to read comments with their owner from database.
 */
public class OwnerComment {
    private String owner;
    private Double rating;
    private String comment;

    public OwnerComment() {}

    public String getOwner() {
        return owner;
    }

    public Double getRating() {
        return rating;
    }

    public String getComment() {
        return comment;
    }

    public void setRating(Double rating) {
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
