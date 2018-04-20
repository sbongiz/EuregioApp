package com.almaviva.euregio.helper;

import android.app.Activity;
import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.Log;

import com.almaviva.euregio.MainActivity;
import com.almaviva.euregio.R;
import com.almaviva.euregio.fragment.ListaFragment;
import com.almaviva.euregio.fragment.MapFragment;
import com.almaviva.euregio.mock.DistrictRestClient;
import com.almaviva.euregio.model.Category;
import com.almaviva.euregio.model.District;
import com.almaviva.euregio.model.Location;
import com.almaviva.euregio.model.Product;
import com.almaviva.euregio.model.Supplier;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Array;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;

import cz.msebera.android.httpclient.Header;

import static java.lang.Math.sqrt;


/**
 * Created by a.sciarretta on 12/04/2018.
 */

public class FilterHelper {

    public static ArrayList<Supplier> arrayEsercenti;
    public static ArrayList<Supplier> arrayEsercentiFiltrato;


    public static void filtraTotale(Context con) {

        try {
            boolean isDescrescente = LocalStorage.getIsDecrescente();
            String testoCercato = LocalStorage.getTestoCercato();

            filtraListaEsercenti();

            filtraListaPerOrdine(con, isDescrescente);

            if (LocalStorage.getTestoCercato() != null && LocalStorage.getTestoCercato() != "") {
                filtraListaPerTesto(testoCercato);
            }

            aggiornaComponentInListaFragment(con);
        } catch (Exception e) {
            String errore = e.toString();
            Log.e(Thread.currentThread().getStackTrace().toString(), e.toString());
        }

    }

    public static void filtraListaPerTesto(String testoCercato) {

        try {
            if (arrayEsercentiFiltrato == null) {
                arrayEsercentiFiltrato = new ArrayList<Supplier>();
            }
            arrayEsercentiFiltrato = LocalStorage.getListOfEsercentiFiltrata();


            Iterator<Supplier> iter = arrayEsercentiFiltrato.iterator();

            testoCercato= testoCercato.toLowerCase();

            while (iter.hasNext()) {
                boolean trovato = false;
                Supplier sup = iter.next();

                if (sup.title != null && sup.title.toLowerCase().contains(testoCercato)) {
                    trovato = true;
                }

                if (sup.location != null && sup.location.description != null && sup.location.description.toLowerCase().contains(testoCercato)) {
                    trovato = true;
                }

                if (sup.location != null && sup.location.municipality != null && sup.location.municipality.toLowerCase().contains(testoCercato)) {
                    trovato = true;
                }

                if (sup.location != null && sup.location.district != null && sup.location.district.name != null && sup.location.district.name.toLowerCase().contains(testoCercato)) {
                    trovato = true;
                }


                if (sup.email != null && sup.email.toLowerCase().contains(testoCercato)) {
                    trovato = true;
                }

                if (sup.web != null && sup.web.toLowerCase().contains(testoCercato)) {
                    trovato = true;
                }

                for (Category cat : sup.categories) {

                    if (cat.name != null && cat.name.toLowerCase().contains(testoCercato)) {
                        trovato = true;
                    }

                }

                for (Product prod : sup.products) {
                    if (prod.descriptionShort != null && prod.descriptionShort.toLowerCase().contains(testoCercato)) {
                        trovato = true;
                    }
                    if (prod.description != null && prod.description.toLowerCase().contains(testoCercato)) {
                        trovato = true;
                    }
                }

                if (!trovato) {
                    iter.remove();
                }

            }


            LocalStorage.setListOfEsercentiFiltrata(arrayEsercentiFiltrato);
        } catch (Exception e) {
            Log.e(Thread.currentThread().getStackTrace().toString(), e.toString());
        }

    }


