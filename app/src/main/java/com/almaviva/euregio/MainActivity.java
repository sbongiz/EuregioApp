package com.almaviva.euregio;

import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.almaviva.euregio.adapter.BottomBarAdapter;
import com.almaviva.euregio.fragment.EuregioFragment;
import com.almaviva.euregio.fragment.ImpostazioniFragment;
import com.almaviva.euregio.fragment.ListaFragment;
import com.almaviva.euregio.fragment.LoginFragment;
import com.almaviva.euregio.fragment.MapFragment;
import com.almaviva.euregio.fragment.VantaggiFragment;
import com.almaviva.euregio.helper.LocalStorage;
import com.almaviva.euregio.pager.NoSwipePager;

public class MainActivity extends AppCompatActivity implements VantaggiFragment.OnFragmentInteractionListener,
        EuregioFragment.OnFragmentInteractionListener,
        ImpostazioniFragment.OnFragmentInteractionListener,
        ListaFragment.OnFragmentInteractionListener,
        MapFragment.OnFragmentInteractionListener,
        LoginFragment.OnFragmentInteractionListener {


    private BottomNavigationView bottomNavigation;
    private NoSwipePager viewPager;
    private BottomBarAdapter pagerAdapter;
    private ListaFragment listaFragment;
    private EuregioFragment euregioFragment;
    private LoginFragment loginFragment;
    private ImpostazioniFragment impostazioniFragment;
    private MapFragment mapFragment;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        try {


            setComponent();

            checkIfLogged();

            viewPager.setPagingEnabled(false);
            pagerAdapter.addFragments(listaFragment);
            pagerAdapter.addFragments(mapFragment);
            pagerAdapter.addFragments(euregioFragment);
            pagerAdapter.addFragments(impostazioniFragment);
            pagerAdapter.addFragments(loginFragment);

            viewPager.setAdapter(pagerAdapter);


            bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    // handle desired action here
                    // One possibility of action is to replace the contents above the nav bar
                    // return true if you want the item to be displayed as the selected itemù
                    int id = item.getItemId();
                    if (id == R.id.navigation_vantaggi) {
                        viewPager.setCurrentItem(0);
                    }
                    if (id == R.id.navigation_mappa) {
                        viewPager.setCurrentItem(1);
                    }
                    if (id == R.id.navigation_euregio) {
                        if (LocalStorage.getIsLogged()) {
                            viewPager.setCurrentItem(2);

                        } else {
                            viewPager.setCurrentItem(4);
                        }
                    }
                    if (id == R.id.navigation_impostazioni) {
                        viewPager.setCurrentItem(3);
                    }


                    return true;
                }
            });

        } catch (Exception e) {
            Log.e(Thread.currentThread().getStackTrace().toString(), e.toString());
        }
    }

    private void checkIfLogged() {

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
        viewPager = (NoSwipePager) findViewById(R.id.pager);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(0xFFFFFFFF);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private void setFragment() {
        listaFragment = new ListaFragment();
        loginFragment = new LoginFragment();
        euregioFragment = new EuregioFragment();
        impostazioniFragment = new ImpostazioniFragment();
        mapFragment = new MapFragment();

    }

 //  @Override
 //  public void onWindowFocusChanged(boolean hasFocus) {
 //      super.onWindowFocusChanged(hasFocus);
 //      if (hasFocus) {
 //          getWindow().getDecorView().setSystemUiVisibility(
 //                  View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
 //                          | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
 //      }
 //  }
    //endregion

    //region LISTENER
    @Override
    public void onFragmentInteraction(Uri uri) {
    }


    //endregion


}
