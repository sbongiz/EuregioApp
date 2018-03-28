package com.almaviva.euregio.fragment;

import android.app.SearchManager;
import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.graphics.PorterDuff;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.CoordinatorLayout;
import android.support.design.widget.TabLayout;
import android.support.v4.app.Fragment;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.ImageView;


import com.almaviva.euregio.R;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;


public class MapFragment extends Fragment implements OnMapReadyCallback {

    private OnFragmentInteractionListener mListener;
    private BottomSheetBehavior mBottomSheetBehavior;
    private View bottomSheet;
    private Marker currentMarker = null;
    private VantaggiFragment parentFragment;
    private TabLayout tab;
    private FrameLayout frame;
    private boolean isDetailCentered = false;
    private boolean onMarkerClick = false;
    private ActionBar actionBar;
    private Toolbar toolBar;
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


            frame = (FrameLayout) view.findViewById(R.id.map_frame);
            setHasOptionsMenu(true);
            toolBar = (Toolbar) getActivity().findViewById(R.id.toolbar);

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
        LatLng lavis = new LatLng(46.497641, 11.355022);
        LatLng lavis1 = new LatLng(46.498118, 11.355330);
        LatLng lavis2 = new LatLng(46.498162, 11.354413);
        LatLng lavis3 = new LatLng(46.498609, 11.354370);
        LatLng lavis4 = new LatLng(46.498565, 11.355169);
        LatLng lavis5 = new LatLng(46.498255, 11.354762);

        googleMap.addMarker(new MarkerOptions().position(lavis).title("Marker in Lavis"));
        googleMap.addMarker(new MarkerOptions().position(lavis1).title("Marker in Lavis1"));
        googleMap.addMarker(new MarkerOptions().position(lavis2).title("Marker in Lavis2"));
        googleMap.addMarker(new MarkerOptions().position(lavis3).title("Marker in Lavis3"));
        googleMap.addMarker(new MarkerOptions().position(lavis4).title("Marker in Lavis4"));
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

   @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.search, menu);
        SearchManager searchManager =
                (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
       MenuItem mSearchMenuItem = menu.findItem(R.id.searchView);
       final SearchView searchView =(SearchView) menu.findItem(R.id.searchView).getActionView();
        searchView.setSearchableInfo(
                searchManager.getSearchableInfo(getActivity().getComponentName()));


        actionBar = ((AppCompatActivity)getActivity()).getSupportActionBar() ;

        final MenuItem searchMenuItem = menu.findItem(R.id.searchView);

        final MenuItem filter = menu.findItem(R.id.filter);

        MenuItemCompat.setOnActionExpandListener(searchMenuItem, new MenuItemCompat.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                Log.d("TAG", "Collapsed");

                actionBar.setBackgroundDrawable(new ColorDrawable(0xFFd32f2f));
                return true;
            }

            @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                Log.d("TAG", "Expanded");

                //getActivity().setTheme(R.style.SearchTheme);
                //actionBar.getna .getNavigationIcon().setColorFilter(getResources().getColor(R.color.blue_gray_15), PorterDuff.Mode.SRC_ATOP);
                ImageView searchClose = (ImageView) searchView.findViewById(android.support.v7.appcompat.R.id.search_close_btn);
                searchClose.setImageResource(R.drawable.icon_remove);


                actionBar.setBackgroundDrawable(new ColorDrawable(0xFFFFFFFF));

                return true;
            }
        });




       //toolBar.getNavigationIcon().setColorFilter(getResources().getColor(R.color.yellow), PorterDuff.Mode.SRC_ATOP);

      //MenuItem filter = menu.findItem(R.id.filter);
      //MenuItemCompat.setActionView(filter,R.layout.badge);
      //RelativeLayout notifCount = (RelativeLayout) MenuItemCompat.getActionView(filter);

      //TextView tv = (TextView) notifCount.findViewById(R.id.actionbar_notifcation_textview);
      //tv.setText("12");

       // View searchPlate = (View) searchView.findViewById(android.support.v7.appcompat.R.id.search_plate);
       // if (searchPlate!=null) {
       //     TextView searchText = (TextView) searchPlate.findViewById(android.support.v7.appcompat.R.id.search_src_text);
       //     if (searchText!=null) {
       //         searchText.setTextColor(Color.BLACK);
       //         searchText.setHintTextColor(Color.BLACK);
       //     }
       // }


        //EditText searchEditText = (EditText) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
        //searchEditText.setTextColor(Color.RED);
        //searchEditText.setHintTextColor(Color.RED);
        //searchEditText.setBackgroundColor(Color.WHITE);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {

                return true;

            }

        });
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
