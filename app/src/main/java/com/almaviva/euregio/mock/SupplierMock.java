package com.almaviva.euregio.mock;

import android.preference.PreferenceActivity;
import android.util.JsonReader;
import android.util.Log;

import com.almaviva.euregio.model.Product;
import com.almaviva.euregio.model.AgreementProperties;
import com.almaviva.euregio.model.Category;
import com.almaviva.euregio.model.CategoryProperties;
import com.almaviva.euregio.model.District;
import com.almaviva.euregio.model.DistrictProperties;
import com.almaviva.euregio.model.Location;
import com.almaviva.euregio.model.LocationProperties;
import com.almaviva.euregio.model.Supplier;
import com.almaviva.euregio.model.SupplierProperties;

import com.google.gson.Gson;
import com.loopj.android.http.*;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.ParseException;
import java.util.ArrayList;

import cz.msebera.android.httpclient.Header;

/**
 * Created by a.sciarretta on 06/04/2018.
 */

public class SupplierMock {

    public static ArrayList<Supplier> supplierMockList;
    public static ArrayList<Supplier> suppliers;
    public static ArrayList<Category> categorie;
    public static ArrayList<District> comprensori;


    public static ArrayList<Supplier> getListaSupplier() throws JSONException {

            suppliers = new ArrayList<Supplier>();

            SupplierRestClient.get("", null, new JsonHttpResponseHandler() {
                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                    // If the response is JSONObject instead of expected JSONArray
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, JSONArray timeline) {
                    // Pull out the first event on the public timeline
                    JSONObject firstEvent = null;
                    try {


                       for (int i = 0;i<timeline.length();i++){
                           firstEvent = (JSONObject) timeline.get(i);
                           Supplier data = new Gson().fromJson(firstEvent.toString(), Supplier.class);
                           suppliers.add(data);
                       }

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });


        return suppliers;
    }

    public static ArrayList<Supplier> getListMock() throws ParseException {

        supplierMockList = new ArrayList<Supplier>();
        categorie = CategoryMock.getListMock();
        comprensori = DistrictMock.getListMock();


        //ESERCENTE 2
        Supplier sup2 = new Supplier();
     //   sup2.properties = new SupplierProperties();
     //   sup2.properties.id = 2;
     //   sup2.properties.title = "Unicredit";
     //   sup2.properties.pictureUrl = "";
     //   sup2.properties.phone = "0000000002";
     //   sup2.properties.email = "uniCredit@mail.it";
     //   sup2.properties.web = "www.uniCredit.it";
     //   sup2.properties.lastUpdate = "20-08";

        Location loc2 = new Location();
        LocationProperties locationProperties2 = new LocationProperties();



        locationProperties2.description = "Piazza del Grano 45";
        locationProperties2.municipality = "Bolzano (BZ)";
        locationProperties2.nation = "Italia";
        locationProperties2.lat = "46.4984421";
        locationProperties2.lon = "11.3544793";

        locationProperties2.district = comprensori.get(3);

        //    loc2.properties = locationProperties2;

        //   sup2.properties.location = loc2;



        ArrayList<Category> categoryList2= new ArrayList<Category>();
        categoryList2.add(categorie.get(3));
        categoryList2.add(categorie.get(4));
        categoryList2.add(categorie.get(5));
        //    sup2.properties.categories = categoryList2;

        ArrayList<Product> vantaggiArrayList2 = new ArrayList<Product>();

        for (int x = 0; x < 2; x++) {
            Product vant = new Product();
            AgreementProperties agreementProperties = new AgreementProperties();
            agreementProperties.id = x;
            agreementProperties.description="descrizione lunga numero "+x;
            agreementProperties.descriptionShort = "shortDescription "+x;
            //       vant.properties =agreementProperties;
            vantaggiArrayList2.add(vant);
        }

        //     sup2.properties.agreements = vantaggiArrayList2;

        supplierMockList.add(sup2);


        //ESERCENTE 1
        Supplier sup = new Supplier();
    //    sup.properties = new SupplierProperties();
    //    sup.properties.id = 1;
    //    sup.properties.title = "Stand Speck Mercatino di natale";
    //    sup.properties.pictureUrl = "";
    //    sup.properties.phone = "0000000001";
    //    sup.properties.email = "standSpeck@mail.it";
    //    sup.properties.web = "www.stendSpeck.it";
    //    sup.properties.lastUpdate = "26-12";

        Location loc = new Location();
        LocationProperties locationProperties = new LocationProperties();




        locationProperties.description = "Piazza del Grano 21";
        locationProperties.municipality = "Bolzano (BZ)";
        locationProperties.nation = "Italia";
        locationProperties.lat = "46.4984357";
        locationProperties.lon = "11.3545733";



        locationProperties.district = comprensori.get(3);

        //    loc.properties = locationProperties;

//        sup.properties.location = loc;

        ArrayList<Category> categoryList= new ArrayList<Category>();
        categoryList.add(categorie.get(0));
        categoryList.add(categorie.get(1));
        categoryList.add(categorie.get(2));


        //     sup.properties.categories = categoryList;

        ArrayList<Product> vantaggiArrayList = new ArrayList<Product>();

        for (int x = 0; x < 3; x++) {
            Product vant = new Product();
            AgreementProperties agreementProperties = new AgreementProperties();
            agreementProperties.id = x;
            agreementProperties.description="descrizione lunga numero "+x;
            agreementProperties.descriptionShort = "shortDescription "+x;
            //      vant.properties =agreementProperties;
            vantaggiArrayList.add(vant);
        }

        //     sup.properties.agreements = vantaggiArrayList;

        supplierMockList.add(sup);


        //ESERCENTE 3
        Supplier sup3 = new Supplier();
   //     sup3.properties = new SupplierProperties();
   //     sup3.properties.id = 3;
   //     sup3.properties.title = "Sparkasse - Cassa di risparmio";
   //     sup3.properties.pictureUrl = "";
   //     sup3.properties.phone = "0000000003";
   //     sup3.properties.email = "sparkasse@mail.it";
   //     sup3.properties.web = "www.sparkasse.it";
   //     sup3.properties.lastUpdate = "06-08";

        Location loc3 = new Location();
        LocationProperties locationProperties3 = new LocationProperties();


        locationProperties3.description = "Piazza Walther 29";
        locationProperties3.municipality = "Bolzano (BZ)";
        locationProperties3.nation = "Italia";
        locationProperties3.lat = "46.498849";
        locationProperties3.lon = "11.354857";



        locationProperties3.district = comprensori.get(3);

        //    loc3.properties = locationProperties3;

        //    sup3.properties.location = loc3;

        ArrayList<Category> categoryList3= new ArrayList<Category>();
        categoryList3.add(categorie.get(1));
        categoryList3.add(categorie.get(5));
        categoryList3.add(categorie.get(7));


        //   sup3.properties.categories = categoryList3;

        ArrayList<Product> vantaggiArrayList3 = new ArrayList<Product>();

        for (int x = 0; x < 2; x++) {
            Product vant = new Product();
            AgreementProperties agreementProperties = new AgreementProperties();
            agreementProperties.id = x;
            agreementProperties.description="descrizione lunga numero "+x;
            agreementProperties.descriptionShort = "shortDescription "+x;
            //      vant.properties =agreementProperties;
            vantaggiArrayList3.add(vant);
        }

        //     sup3.properties.agreements = vantaggiArrayList3;

        supplierMockList.add(sup3);

        //ESERCENTE 4
        Supplier sup4 = new Supplier();
    //    sup4.properties = new SupplierProperties();
    //    sup4.properties.id = 4;
    //    sup4.properties.title = "Palazzo Menz";
    //    sup4.properties.pictureUrl = "";
    //    sup4.properties.phone = "0000000004";
    //    sup4.properties.email = "menz@mail.it";
    //    sup4.properties.web = "www.menz.it";
    //    sup4.properties.lastUpdate = "18-01";

        Location loc4 = new Location();
        LocationProperties locationProperties4 = new LocationProperties();


        locationProperties4.description = "Via della mostra 3";
        locationProperties4.municipality = "Bolzano (BZ)";
        locationProperties4.nation = "Italia";
        locationProperties4.lat = "46.498509";
        locationProperties4.lon = "11.353978";



        locationProperties4.district = comprensori.get(1);

        //     loc4.properties = locationProperties4;

        //    sup4.properties.location = loc4;
//
        ArrayList<Category> categoryList4= new ArrayList<Category>();
        categoryList4.add(categorie.get(1));
        categoryList4.add(categorie.get(4));
        categoryList4.add(categorie.get(8));


        //    sup4.properties.categories = categoryList4;

        ArrayList<Product> vantaggiArrayList4 = new ArrayList<Product>();

        for (int x = 0; x < 3; x++) {
            Product vant = new Product();
            AgreementProperties agreementProperties = new AgreementProperties();
            agreementProperties.id = x;
            agreementProperties.description="descrizione lunga numero "+x;
            agreementProperties.descriptionShort = "shortDescription "+x;
            //       vant.properties =agreementProperties;
            vantaggiArrayList4.add(vant);
        }

        //     sup4.properties.agreements = vantaggiArrayList4;

        supplierMockList.add(sup4);

        //ESERCENTE 5
        Supplier sup5 = new Supplier();
   //     sup5.properties = new SupplierProperties();
   //     sup5.properties.id = 5;
   //     sup5.properties.title = "Italia & Amore";
   //     sup5.properties.pictureUrl = "";
   //     sup5.properties.phone = "0000000005";
   //     sup5.properties.email = "italiaeamore.it";
   //     sup5.properties.lastUpdate = "05-04";

        Location loc5 = new Location();
        LocationProperties locationProperties5 = new LocationProperties();


        locationProperties5.description = "Via Argentieri 9";
        locationProperties5.municipality = "Bolzano (BZ)";
        locationProperties5.nation = "Italia";
        locationProperties5.lat = "46.498978";
        locationProperties5.lon = "11.354363";



        locationProperties5.district = comprensori.get(3);

        //     loc5.properties = locationProperties5;

        //     sup5.properties.location = loc5;

        ArrayList<Category> categoryList5= new ArrayList<Category>();
        categoryList5.add(categorie.get(1));
        categoryList5.add(categorie.get(2));
        categoryList5.add(categorie.get(9));


        //      sup5.properties.categories = categoryList5;

        ArrayList<Product> vantaggiArrayList5 = new ArrayList<Product>();

        for (int x = 0; x < 1; x++) {
            Product vant = new Product();
            AgreementProperties agreementProperties = new AgreementProperties();
            agreementProperties.id = x;
            agreementProperties.description="descrizione lunga numero "+x;
            agreementProperties.descriptionShort = "shortDescription "+x;
            //          vant.properties =agreementProperties;
            vantaggiArrayList5.add(vant);
        }

        //     sup5.properties.agreements = vantaggiArrayList5;

        supplierMockList.add(sup5);


        return supplierMockList;
    }
}




