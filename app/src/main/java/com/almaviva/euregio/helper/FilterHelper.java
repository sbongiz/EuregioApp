package com.almaviva.euregio.helper;

import com.almaviva.euregio.model.Category;
import com.almaviva.euregio.model.District;
import com.almaviva.euregio.model.Supplier;

import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Iterator;
import java.util.List;

/**
 * Created by a.sciarretta on 12/04/2018.
 */

public class FilterHelper {

    public static ArrayList<Supplier> arrayEsercenti;
    public static ArrayList<Supplier> arrayEsercentiFiltrato;

    public static ArrayList<Supplier> filtraListaEsercenti(){

        arrayEsercenti = new ArrayList<Supplier>();
        arrayEsercentiFiltrato = new ArrayList<Supplier>();

        arrayEsercenti = LocalStorage.getListOfEsercenti();
        arrayEsercentiFiltrato.addAll(arrayEsercenti);

        final ArrayList<Integer> listCategorieId = LocalStorage.getFiltriCategoria();
        Integer comprensorioId = LocalStorage.getFiltriComprensorio();

        Iterator<Supplier> iter = arrayEsercentiFiltrato.iterator();


        while (iter.hasNext()){
            boolean trovato=false;
            boolean rimosso= false;
            Supplier sup = iter.next();
            ArrayList<Category> categorieEsercente  =  sup.properties.categories;


            if(listCategorieId!=null){
                for (Category cat: categorieEsercente) {

                    if (listCategorieId.contains(cat.properties.id)){
                        trovato = true;
                        break;
                    }
                    String e ="";
                }
            }

            if(listCategorieId.size()==0){
                //se non specificato il filtro su categoria ritorno tutto!!!
                trovato = true;
            }

            if(!trovato){
                iter.remove();
                rimosso = true;
            }

            District comprensorioEsercente = sup.properties.location.properties.district;


            if(comprensorioId!= null && comprensorioId != comprensorioEsercente.properties.id){
            if(!rimosso) {
                iter.remove();
            }
            }

        }
        LocalStorage.setListOfEsercentiFiltrata(arrayEsercentiFiltrato);
        return arrayEsercentiFiltrato;

    }



}
