package com.example.supera.kamil.quicktravel.activities;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;

import com.example.supera.kamil.quicktravel.R;
import com.example.supera.kamil.quicktravel.fragments.GoogleMapFragment;
import com.example.supera.kamil.quicktravel.gps_location.CityLookupFail;
import com.example.supera.kamil.quicktravel.gps_location.DeviceDisabled;
import com.example.supera.kamil.quicktravel.gps_location.GPSLocation;
import com.example.supera.kamil.quicktravel.utils.AppViewModelActions;
import com.example.supera.kamil.quicktravel.utils.Utils;
import com.example.supera.kamil.quicktravel.viewmodels.AppViewModel;

import java.io.IOException;
import java.util.Set;


public class MainActivity extends AppCompatActivity {
    public DrawerLayout drawer;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        AppViewModel model = ViewModelProviders.of(this)
            .get(AppViewModel.class);

        //Set custom toolbar pass to actionbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(new MainNavigation(this));

        NavigationView nav2 = findViewById(R.id.likes_nav);
        nav2.setNavigationItemSelectedListener(new LikesNavigation(this));

        Menu menu = navigationView.getMenu();
        Menu likesMenu = nav2.getMenu();
        // Adding subMenu to make give user some description and make it not clickable.
        SubMenu subMenu = menu.addSubMenu(R.string.nav_stop);
        AppViewModelActions.loadStopsToDrawer(this, this, model, subMenu, drawer);

        Utils.rotateBar(drawer, toolbar, this);

        Context context = getApplicationContext();
        SharedPreferences preferences = context.getSharedPreferences(
            getString(R.string.preferences_file_key), Context.MODE_PRIVATE);

        Set<String> likes = preferences.getStringSet("likes", null);

        if (likes != null) {
            likes.forEach(likesMenu::add);
        }

        FragmentManager fragmentManager = this.getSupportFragmentManager();
        Fragment map = new GoogleMapFragment();
        Utils.swapFragment(fragmentManager, map);
    }


    /**
     * Close nav drawer when open. Protect for closed app when drawer nav open.
     */
    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }
    }

    private class MainNavigation implements NavigationView.OnNavigationItemSelectedListener {
        private Activity activity;

        public MainNavigation(Activity activity) {
            this.activity = activity;
        }

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            Intent intent = new Intent(activity, RoutesActivity.class);
            intent.putExtra("title", item.getTitle().toString());
            startActivity(intent);

            drawer.closeDrawer(GravityCompat.START);
            return true;
        }
    }

    private class LikesNavigation implements NavigationView.OnNavigationItemSelectedListener {
        private Activity activity;

        public LikesNavigation(Activity activity) {
            this.activity = activity;
        }


        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            Intent intent = new Intent(activity, RouteDetailActivity.class);
            intent.putExtra("route", menuItem.getTitle().toString());
            startActivity(intent);
            drawer.closeDrawer(GravityCompat.END);

            return true;
        }
    }
}