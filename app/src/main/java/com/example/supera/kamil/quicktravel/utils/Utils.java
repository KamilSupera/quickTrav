package com.example.supera.kamil.quicktravel.utils;

import android.app.Activity;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.widget.Toolbar;

import com.example.supera.kamil.quicktravel.R;

public class Utils {

    /**
     * Replace fragment with specific container.
     * @param manager
     * @param fragment
     */
    public static void swapFragment(FragmentManager manager, Fragment fragment) {
        FragmentTransaction fragmentTransaction = manager.beginTransaction();
        fragmentTransaction.replace(R.id.fragment_container, fragment);
        fragmentTransaction.addToBackStack(null);
        fragmentTransaction.commit();
    }

    /**
     * Add bar in toolbar to open navigation drawer.
     * @param drawer
     * @param toolbar
     * @param activity
     */
    public static void rotateBar(DrawerLayout drawer, Toolbar toolbar, Activity activity) {
        //Rotate menu icon
        ActionBarDrawerToggle toggle = new ActionBarDrawerToggle(activity, drawer, toolbar,
            R.string.nav_drawer_open, R.string.nav_drawer_close);
        drawer.addDrawerListener(toggle);
        toggle.syncState();
    }
}
