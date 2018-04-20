package com.almaviva.euregio.model;

import android.preference.PreferenceCategory;

import java.util.ArrayList;

/**
 * Created by a.sciarretta on 28/03/2018.
 */

public class Supplier {

    public int id;
    public String title;
    public Location location;
    public ArrayList<Category> categories;
    public String pictureUrl;
    public String phone;
    public String email;
    public String web;
    public ArrayList<Product> products;
    public String lastUpdate;


    public String getTitle(){
        return this.title;
    }

    public String getDate(){
        return this.lastUpdate;
    }

    public String getIndirizzo(){

        if(this.location.description == null && this.location.municipality!=null){
            return this.location.municipality;
        }else if(this.location.description!= null && this.location.municipality == null){
            return this.location.description;
        }else if(this.location.description == null && this.location.municipality == null){
            return "";
        }else{
            return this.location.description + "," + this.location.municipality;
        }
    }

    public ArrayList<Product> getListaVantaggi(){
        return this.products;
    }

}

