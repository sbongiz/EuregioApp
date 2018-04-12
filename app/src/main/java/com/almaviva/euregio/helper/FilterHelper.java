package com.almaviva.euregio.helper;

import android.app.Activity;
import android.content.Context;
import android.support.v4.app.Fragment;

import com.almaviva.euregio.MainActivity;
import com.almaviva.euregio.R;
import com.almaviva.euregio.fragment.ListaFragment;
import com.almaviva.euregio.model.Category;
import com.almaviva.euregio.model.District;
import com.almaviva.euregio.model.Location;
import com.almaviva.euregio.model.Supplier;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.List;

/**
 * Created by a.sciarretta on 12/04/2018.
 */

public class FilterHelper {

    public static ArrayList<Supplier> arrayEsercenti;
    public static ArrayList<Supplier> arrayEsercentiFiltrato;


    public static void filtraTotale(Context con){

        boolean isDescrescente = LocalStorage.getIsDecrescente();
        String testoCercato= LocalStorage.getTestoCercato()
                ;

        filtraListaEsercenti();

        filtraListaPerOrdine(con,isDescrescente);

        filtraListaPerTesto(testoCercato);

        aggiornaComponentInListaFragment(con);


    }

    public static void filtraListaPerTesto(String testoCercato){

        if (arrayEsercentiFiltrato == null) {
            arrayEsercentiFiltrato = new ArrayList<Supplier>();
        }
        arrayEsercentiFiltrato = LocalStorage.getListOfEsercentiFiltrata();


        //DO MAGIC THINGS

        LocalStorage.setListOfEsercentiFiltrata(arrayEsercentiFiltrato);
    }


    public static void filtraListaPerOrdine(Context con, boolean isDescrescente) {


        if (arrayEsercentiFiltrato == null) {
            arrayEsercentiFiltrato = new ArrayList<Supplier>();
        }
        arrayEsercentiFiltrato = LocalStorage.getListOfEsercentiFiltrata();

        String tipoOrdine = LocalStorage.getFiltriOrdine();

        if(tipoOrdine == con.getString(R.string.alfabetico)){

            if(!isDescrescente){
                Collections.sort(arrayEsercentiFiltrato, new Comparator<Supplier>() {
                    @Override
                    public int compare(Supplier s1, Supplier s2) {
                        String prima = s1.properties.title;
                        String seconda= s2.properties.title;
                        return prima.compareToIgnoreCase(seconda);
                    }
                });
            }else{
                Collections.sort(arrayEsercentiFiltrato, new Comparator<Supplier>() {
                    @Override
                    public int compare(Supplier s1, Supplier s2) {
                        String prima = s1.properties.title;
                        String seconda= s2.properties.title;
                        return seconda.compareToIgnoreCase(prima);
                    }
                });
            }
        }else{

            if(!isDescrescente){
                Collections.sort(arrayEsercentiFiltrato, new Comparator<Supplier>() {
                    @Override
                    public int compare(Supplier s1, Supplier s2) {
                        String prima = s1.properties.lastUpdate;
                        String seconda= s2.properties.lastUpdate;
                        return prima.compareToIgnoreCase(seconda);
                    }
                });
            }else{
                Collections.sort(arrayEsercentiFiltrato, new Comparator<Supplier>() {
                    @Override
                    public int compare(Supplier s1, Supplier s2) {
                        String prima = s1.properties.lastUpdate;
                        String seconda= s2.properties.lastUpdate;
                        return seconda.compareToIgnoreCase(prima);
                    }
                });
            }

        }

        LocalStorage.setListOfEsercentiFiltrata(arrayEsercentiFiltrato);

    }

    public static void filtraListaEsercenti() {

        arrayEsercenti = new ArrayList<Supplier>();
        arrayEsercentiFiltrato = new ArrayList<Supplier>();

        arrayEsercenti = LocalStorage.getListOfEsercenti();
        arrayEsercentiFiltrato.addAll(arrayEsercenti);

        final ArrayList<Integer> listCategorieId = LocalStorage.getFiltriCategoria();
        Integer comprensorioId = LocalStorage.getFiltriComprensorio();

        Iterator<Supplier> iter = arrayEsercentiFiltrato.iterator();


        while (iter.hasNext()) {
            boolean trovato = false;
            boolean rimosso = false;
            Supplier sup = iter.next();
            ArrayList<Category> categorieEsercente = sup.properties.categories;


            if (listCategorieId != null) {
                for (Category cat : categorieEsercente) {

                    if (listCategorieId.contains(cat.properties.id)) {
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

            District comprensorioEsercente = sup.properties.location.properties.district;


            if (comprensorioId != 0 && comprensorioId != comprensorioEsercente.properties.id) {
                if (!rimosso) {
                    iter.remove();
                }
            }

        }
        LocalStorage.setListOfEsercentiFiltrata(arrayEsercentiFiltrato);
    }

    public static void aggiornaComponentInListaFragment(Context con){
        MainActivity context = (MainActivity) con;
        List<Fragment> fra = context.getSupportFragmentManager().getFragments();
        ListaFragment listaFragment = (ListaFragment) fra.get(0);

        ArrayList<Supplier> ar = LocalStorage.getListOfEsercentiFiltrata();

        listaFragment.esercenteListAdapter.add(LocalStorage.getListOfEsercentiFiltrata());
        listaFragment.esercenteListAdapter.notifyDataSetChanged();

        int numero_risultati = LocalStorage.getListOfEsercentiFiltrata().size();

        listaFragment.textViewNumeroRisultati.setText(numero_risultati + " " + con.getString(R.string.risultati));

    }
}
