package com.example.supera.kamil.quicktravel.firebase.mapper;

import com.google.firebase.database.DataSnapshot;

import java.util.List;

/**
 ************************************************************************
 * Inject function for mapping {@link DataSnapshot} object              *
 * into Route class.                                                    *
 *                                                                      *
 * Now important thing!                                                 *
 *                                                                      *
 * As you see I added second mapList abstract method.                   *
 * Why?                                                                 *
 * This method: {@link DataSnapshot#getChildren()} is returning for     *
 * us Iterable<DataSnapshot> instead of DataSnapshot.                   *
 * Okay so you may think: "where this guy see a problem,                *
 * he can just change type". So I can't. We need both of those method   *
 * because DataSnapshot is class used for reading from loaded root      *
 * of database reference and Iterable<DataSnapshot> is used when I want *
 * to map children node.                                                *
 *                                                                      *
 * So I thought about two ways of resolving this problem:               *
 * 1. Using optionals parameters with two (Of course this               *
 *     do not work cause it's JAVA!)                                    *
 * 2. Using overloading(I believe you know what this is,                *
 *     don't worry tho I will try my best to describe it for you).      *
 *     Overloading is mechanism used to generate methods with same name *
 *     with different type. There is one thing which i hate about this  *
 *     solution. I must always override both methods in child classes   *
 *     even when I don't use one of those methods.                      *
 *                                                                      *
 * @param <Model> One of our models from models package.                *
 ************************************************************************/
public abstract class FirebaseMapper<Model> {
    public abstract List<Model> mapList(DataSnapshot dataSnapshot);

    public abstract List<Model> mapList(Iterable<DataSnapshot> dataSnapshot);
}
