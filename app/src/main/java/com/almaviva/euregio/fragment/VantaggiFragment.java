package com.almaviva.euregio.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.PagerAdapter;
import android.support.v4.view.ViewPager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.almaviva.euregio.R;
import com.almaviva.euregio.pager.SwipePagerAdapter;


public class VantaggiFragment extends Fragment {


    private OnFragmentInteractionListener mListener;
    private ViewPager viewPager;
    private PagerAdapter pagerAdapter;
    private TabLayout tabLayout;

    public VantaggiFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        // Specify that tabs should be displayed in the action bar.


    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_vantaggi, container, false);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {

        setComponent();
        viewPager.addOnPageChangeListener(new TabLayout.TabLayoutOnPageChangeListener(tabLayout));
        tabLayout.setOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                try {
                    viewPager.setCurrentItem(tab.getPosition());

                }catch (Exception e){
                    Log.e(Thread.currentThread().getStackTrace().toString(), e.toString());
                }
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        super.onActivityCreated(savedInstanceState);
    }

    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }


    public void setComponent(){
        try {
            tabLayout = getView().findViewById(R.id.tab_layout);
            tabLayout.addTab(tabLayout.newTab().setText(getResources().getString(R.string.tab_lista)));
            tabLayout.addTab(tabLayout.newTab().setText(getResources().getString(R.string.tab_mappa)));
            tabLayout.setTabGravity(TabLayout.GRAVITY_FILL);
            viewPager = getView().findViewById(R.id.view_pager);
            pagerAdapter = new SwipePagerAdapter(getChildFragmentManager(), tabLayout.getTabCount());
            viewPager.setAdapter(pagerAdapter);
        }catch(Exception e){
            Log.e(Thread.currentThread().getStackTrace().toString(), e.toString());
        }
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }


}
