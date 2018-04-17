package com.almaviva.euregio.model;

import java.text.ParseException;

/**
 * Created by a.sciarretta on 28/03/2018.
 */

public class Product {


    public int id;
    public String description;
    public String descriptionShort;


    public String getDescriptionShort() {
        return this.descriptionShort;
    }

    public void setDescriptionShort(String descriptionShort){
        this.descriptionShort = descriptionShort;
    }
}
