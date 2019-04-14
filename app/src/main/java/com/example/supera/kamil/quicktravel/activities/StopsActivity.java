package com.example.supera.kamil.quicktravel.activities;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.GravityCompat;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v7.app.AppCompatActivity;

import com.example.supera.kamil.quicktravel.R;
import com.example.supera.kamil.quicktravel.fragments.DeparturesWorker;

/**
 * Activity responsible for displaying all departures for single stops.
 * It uses special widget from support library {@link ViewPager} to swipe screen beetwen
 * departures types i.e worker or weekend.
 *
 * It cointains 3 private fields:
 *     * routeName - name of route which stop belong.
 *     * stopName - name of stop clicked by user in {@link RouteDetailActivity}.
 *     * PAGE_COUNT - Number of pages swipe-able pages by ViewPager.
 *
 * It implements {@link ScreenSlidePagerAdapter} adapter to set number of pages and what
 * should fragment be associated to specific page.
 *
 * Currently there is only one fragment for both pages {@link DeparturesWorker}. That fragment
 * contains all logic for making and showing timetable for clicked stop. It requires three bundle parameters:
 *     * routeName - name of route which stop belong.
 *     * stopName - name of stop clicked by user in {@link RouteDetailActivity}.
 *     * type - type of timetable i.e weekend or worker
 */
public class StopsActivity extends AppCompatActivity {
    private String routeName;
    private String stopName;
    private final int PAGE_COUNT = 2;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.departures);

        routeName = getIntent().getStringExtra("route");
        stopName = getIntent().getStringExtra("stop");

        ViewPager mPager = findViewById(R.id.pager);
        PagerAdapter pagerAdapter = new ScreenSlidePagerAdapter(getSupportFragmentManager());
        mPager.setAdapter(pagerAdapter);
    }

    private class ScreenSlidePagerAdapter extends FragmentStatePagerAdapter {
        public ScreenSlidePagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            Fragment fragment = new DeparturesWorker();
            Bundle bundle = new Bundle();
            bundle.putString("routeName", routeName);
            bundle.putString("stopName", stopName);

            if (position == 0) {
                bundle.putString("type", "worker");
            } else {
                bundle.putString("type", "weekend");
            }

            fragment.setArguments(bundle);
            return fragment;
        }

        @Override
        public int getCount() {
            return PAGE_COUNT;
        }
    }

    @Override
    public void onBackPressed() {
        finish();
        super.onBackPressed();
    }
}
