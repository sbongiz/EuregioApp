package com.almaviva.euregio.helper;

import android.content.Context;

import com.almaviva.euregio.model.Category;
import com.almaviva.euregio.model.District;
import com.almaviva.euregio.model.Supplier;

import java.sql.Array;
import java.util.ArrayList;

/**
 * Created by a.sciarretta on 10/04/2018.
 */

public  class LocalStorage {

    public static ArrayList<Integer> filtriCategoria = new ArrayList<Integer>();
    public static Integer filtroComprensorio=null;
    public static String filtroOrdine = null;
    public static boolean isSetCategoria=false;
    public static boolean isSetComprensorio=false;
    public static ArrayList<Supplier> listOfEsercenti = new ArrayList<Supplier>() ;
    public static ArrayList<Supplier> listOfEsercentiFiltrata = new ArrayList<Supplier>();
    public static boolean isDecrescente=false;
    public static String testoCercato;


    public static void setFiltriCategoria(ArrayList<Integer> filtriCategoriaNew){
        filtriCategoria = filtriCategoriaNew;
    }

    public static ArrayList<Integer> getFiltriCategoria(){
        return  filtriCategoria;
    }

    public static void  setFiltroComprensorio(Integer filtroComprensorioNew){
        filtroComprensorio = filtroComprensorioNew;
    }
    public static Integer getFiltriComprensorio(){
        return  filtroComprensorio;
    }

    public static void  setFiltroOrdine(String filtroOrdineNew){

        filtroOrdine = filtroOrdineNew;
    }
    public static String getFiltriOrdine(){

        return  filtroOrdine;
    }

    public static void  setIsSetCategoria(boolean isSetCategoriaNew){
        isSetCategoria = isSetCategoriaNew;
    }
    public static boolean getIsSetCategoria(){
        return  isSetCategoria;
    }


    public static void  setIsSetComprensorio(boolean isSetComprensorioNew){
        isSetComprensorio = isSetComprensorioNew;
    }
    public static boolean getIsSetComprensorio(){
        return  isSetComprensorio;
    }


    public static Integer getNumberOfFilterSet(){
        Integer number = 0;
        if(isSetCategoria){
            number += 1;
        }
        if(isSetComprensorio){
            number += 1;
        }
        return number;
    }

    public static void setListOfEsercenti(ArrayList<Supplier> listOfEsercentiNew){
        listOfEsercenti = listOfEsercentiNew;
    }

    public static ArrayList<Supplier> getListOfEsercenti(){
        return  listOfEsercenti;
    }


    public static void setListOfEsercentiFiltrata(ArrayList<Supplier> listOfEsercentiFiltrataNew){
        listOfEsercentiFiltrata = listOfEsercentiFiltrataNew;
    }

    public static ArrayList<Supplier> getListOfEsercentiFiltrata(){
        return  listOfEsercentiFiltrata;
    }

    public static void setIsDecrescente(boolean isDecrescenteNew){
        isDecrescente = isDecrescenteNew;
    }

    public static boolean getIsDecrescente(){
            return  isDecrescente;
    }

    public static void setTestoCercato(String testoCercatoNew){
        testoCercato = testoCercatoNew;
    }

    public static String getTestoCercato(){
        return  testoCercato;
    }


}
