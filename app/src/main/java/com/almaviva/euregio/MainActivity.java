package com.almaviva.euregio;

import android.app.ActionBar;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.MenuItem;
import android.widget.TextView;

import com.almaviva.euregio.adapter.BottomBarAdapter;
import com.almaviva.euregio.fragment.EuregioFragment;
import com.almaviva.euregio.fragment.ImpostazioniFragment;
import com.almaviva.euregio.fragment.ListaFragment;
import com.almaviva.euregio.fragment.MapFragment;
import com.almaviva.euregio.fragment.VantaggiFragment;
import com.almaviva.euregio.pager.NoSwipePager;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;

public class MainActivity extends AppCompatActivity implements  VantaggiFragment.OnFragmentInteractionListener,
                                                                EuregioFragment.OnFragmentInteractionListener,
                                                                ImpostazioniFragment.OnFragmentInteractionListener,
                                                                ListaFragment.OnFragmentInteractionListener,
                                                                MapFragment.OnFragmentInteractionListener{


    private TextView mTextMessage;
    private BottomNavigationView bottomNavigation;
    private NoSwipePager viewPager;
    private BottomBarAdapter pagerAdapter;
    private VantaggiFragment vantaggiFragment;
    private EuregioFragment euregioFragment;
    private ImpostazioniFragment impostazioniFragment;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        try {


            setComponent();

            viewPager.setPagingEnabled(false);
            pagerAdapter.addFragments(vantaggiFragment);
            pagerAdapter.addFragments(euregioFragment);
            pagerAdapter.addFragments(impostazioniFragment);

            viewPager.setAdapter(pagerAdapter);

            //bottomNavigation.setCurrentItem(0);


        //    bottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
        //        @Override
        //        public void onTabSelected(int position, boolean wasSelected) {
        //         if(!wasSelected){
        //          bottomNavigation.setCurrentItem(position);
        //             viewPager.setCurrentItem(position);
        //         }

        //        }
        //    });

            bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    // handle desired action here
                    // One possibility of action is to replace the contents above the nav bar
                    // return true if you want the item to be displayed as the selected itemù
                    int id = item.getItemId();
                    if(id == R.id.navigation_vantaggi){
                    viewPager.setCurrentItem(0);
                    }
                    if(id == R.id.navigation_euregio){
                        viewPager.setCurrentItem(1);
                    }
                    if(id == R.id.navigation_impostazioni){
                        viewPager.setCurrentItem(2);
                    }
                    return true;
                }
            });

        } catch (Exception e) {
            Log.e(Thread.currentThread().getStackTrace().toString(), e.toString());
        }
    }


    //region SET COMPONENT
    private void setComponent() {
        try {
            //Find and initialize the view component
            findComponentInView();
            //Initialize the fragment
            setFragment();
            //Create the ButtonNavigationMenu
            //createNavigationButton();
            bottomNavigation.setBackground(new ColorDrawable(256));
            pagerAdapter = new BottomBarAdapter(getSupportFragmentManager());
        } catch (Exception e) {
            Log.e(Thread.currentThread().getStackTrace().toString(), e.toString());
        }
    }

    private void findComponentInView() {
        bottomNavigation = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        mTextMessage = (TextView) findViewById(R.id.textView);
        viewPager = (NoSwipePager) findViewById(R.id.pager);
    }

    private void setFragment() {
        vantaggiFragment = new VantaggiFragment();
        euregioFragment = new EuregioFragment();
        impostazioniFragment = new ImpostazioniFragment();
    }

  // private void createNavigationButton() {

  //     try {

  //         AHBottomNavigationItem item1 = new AHBottomNavigationItem(getResources().getString(R.string.title_vantaggi), R.drawable.ic_dashboard_black_24dp);
  //         AHBottomNavigationItem item2 = new AHBottomNavigationItem(getResources().getString(R.string.title_euregio), R.drawable.ic_home_black_24dp);
  //         AHBottomNavigationItem item3 = new AHBottomNavigationItem(getResources().getString(R.string.title_impostazioni), R.drawable.ic_notifications_black_24dp);
  //         bottomNavigation.addItem(item1);
  //         bottomNavigation.addItem(item2);
  //         bottomNavigation.addItem(item3);
  //     } catch (Exception e) {
  //         Log.e(Thread.currentThread().getStackTrace().toString(), e.toString());
  //     }
  // }
    //endregion

    //region LISTENER
    @Override
    public void onFragmentInteraction(Uri uri) {
        String mtva = "";
    }


    //endregion

}
