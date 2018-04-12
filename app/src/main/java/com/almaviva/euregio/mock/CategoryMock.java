package com.almaviva.euregio.mock;

import com.almaviva.euregio.model.Category;
import com.almaviva.euregio.model.CategoryProperties;

import java.text.ParseException;
import java.util.ArrayList;

/**
 * Created by a.sciarretta on 10/04/2018.
 */

public class CategoryMock {
    public static ArrayList<Category> categoryArrayList;

    public static ArrayList<Category> getListMock() throws ParseException {

        categoryArrayList = new ArrayList<Category>();

        CategoryProperties categoryProperties = new CategoryProperties();

        categoryProperties.id=0;
        categoryProperties.name="Alimentari";

        Category cat = new Category();
        cat.properties = categoryProperties;
        categoryArrayList.add(cat);



        CategoryProperties categoryProperties1 = new CategoryProperties();

        categoryProperties1.id=1;
        categoryProperties1.name="Casalinghi e abitare";

        Category cat1 = new Category();
        cat1.properties = categoryProperties1;
        categoryArrayList.add(cat1);


        CategoryProperties categoryProperties2 = new CategoryProperties();

        categoryProperties2.id=2;
        categoryProperties2.name="Vestiti";

        Category cat2 = new Category();
        cat2.properties = categoryProperties2;
        categoryArrayList.add(cat2);


        CategoryProperties categoryProperties3 = new CategoryProperties();

        categoryProperties3.id=3;
        categoryProperties3.name="Trasporti";

        Category cat3 = new Category();
        cat3.properties = categoryProperties3;
        categoryArrayList.add(cat3);


        CategoryProperties categoryProperties4 = new CategoryProperties();

        categoryProperties4.id=4;
        categoryProperties4.name="Infanzia e giocattoli";

        Category cat4 = new Category();
        cat4.properties = categoryProperties4;
        categoryArrayList.add(cat4);



        CategoryProperties categoryProperties5 = new CategoryProperties();

        categoryProperties5.id=5;
        categoryProperties5.name="Gastornomia/hotel/strutture ricettive";

        Category cat5 = new Category();
        cat5.properties = categoryProperties5;
        categoryArrayList.add(cat5);


        CategoryProperties categoryProperties6 = new CategoryProperties();

        categoryProperties6.id=6;
        categoryProperties6.name="Formazione";

        Category cat6 = new Category();
        cat6.properties = categoryProperties6;
        categoryArrayList.add(cat6);



        CategoryProperties categoryProperties7 = new CategoryProperties();

        categoryProperties7.id=21;
        categoryProperties7.name="Tempo libero/cultura/sport";

        Category cat7 = new Category();
        cat7.properties = categoryProperties7;
        categoryArrayList.add(cat7);




        CategoryProperties categoryProperties8 = new CategoryProperties();

        categoryProperties8.id=8;
        categoryProperties8.name="Cartolibreria";

        Category cat8 = new Category();
        cat8.properties = categoryProperties8;
        categoryArrayList.add(cat8);


        CategoryProperties categoryProperties9 = new CategoryProperties();

        categoryProperties9.id=17;
        categoryProperties9.name="Comunicazione";

        Category cat9 = new Category();
        cat9.properties = categoryProperties9;
        categoryArrayList.add(cat9);



        CategoryProperties categoryProperties10 = new CategoryProperties();

        categoryProperties10.id=10;
        categoryProperties10.name="Salute e bellezza";

        Category cat10 = new Category();
        cat10.properties = categoryProperties10;
        categoryArrayList.add(cat10);



        CategoryProperties categoryProperties11 = new CategoryProperties();

        categoryProperties11.id=11;
        categoryProperties11.name="Banche ed assicurazioni";

        Category cat11 = new Category();
        cat11.properties = categoryProperties11;
        categoryArrayList.add(cat11);



        CategoryProperties categoryProperties12 = new CategoryProperties();

        categoryProperties12.id=15;
        categoryProperties12.name="Altro";

        Category cat12 = new Category();
        cat12.properties = categoryProperties12;
        categoryArrayList.add(cat12);

        return categoryArrayList;
    }
}