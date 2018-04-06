package com.almaviva.euregio.model;

import java.text.ParseException;

/**
 * Created by a.sciarretta on 28/03/2018.
 */

public class Agreement {


    public AgreementProperties properties;


    public String getDescriptionShort() {
        return this.properties.descriptionShort;
    }

    public void setDescriptionShort(String descriptionShort){
        this.properties.descriptionShort = descriptionShort;
    }
}
