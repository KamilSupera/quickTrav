package com.example.supera.kamil.quicktravel.mapper;

import com.example.supera.kamil.quicktravel.firebase.mapper.FirebaseMapper;
import com.example.supera.kamil.quicktravel.models.OwnerComment;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import static java.lang.Double.parseDouble;


public class OwnerCommentMapper extends FirebaseMapper<OwnerComment> {
    @Override
    public List<OwnerComment> mapList(DataSnapshot dataSnapshot) {
        return null;
    }

    @Override
    public List<OwnerComment> mapList(Iterable<DataSnapshot> dataSnapshot) {
        List<OwnerComment> comments = new ArrayList<>();

        for (DataSnapshot ds : dataSnapshot) {
            OwnerComment comment = new OwnerComment();
            comment.setOwner(ds.getKey());
            loadRating(comment, ds);
            comments.add(comment);
        }

        return comments;
    }

    private void loadRating(OwnerComment comment, DataSnapshot dataSnapshot) {
       for (DataSnapshot ds : dataSnapshot.getChildren()) {
           (Objects.equals(ds.getKey(), "comment")) ?
               comment.setComment(ds.getValue().toString()) :
               comment.setRating(parseDouble(ds.getValue().toString()));
       } 
    }
}