    public static void filtraListaPerOrdine(Context con, boolean isDescrescente) {

        try {
            if (arrayEsercentiFiltrato == null) {
                arrayEsercentiFiltrato = new ArrayList<Supplier>();
            }
            arrayEsercentiFiltrato = LocalStorage.getListOfEsercentiFiltrata();

            String tipoOrdine = LocalStorage.getFiltriOrdine();

            if (tipoOrdine == con.getString(R.string.alfabetico)) {

                if (!isDescrescente) {
                    Collections.sort(arrayEsercentiFiltrato, new Comparator<Supplier>() {
                        @Override
                        public int compare(Supplier s1, Supplier s2) {
                            String prima = s1.title;
                            String seconda = s2.title;
                            return prima.compareToIgnoreCase(seconda);
                        }
                    });
                } else {
                    Collections.sort(arrayEsercentiFiltrato, new Comparator<Supplier>() {
                        @Override
                        public int compare(Supplier s1, Supplier s2) {
                            String prima = s1.title;
                            String seconda = s2.title;
                            return seconda.compareToIgnoreCase(prima);
                        }
                    });
                }
            } else if (tipoOrdine == con.getString(R.string.data_aggiornamento)) {

                if (!isDescrescente) {
                    Collections.sort(arrayEsercentiFiltrato, new Comparator<Supplier>() {
                        @Override
                        public int compare(Supplier s1, Supplier s2) {

                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
                            Date convertedDates1 = new Date();
                            Date convertedDates2 = new Date();
                            try {
                                convertedDates1 = sdf.parse(s1.lastUpdate);
                                convertedDates2 = sdf.parse(s2.lastUpdate);
                            } catch (ParseException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }

                            return convertedDates1.compareTo(convertedDates2);
                        }
                    });
                } else {
                    Collections.sort(arrayEsercentiFiltrato, new Comparator<Supplier>() {
                        @Override
                        public int compare(Supplier s1, Supplier s2) {
                            SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
                            Date convertedDates1 = new Date();
                            Date convertedDates2 = new Date();
                            try {
                                convertedDates1 = sdf.parse(s1.lastUpdate);
                                convertedDates2 = sdf.parse(s2.lastUpdate);
                            } catch (ParseException e) {
                                // TODO Auto-generated catch block
                                e.printStackTrace();
                            }
                            return convertedDates2.compareTo(convertedDates1);
                        }
                    });
                }

            } else {


                int metri = 5;

                double myLongLong = 0;
                double myLatLong = 0;
                if (LocalStorage.getMyLastKnownLocation() != null) {
                    myLongLong = Double.valueOf(LocalStorage.getMyLastKnownLocation().longitude);
                    myLatLong = Double.valueOf(LocalStorage.getMyLastKnownLocation().latitude);


                    Iterator<Supplier> iter = arrayEsercentiFiltrato.iterator();


                    while (iter.hasNext()) {

                        Supplier sup = iter.next();


                        // float distanceInMeters =  targetLocation.distanceTo(myLocation);

                        double esercenteLongLong = Double.valueOf(sup.location.lon);
                        double esercdenteLatLong = Double.valueOf(sup.location.lat);
                        boolean isInRadius = (metri * 111000f) >= sqrt(Math.pow(myLongLong - esercenteLongLong, 2) + Math.pow(myLatLong - esercdenteLatLong, 2));

                        if (!isInRadius) {
                            iter.remove();
                        }

                    }
                } else {
                    //ATTENZIONE PER QUESTO TIPO DI ORDINAMENTO ATTIVARTE LOCALIZZAZIONE
                }

            }

            LocalStorage.setListOfEsercentiFiltrata(arrayEsercentiFiltrato);
        } catch (Exception e) {
            Log.e(Thread.currentThread().getStackTrace().toString(), e.toString());
        }


    }

    public static void filtraListaEsercenti() {

        try {
            arrayEsercenti = new ArrayList<Supplier>();
            arrayEsercentiFiltrato = new ArrayList<Supplier>();

            arrayEsercenti = LocalStorage.getListOfEsercenti();
            arrayEsercentiFiltrato.addAll(arrayEsercenti);

            final ArrayList<Integer> listCategorieId = LocalStorage.getFiltriCategoria();
            final ArrayList<Integer> listcomprensorioId = LocalStorage.getFiltriComprensorio();

            Iterator<Supplier> iter = arrayEsercentiFiltrato.iterator();


            while (iter.hasNext()) {
                boolean trovato = false;
                boolean rimosso = false;
                Supplier sup = iter.next();
                ArrayList<Category> categorieEsercente = sup.categories;


                if (listCategorieId != null) {
                    for (Category cat : categorieEsercente) {

                        if (listCategorieId.contains(cat.id)) {
                            trovato = true;
                            break;
                        }
                        String e = "";
                    }
                }

                if (listCategorieId.size() == 0) {
                    //se non specificato il filtro su categoria ritorno tutto!!!
                    trovato = true;
                }

                if (!trovato) {
                    iter.remove();
                    rimosso = true;
                }

                District comprensorioEsercente = sup.location.district;


                if (comprensorioEsercente != null && listcomprensorioId.size() > 0 && !listcomprensorioId.contains(comprensorioEsercente.id)) {
                    if (!rimosso) {
                        iter.remove();
                    }
                }

            }
            LocalStorage.setListOfEsercentiFiltrata(arrayEsercentiFiltrato);
        } catch (Exception e) {
            Log.e(Thread.currentThread().getStackTrace().toString(), e.toString());
        }

    }

