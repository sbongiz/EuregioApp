package com.almaviva.euregio.fragment;

import android.Manifest;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.TransitionDrawable;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.app.SupportActivity;
import android.support.v4.content.ContextCompat;
import android.support.v4.internal.view.SupportMenuItem;
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
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.AutoCompleteTextView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.almaviva.euregio.MainActivity;
import com.almaviva.euregio.R;

import com.almaviva.euregio.behavior.AnchorBottomSheetBehavior;
import com.almaviva.euregio.helper.BottomSheet2DialogFragment;
import com.almaviva.euregio.helper.FilterHelper;
import com.almaviva.euregio.helper.LocalStorage;
import com.almaviva.euregio.mock.CardRestClient;
import com.almaviva.euregio.mock.ImageRestClient;
import com.almaviva.euregio.mock.SupplierRestClient;
import com.almaviva.euregio.model.Product;
import com.almaviva.euregio.model.Supplier;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.CameraPosition;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.gson.Gson;
import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

import cz.msebera.android.httpclient.Header;

import static com.almaviva.euregio.fragment.ListaFragment.esercentiArrayList;
import static com.almaviva.euregio.fragment.ListaFragment.spref;


public class MapFragment extends Fragment implements OnMapReadyCallback {

    private OnFragmentInteractionListener mListener;
    private AnchorBottomSheetBehavior mBottomSheetBehavior;
    private View bottomSheet;
    private View vantaggiBrevi;
    private Marker currentMarker = null;
    private boolean onMarkerClick = false;
    private Toolbar toolbar;
    public static SupportMapFragment mapFragment;
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
    private FusedLocationProviderClient mFusedLocationClient;
    private int previousBottomSheetStatus;
    private Supplier esercenteSelezionato;
    public static SharedPreferences spref;
    public static ArrayList<Supplier> esercentiArrayList;
    public static MainActivity activity;
    public static MapFragment contextMap;
    public static String immagineEsercentePath = null;
    private LinearLayout layoutBottomSheet;
    public static Context context;

    public MapFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        try {

        } catch (Exception e) {
            Log.e(Thread.currentThread().getStackTrace().toString(), e.toString());
        }
        mFusedLocationClient = LocationServices.getFusedLocationProviderClient(getActivity());
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {


        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_map, container, false);

