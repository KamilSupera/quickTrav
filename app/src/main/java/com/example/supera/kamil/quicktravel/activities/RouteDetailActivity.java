package com.example.supera.kamil.quicktravel.activities;

import android.app.Activity;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.view.Menu;
import android.view.MenuItem;
import android.view.SubMenu;

import com.example.supera.kamil.quicktravel.R;
import com.example.supera.kamil.quicktravel.fragments.GoogleMapFragment;
import com.example.supera.kamil.quicktravel.fragments.RatingFragment;
import com.example.supera.kamil.quicktravel.utils.AppViewModelActions;
import com.example.supera.kamil.quicktravel.utils.Utils;
import com.example.supera.kamil.quicktravel.viewmodels.AppViewModel;

import java.util.Map;
import java.util.Set;

public class RouteDetailActivity extends AppCompatActivity {
    private DrawerLayout drawer;
    private String route;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.route_details);

        if (getIntent().getStringExtra("route") != null) {
            route = getIntent().getStringExtra("route");
        } else {
            Context context = getBaseContext();
            SharedPreferences preferences = context.getSharedPreferences(
                getString(R.string.preferences_file_key), Context.MODE_PRIVATE);
            route = preferences.getString("route", null);
        }

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
        SubMenu subMenu = menu.addSubMenu(R.string.nav_route);
        AppViewModelActions.routeDetails(this, model, subMenu, route);

        Utils.rotateBar(drawer, toolbar, this);

        menu.add("Cofnij");

        Context context = getApplicationContext();
        SharedPreferences preferences = context.getSharedPreferences(
            getString(R.string.preferences_file_key), Context.MODE_PRIVATE);

        model.getRoutes().observe(this, routes -> {
           if (routes != null) {
               Map<String, ?> stringMap = preferences.getAll();

               routes.forEach(route1 -> {
                   if (stringMap.containsKey(route1.getName())) {
                       likesMenu.add(route1.getName());
                   }
               });
           }
        });

        Bundle bundle = new Bundle();
        bundle.putString("type", "route_detail");
        bundle.putString("route", route);

        FragmentManager fragmentManager = this.getSupportFragmentManager();
        Fragment map = new GoogleMapFragment();
        map.setArguments(bundle);
        Utils.swapFragment(fragmentManager, map);

        Fragment rating = new RatingFragment();
        rating.setArguments(bundle);
        Utils.swapFragment(fragmentManager, rating, R.id.ratings);
    }

    /**
     * Close nav drawer when open and finish current activity to reload parent.
     */
    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
            super.onBackPressed();
        } else {
            super.onBackPressed();
        }

        setResult(RESULT_OK);
        finish();
    }

    private class MainNavigation implements NavigationView.OnNavigationItemSelectedListener {
        private Activity activity;

        public MainNavigation(Activity activity) {
            this.activity = activity;
        }

        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
            if (menuItem.getTitle().toString().equals("Cofnij")) {
                onBackPressed();
            } else {
                Intent intent = new Intent(activity, StopsActivity.class);
                intent.putExtra("stop", menuItem.getTitle().toString());
                intent.putExtra("route", route);
                startActivity(intent);
                drawer.closeDrawer(GravityCompat.START);
            }

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
