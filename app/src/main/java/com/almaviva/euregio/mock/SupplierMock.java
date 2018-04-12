package com.almaviva.euregio.mock;

import com.almaviva.euregio.model.Agreement;
import com.almaviva.euregio.model.AgreementProperties;
import com.almaviva.euregio.model.Category;
import com.almaviva.euregio.model.CategoryProperties;
import com.almaviva.euregio.model.District;
import com.almaviva.euregio.model.DistrictProperties;
import com.almaviva.euregio.model.Location;
import com.almaviva.euregio.model.LocationProperties;
import com.almaviva.euregio.model.Supplier;
import com.almaviva.euregio.model.SupplierProperties;

import java.text.ParseException;
import java.util.ArrayList;

/**
 * Created by a.sciarretta on 06/04/2018.
 */

public class SupplierMock {

    public static ArrayList<Supplier> supplierMockList;
    public static ArrayList<Category> categorie;
    public static ArrayList<District> comprensori;

    public static ArrayList<Supplier> getListMock() throws ParseException {

        supplierMockList = new ArrayList<Supplier>();
        categorie = CategoryMock.getListMock();
        comprensori = DistrictMock.getListMock();


        //ESERCENTE 1
        Supplier sup = new Supplier();
        sup.properties = new SupplierProperties();
        sup.properties.id = 1;
        sup.properties.title = "Stand Speck Mercatino di natale";
        sup.properties.pictureUrl = "";
        sup.properties.phone = "0000000001";
        sup.properties.email = "standSpeck@mail.it";
        sup.properties.web = "www.stendSpeck.it";
        sup.properties.lastUpdate = "26-12";

        Location loc = new Location();
        LocationProperties locationProperties = new LocationProperties();




        locationProperties.description = "Piazza del Grano 21";
        locationProperties.municipality = "Bolzan0o (BZ)";
        locationProperties.nation = "Italia";
        locationProperties.lat = "46.4984357";
        locationProperties.lon = "11.3545733";



        locationProperties.district = comprensori.get(2);

        loc.properties = locationProperties;

        sup.properties.location = loc;

        ArrayList<Category> categoryList= new ArrayList<Category>();
        categoryList.add(categorie.get(0));
        categoryList.add(categorie.get(1));
        categoryList.add(categorie.get(2));


        sup.properties.categories = categoryList;

        ArrayList<Agreement> vantaggiArrayList = new ArrayList<Agreement>();

        for (int x = 0; x < 3; x++) {
            Agreement vant = new Agreement();
            AgreementProperties agreementProperties = new AgreementProperties();
            agreementProperties.id = x;
            agreementProperties.description="descrizione lunga numero "+x;
            agreementProperties.descriptionShort = "shortDescription "+x;
            vant.properties =agreementProperties;
            vantaggiArrayList.add(vant);
        }

        sup.properties.agreements = vantaggiArrayList;

        supplierMockList.add(sup);

        //ESERCENTE 2
        Supplier sup2 = new Supplier();
        sup2.properties = new SupplierProperties();
        sup2.properties.id = 2;
        sup2.properties.title = "Unicredit";
        sup2.properties.pictureUrl = "";
        sup2.properties.phone = "0000000002";
        sup2.properties.email = "uniCredit@mail.it";
        sup2.properties.web = "www.uniCredit.it";
        sup2.properties.lastUpdate = "20-08";

        Location loc2 = new Location();
        LocationProperties locationProperties2 = new LocationProperties();



        locationProperties2.description = "Piazza del Grano 45";
        locationProperties2.municipality = "Bolzano (BZ)";
        locationProperties2.nation = "Italia";
        locationProperties2.lat = "46.4984421";
        locationProperties2.lon = "11.3544793";

        locationProperties2.district = comprensori.get(2);

        loc2.properties = locationProperties2;

        sup2.properties.location = loc2;



        ArrayList<Category> categoryList2= new ArrayList<Category>();
        categoryList2.add(categorie.get(3));
        categoryList2.add(categorie.get(4));
        categoryList2.add(categorie.get(5));
        sup2.properties.categories = categoryList2;

        ArrayList<Agreement> vantaggiArrayList2 = new ArrayList<Agreement>();

        for (int x = 0; x < 2; x++) {
            Agreement vant = new Agreement();
            AgreementProperties agreementProperties = new AgreementProperties();
            agreementProperties.id = x;
            agreementProperties.description="descrizione lunga numero "+x;
            agreementProperties.descriptionShort = "shortDescription "+x;
            vant.properties =agreementProperties;
            vantaggiArrayList2.add(vant);
        }

        sup2.properties.agreements = vantaggiArrayList2;

        supplierMockList.add(sup2);


        return supplierMockList;
    }
}




