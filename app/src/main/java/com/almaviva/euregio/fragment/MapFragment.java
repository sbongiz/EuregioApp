package com.almaviva.euregio.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.BottomSheetBehavior;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.almaviva.euregio.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


public class MapFragment extends Fragment implements OnMapReadyCallback {

    private OnFragmentInteractionListener mListener;
    private MapView mapView;
    private BottomSheetBehavior mBottomSheetBehavior;
    private View bottomSheet;
    GoogleMap googleMap;

    public MapFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        try {
            SupportMapFragment mapFragment = (SupportMapFragment) getChildFragmentManager()
                    .findFragmentById(R.id.map_view);
            mapFragment.getMapAsync(this);

            bottomSheet = view.findViewById(R.id.bottom_sheet);
            mBottomSheetBehavior = BottomSheetBehavior.from(bottomSheet);
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);

            mBottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
                @Override
                public void onStateChanged(View bottomSheet, int newState) {

                    if (newState == BottomSheetBehavior.STATE_COLLAPSED) {

                        Log.e("STATE CHANGE","STATE COLLAPSED");
                    }
                    if (newState == BottomSheetBehavior.STATE_DRAGGING){
                        Log.e("STATE CHANGE", "STATE DRAGGING");
                    }
                    if(newState == BottomSheetBehavior.STATE_EXPANDED){
                        Log.e("STATE CHANGE", "STATE EXPANDED");
                    }
                    if(newState == BottomSheetBehavior.STATE_HIDDEN){
                        Log.e("STATE CHANGE", "STATE HIDDEN");

                    }
                    if(newState == BottomSheetBehavior.STATE_SETTLING){
                       Log.e("STATE CHANGE", "STATE SETTLING");
                    }
                    if(newState == BottomSheetBehavior.PEEK_HEIGHT_AUTO){
                        Log.e("STATE CHANGE", "STATE HEIGHT AUTO");
                    }
                }
                @Override
                public void onSlide(View bottomSheet, float slideOffset) {

                }
            });

        } catch (Exception e) {
            Log.e(Thread.currentThread().getStackTrace().toString(), e.toString());
        }

        return view;
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

    @Override
    public void onMapReady(GoogleMap mappa) {
        googleMap = mappa;

        // Add a marker in Sydney and move the camera
        LatLng lavis = new LatLng(46.14, 11.10);
        googleMap.addMarker(new MarkerOptions().position(lavis).title("Marker in Lavis"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(lavis));
        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {

                Log.e("CURRENT STATE", String.valueOf(mBottomSheetBehavior.getState()));
               if(mBottomSheetBehavior.getState()!= BottomSheetBehavior.STATE_EXPANDED){
                   mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                   Log.e("PEEK HEIGHT EXPAND", String.valueOf(mBottomSheetBehavior.getPeekHeight()));
                   mBottomSheetBehavior.setPeekHeight(200);
               }else{
                   mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                   Log.e("PEEK HEIGHT COLLAPSE", String.valueOf(mBottomSheetBehavior.getPeekHeight()));
                    mBottomSheetBehavior.setPeekHeight(0);
               }
                Log.e("AFTER HEIGHT", String.valueOf(String.valueOf(mBottomSheetBehavior.getPeekHeight())));


                return  true;
            }
        });
    }


    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
