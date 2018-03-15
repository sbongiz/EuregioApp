package com.almaviva.euregio.adapter;

import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentStatePagerAdapter;

import com.almaviva.euregio.fragment.EuregioFragment;
import com.almaviva.euregio.fragment.ImpostazioniFragment;

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

        switch (position) {
            case 0:
                EuregioFragment tab1 = new EuregioFragment();
                return tab1;
            case 1:
                ImpostazioniFragment tab2 = new ImpostazioniFragment();
                return tab2;
            default:
                return null;
        }

    }

    @Override
    public int getCount() {
        return mNumOfTabs;
    }
}