    public static void filtraSuMappa(Context con) {
        try {
            arrayEsercenti = new ArrayList<Supplier>();
            arrayEsercentiFiltrato = new ArrayList<Supplier>();

            arrayEsercenti = LocalStorage.getListOfEsercenti();
            arrayEsercentiFiltrato.addAll(arrayEsercenti);

            final ArrayList<Integer> listCategorieId = LocalStorage.getFiltriCategoriaMappa();

            Iterator<Supplier> iter = arrayEsercentiFiltrato.iterator();


            while (iter.hasNext()) {
                boolean trovato = false;
                boolean rimosso = false;
                Supplier sup = iter.next();
                ArrayList<Category> categorieEsercente = sup.categories;


                if (listCategorieId != null) {
                    for (Category cat : categorieEsercente) {

                        if (listCategorieId.contains(cat.id)) {
                            trovato = true;
                            break;
                        }
                        String e = "";
                    }
                }

                if (listCategorieId.size() == 0) {
                    //se non specificato il filtro su categoria ritorno tutto!!!
                    trovato = true;
                }

                if (!trovato) {
                    iter.remove();
                    rimosso = true;
                }


            }

            LocalStorage.setListOfEsercentiFiltrataMappa(arrayEsercentiFiltrato);

            aggiornaComponentInMapFragment(con);
        } catch (Exception e) {
            Log.e(Thread.currentThread().getStackTrace().toString(), e.toString());
        }

    }


    public static void aggiornaComponentInListaFragment(Context con) {
        try {
            MainActivity context = (MainActivity) con;
            List<Fragment> fra = context.getSupportFragmentManager().getFragments();
            ListaFragment listaFragment = null;
            for (Fragment f : fra) {

                if (f != null && f.getClass().toString().equals("class com.almaviva.euregio.fragment.ListaFragment")) {
                    listaFragment = (ListaFragment) f;
                }
            }

            ArrayList<Supplier> ar = LocalStorage.getListOfEsercentiFiltrata();

            if (listaFragment != null) {

                listaFragment.esercenteListAdapter.add(LocalStorage.getListOfEsercentiFiltrata());
                listaFragment.esercenteListAdapter.notifyDataSetChanged();

                int numero_risultati = LocalStorage.getListOfEsercentiFiltrata().size();

                listaFragment.textViewNumeroRisultati.setText(numero_risultati + " " + con.getString(R.string.risultati));
            }
        } catch (Exception e) {
            Log.e(Thread.currentThread().getStackTrace().toString(), e.toString());
        }


    }


    public static void aggiornaComponentInMapFragment(Context con) {
        try {
            MainActivity context = (MainActivity) con;
            List<Fragment> fra = context.getSupportFragmentManager().getFragments();
            MapFragment mapFragment = null;


            for (Fragment f : fra) {

                if (f != null && f.getClass().toString().equals("class com.almaviva.euregio.fragment.MapFragment")) {
                    mapFragment = (MapFragment) f;
                }
            }
            if (mapFragment != null) {
                ArrayList<Supplier> ar = LocalStorage.getListOfEsercentiFiltrataMappa();

                mapFragment.googleMap.clear();

                for (Supplier sup : ar) {

                    Double lat = Double.parseDouble(sup.location.lat);
                    Double lon = Double.parseDouble(sup.location.lon);
                    LatLng markerLatLng = new LatLng(lat, lon);

                    MarkerOptions markerCorrente = new MarkerOptions().position(markerLatLng).title(sup.location.description);
                    String id = Integer.toString(sup.id);
                    markerCorrente.snippet(id);
                    mapFragment.googleMap.addMarker(markerCorrente);
                }

            }


        } catch (Exception e) {
            Log.e(Thread.currentThread().getStackTrace().toString(), e.toString());
        }

    }


