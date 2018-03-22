package com.almaviva.euregio.fragment;

import android.content.Context;
import android.content.res.Resources;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.DecelerateInterpolator;
import android.view.animation.TranslateAnimation;
import android.widget.FrameLayout;

import com.almaviva.euregio.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


public class MapFragment extends Fragment implements OnMapReadyCallback {

    private OnFragmentInteractionListener mListener;
    private MapView mapView;
    private BottomSheetBehavior mBottomSheetBehavior;
    private View bottomSheet;
    private Marker currentMarker = null;
    private VantaggiFragment parentFragment;
    private TabLayout tab;
    private FrameLayout frame;
    private boolean isDetailCentered = false;
    private boolean onMarkerClick = false;
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
            mBottomSheetBehavior.setPeekHeight(350);
            mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_HIDDEN);


            parentFragment = (VantaggiFragment) this.getParentFragment();

            tab = parentFragment.getView().findViewById(R.id.tab_layout);
            frame = view.findViewById(R.id.map_frame);

            //region BOTTOM SHEET LISTENER


            mBottomSheetBehavior.setBottomSheetCallback(new BottomSheetBehavior.BottomSheetCallback() {
                @Override
                public void onStateChanged(@NonNull View bottomSheet, int newState) {

                    if (newState == BottomSheetBehavior.STATE_EXPANDED) {

                        centerMarkerDetail(currentMarker, true);


                    }
                    if (newState == BottomSheetBehavior.STATE_COLLAPSED) {
                        if (!onMarkerClick) {
                            centerMarkerDetail(currentMarker, false);
                        }

                    }
                    onMarkerClick = false;
                }


            @Override
            public void onSlide (@NonNull View bottomSheet,float slideOffset){
                // React to dragging events
            }
        });


        bottomSheet.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                return true;
            }
        });

       bottomSheet.setOnClickListener(new View.OnClickListener() {

           @Override
           public void onClick(View view) {

               CoordinatorLayout.LayoutParams lp = new CoordinatorLayout.LayoutParams(CoordinatorLayout.LayoutParams.MATCH_PARENT, CoordinatorLayout.LayoutParams.MATCH_PARENT);
               if (mBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED) {
                   mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
                   // lp.setMargins(0,0,0,convertDpIntoPx(56));
                   // frame.setLayoutParams(lp);
               } else if (mBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                   mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
               }
           }
       });

      //  bottomSheet.setOnTouchListener(new View.OnTouchListener() {

      //      @RequiresApi(api = Build.VERSION_CODES.KITKAT)
      //      @Override
      //      public boolean onTouch(View view, MotionEvent motionEvent) {

      //          //CoordinatorLayout.LayoutParams lp = new CoordinatorLayout.LayoutParams(CoordinatorLayout.LayoutParams.MATCH_PARENT, CoordinatorLayout.LayoutParams.MATCH_PARENT);

      //          if (mBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_COLLAPSED && motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
      //              mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_EXPANDED);
      //          } else if (mBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED && motionEvent.getAction() == MotionEvent.ACTION_MOVE) {
      //              mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
      //          }
      //          return false;
      //      }
      //  });

//endregion

    } catch(Exception e){
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
        setCenterOfMap();
        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                currentMarker = marker;

                centerMarker(currentMarker);

                if (mBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_HIDDEN || mBottomSheetBehavior.getState() == BottomSheetBehavior.STATE_EXPANDED) {
                    onMarkerClick = true;
                    mBottomSheetBehavior.setState(BottomSheetBehavior.STATE_COLLAPSED);
                    mBottomSheetBehavior.setPeekHeight(350);
                }


                return true;
            }
        });
    }

    public void centerMarker(Marker marker) {
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(marker.getPosition().latitude, marker.getPosition().longitude))
                .zoom(15)
                .bearing(0)
                .tilt(45)
                .build();
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
    }

    public void centerMarkerDetail(Marker marker, boolean isDetailCentered) {


        double lat = marker.getPosition().latitude;
        double lon = marker.getPosition().longitude;
        if (isDetailCentered) {
            lat = lat - 0.006;
        } else {
            //lat remains equal
        }

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(lat, lon))
                .zoom(15)
                .bearing(0)
                .tilt(45)
                .build();
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition),500,null);
    }

    public void setCenterOfMap() {
        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(46.673211, 11.428050)) //Alto Adige center
                .zoom(8)
                .bearing(0)
                .tilt(45)
                .build();
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));

    }

public interface OnFragmentInteractionListener {
    // TODO: Update argument type and name
    void onFragmentInteraction(Uri uri);

}

    public int convertDpIntoPx(int dpMeasure) {
        Resources r = getResources();
        int px = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP,
                dpMeasure,
                r.getDisplayMetrics()
        );
        return px;
    }
}
