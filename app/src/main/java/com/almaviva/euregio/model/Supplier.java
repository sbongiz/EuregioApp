package com.almaviva.euregio.model;

import java.util.ArrayList;

/**
 * Created by a.sciarretta on 28/03/2018.
 */

public class Supplier {

    private SupplierProperties properties;

    private class SupplierProperties{
        private int id;
        private String title;
        private Location location;
        private ArrayList<Category> categories;
        private String pictureUrl;
        private String phone;
        private String email;
        private String web;
        private ArrayList<Agreement> agreements;
        private String lastUpdate;
    }


    private class Location{
        private LocationProperties properties;
    }
    private class LocationProperties{
        private String description;
        private String municipality;
        private District district;
        private String nation;
        private String lat;
        private String lon;
    }



}
