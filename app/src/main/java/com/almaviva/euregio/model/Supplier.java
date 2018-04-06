package com.almaviva.euregio.model;

import java.util.ArrayList;

/**
 * Created by a.sciarretta on 28/03/2018.
 */

public class Supplier {

    public SupplierProperties properties;


    public String getTitle(){
        return this.properties.title;
    }

    public String getDate(){
        return this.properties.lastUpdate;
    }

    public String getIndirizzo(){
        return this.properties.location.properties.description + "," + this.properties.location.properties.municipality;
    }

    public ArrayList<Agreement> getListaVantaggi(){
        return this.properties.agreements;
    }

}

