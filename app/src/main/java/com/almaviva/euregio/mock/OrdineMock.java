package com.almaviva.euregio.mock;

import com.almaviva.euregio.model.District;
import com.almaviva.euregio.model.OrdineFiltro;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.StringTokenizer;

/**
 * Created by a.sciarretta on 11/04/2018.
 */

public class OrdineMock {
    public static ArrayList<OrdineFiltro> ordineArrayList;

    public static ArrayList<OrdineFiltro> getListMock() throws ParseException {
        ordineArrayList = new ArrayList<OrdineFiltro>();

        OrdineFiltro ordineFiltro = new OrdineFiltro();

        ordineFiltro.id= 0;
        ordineFiltro.descr = "Alfabetico";

        ordineArrayList.add(ordineFiltro);

        OrdineFiltro ordineFiltro1 = new OrdineFiltro();

        ordineFiltro1.id=1;
        ordineFiltro1.descr ="Data di aggiornamento";

        ordineArrayList.add(ordineFiltro1);

        return  ordineArrayList;
    }
}



