package com.almaviva.euregio.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.preference.PreferenceScreen;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.almaviva.euregio.R;
import com.almaviva.euregio.helper.LocalStorage;
import com.almaviva.euregio.mock.CategoryMock;
import com.almaviva.euregio.mock.DistrictMock;
import com.almaviva.euregio.model.Category;
import com.almaviva.euregio.model.District;

import java.text.ParseException;

import static android.R.attr.category;

/**
 * Created by a.sciarretta on 16/04/2018.
 */

public class SettingsFragment extends PreferenceFragment {


    private PreferenceScreen categorie;
    private PreferenceCategory categoriaLingua;
    private PreferenceCategory categoriaPush;
    private PreferenceCategory categoriaFiltri;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {


        PreferenceScreen screenPrincipale = getPreferenceManager().createPreferenceScreen(getActivity());

        categoriaLingua = new PreferenceCategory(getActivity());
        categoriaLingua.setKey("categoria_lingua");
        categoriaLingua.setTitle("Lingua");

        screenPrincipale.addPreference(categoriaLingua);

        categoriaPush = new PreferenceCategory(getActivity());
        categoriaPush.setKey("categoria_push");
        categoriaPush.setTitle("Push Notification");
        screenPrincipale.addPreference(categoriaPush);

        categoriaFiltri = new PreferenceCategory(getActivity());
        categoriaFiltri.setKey("categoria_filtri");
        categoriaFiltri.setTitle("Filtri");
        screenPrincipale.addPreference(categoriaFiltri);

        createComponentLingua(categoriaLingua);
        createComponentPush(categoriaPush);
        createComponentMascheraIniziale(categoriaFiltri);
        createComponentVisualizzazione(categoriaFiltri);

        try {
            createComponentCategorie(categoriaFiltri);
        } catch (ParseException e) {
            e.printStackTrace();
        }


        setPreferenceScreen(screenPrincipale);

        // Load the preferences from an XML resource
        // addPreferencesFromResource(R.xml.fragment_settings);
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    private void createComponentVisualizzazione(PreferenceCategory categoriaFiltri){

        final ListPreference listaVisualizzazione = new ListPreference(getActivity());

        String[] charSequenceLingua = new String[3];

        charSequenceLingua[0] = "Mappa";
        charSequenceLingua[1] = "Lista Alfabetica";
        charSequenceLingua[2] = "Lista Data Aggiornamento";

        listaVisualizzazione.setKey("lista_visualizzazione");
        listaVisualizzazione.setTitle("Visualizzazione Esercenti");

        listaVisualizzazione.setDialogTitle("Visualizzazione Esercenti");
        listaVisualizzazione.setEntries(charSequenceLingua);
        listaVisualizzazione.setEntryValues(charSequenceLingua);
        listaVisualizzazione.setDefaultValue(charSequenceLingua[0]);

        listaVisualizzazione.setSummary(charSequenceLingua[0]);

        listaVisualizzazione.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                listaVisualizzazione.setValue(newValue.toString());
                listaVisualizzazione.setSummary(newValue.toString());
                return false;
            }
        });

        categoriaFiltri.addPreference(listaVisualizzazione);
    }

    private void createComponentMascheraIniziale(PreferenceCategory categoriaFiltri) {
       final ListPreference listaMaschere = new ListPreference(getActivity());

        String[] charSequenceLingua = new String[2];

        charSequenceLingua[0] = "Esercenti";
        charSequenceLingua[1] = "Mappa";

        listaMaschere.setKey("lista_maschera");
        listaMaschere.setTitle("Maschera Iniziale");

        listaMaschere.setDialogTitle("Maschera Iniziale");
        listaMaschere.setEntries(charSequenceLingua);
        listaMaschere.setEntryValues(charSequenceLingua);
        listaMaschere.setDefaultValue(charSequenceLingua[0]);
        listaMaschere.setSummary(charSequenceLingua[0]);

        listaMaschere.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                listaMaschere.setValue(newValue.toString());
                listaMaschere.setSummary(newValue.toString());
                return false;
            }
        });

        categoriaFiltri.addPreference(listaMaschere);
    }

    private void createComponentPush(PreferenceCategory categoriaPush) {

        CheckBoxPreference checkBoxPref = new CheckBoxPreference(getActivity());
        checkBoxPref.setSummary("Ricevi notifiche Push");
        checkBoxPref.setChecked(true);

        categoriaPush.addPreference(checkBoxPref);

    }

    private void createComponentLingua(final PreferenceCategory categoriaLingua) {

        final ListPreference listaLingue = new ListPreference(getActivity());

        String[] charSequenceLingua = new String[2];

        charSequenceLingua[0] = "Italiano";
        charSequenceLingua[1] = "Deautche";

        listaLingue.setKey("lista_lingue");
        listaLingue.setDialogTitle("Lingua");
        listaLingue.setEntries(charSequenceLingua);
        listaLingue.setEntryValues(charSequenceLingua);

        listaLingue.setSummary(charSequenceLingua[0]);

        listaLingue.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                listaLingue.setValue(newValue.toString());
                listaLingue.setSummary(newValue.toString());
                return false;
            }
        });

        categoriaLingua.addPreference(listaLingue);

    }

    private void createComponentCategorie(PreferenceCategory categoriaFiltri) throws ParseException {

        PreferenceScreen screenCategory = getPreferenceManager().createPreferenceScreen(getActivity());
        screenCategory.setTitle(getString(R.string.comprensorio));



        for (District dis : DistrictMock.getListMock()) {
            CheckBoxPreference checkBoxPref = new CheckBoxPreference(getActivity());
            checkBoxPref.setSummary(dis.properties.name);
            checkBoxPref.setChecked(false);
            screenCategory.addPreference(checkBoxPref);
        }



        categoriaFiltri.addPreference(screenCategory);

    }


}
