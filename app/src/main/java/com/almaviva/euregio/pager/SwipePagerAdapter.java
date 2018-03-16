package com.almaviva.euregio.pager;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.util.Log;

import com.almaviva.euregio.fragment.EuregioFragment;
import com.almaviva.euregio.fragment.ImpostazioniFragment;
import com.almaviva.euregio.fragment.ListaFragment;
import com.almaviva.euregio.fragment.MapFragment;

/**
 * Created by ascia on 15/03/2018.
 */

public class SwipePagerAdapter extends FragmentPagerAdapter {
    int mNumOfTabs;

    public SwipePagerAdapter(FragmentManager fm, int NumOfTabs) {
        super(fm);
        this.mNumOfTabs = NumOfTabs;
    }

    @Override
    public Fragment getItem(int position) {

        try {
            switch (position) {
                case 0:
                    ListaFragment tab1 = new ListaFragment();
                    return tab1;
                case 1:
                    MapFragment tab2 = new MapFragment();
                    return tab2;
                default:
                    return null;
            }
        }catch (Exception e){
            Log.e(Thread.currentThread().getStackTrace().toString(), e.toString());
            return null;
        }

    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
