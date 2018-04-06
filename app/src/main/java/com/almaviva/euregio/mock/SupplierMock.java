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

    public static ArrayList<Supplier> getListMock() throws ParseException {

        supplierMockList = new ArrayList<Supplier>();


        //ESERCENTE 1
        Supplier sup = new Supplier();
        sup.properties = new SupplierProperties();
        sup.properties.id = 1;
        sup.properties.title = "Stand Speck Mercatino di natale";
        sup.properties.pictureUrl = "";
        sup.properties.phone = "0000000001";
        sup.properties.email = "standSpeck@mail.it";
        sup.properties.web = "www.stendSpeck.it";
        sup.properties.lastUpdate = "2017-12-26";

        Location loc = new Location();
        LocationProperties locationProperties = new LocationProperties();
        District dis = new District();
        DistrictProperties districtProperties = new DistrictProperties();


        locationProperties.description = "Piazza del Grano 21";
        locationProperties.municipality = "Bolzan0o (BZ)";
        locationProperties.nation = "Italia";
        locationProperties.lat = "46.4984357";
        locationProperties.lon = "11.3545733";

        districtProperties.id = 1;
        districtProperties.name = "Alto-Adige";
        dis.description = "Descrizione Alto-adige";
        dis.properties = districtProperties;

        locationProperties.district = dis;

        loc.properties = locationProperties;


        Category cat = new Category();
        CategoryProperties categoryProperties = new CategoryProperties();

        categoryProperties.id = 1;
        categoryProperties.name = "Commerciante";
        cat.properties = categoryProperties;

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
        sup2.properties.lastUpdate = "2017-08-20";

        Location loc2 = new Location();
        LocationProperties locationProperties2 = new LocationProperties();
        District dis2 = new District();
        DistrictProperties districtProperties2 = new DistrictProperties();


        locationProperties2.description = "Piazza del Grano 45";
        locationProperties2.municipality = "Bolzano (BZ)";
        locationProperties2.nation = "Italia";
        locationProperties2.lat = "46.4984421";
        locationProperties2.lon = "11.3544793";

        districtProperties2.id = 1;
        districtProperties2.name = "Alto-Adige";
        dis2.description = "Descrizione Alto-adige";
        dis2.properties = districtProperties2;

        locationProperties2.district = dis2;

        loc2.properties = locationProperties2;


        Category cat2 = new Category();
        CategoryProperties categoryProperties2 = new CategoryProperties();

        categoryProperties2.id = 1;
        categoryProperties2.name = "Commerciante";
        cat2.properties = categoryProperties2;

        ArrayList<Agreement> vantaggiArrayList2 = new ArrayList<Agreement>();

        for (int x = 0; x < 1; x++) {
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




