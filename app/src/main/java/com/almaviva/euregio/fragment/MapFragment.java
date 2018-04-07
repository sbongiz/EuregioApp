package com.almaviva.euregio.fragment;

import android.app.SearchManager;
import android.content.Context;
import android.graphics.drawable.TransitionDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.internal.view.SupportMenuItem;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AutoCompleteTextView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.TextView;

import com.almaviva.euregio.R;

import com.almaviva.euregio.behavior.AnchorBottomSheetBehavior;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import java.lang.reflect.Field;


public class MapFragment extends Fragment implements OnMapReadyCallback {

    private OnFragmentInteractionListener mListener;
    private AnchorBottomSheetBehavior mBottomSheetBehavior;
    private View bottomSheet;
    private Marker currentMarker = null;
    private boolean onMarkerClick = false;
    private Toolbar toolbar;
    private SupportMapFragment mapFragment;
    private SearchManager searchManager;
    private SearchView searchView;
    private MenuItem filter;
    private TextView badgeTextView;
    private ImageView searchClose;
    private  TransitionDrawable transition;
    private boolean isFragmentShowing= false;
    private boolean isSearchWhite= false;
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
            setComponent(view);

            mapFragment.getMapAsync(this);

            mBottomSheetBehavior = AnchorBottomSheetBehavior.from(bottomSheet);
            mBottomSheetBehavior.setPeekHeight(350);
            mBottomSheetBehavior.setState(AnchorBottomSheetBehavior.STATE_HIDDEN);

            setHasOptionsMenu(true);

