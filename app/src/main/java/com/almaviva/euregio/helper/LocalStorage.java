package com.almaviva.euregio.helper;

import android.content.Context;

import com.almaviva.euregio.model.Category;
import com.almaviva.euregio.model.District;
import com.almaviva.euregio.model.Supplier;
import com.google.android.gms.maps.model.LatLng;

import java.sql.Array;
import java.util.ArrayList;

/**
 * Created by a.sciarretta on 10/04/2018.
 */

public  class LocalStorage {

    public static ArrayList<Integer> filtriCategoria = new ArrayList<Integer>();
    public static ArrayList<Integer> filtriCategoriaMappa = new ArrayList<Integer>();
    public static ArrayList<Integer> filtroComprensorio=null;
    public static String filtroOrdine = null;
    public static boolean isSetCategoria=false;
    public static boolean isSetCategoriaMappa= false;
    public static boolean isSetComprensorio=false;
    public static ArrayList<Supplier> listOfEsercenti = new ArrayList<Supplier>() ;
    public static ArrayList<Supplier> listOfEsercentiFiltrata = new ArrayList<Supplier>();
    public static ArrayList<Supplier> listOfEsercentiFiltrataMappa = new ArrayList<Supplier>();

    public static ArrayList<District> listOfDistricts = new ArrayList<District>();

    public static ArrayList<Category> listOfCategories= new ArrayList<Category>();

    public static boolean isDecrescente=false;
    public static String testoCercato;
    public static String testoCercatoMappa;
    public static boolean isLogged=false;

    public static String prevoiusLanguageLista;
    public static String prevoiusLanguageMap;

    public static LatLng myLastKnownLocation = null;


    public static void setFiltriCategoriaMappa(ArrayList<Integer> filtriCategoriaMappaNew){
        filtriCategoriaMappa = filtriCategoriaMappaNew;
    }

    public static ArrayList<Integer> getFiltriCategoriaMappa(){
        return  filtriCategoriaMappa;
    }

    public static void setFiltriCategoria(ArrayList<Integer> filtriCategoriaNew){
        filtriCategoria = filtriCategoriaNew;
    }

    public static ArrayList<Integer> getFiltriCategoria(){
        return  filtriCategoria;
    }


    public static void  setFiltroComprensorio(ArrayList<Integer> filtroComprensorioNew){
        filtroComprensorio = filtroComprensorioNew;
    }
    public static ArrayList<Integer> getFiltriComprensorio(){
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

    public static void  setIsSetCategoriaMappa(boolean isSetCategoriaMappaNew){
        isSetCategoriaMappa = isSetCategoriaMappaNew;
    }
    public static boolean getIsSetCategoriaMappa(){
        return  isSetCategoriaMappa;
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

    public static Integer getNumberOfFilterSetMappa(){
        Integer number = 0;
        if(isSetCategoriaMappa){
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

    public static void setListOfEsercentiFiltrataMappa(ArrayList<Supplier> listOfEsercentiFiltrataMappaNew){
        listOfEsercentiFiltrataMappa = listOfEsercentiFiltrataMappaNew;
    }

    public static ArrayList<Supplier> getListOfEsercentiFiltrataMappa(){
        return  listOfEsercentiFiltrataMappa;
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

    public static void setTestoCercatoMappa(String testoCercatoMappaNew){
        testoCercatoMappa = testoCercatoMappaNew;
    }

    public static String getTestoCercatoMappa(){
        return  testoCercatoMappa;
    }


    public static void setIsLogged(boolean isLoggedNew){
        isLogged=isLoggedNew;
    }

    public static boolean getIsLogged(){
        return  isLogged;
    }


    public static void setListOfComprensori(ArrayList<District> listOfComprensoriNew){
        listOfDistricts = listOfComprensoriNew;
    }

    public static ArrayList<District> getListOfComprensori(){
        return  listOfDistricts;
    }

    public static void setListOfCategories(ArrayList<Category> listOfCategoriesNew){
        listOfCategories = listOfCategoriesNew;
    }

    public static ArrayList<Category> getListOfCategories(){
        return  listOfCategories;
    }

    public static void setMyLastKnownLocation(LatLng myLocationNew){
        myLastKnownLocation = myLocationNew;
    }

    public static LatLng getMyLastKnownLocation(){
        return  myLastKnownLocation;
    }


    public static void setPrevoiusLanguageLista(String previousLanguageNew){
        prevoiusLanguageLista = previousLanguageNew;
    }
    public static String getPrevoiusLanguageLista(){
        return prevoiusLanguageLista;
    }

    public static void setPrevoiusLanguageMap(String prevoiusLanguageNew){
        prevoiusLanguageMap = prevoiusLanguageNew;
    }

    public static String getPrevoiusLanguageMap(){
        return prevoiusLanguageMap;
    }

}