        try {
            context = getContext();
            setComponent(view);
            spref = PreferenceManager.getDefaultSharedPreferences(getActivity());
            activity = (MainActivity) getActivity();
            contextMap = this;

            if (LocalStorage.getListOfEsercentiFiltrataMappa().size() == 0) {
                if (LocalStorage.getListOfEsercenti().size() != 0) {
                    LocalStorage.setListOfEsercentiFiltrataMappa(LocalStorage.getListOfEsercenti());
                    mapFragment.getMapAsync(this);
                } else {
                    retriveFilterAsyncTask();
                }
            } else {
                mapFragment.getMapAsync(this);
            }


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
                        // layoutImmagine.setVisibility(View.VISIBLE);
                        nascondiComponentiDettaglio();

                        mBottomSheetBehavior.setState(AnchorBottomSheetBehavior.STATE_EXPANDED);


                    } else if (mBottomSheetBehavior.getState() == AnchorBottomSheetBehavior.STATE_EXPANDED) {
                        centerMarkerDetail(currentMarker, true);
                        layoutImmagine.setVisibility(View.GONE);


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
        try {

            if (immagineEsercentePath != null) {
                File fileImmagine = new File(immagineEsercentePath);
                Bitmap myBitmap = BitmapFactory.decodeFile(fileImmagine.getAbsolutePath());
                imageBehind.setImageBitmap(myBitmap);
                Resources r = getResources();
                float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 181, r.getDisplayMetrics());
                Animation animation = new TranslateAnimation(0, 0, px, 0);
                animation.setDuration(1000);
                animation.setFillAfter(true);
                imageBehind.startAnimation(animation);
            }

        } catch (Exception e) {
            Log.e(Thread.currentThread().getStackTrace().toString(), e.toString());
        }

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
            final View thisView = view;
            view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    int heightDiff = thisView.getRootView().getHeight() - thisView.getHeight();
                    if (heightDiff > dpToPx(200)) { // if more than 200 dp, it's probably a keyboard...


                    } else {
                        MainActivity activity = (MainActivity) getActivity();
                        if(activity!=null){
                            activity.getWindow().getDecorView().setSystemUiVisibility(
                                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
                        }
                    }
                }
            });


        } catch (Exception e) {
            Log.e(Thread.currentThread().getStackTrace().toString(), e.toString());
        }
    }

    public float dpToPx(float valueInDp) {
        try{
            DisplayMetrics metrics = context.getResources().getDisplayMetrics();
            return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, valueInDp, metrics);
        }catch (Exception e){
            Log.e(Thread.currentThread().getStackTrace().toString(), e.toString());
        }
       return 0f;
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
    public void onRequestPermissionsResult(int requestCode,
                                           String permissions[], int[] grantResults) {
        switch (requestCode) {
            case 0: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {


                } else {


                }
                return;
            }


        }
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
    }

    @Override
    public void onMapReady(GoogleMap mappa) {


        try {
            googleMap = mappa;


            if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {

                if (googleMap != null) {
                    googleMap.setMyLocationEnabled(true);
                }


                mFusedLocationClient.getLastLocation()
                        .addOnSuccessListener(activity, new OnSuccessListener<Location>() {
                            @Override
                            public void onSuccess(Location location) {
                                // Got last known location. In some rare situations this can be null.
                                if (location != null) {
                                    LatLng myLocation = new LatLng(location.getLongitude(), location.getLatitude());
                                    LocalStorage.setMyLastKnownLocation(myLocation);
                                }
                            }
                        });

            } else {
                // No explanation needed; request the permission
                ActivityCompat.requestPermissions(activity,
                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                        0);
            }


            for (Supplier sup : LocalStorage.getListOfEsercentiFiltrataMappa()) {

                //DEVELOPER
                if (sup.location.lat == null) {
                    sup.location.lat = "46.082934";
                }
                if (sup.location.lon == null) {
                    sup.location.lon = "11.283741";
                }

                if (sup.location.lat != null && sup.location.lon != null) {

                    Double lat = Double.parseDouble(sup.location.lat);
                    Double lon = Double.parseDouble(sup.location.lon);
                    LatLng markerLatLng = new LatLng(lat, lon);

                    MarkerOptions markerCorrente = new MarkerOptions().position(markerLatLng).title(sup.location.description);
                    String id = Integer.toString(sup.id);
                    markerCorrente.snippet(id);
                    googleMap.addMarker(markerCorrente);
                }

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
                        mBottomSheetBehavior.setPeekHeight((int) px);

                    }
                    return true;
                }
            });


            //endregion
        } catch (Exception e) {
            Log.e(Thread.currentThread().getStackTrace().toString(), e.toString());
        }


    }


    public void setDettaglioComponentInBottomSheet(final Marker currentMarker) {

        try {
            String markerSnippet = currentMarker.getSnippet();
            int id = Integer.valueOf(markerSnippet);

            esercenteSelezionato = new Supplier();

            for (Supplier sup : LocalStorage.getListOfEsercentiFiltrataMappa()) {

                if (sup.id == id) {
                    esercenteSelezionato = sup;
                }
            }

            if (esercenteSelezionato.pictureUrl != null) {

                final String idCorrente = String.valueOf(esercenteSelezionato.id);
                ImageRestClient.get(esercenteSelezionato.pictureUrl, null, new FileAsyncHttpResponseHandler(getActivity()) {

                    @Override
                    public void onFailure(int statusCode, Header[] headers, Throwable throwable, File file) {
                        String a = "";
                    }

                    @Override
                    public void onSuccess(int statusCode, Header[] headers, File file) {

                        SharedPreferences.Editor editor = spref.edit();


                        File fileNuovo = new File(getContext().getFilesDir(), "immagineEsercente_" + idCorrente);
                        try {

                            InputStream is = new FileInputStream(file);
                            OutputStream os = new FileOutputStream(fileNuovo);
                            byte[] buff = new byte[1024];
                            int len;
                            while ((len = is.read(buff)) > 0) {
                                os.write(buff, 0, len);
                            }
                            is.close();
                            os.close();


                        } catch (IOException e) {

                        }
                    }
                });
            }


            immagineEsercentePath = getContext().getFilesDir() + "/" + "immagineEsercente_" + esercenteSelezionato.id;

            TextView esercenteDettaglioTitolo = (TextView) bottomSheet.findViewById(R.id.esercente_dettaglio_titolo);
            final TextView esercenteDettaglioData = (TextView) bottomSheet.findViewById(R.id.esercente_dettaglio_data);
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

            String data = esercenteSelezionato.lastUpdate;
            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
            Date convertedDate = new Date();
            try {
                convertedDate = sdf.parse(data);
            } catch (ParseException e) {
                // TODO Auto-generated catch block
                e.printStackTrace();
            }

            int var = convertedDate.getMonth() + 1;

            String b = String.valueOf(var);

            esercenteDettaglioData.setText(convertedDate.getDate() + "/" + b);
            esercenteDettaglioIndirizzo.setText(esercenteSelezionato.location.description);


            layoutTelefono.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    if (esercenteSelezionato != null && esercenteSelezionato.phone != null) {
                        MainActivity activity = (MainActivity) getActivity();
                        if (activity != null) {
                            if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                                Intent callIntent = new Intent(Intent.ACTION_CALL);
                                callIntent.setData(Uri.parse("tel:" + esercenteSelezionato.phone));
                                startActivity(callIntent);
                            } else {
                                // No explanation needed; request the permission
                                ActivityCompat.requestPermissions(activity,
                                        new String[]{Manifest.permission.CALL_PHONE},
                                        1);
                            }
                        }
                    } else {
                        Context context = getContext();
                        CharSequence text = getString(R.string.errore_funzione);
                        int duration = Toast.LENGTH_SHORT;

                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
                    }

                }
            });

            layoutMail.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {


                    if (esercenteSelezionato != null && esercenteSelezionato.email != null) {
                        Intent intent = new Intent(Intent.ACTION_SEND);
                        intent.setType("message/rfc822");
                        intent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{esercenteSelezionato.email});
                        intent.putExtra(Intent.EXTRA_SUBJECT, "");
                        intent.putExtra(Intent.EXTRA_TEXT, "");
                        Intent mailer = Intent.createChooser(intent, null);
                        startActivity(mailer);
                    } else {
                        Context context = getContext();
                        CharSequence text = getString(R.string.errore_funzione);
                        int duration = Toast.LENGTH_SHORT;

                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
                    }

                }
            });

            layoutSito.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (esercenteSelezionato != null && esercenteSelezionato.web != null) {
                        String url = "http://" + esercenteSelezionato.web;
                        Intent i = new Intent(Intent.ACTION_VIEW);
                        i.setData(Uri.parse(url));
                        startActivity(i);
                    } else {
                        Context context = getContext();
                        CharSequence text = getString(R.string.errore_funzione);
                        int duration = Toast.LENGTH_SHORT;

                        Toast toast = Toast.makeText(context, text, duration);
                        toast.show();
                    }

                }
            });

            layoutIndicazioni.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    LatLng position = currentMarker.getPosition();
                    String lat = Double.toString(position.latitude);
                    String lon = Double.toString(position.longitude);
                    Uri gmmIntentUri = Uri.parse("google.navigation:q=" + lat + "," + lon);
                    Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                    mapIntent.setPackage("com.google.android.apps.maps");
                    startActivity(mapIntent);
                }
            });
        } catch (Exception e) {
            Log.e(Thread.currentThread().getStackTrace().toString(), e.toString());
        }


    }


    public void centerMarker(Marker marker) {

        try {
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(marker.getPosition().latitude, marker.getPosition().longitude))
                    .zoom(15)
                    .bearing(0)
                    .tilt(45)
                    .build();
            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        } catch (Exception e) {
            Log.e(Thread.currentThread().getStackTrace().toString(), e.toString());
        }


    }

    public void centerMarkerDetail(Marker marker, boolean isDetailCentered) {

        try {
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
        } catch (Exception e) {
            Log.e(Thread.currentThread().getStackTrace().toString(), e.toString());
        }
    }

    public void setCenterOfMap() {
        try {
            CameraPosition cameraPosition = new CameraPosition.Builder()
                    .target(new LatLng(46.673211, 11.428050)) //Alto Adige center
                    .zoom(8)
                    .bearing(0)
                    .tilt(45)
                    .build();
            googleMap.animateCamera(CameraUpdateFactory.newCameraPosition(cameraPosition));
        } catch (Exception e) {
            Log.e(Thread.currentThread().getStackTrace().toString(), e.toString());
        }
    }

    public interface OnFragmentInteractionListener {
        void onFragmentInteraction(Uri uri);

    }

    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        try{
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
                    LocalStorage.setTestoCercato(query);
                    FilterHelper.filtraTotale(getActivity());
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    if(newText.equals("")){
                        LocalStorage.setTestoCercato(newText);
                        FilterHelper.filtraTotale(getActivity());
                    }
                    return false;
                }
            });
            //endregion
        }catch (Exception e){
            Log.e(Thread.currentThread().getStackTrace().toString(), e.toString());

        }


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
        try {
            bottomSheetDialogFragment = new BottomSheet2DialogFragment();
            bottomSheetDialogFragment.show(getActivity().getSupportFragmentManager(), bottomSheetDialogFragment.getTag());
        }catch (Exception e){
            Log.e(Thread.currentThread().getStackTrace().toString(), e.toString());
        }

    }


    private void changeSearchToNormalMode() {
        try {
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

        } catch (Exception e) {
            Log.e(Thread.currentThread().getStackTrace().toString(), e.toString());
        }
    }

    private void changeSearchToWhiteMode() {
        try {


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
        } catch (Exception e) {
            Log.e(Thread.currentThread().getStackTrace().toString(), e.toString());
        }
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

        try {
            super.setUserVisibleHint(isVisibleToUser);
            if (isVisibleToUser) {

                isFragmentShowing = true;

                if (activity != null) {

                }


            } else {
                if (isFragmentShowing) {
                    changeSearchToNormalMode();
                }
                isFragmentShowing = false;
            }
        } catch (Exception e) {
            Log.e(Thread.currentThread().getStackTrace().toString(), e.toString());
        }
    }

    public static float convertPixelsToDp(float px, Context context) {
        float dp = 0f;
        try {
            Resources resources = context.getResources();
            DisplayMetrics metrics = resources.getDisplayMetrics();
            dp = px / ((float) metrics.densityDpi / DisplayMetrics.DENSITY_DEFAULT);

        } catch (Exception e) {
            Log.e(Thread.currentThread().getStackTrace().toString(), e.toString());
        }
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

        try {
            Animation a = AnimationUtils.loadAnimation(ctx, R.anim.slide_down);

            if (a != null) {
                a.reset();
                if (v != null) {
                    v.clearAnimation();
                    v.startAnimation(a);
                }
            }
        }catch (Exception e){
            Log.e(Thread.currentThread().getStackTrace().toString(), e.toString());
        }

    }


    private static void slideUp(Context ctx, View v) {
        try {
            Animation a = AnimationUtils.loadAnimation(ctx, R.anim.slide_up);

            if (a != null) {
                a.reset();
                if (v != null) {
                    v.clearAnimation();
                    v.startAnimation(a);
                }
            }
        }catch (Exception e){
            Log.e(Thread.currentThread().getStackTrace().toString(), e.toString());
        }

    }


    public static void retriveFilterAsyncTask() {
        try {
            esercentiArrayList = new ArrayList<Supplier>();

            String lingua = spref.getString("lingua", "");
            RequestParams params = new RequestParams();
            if (lingua.equals("Italiano")) {
                params.put("lang", "it");
            } else {
                params.put("lang", "de");
            }

            SupplierRestClient.get("", null, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    // If the response is JSONObject instead of expected JSONArray
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray timeline) {
                    // Pull out the first event on the public timeline
                    JSONObject firstEvent = null;
                    try {


                        for (int i = 0; i < timeline.length(); i++) {
                            firstEvent = (JSONObject) timeline.get(i);
                            Supplier data = new Gson().fromJson(firstEvent.toString(), Supplier.class);
                            esercentiArrayList.add(data);
                        }

                        LocalStorage.setListOfEsercenti(esercentiArrayList);
                        LocalStorage.setListOfEsercentiFiltrataMappa(esercentiArrayList);


                        FilterHelper.filtraTotale(activity);

                        mapFragment.getMapAsync(contextMap);
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });
        }catch (Exception e){
            Log.e(Thread.currentThread().getStackTrace().toString(), e.toString());
        }


    }

}
