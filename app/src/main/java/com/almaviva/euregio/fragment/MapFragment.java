package com.almaviva.euregio.fragment;

import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.TransitionDrawable;
import android.media.Image;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.internal.view.SupportMenuItem;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.view.accessibility.AccessibilityManagerCompat;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.AutoCompleteTextView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.almaviva.euregio.MainActivity;
import com.almaviva.euregio.R;

import com.almaviva.euregio.behavior.AnchorBottomSheetBehavior;
import com.almaviva.euregio.helper.BottomSheet2DialogFragment;
import com.almaviva.euregio.helper.BottomSheet3DialogFragment;
import com.almaviva.euregio.helper.LocalStorage;
import com.almaviva.euregio.model.Product;
import com.almaviva.euregio.model.Product;
import com.almaviva.euregio.model.Supplier;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;

import org.w3c.dom.Document;
import org.w3c.dom.Text;

import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.List;


public class MapFragment extends Fragment implements OnMapReadyCallback {

    private OnFragmentInteractionListener mListener;
    private AnchorBottomSheetBehavior mBottomSheetBehavior;
    private View bottomSheet;
    private View vantaggiBrevi;
    private Marker currentMarker = null;
    private boolean onMarkerClick = false;
    private Toolbar toolbar;
    private SupportMapFragment mapFragment;
    private SearchManager searchManager;
    private SearchView searchView;
    public static MenuItem filter;
    private ImageView searchClose;
    private TransitionDrawable transition;
    private boolean isFragmentShowing = false;
    public static boolean isSearchWhite = false;
    private BottomSheetDialogFragment bottomSheetDialogFragment;
    public static GoogleMap googleMap;
    private LinearLayout layoutImmagine;
    private ImageView imageBehind;
    private LinearLayout layoutTelefono;
    private LinearLayout layoutMail;
    private LinearLayout layoutSito;
    private LinearLayout layoutIndicazioni;

    private int previousBottomSheetStatus;

    private LinearLayout layoutBottomSheet;

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

            if (LocalStorage.getListOfEsercentiFiltrataMappa().size() == 0) {
                LocalStorage.setListOfEsercentiFiltrataMappa(LocalStorage.getListOfEsercenti());
            }

            mapFragment.getMapAsync(this);

            mBottomSheetBehavior = AnchorBottomSheetBehavior.from(bottomSheet);

