package com.almaviva.euregio.model;

/**
 * Created by a.sciarretta on 28/03/2018.
 */

public class Agreement {

    private AgreementProperties properties;

    private class AgreementProperties{
        private int id;
        private String description;
        private String descriptionShort;
    }
}
