package com.example.supera.kamil.quicktravel.activities;

import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
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
import com.example.supera.kamil.quicktravel.utils.AppViewModelActions;
import com.example.supera.kamil.quicktravel.utils.Utils;
import com.example.supera.kamil.quicktravel.viewmodels.AppViewModel;


public class RoutesActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {
    public DrawerLayout drawer;
    private FragmentManager fragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.routes);

        String title = getIntent().getStringExtra("title");

        AppViewModel model = ViewModelProviders.of(this)
            .get(AppViewModel.class);

        //Set custom toolbar pass to actionbar
        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        drawer = findViewById(R.id.drawer_layout);
        NavigationView navigationView = findViewById(R.id.nav_view);
        navigationView.setNavigationItemSelectedListener(this);

        Menu menu = navigationView.getMenu();
        // Adding subMenu to make give user some description and make it not clickable.
        SubMenu subMenu = menu.addSubMenu(R.string.nav_routes);
        AppViewModelActions.loadRoutesToDrawer(this, model, subMenu, title);

        Utils.rotateBar(drawer, toolbar, this);

        menu.add("Cofnij");

        fragmentManager = this.getSupportFragmentManager();
        Fragment map = new GoogleMapFragment();
        Bundle bundle = new Bundle();
        bundle.putString("type", "routes");
        bundle.putString("title", title);
        map.setArguments(bundle);
        Utils.swapFragment(fragmentManager, map);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem menuItem) {
        if (menuItem.getTitle().toString().equals("Cofnij")) {
            onBackPressed();
        } else {
            Intent intent = new Intent(this, RouteDetailActivity.class);
            intent.putExtra("route", menuItem.getTitle().toString());
            startActivity(intent);
            drawer.closeDrawer(GravityCompat.START);
        }
        return true;
    }

    /**
     * Close nav drawer when open and finish current activity to reload parent.
     */
    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawer(GravityCompat.START);
        } else {
            super.onBackPressed();
        }

        finish();
    }
}
