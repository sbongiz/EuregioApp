package com.almaviva.euregio;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.view.MenuItem;
import android.widget.TextView;

import com.almaviva.euregio.adapter.BottomBarAdapter;
import com.almaviva.euregio.fragment.VantaggiFragment;
import com.almaviva.euregio.pager.NoSwipePager;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigation;
import com.aurelhubert.ahbottomnavigation.AHBottomNavigationItem;

public class MainActivity extends AppCompatActivity {


    private TextView mTextMessage;
    private AHBottomNavigation bottomNavigation;
    private NoSwipePager viewPager;
    private BottomBarAdapter pagerAdapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        bottomNavigation = (AHBottomNavigation) findViewById(R.id.bottom_navigation);
        mTextMessage = (TextView) findViewById(R.id.textView);
        viewPager.setPagingEnabled(false);
        pagerAdapter = new BottomBarAdapter(getSupportFragmentManager());

        VantaggiFragment van = new VantaggiFragment();
        pagerAdapter.addFragments(van);
        viewPager.setAdapter(pagerAdapter);

        bottomNavigation.setCurrentItem(0);
        createNavigationButton();

        bottomNavigation.setOnTabSelectedListener(new AHBottomNavigation.OnTabSelectedListener() {
            @Override
            public void onTabSelected(int position, boolean wasSelected) {
                if(position==0) {
                    mTextMessage.setText(getResources().getString(R.string.title_vantaggi));
                }
                if(position == 1){
                    mTextMessage.setText(getResources().getString(R.string.title_euregio));
                }
                if(position == 2){
                    mTextMessage.setText(getResources().getString(R.string.title_impostazioni));
                }
            }
        });
    }


    private void createNavigationButton() {

        AHBottomNavigationItem item1 = new AHBottomNavigationItem(getResources().getString(R.string.title_vantaggi), R.drawable.ic_dashboard_black_24dp);
        AHBottomNavigationItem item2 = new AHBottomNavigationItem(getResources().getString(R.string.title_euregio), R.drawable.ic_home_black_24dp);
        AHBottomNavigationItem item3 = new AHBottomNavigationItem(getResources().getString(R.string.title_impostazioni), R.drawable.ic_notifications_black_24dp);
        bottomNavigation.addItem(item1);
        bottomNavigation.addItem(item2);
        bottomNavigation.addItem(item3);
    }
}
