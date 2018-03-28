package com.almaviva.euregio.model;

/**
 * Created by a.sciarretta on 28/03/2018.
 */

public class District {
    private String description;
    private DistrictProperties properties;

    private class DistrictProperties{
        private int id;
        private String name;
    }
}