    public static void aggiornaFiltriImpostati(Context con, boolean isFromImpostazioni) {
        try {
            SharedPreferences spref = PreferenceManager.getDefaultSharedPreferences(con);

            setLanguage(spref);
            setOrdinamentoEsercenti(spref, con);

            if (isFromImpostazioni) {

                //Altrimenti sono già stati impostati in MainActivity prima del lancio dell'app
                setComprensori(spref);
            }
        } catch (Exception e) {
            Log.e(Thread.currentThread().getStackTrace().toString(), e.toString());
        }


    }

    private static void setLanguage(SharedPreferences spref) {
        try {
            String lingua = spref.getString("lingua", "");

            if (lingua == "") {
                //Al primo avvio controllo la lingua di sistema

                String linguaSistema = Locale.getDefault().getDisplayLanguage().toString();

                if (linguaSistema.equals("Deutsch")) {
                    SharedPreferences.Editor editor = spref.edit();
                    editor.putString("lingua", "Deutsch");
                    editor.commit();
                    LocalStorage.setPrevoiusLanguageLista("Deutsch");
                    LocalStorage.setPrevoiusLanguageMap("Deutsch");
                } else {
                    SharedPreferences.Editor editor = spref.edit();
                    editor.putString("lingua", "Italiano");
                    editor.commit();
                    LocalStorage.setPrevoiusLanguageLista("Italiano");
                    LocalStorage.setPrevoiusLanguageMap("Italiano");
                }

            } else {
                //Se non è il primo avvio controllo la preferenza della lingua

                if (lingua.equals("Deutsch")) {
                    //IMPOSTA TEDESCO
                    SharedPreferences.Editor editor = spref.edit();
                    editor.putString("lingua", "Deutsch");
                    editor.commit();

                    if(LocalStorage.getPrevoiusLanguageLista()==null && LocalStorage.getPrevoiusLanguageMap()==null){
                        LocalStorage.setPrevoiusLanguageLista("Deutsch");
                        LocalStorage.setPrevoiusLanguageMap("Deutsch");
                    }
                } else {
                    //IMPOSTA ITALIANO
                    SharedPreferences.Editor editor = spref.edit();
                    editor.putString("lingua", "Italiano");
                    editor.commit();
                    if(LocalStorage.getPrevoiusLanguageLista()==null && LocalStorage.getPrevoiusLanguageMap()==null){
                        LocalStorage.setPrevoiusLanguageLista("Italiano");
                        LocalStorage.setPrevoiusLanguageMap("Italiano");
                    }
                }
            }
        } catch (Exception e) {
            Log.e(Thread.currentThread().getStackTrace().toString(), e.toString());
        }

    }


    private static void setOrdinamentoEsercenti(SharedPreferences spref, Context con) {
        try {
            String ordinamentoEsercenti = spref.getString("ordinamento_esercenti", "");

            if (ordinamentoEsercenti.equals("")) {
                SharedPreferences.Editor editor = spref.edit();
                editor.putString("ordinamento_esercenti", con.getString(R.string.lista_data));
                editor.commit();
                LocalStorage.setFiltroOrdine(con.getString(R.string.data_aggiornamento));
            } else {

                if (ordinamentoEsercenti.equals(con.getString(R.string.lista_data))) {
                    SharedPreferences.Editor editor = spref.edit();
                    editor.putString("ordinamento_esercenti", con.getString(R.string.lista_data));
                    editor.commit();
                    LocalStorage.setFiltroOrdine(con.getString(R.string.data_aggiornamento));
                } else if (ordinamentoEsercenti.equals(con.getString(R.string.lista_alfabetica))) {
                    SharedPreferences.Editor editor = spref.edit();
                    editor.putString("ordinamento_esercenti", con.getString(R.string.lista_alfabetica));
                    editor.commit();
                    LocalStorage.setFiltroOrdine(con.getString(R.string.alfabetico));
                }

            }
        } catch (Exception e) {
            Log.e(Thread.currentThread().getStackTrace().toString(), e.toString());
        }

    }

    private static void setComprensori(SharedPreferences spref) {
        ArrayList<District> comprensori = new ArrayList<District>();

        comprensori = LocalStorage.getListOfComprensori();
        try {
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


        } catch (Exception e) {
            e.printStackTrace();
        }

    }

}