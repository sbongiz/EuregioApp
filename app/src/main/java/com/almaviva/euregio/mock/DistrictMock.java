package com.almaviva.euregio.mock;

import com.almaviva.euregio.model.Category;
import com.almaviva.euregio.model.District;
import com.almaviva.euregio.model.DistrictProperties;

import java.text.ParseException;
import java.util.ArrayList;

/**
 * Created by a.sciarretta on 11/04/2018.
 */

public class DistrictMock {
    public static ArrayList<District> comprensorioArrayList;

    public static ArrayList<District> getListMock() throws ParseException {
        comprensorioArrayList = new ArrayList<District>();


        District dis = new District();
        DistrictProperties districtProperties = new DistrictProperties();
        districtProperties.id = 1;
        districtProperties.name = "Val Venosta";
      //  dis.properties = districtProperties;
        comprensorioArrayList.add(dis);

        District dis2 = new District();
        DistrictProperties districtProperties2 = new DistrictProperties();
        districtProperties2.id = 2;
        districtProperties2.name = "Burgraviato";
       // dis2.properties = districtProperties2;
        comprensorioArrayList.add(dis2);

        District dis3 = new District();
        DistrictProperties districtProperties3 = new DistrictProperties();
        districtProperties3.id = 3;
        districtProperties3.name = "Bolzano";
   //     dis3.properties = districtProperties3;
        comprensorioArrayList.add(dis3);

        District dis4 = new District();
        DistrictProperties districtProperties4 = new DistrictProperties();
        districtProperties4.id = 4;
        districtProperties4.name = "Oltradige/Bassa Atesina";
     //   dis4.properties = districtProperties4;
        comprensorioArrayList.add(dis4);

        District dis5 = new District();
        DistrictProperties districtProperties5 = new DistrictProperties();
        districtProperties5.id = 5;
        districtProperties5.name = "Val Isarco";
        //  dis5.properties = districtProperties5;
        comprensorioArrayList.add(dis5);

        District dis6 = new District();
        DistrictProperties districtProperties6 = new DistrictProperties();
        districtProperties6.id = 6;
        districtProperties6.name = "Wipptal";
        //  dis6.properties = districtProperties6;
        comprensorioArrayList.add(dis6);

        District dis7 = new District();
        DistrictProperties districtProperties7 = new DistrictProperties();
        districtProperties7.id = 16;
        districtProperties7.name = "Val Pusteria";
        //  dis7.properties = districtProperties7;
        comprensorioArrayList.add(dis7);


        District dis8 = new District();
        DistrictProperties districtProperties8 = new DistrictProperties();
        districtProperties8.id = 7;
        districtProperties8.name = "Salto-Sarentino-Renon";
        //     dis8.properties = districtProperties8;
        comprensorioArrayList.add(dis8);

        District dis9 = new District();
        DistrictProperties districtProperties9 = new DistrictProperties();
        districtProperties9.id = 8;
        districtProperties9.name = "Tirolo";
        //    dis9.properties = districtProperties9;
        comprensorioArrayList.add(dis9);

        District dis10 = new District();
        DistrictProperties districtProperties10 = new DistrictProperties();
        districtProperties10.id = 29;
        districtProperties10.name = "Trentino";
        //    dis10.properties = districtProperties10;
        comprensorioArrayList.add(dis10);

        return comprensorioArrayList;
    }
}
