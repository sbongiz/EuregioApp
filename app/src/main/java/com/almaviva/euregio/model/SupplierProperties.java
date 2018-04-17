package com.almaviva.euregio.model;

import java.util.ArrayList;

/**
 * Created by a.sciarretta on 06/04/2018.
 */

public class SupplierProperties {
    public int id;
    public String title;
    public Location location;
    public ArrayList<Category> categories;
    public String pictureUrl;
    public String phone;
    public String email;
    public String web;
    public ArrayList<Product> product;
    public String lastUpdate;
}
