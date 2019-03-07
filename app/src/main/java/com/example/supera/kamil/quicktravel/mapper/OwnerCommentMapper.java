package com.example.supera.kamil.quicktravel.mapper;

import com.example.supera.kamil.quicktravel.firebase.mapper.FirebaseMapper;
import com.example.supera.kamil.quicktravel.models.OwnerComment;
import com.google.firebase.database.DataSnapshot;

import java.util.ArrayList;
import java.util.List;


public class OwnerCommentMapper extends FirebaseMapper<OwnerComment> {
    @Override
    public List<OwnerComment> mapList(DataSnapshot dataSnapshot) {
        return null;
    }

    @Override
    public List<OwnerComment> mapList(Iterable<DataSnapshot> dataSnapshot) {
        // TODO: Add full map for OwnerComment model.
        List<OwnerComment> comments = new ArrayList<>();

        for (DataSnapshot ds : dataSnapshot) {
            OwnerComment comment = new OwnerComment();

            comments.add(comment);
        }

        return comments;
    }
}