            //region BOTTOM SHEET LISTENER
            mBottomSheetBehavior.setBottomSheetCallback(new AnchorBottomSheetBehavior.BottomSheetCallback() {
                @Override
                public void onStateChanged(@NonNull View bottomSheet, int newState) {

                    if (newState == AnchorBottomSheetBehavior.STATE_EXPANDED) {
                        Log.e("EXP","ANDED");
                        centerMarkerDetail(currentMarker, true);
                    }
                    if (newState == AnchorBottomSheetBehavior.STATE_COLLAPSED) {
                        Log.e("COLL","APSED");
                        if (!onMarkerClick) {

                            centerMarkerDetail(currentMarker, false);
                        }
                    }
                    if(newState == AnchorBottomSheetBehavior.STATE_ANCHOR_POINT){
                        Log.e("ANCHOR","POINT");
                    }
                    onMarkerClick = false;
                }

                @Override
                public void onSlide(@NonNull View bottomSheet, float slideOffset) {
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
                    if (mBottomSheetBehavior.getState() == AnchorBottomSheetBehavior.STATE_COLLAPSED) {
                        mBottomSheetBehavior.setState(AnchorBottomSheetBehavior.STATE_EXPANDED);
                    } else if (mBottomSheetBehavior.getState() == AnchorBottomSheetBehavior.STATE_EXPANDED) {
                        mBottomSheetBehavior.setState(AnchorBottomSheetBehavior.STATE_COLLAPSED);
                    }
                }
            });
//endregion

        } catch (Exception e) {
            Log.e(Thread.currentThread().getStackTrace().toString(), e.toString());
        }

        return view;
    }


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

    private void setComponent(View view) {
        try {
            mapFragment = (SupportMapFragment) getChildFragmentManager().findFragmentById(R.id.map_view);
            bottomSheet = view.findViewById(R.id.bottom_sheet);

        } catch (Exception e) {
            Log.e(Thread.currentThread().getStackTrace().toString(), e.toString());
        }
    }

    private void setMenuComponent(Menu menu) {
        try {
            searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
            searchView = (SearchView) menu.findItem(R.id.searchView).getActionView();
            toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
            filter = menu.findItem(R.id.filter);
            FrameLayout notifCount = (FrameLayout) MenuItemCompat.getActionView(filter);
            badgeTextView = (TextView) notifCount.findViewById(R.id.actionbar_notifcation_textview);
            searchClose = (ImageView) searchView.findViewById(android.support.v7.appcompat.R.id.search_close_btn);
        } catch (Exception e) {
            Log.e(Thread.currentThread().getStackTrace().toString(), e.toString());
        }
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

        googleMap.addMarker(new MarkerOptions().position(lavis).title("Marker in Lavis"));
        googleMap.addMarker(new MarkerOptions().position(lavis1).title("Marker in Lavis1"));
        googleMap.addMarker(new MarkerOptions().position(lavis2).title("Marker in Lavis2"));
        googleMap.addMarker(new MarkerOptions().position(lavis3).title("Marker in Lavis3"));
        googleMap.addMarker(new MarkerOptions().position(lavis4).title("Marker in Lavis4"));
        googleMap.moveCamera(CameraUpdateFactory.newLatLng(lavis));
        setCenterOfMap();

        //region GOOGLE MAP LISTENER
        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                currentMarker = marker;
                centerMarker(currentMarker);

                if (mBottomSheetBehavior.getState() == AnchorBottomSheetBehavior.STATE_HIDDEN || mBottomSheetBehavior.getState() == AnchorBottomSheetBehavior.STATE_EXPANDED) {
                    onMarkerClick = true;
                    mBottomSheetBehavior.setState(AnchorBottomSheetBehavior.STATE_COLLAPSED);
                    mBottomSheetBehavior.setPeekHeight(350);
                }
                return true;
            }
        });
        //endregion
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
        }

        CameraPosition cameraPosition = new CameraPosition.Builder()
                .target(new LatLng(lat, lon))
                .zoom(15)
                .bearing(0)
                .tilt(45)
                .build();
        googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition), 500, null);
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
        void onFragmentInteraction(Uri uri);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.search, menu);

        setMenuComponent(menu);

        customizeSearchView();

        //region MENU LISTENER
        final SupportMenuItem menuNonCompat = (SupportMenuItem) menu.findItem(R.id.searchView);


        menuNonCompat.setSupportOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                Log.d("TAG", "Collapsed");
                changeSearchToNormalMode();
                return true;
            }

            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                Log.d("TAG", "Expanded");
                changeSearchToWhiteMode();
                return true;
            }
        });


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
        //endregion

    }

    private void changeSearchToNormalMode() {
        MenuItemCompat.setActionView(filter, R.layout.badge);
        if(transition==null){
            transition = (TransitionDrawable) toolbar.getBackground();
        }

        if(isSearchWhite){
        transition.reverseTransition(300);
        }
        isSearchWhite =false;
    }

    private void changeSearchToWhiteMode() {
        MenuItemCompat.setActionView(filter, R.layout.badge_grey);

        if(transition==null){
            transition = (TransitionDrawable) toolbar.getBackground();
        }

        transition.startTransition(300);
        isSearchWhite=true;

    }

    private void customizeSearchView() {
        try {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));
            MenuItemCompat.setActionView(filter, R.layout.badge);
            badgeTextView.setText("12");
            searchClose.setImageResource(R.drawable.icon_remove);

            View searchPlate = searchView.findViewById(android.support.v7.appcompat.R.id.search_plate);
            if (searchPlate != null) {
                TextView searchText = (TextView) searchPlate.findViewById(android.support.v7.appcompat.R.id.search_src_text);
                if (searchText != null) {
                    searchText.setTextColor(ContextCompat.getColor(getActivity(), R.color.greyText));
                    searchText.setHintTextColor(ContextCompat.getColor(getActivity(), R.color.greyText));
                }
            }

            AutoCompleteTextView searchTextView = (AutoCompleteTextView) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
            try {
                Field mCursorDrawableRes = TextView.class.getDeclaredField("mCursorDrawableRes");
                mCursorDrawableRes.setAccessible(true);
              //  mCursorDrawableRes.set(searchTextView, R.drawable.search_cursor); //This sets the cursor resource ID to 0 or @null which will make it visible on white background
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            Log.e(Thread.currentThread().getStackTrace().toString(), e.toString());
        }

    }

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);
        if (isVisibleToUser) {
            isFragmentShowing= true;
        }
        else {
            if(isFragmentShowing){
                 changeSearchToNormalMode();
            }
            isFragmentShowing=false;
        }
    }



}
