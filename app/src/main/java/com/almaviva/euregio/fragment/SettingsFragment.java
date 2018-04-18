package com.almaviva.euregio.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.CheckBoxPreference;
import android.preference.ListPreference;
import android.preference.Preference;
import android.preference.PreferenceCategory;
import android.preference.PreferenceFragment;
import android.preference.PreferenceManager;
import android.preference.PreferenceScreen;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.almaviva.euregio.R;
import com.almaviva.euregio.helper.FilterHelper;
import com.almaviva.euregio.helper.LocalStorage;
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
    private SharedPreferences spref;


    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        spref = PreferenceManager.getDefaultSharedPreferences(getActivity());

        PreferenceScreen screenPrincipale = getPreferenceManager().createPreferenceScreen(getActivity());

        categoriaLingua = new PreferenceCategory(getActivity());
        categoriaLingua.setKey("categoria_lingua");
        categoriaLingua.setTitle(getString(R.string.lingua));

        screenPrincipale.addPreference(categoriaLingua);

        categoriaPush = new PreferenceCategory(getActivity());
        categoriaPush.setKey("categoria_push");
        categoriaPush.setTitle(getString(R.string.push));
        screenPrincipale.addPreference(categoriaPush);

        categoriaFiltri = new PreferenceCategory(getActivity());
        categoriaFiltri.setKey("categoria_filtri");
        categoriaFiltri.setTitle(getString(R.string.filtri));
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

    private void createComponentVisualizzazione(PreferenceCategory categoriaFiltri) {

        String ordinamentoEsercenti = spref.getString("ordinamento_esercenti", "");


        final ListPreference listaVisualizzazione = new ListPreference(getActivity());

        String[] charSequenceLingua = new String[2];

        charSequenceLingua[0] = getString(R.string.lista_alfabetica);
        charSequenceLingua[1] = getString(R.string.lista_data);

        listaVisualizzazione.setKey("ordinamento_esercenti");
        listaVisualizzazione.setTitle(getString(R.string.visualizzazione_esercenti));

        listaVisualizzazione.setDialogTitle(getString(R.string.visualizzazione_esercenti));
        listaVisualizzazione.setEntries(charSequenceLingua);
        listaVisualizzazione.setEntryValues(charSequenceLingua);
        listaVisualizzazione.setDefaultValue(charSequenceLingua[0]);
        listaVisualizzazione.setValue(ordinamentoEsercenti);
        listaVisualizzazione.setSummary(ordinamentoEsercenti);

        listaVisualizzazione.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                listaVisualizzazione.setValue(newValue.toString());
                listaVisualizzazione.setSummary(newValue.toString());
                spref.edit().putString("ordinamento_esercenti", newValue.toString());
                spref.edit().commit();
                return false;
            }
        });

        categoriaFiltri.addPreference(listaVisualizzazione);
    }

    private void createComponentMascheraIniziale(PreferenceCategory categoriaFiltri) {
        String paginaHome = spref.getString("pagina_home", "");

        final ListPreference listaMaschere = new ListPreference(getActivity());

        String[] charSequenceLingua = new String[2];

        charSequenceLingua[0] = getString(R.string.esercenti);
        charSequenceLingua[1] = getString(R.string.title_mappa);

        listaMaschere.setKey("pagina_home");
        listaMaschere.setTitle(getString(R.string.pagina_home));

        listaMaschere.setDialogTitle(getString(R.string.pagina_home));
        listaMaschere.setEntries(charSequenceLingua);
        listaMaschere.setEntryValues(charSequenceLingua);
        listaMaschere.setDefaultValue(charSequenceLingua[0]);
        listaMaschere.setSummary(paginaHome);
        listaMaschere.setValue(paginaHome);

        listaMaschere.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                listaMaschere.setValue(newValue.toString());
                listaMaschere.setSummary(newValue.toString());
                spref.edit().putString("pagina_home", newValue.toString());
                spref.edit().commit();
                return false;
            }
        });

        categoriaFiltri.addPreference(listaMaschere);
    }

    private void createComponentPush(PreferenceCategory categoriaPush) {

        CheckBoxPreference checkBoxPref = new CheckBoxPreference(getActivity());
        checkBoxPref.setSummary(getString(R.string.push));
        checkBoxPref.setChecked(true);

        categoriaPush.addPreference(checkBoxPref);

    }

    private void createComponentLingua(final PreferenceCategory categoriaLingua) {


        String lingua = spref.getString("lingua", "");

        final ListPreference listaLingue = new ListPreference(getActivity());

        String[] charSequenceLingua = new String[2];

        charSequenceLingua[0] = "Italiano";
        charSequenceLingua[1] = "Deutsch";

        listaLingue.setKey("lingua");
        listaLingue.setDialogTitle(getString(R.string.lingua));
        listaLingue.setEntries(charSequenceLingua);
        listaLingue.setEntryValues(charSequenceLingua);
        listaLingue.setValue(lingua);
        listaLingue.setSummary(lingua);

        listaLingue.setOnPreferenceChangeListener(new Preference.OnPreferenceChangeListener() {
            @Override
            public boolean onPreferenceChange(Preference preference, Object newValue) {
                listaLingue.setValue(newValue.toString());
                listaLingue.setSummary(newValue.toString());
                spref.edit().putString("lingua", newValue.toString());
                spref.edit().commit();

                return false;
            }
        });

        categoriaLingua.addPreference(listaLingue);

    }

    private void createComponentCategorie(PreferenceCategory categoriaFiltri) throws ParseException {

        PreferenceScreen screenCategory = getPreferenceManager().createPreferenceScreen(getActivity());
        screenCategory.setTitle(getString(R.string.comprensorio));


        for (District dis : LocalStorage.getListOfComprensori()) {
            CheckBoxPreference checkBoxPref = new CheckBoxPreference(getActivity());
            checkBoxPref.setKey("check" + dis.id);
            checkBoxPref.setSummary(dis.name);
            checkBoxPref.setChecked(false);
            screenCategory.addPreference(checkBoxPref);
        }


        categoriaFiltri.addPreference(screenCategory);

    }



}
