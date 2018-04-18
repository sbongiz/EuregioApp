package com.almaviva.euregio;

import android.content.SharedPreferences;
import android.graphics.drawable.ColorDrawable;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;

import com.almaviva.euregio.adapter.BottomBarAdapter;
import com.almaviva.euregio.fragment.EuregioFragment;
import com.almaviva.euregio.fragment.ImpostazioniFragment;
import com.almaviva.euregio.fragment.ListaFragment;
import com.almaviva.euregio.fragment.LoginFragment;
import com.almaviva.euregio.fragment.MapFragment;
import com.almaviva.euregio.fragment.SettingsFragment;
import com.almaviva.euregio.fragment.VantaggiFragment;
import com.almaviva.euregio.helper.LocalStorage;
import com.almaviva.euregio.mock.CategoryRestClient;
import com.almaviva.euregio.mock.DistrictRestClient;
import com.almaviva.euregio.mock.SupplierRestClient;
import com.almaviva.euregio.model.Category;
import com.almaviva.euregio.model.District;
import com.almaviva.euregio.model.Supplier;
import com.almaviva.euregio.pager.NoSwipePager;
import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.sql.Array;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;

public class MainActivity extends AppCompatActivity implements VantaggiFragment.OnFragmentInteractionListener,
        EuregioFragment.OnFragmentInteractionListener,
        ImpostazioniFragment.OnFragmentInteractionListener,
        ListaFragment.OnFragmentInteractionListener,
        MapFragment.OnFragmentInteractionListener,
        LoginFragment.OnFragmentInteractionListener {


    private BottomNavigationView bottomNavigation;
    public static NoSwipePager viewPager;
    private BottomBarAdapter pagerAdapter;
    private ListaFragment listaFragment;
    private EuregioFragment euregioFragment;
    private LoginFragment loginFragment;
    private ImpostazioniFragment impostazioniFragment;
    private SettingsFragment settingsFragment;
    private MapFragment mapFragment;
    private SharedPreferences spref;
    private String paginaHome;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_main);
        try {


            spref = PreferenceManager.getDefaultSharedPreferences(this);

            setComponent();

            //FILTRI
            setLanguage();
            setPaginaHome();
            setOrdinamentoEsercenti();
            setComprensori();




            setCategory();
            checkIfLogged();
            cardFronteRetro();

            viewPager.setPagingEnabled(false);
            pagerAdapter.addFragments(listaFragment);
            pagerAdapter.addFragments(mapFragment);
            pagerAdapter.addFragments(euregioFragment);
            pagerAdapter.addFragments(impostazioniFragment);
            pagerAdapter.addFragments(loginFragment);


            viewPager.setAdapter(pagerAdapter);


            if (paginaHome.equals(getString(R.string.esercenti))) {
                bottomNavigation.setSelectedItemId(R.id.navigation_vantaggi);
                viewPager.setCurrentItem(0);
            } else if (paginaHome.equals(getString(R.string.title_mappa))) {
                bottomNavigation.setSelectedItemId(R.id.navigation_mappa);
                viewPager.setCurrentItem(1);
            }

            bottomNavigation.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
                @Override
                public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                    // handle desired action here
                    // One possibility of action is to replace the contents above the nav bar
                    // return true if you want the item to be displayed as the selected itemù
                    int id = item.getItemId();
                    if (id == R.id.navigation_vantaggi) {
                        viewPager.setCurrentItem(0);
                    }
                    if (id == R.id.navigation_mappa) {
                        viewPager.setCurrentItem(1);
                    }
                    if (id == R.id.navigation_euregio) {
                        if (spref.getBoolean("isLogged",false)) {
                            viewPager.setCurrentItem(2);

                        } else {
                            viewPager.setCurrentItem(4);
                        }
                    }
                    if (id == R.id.navigation_impostazioni) {
                        viewPager.setCurrentItem(3);
                    }


                    return true;
                }
            });

        } catch (Exception e) {
            Log.e(Thread.currentThread().getStackTrace().toString(), e.toString());
        }
    }

    private void setLanguage() {
        String lingua = spref.getString("lingua", "");

        if (lingua == "") {
            //Al primo avvio controllo la lingua di sistema

            String linguaSistema = Locale.getDefault().getDisplayLanguage().toString();

            if (linguaSistema.equals("Deutsch")) {
                SharedPreferences.Editor editor = spref.edit();
                editor.putString("lingua", "Deutsch");
                editor.commit();
            } else {
                SharedPreferences.Editor editor = spref.edit();
                editor.putString("lingua", "Italiano");
                editor.commit();
            }

        } else {
            //Se non è il primo avvio controllo la preferenza della lingua

            if (lingua.equals("Deutsch")) {
                //IMPOSTA TEDESCO
                SharedPreferences.Editor editor = spref.edit();
                editor.putString("lingua", "Deutsch");
                editor.commit();
            } else {
                //IMPOSTA ITALIANO
                SharedPreferences.Editor editor = spref.edit();
                editor.putString("lingua", "Italiano");
                editor.commit();
            }
        }
    }

    private void setPaginaHome() {
        String pagina_home = spref.getString("pagina_home", "");

        if (pagina_home.equals("")) {
            SharedPreferences.Editor editor = spref.edit();
            editor.putString("pagina_home", getString(R.string.esercenti));
            editor.commit();
            paginaHome = getString(R.string.esercenti);
        } else {

            if (pagina_home.equals(getString(R.string.esercenti))) {
                paginaHome = getString(R.string.esercenti);
            } else {
                paginaHome = getString(R.string.title_mappa);
            }

        }

    }

    private void setOrdinamentoEsercenti() {
        String ordinamentoEsercenti = spref.getString("ordinamento_esercenti", "");

        if (ordinamentoEsercenti.equals("")) {
            SharedPreferences.Editor editor = spref.edit();
            editor.putString("ordinamento_esercenti", getString(R.string.lista_data));
            editor.commit();
            LocalStorage.setFiltroOrdine(getString(R.string.data_aggiornamento));
        } else {

            if (ordinamentoEsercenti.equals(getString(R.string.lista_data))) {
                SharedPreferences.Editor editor = spref.edit();
                editor.putString("ordinamento_esercenti", getString(R.string.lista_data));
                editor.commit();
                LocalStorage.setFiltroOrdine(getString(R.string.data_aggiornamento));
            } else {
                SharedPreferences.Editor editor = spref.edit();
                editor.putString("ordinamento_esercenti", getString(R.string.lista_alfabetica));
                editor.commit();
                LocalStorage.setFiltroOrdine(getString(R.string.alfabetico));
            }

        }
    }

    private void setComprensori() {

        final ArrayList<District> comprensori = new ArrayList<District>();

        DistrictRestClient.get("", null, new JsonHttpResponseHandler() {
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
                        District data = new Gson().fromJson(firstEvent.toString(), District.class);
                        comprensori.add(data);
                    }

                    LocalStorage.setListOfComprensori(comprensori);

                    ArrayList<Integer> arrayCompId = new ArrayList<Integer>();

                    for (District dis : comprensori) {

                        boolean checkPreference = spref.getBoolean("check" + dis.id, false);

                        if (checkPreference == true) {
                            arrayCompId.add(dis.id);
                        }
                    }

                    LocalStorage.setFiltroComprensorio(arrayCompId);
                    if (arrayCompId.size() > 0) {
                        LocalStorage.setIsSetComprensorio(true);
                    } else {
                        LocalStorage.setIsSetComprensorio(false);
                    }

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });





    }


    private void setCategory(){

        final ArrayList<Category> categories = new ArrayList<Category>();

        CategoryRestClient.get("", null, new JsonHttpResponseHandler() {
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
                        Category data = new Gson().fromJson(firstEvent.toString(), Category.class);
                        categories.add(data);
                    }

                    LocalStorage.setListOfCategories(categories);

                    ArrayList<Integer> arrayCompId = new ArrayList<Integer>();


                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });
    }

    private void checkIfLogged() {
        boolean isLogged = spref.getBoolean("isLogged", false);




        if(isLogged == false ){
            SharedPreferences.Editor editor = spref.edit();
            editor.putBoolean("isLogged",false);
            editor.commit();
        }
    }

    private void cardFronteRetro(){


        String fronteRetro = spref.getString("fronte_retro", "");


        if(fronteRetro.equals("")){
            SharedPreferences.Editor editor = spref.edit();
            editor.putString("fronte_retro", "front");
            editor.commit();
        }




        String path = spref.getString("card_fronte_path","");

        String s ="£";

    }

    //region SET COMPONENT
    private void setComponent() {
        try {
            //Find and initialize the view component
            findComponentInView();
            //Initialize the fragment
            setFragment();

            //Create the ButtonNavigationMenu
            //createNavigationButton();
            bottomNavigation.setBackground(new ColorDrawable(256));
            pagerAdapter = new BottomBarAdapter(getSupportFragmentManager());
        } catch (Exception e) {
            Log.e(Thread.currentThread().getStackTrace().toString(), e.toString());
        }
    }

    private void findComponentInView() {
        bottomNavigation = (BottomNavigationView) findViewById(R.id.bottom_navigation);
        viewPager = (NoSwipePager) findViewById(R.id.pager);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitleTextColor(0xFFFFFFFF);
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
    }

    private void setFragment() {
        listaFragment = new ListaFragment();
        loginFragment = new LoginFragment();
        euregioFragment = new EuregioFragment();
        impostazioniFragment = new ImpostazioniFragment();
        mapFragment = new MapFragment();
        settingsFragment = new SettingsFragment();

    }

    //  @Override
    //  public void onWindowFocusChanged(boolean hasFocus) {
    //      super.onWindowFocusChanged(hasFocus);
    //      if (hasFocus) {
    //          getWindow().getDecorView().setSystemUiVisibility(
    //                  View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
    //                          | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
    //      }
    //  }
    //endregion

    //region LISTENER
    @Override
    public void onFragmentInteraction(Uri uri) {
    }


    //endregion


}