            Resources r = getResources();
            float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 300, r.getDisplayMetrics());
            mBottomSheetBehavior.setPeekHeight((int) px);
            mBottomSheetBehavior.setState(AnchorBottomSheetBehavior.STATE_HIDDEN);
            previousBottomSheetStatus = 5; // IMPOSTO LO STATO A HIDDEN
            setHasOptionsMenu(true);

            //region BOTTOM SHEET LISTENER
            mBottomSheetBehavior.setBottomSheetCallback(new AnchorBottomSheetBehavior.BottomSheetCallback() {
                @Override
                public void onStateChanged(@NonNull View bottomSheet, int newState) {

                    if (newState == AnchorBottomSheetBehavior.STATE_EXPANDED) {
                        centerMarkerDetail(currentMarker, true);
                        layoutImmagine.setVisibility(View.VISIBLE);
                        animateImage();
                    }
                    if (newState == AnchorBottomSheetBehavior.STATE_COLLAPSED) {

                        if (previousBottomSheetStatus == AnchorBottomSheetBehavior.STATE_EXPANDED) {
                            mostraComponentiDettaglio();
                        } else if (previousBottomSheetStatus == AnchorBottomSheetBehavior.STATE_HIDDEN) {
                            mostraComponentiDettaglio();
                        } else if (previousBottomSheetStatus == AnchorBottomSheetBehavior.STATE_COLLAPSED) {
                            mostraComponentiDettaglio();

                        }
                        if (!onMarkerClick) {
                            centerMarkerDetail(currentMarker, false);
                        }
                    }
                    if (newState == AnchorBottomSheetBehavior.STATE_DRAGGING) {
                        layoutImmagine.setVisibility(View.GONE);
                        if (previousBottomSheetStatus == AnchorBottomSheetBehavior.STATE_COLLAPSED) {
                            //nascondo il dettaglio
                            nascondiComponentiDettaglio();
                        }
                    }
                    if (newState == AnchorBottomSheetBehavior.STATE_SETTLING) {
                        if (previousBottomSheetStatus == AnchorBottomSheetBehavior.STATE_HIDDEN) {
                            //mostraComponentiDettaglio();
                        }
                    }
                    if (newState == AnchorBottomSheetBehavior.STATE_HIDDEN) {

                        nascondiComponentiDettaglio();


                        centerMarker(currentMarker);
                    }


                    if (newState != AnchorBottomSheetBehavior.STATE_SETTLING && newState != AnchorBottomSheetBehavior.STATE_DRAGGING) {
                        previousBottomSheetStatus = newState;

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

    public void animateImage() {
        Resources r = getResources();
        float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 181, r.getDisplayMetrics());
        Animation animation = new TranslateAnimation(0, 0, px, 0);
        animation.setDuration(1000);
        animation.setFillAfter(true);
        imageBehind.startAnimation(animation);
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
            vantaggiBrevi = view.findViewById(R.id.vantaggi_brevi);
            layoutImmagine = view.findViewById(R.id.image_dietro);
            imageBehind = layoutImmagine.findViewById(R.id.image_behind_inside);



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
            searchClose = (ImageView) searchView.findViewById(android.support.v7.appcompat.R.id.search_close_btn);
        } catch (Exception e) {
            Log.e(Thread.currentThread().getStackTrace().toString(), e.toString());
        }
    }

    @Override
    public void onMapReady(GoogleMap mappa) {
        googleMap = mappa;


        for (Supplier sup : LocalStorage.getListOfEsercentiFiltrataMappa()) {

            Double lat = Double.parseDouble(sup.location.lat);
            Double lon = Double.parseDouble(sup.location.lon);
            LatLng markerLatLng = new LatLng(lat, lon);

            MarkerOptions markerCorrente = new MarkerOptions().position(markerLatLng).title(sup.location.description);
            String id = Integer.toString(sup.id);
            markerCorrente.snippet(id);
            googleMap.addMarker(markerCorrente);
        }


        // googleMap.moveCamera(CameraUpdateFactory.newLatLng(lavis));


        setCenterOfMap();

        //region GOOGLE MAP LISTENER
        googleMap.setOnMarkerClickListener(new GoogleMap.OnMarkerClickListener() {
            @Override
            public boolean onMarkerClick(Marker marker) {
                currentMarker = marker;
                centerMarker(currentMarker);
                setDettaglioComponentInBottomSheet(currentMarker);

                if (mBottomSheetBehavior.getState() == AnchorBottomSheetBehavior.STATE_HIDDEN || mBottomSheetBehavior.getState() == AnchorBottomSheetBehavior.STATE_EXPANDED) {
                    onMarkerClick = true;
                    mBottomSheetBehavior.setState(AnchorBottomSheetBehavior.STATE_COLLAPSED);
                    Resources r = getResources();
                    float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 200, r.getDisplayMetrics());
                    mBottomSheetBehavior.setPeekHeight((int)px);

                }
                return true;
            }
        });
        //endregion
    }


    public void setDettaglioComponentInBottomSheet(final Marker currentMarker) {
        String markerSnippet = currentMarker.getSnippet();
        int id = Integer.valueOf(markerSnippet);

        Supplier esercenteSelezionato = new Supplier();

        for (Supplier sup : LocalStorage.getListOfEsercentiFiltrataMappa()) {

            if (sup.id == id) {
                esercenteSelezionato = sup;
            }
        }

        TextView esercenteDettaglioTitolo = (TextView) bottomSheet.findViewById(R.id.esercente_dettaglio_titolo);
        TextView esercenteDettaglioData = (TextView) bottomSheet.findViewById(R.id.esercente_dettaglio_data);
        TextView esercenteDettaglioIndirizzo = (TextView) bottomSheet.findViewById(R.id.esercente_dettaglio_indirizzo);
        LinearLayout esercenteDettaglioListaVantaggi = (LinearLayout) vantaggiBrevi.findViewById(R.id.esercente_dettaglio_vantaggi);
        LinearLayout esercenteDettaglioLungo = (LinearLayout) bottomSheet.findViewById(R.id.layout_informazioni);
        layoutTelefono = (LinearLayout) bottomSheet.findViewById(R.id.layout_telefono);
        layoutMail = (LinearLayout) bottomSheet.findViewById(R.id.layout_mail);
        layoutSito = (LinearLayout) bottomSheet.findViewById(R.id.layout_sito);
        layoutIndicazioni = (LinearLayout) bottomSheet.findViewById(R.id.layout_indicazioni);

        ArrayList<Product> listaVantaggi = esercenteSelezionato.products;

        esercenteDettaglioListaVantaggi.removeAllViews();
        esercenteDettaglioLungo.removeAllViews();

        for (Product vant : listaVantaggi) {

            TextView textTmp = new TextView(getActivity());
            TextView textTmpLunga = new TextView(getActivity());
            textTmp.setText(vant.getDescriptionShort());
            textTmpLunga.setText(vant.description);
            textTmp.setTextColor(getResources().getColor(R.color.colorPrimary));
            textTmpLunga.setTextColor(getResources().getColor(R.color.colorPrimary));
            textTmp.setTextSize(13);
            textTmpLunga.setTextSize(13);

            textTmpLunga.setGravity(Gravity.CENTER_VERTICAL);

            esercenteDettaglioListaVantaggi.addView(textTmp);
            esercenteDettaglioLungo.addView(textTmpLunga);

        }


        esercenteDettaglioTitolo.setText(esercenteSelezionato.title);
        esercenteDettaglioData.setText(esercenteSelezionato.lastUpdate);
        esercenteDettaglioIndirizzo.setText(esercenteSelezionato.location.description);



        layoutTelefono.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent callIntent = new Intent(Intent.ACTION_CALL);
                callIntent.setData(Uri.parse("tel:3477453297"));
                startActivity(callIntent);
            }
        });

        layoutMail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_SEND);
                intent.setType("message/rfc822");
                intent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[] {"a.sciarretta.tn@gmail.com"});
                intent.putExtra(Intent.EXTRA_SUBJECT, "k");
                intent.putExtra(Intent.EXTRA_TEXT, "");
                Intent mailer = Intent.createChooser(intent, null);
                startActivity(mailer);
            }
        });

        layoutSito.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String url = "http://www.example.com";
                Intent i = new Intent(Intent.ACTION_VIEW);
                i.setData(Uri.parse(url));
                startActivity(i);
            }
        });

        layoutIndicazioni.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LatLng position = currentMarker.getPosition();
                String lat= Double.toString(position.latitude);
                String lon = Double.toString(position.longitude);
                Uri gmmIntentUri = Uri.parse("google.navigation:q="+lat+","+lon);
                Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                mapIntent.setPackage("com.google.android.apps.maps");
                startActivity(mapIntent);
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

        changeSearchToNormalMode();

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

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.searchView:
                return true;
            case R.id.filter:
                showModalBottomSheet();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }

    public void showModalBottomSheet() {
        bottomSheetDialogFragment = new BottomSheet2DialogFragment();
        bottomSheetDialogFragment.show(getActivity().getSupportFragmentManager(), bottomSheetDialogFragment.getTag());
    }



    private void changeSearchToNormalMode() {

        Integer numero_risultati = LocalStorage.getNumberOfFilterSetMappa();

        if (numero_risultati == 1) {
            filter.setIcon(R.drawable.icon_settingswhite1);
        } else {
            filter.setIcon(R.drawable.icon_settingswhite);
        }


        if (transition == null) {
            transition = (TransitionDrawable) toolbar.getBackground();
        }

        if (isSearchWhite) {
            transition.reverseTransition(300);
        }
        isSearchWhite = false;
}

    private void changeSearchToWhiteMode() {

        Integer numero_risultati = LocalStorage.getNumberOfFilterSetMappa();

        if (numero_risultati == 1) {
            filter.setIcon(R.drawable.icon_settingsgray1);
        } else {
            filter.setIcon(R.drawable.icon_settingsgrey);
        }

        if (transition == null) {
            transition = (TransitionDrawable) toolbar.getBackground();
        }


        transition.startTransition(300);
        isSearchWhite = true;

    }

    private void customizeSearchView() {
        try {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));

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
            isFragmentShowing = true;
        } else {
            if (isFragmentShowing) {
            changeSearchToNormalMode();
            }
            isFragmentShowing = false;
        }
    }

    public static float convertPixelsToDp(float px, Context context) {
        Resources resources = context.getResources();
        DisplayMetrics metrics = resources.getDisplayMetrics();
        float dp = px / ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);
        return dp;
    }


    public void mostraComponentiDettaglio() {

        //slideUpBottom(getActivity(),vantaggiBrevi);
        vantaggiBrevi.setVisibility(View.VISIBLE);

    }


    public void nascondiComponentiDettaglio() {

        //slideUp(getActivity(),vantaggiBrevi);
        vantaggiBrevi.setVisibility(View.GONE);

    }


    private static void slideUpBottom(Context ctx, View v) {
        Animation a = AnimationUtils.loadAnimation(ctx, R.anim.slide_down);

        if (a != null) {
            a.reset();
            if (v != null) {
                v.clearAnimation();
                v.startAnimation(a);
            }
        }
    }


    private static void slideUp(Context ctx, View v) {
        Animation a = AnimationUtils.loadAnimation(ctx, R.anim.slide_up);

        if (a != null) {
            a.reset();
            if (v != null) {
                v.clearAnimation();
                v.startAnimation(a);
            }
        }
    }


}
