package com.almaviva.euregio.helper;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.support.v7.app.ActionBar;
import android.support.v7.widget.ActionMenuView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;

import com.almaviva.euregio.MainActivity;
import com.almaviva.euregio.R;
import com.almaviva.euregio.fragment.ListaFragment;
import com.almaviva.euregio.fragment.MapFragment;
import com.almaviva.euregio.model.Category;
import com.almaviva.euregio.model.District;
import com.almaviva.euregio.model.OrdineFiltro;
import com.almaviva.euregio.model.Supplier;

import org.w3c.dom.Text;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/**
 * Created by a.sciarretta on 10/04/2018.
 */

public class BottomSheet3DialogFragment extends android.support.design.widget.BottomSheetDialogFragment {

    private RadioGroup categoryRadioGroup;
    private RadioGroup comprensorioRadioGroup;
    private ArrayList<Category> categoryList;
    private ArrayList<District> comprensorioList;
    private boolean isCategoryListShown = false;
    private boolean isComprensorioListShown = false;
    private TextView filtroSelezionato;
    private TextView filtroSelezionatoComprensorio;
    private TextView resetButton;
    private TextView confermaButton;
    private View iconaExpand;
    private View iconaExpandComprensorio;
    private LinearLayout scrollViewCategorie;
    private LinearLayout scrollViewComprensorio;
    private ArrayList<Integer> filtriSelezionatiCategoriaId;
    private ArrayList<Integer> filtriSelezionatiComprensorioId;
    private ProgressBar loading;



    private CharSequence titleFiltroCategorie = "";
    private CharSequence titleFiltroComprensorio="";


    @Override
    public void setupDialog(final Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        View contentView = View.inflate(getContext(), R.layout.filter_bottom_sheet, null);
        dialog.setContentView(contentView);
        getComponentInView(contentView);
        categoryRadioGroup.setVisibility(View.GONE);
        comprensorioRadioGroup.setVisibility(View.GONE);
        loading.setVisibility(View.GONE);

        try {
            getCategory();
            getComprensori();
        } catch (ParseException e) {
            e.printStackTrace();
        }

        iconaExpand.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                try {
                    animateRadioGroup();
                } catch (Exception e) {
                    Log.e(Thread.currentThread().getStackTrace().toString(), e.toString());
                }

            }
        });

        iconaExpandComprensorio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                try {
                    animateRadioGroupComprensorio();
                } catch (Exception e) {
                    Log.e(Thread.currentThread().getStackTrace().toString(), e.toString());
                }

            }
        });



        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateReset();
                try {
                    uncheckAllSelectedCheckBox();
                    uncheckComprensorioRadioButton();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                setTitleCategorie();
                setTitleComprensorio();
                LocalStorage.setTestoCercato("");

                FilterHelper.filtraTotale(getActivity());

                LocalStorage.setIsSetCategoria(false);
                LocalStorage.setIsSetComprensorio(false);

                Log.e("FILTRI NUMERO: ", LocalStorage.getNumberOfFilterSet().toString());
                setIconaFiltroMenu();
            }
        });

        confermaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().hide();


            }
        });


    }

    private void getComponentInView(View contentView){
        iconaExpand = contentView.findViewById(R.id.icona_expand);
        iconaExpandComprensorio = contentView.findViewById(R.id.icona_expand_comprensorio);
        categoryRadioGroup = (RadioGroup) contentView.findViewById(R.id.radioGroup_category);
        comprensorioRadioGroup = (RadioGroup) contentView.findViewById(R.id.radioGroup_comprensorio);
        filtroSelezionato = (TextView) contentView.findViewById(R.id.textView_filtro_selezionato);
        filtroSelezionatoComprensorio = (TextView) contentView.findViewById(R.id.textView_filtro_selezionato_comprensorio);
        resetButton = (TextView) contentView.findViewById(R.id.button_reset);
        confermaButton = (TextView) contentView.findViewById(R.id.button_conferma);
        scrollViewCategorie = (LinearLayout) contentView.findViewById(R.id.scrollViewCategorie);
        scrollViewComprensorio = (LinearLayout) contentView.findViewById(R.id.scrollViewCategorie_comprensorio);
        loading = (ProgressBar) contentView.findViewById(R.id.loading);
    }

    private void uncheckAllSelectedCheckBox() throws ParseException {

        ArrayList<Integer> a = new ArrayList<Integer>();
        a.addAll(LocalStorage.getFiltriCategoria());
        for (Integer i : a) {
            CheckBox selectedRadioButton = (CheckBox) categoryRadioGroup.findViewById(i);
            selectedRadioButton.setChecked(false);
        }

        filtriSelezionatiCategoriaId = new ArrayList<Integer>();
        LocalStorage.setFiltriCategoria(filtriSelezionatiCategoriaId);
    }

    private void uncheckComprensorioRadioButton(){

        ArrayList<Integer> a = new ArrayList<Integer>();
        a.addAll(LocalStorage.getFiltriComprensorio());
        for (Integer i : a) {
            CheckBox selectedRadioButton = (CheckBox) comprensorioRadioGroup.findViewById(i);
            selectedRadioButton.setChecked(false);
        }
        filtriSelezionatiComprensorioId = new ArrayList<Integer>();
        LocalStorage.setFiltroComprensorio(filtriSelezionatiComprensorioId);

    }

    private void animateRadioGroup() {
        if (isCategoryListShown) {

            slideUp(getContext(), scrollViewCategorie);
            rotateFrecciaDown(getContext(), iconaExpand);
            categoryRadioGroup.setBackgroundColor(getResources().getColor(R.color.white));
            scrollViewCategorie.setVisibility(View.GONE);
            categoryRadioGroup.setVisibility(View.GONE);
            isCategoryListShown = false;
        } else {
            categoryRadioGroup.setBackgroundColor(getResources().getColor(R.color.light_grey));
            slideDown(getContext(), scrollViewCategorie);
            rotateFrecciaUp(getContext(), iconaExpand);
            scrollViewCategorie.setVisibility(View.VISIBLE);
            categoryRadioGroup.setVisibility(View.VISIBLE);
            isCategoryListShown = true;
        }
    }

    private void animateRadioGroupComprensorio() {
        if (isComprensorioListShown) {

            slideUp(getContext(), scrollViewComprensorio);
            rotateFrecciaDown(getContext(), iconaExpandComprensorio);
            comprensorioRadioGroup.setBackgroundColor(getResources().getColor(R.color.white));
            scrollViewComprensorio.setVisibility(View.GONE);
            comprensorioRadioGroup.setVisibility(View.GONE);
            isComprensorioListShown = false;
        } else {
            comprensorioRadioGroup.setBackgroundColor(getResources().getColor(R.color.light_grey));
            slideDown(getContext(), scrollViewComprensorio);
            rotateFrecciaUp(getContext(), iconaExpandComprensorio);
            scrollViewComprensorio.setVisibility(View.VISIBLE);
            comprensorioRadioGroup.setVisibility(View.VISIBLE);
            isComprensorioListShown = true;
        }
    }

    private void animateReset() {
        if (isCategoryListShown) {
            slideUp(getContext(), scrollViewCategorie);
            rotateFrecciaDown(getContext(), iconaExpand);
            categoryRadioGroup.setBackgroundColor(getResources().getColor(R.color.white));
            categoryRadioGroup.setVisibility(View.GONE);
            scrollViewCategorie.setVisibility(View.GONE);
            filtroSelezionato.setText(getString(R.string.tutte));
            isCategoryListShown = false;

        }
        if(isComprensorioListShown){
            slideUp(getContext(), scrollViewComprensorio);
            rotateFrecciaDown(getContext(), iconaExpandComprensorio);
            comprensorioRadioGroup.setBackgroundColor(getResources().getColor(R.color.white));
            scrollViewComprensorio.setVisibility(View.GONE);
            comprensorioRadioGroup.setVisibility(View.GONE);
            filtroSelezionatoComprensorio.setText(getString(R.string.tutte));
            isComprensorioListShown = false;
        }


    }

    private void getCategory() throws ParseException {


        categoryList = LocalStorage.getListOfCategories();

        for (Category cat : categoryList) {
            CheckBox cb = new CheckBox(getContext());
            cb.setText(cat.name);
            cb.setId(cat.id);
            cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    if (filtriSelezionatiCategoriaId == null) {
                        filtriSelezionatiCategoriaId = new ArrayList<Integer>();
                    }
                    if (!filtriSelezionatiCategoriaId.contains(buttonView.getId()) &&isChecked) {
                        filtriSelezionatiCategoriaId.add(buttonView.getId());
                    }else if(filtriSelezionatiCategoriaId.contains(buttonView.getId()) && !isChecked){
                        filtriSelezionatiCategoriaId.remove( filtriSelezionatiCategoriaId.indexOf(buttonView.getId()));
                    }

                    if(filtriSelezionatiCategoriaId.size()==0){
                        LocalStorage.setIsSetCategoria(false);
                    }else{
                        LocalStorage.setIsSetCategoria(true);
                    }
                    Log.e("FILTRI NUMERO",LocalStorage.getNumberOfFilterSet().toString());
                    setIconaFiltroMenu();
                    LocalStorage.setFiltriCategoria(filtriSelezionatiCategoriaId);
                    filtraListaEsercenti();
                    setTitleCategorie();
                    filtroSelezionato.setText(titleFiltroCategorie);


                }
            });
            categoryRadioGroup.addView(cb);

        }

        if (LocalStorage.getFiltriCategoria().size() == 0) {
            filtroSelezionato.setText(getString(R.string.tutte));
        } else {
            setTitleCategorie();
            filtroSelezionato.setText(titleFiltroCategorie);
        }

    }


    private void setIconaFiltroMenu(){

        int numero_filtri_impostati  = LocalStorage.getNumberOfFilterSet();

        MainActivity mainActivity = (MainActivity) getActivity();
        List<Fragment> fra = mainActivity.getSupportFragmentManager().getFragments();
        ListaFragment listaFragment=null;
        for (Fragment f: fra) {

            if(f!=null && f.getClass().toString().equals("class com.almaviva.euregio.fragment.ListaFragment")){
                listaFragment = (ListaFragment) f;
            }
        }

        if(listaFragment !=null){
            if(numero_filtri_impostati==1){
                if(listaFragment.isSearchWhite){
                    listaFragment.filter.setIcon(R.drawable.icon_settingsgray1);
                }else{
                    listaFragment.filter.setIcon(R.drawable.icon_settingswhite1);
                }
            }else if( numero_filtri_impostati==2){
                if(listaFragment.isSearchWhite){
                    listaFragment.filter.setIcon(R.drawable.icon_settingsgray2);
                }else{
                    listaFragment.filter.setIcon(R.drawable.icon_settingswhite2);
                }
            }else{
                if(listaFragment.isSearchWhite){
                    listaFragment.filter.setIcon(R.drawable.icon_settingsgrey);
                }else{
                    listaFragment.filter.setIcon(R.drawable.icon_settingswhite);
                }
            }
        }

    }

    private void setTitleCategorie() {
        titleFiltroCategorie = "";
        for (Integer i : LocalStorage.getFiltriCategoria()) {

            CheckBox selectedRadioButton = (CheckBox) categoryRadioGroup.findViewById(i);

            if (titleFiltroCategorie == "") {
                titleFiltroCategorie = selectedRadioButton.getText();
            } else {
                titleFiltroCategorie = titleFiltroCategorie.toString() + ", " + selectedRadioButton.getText();
            }

            selectedRadioButton.setChecked(true);
        }
        if(titleFiltroCategorie==""){
            titleFiltroCategorie = getString(R.string.tutte);


        }
    }

    private void setTitleComprensorio() {
        titleFiltroComprensorio = "";
        for (Integer i : LocalStorage.getFiltriComprensorio()) {

            CheckBox selectedRadioButton = (CheckBox) comprensorioRadioGroup.findViewById(i);

            if (titleFiltroComprensorio == "") {
                titleFiltroComprensorio = selectedRadioButton.getText();
            } else {
                titleFiltroComprensorio = titleFiltroComprensorio.toString() + ", " + selectedRadioButton.getText();
            }

            selectedRadioButton.setChecked(true);
        }
        if(titleFiltroComprensorio==""){
            titleFiltroComprensorio = getString(R.string.tutte);
        }
    }

    private void getComprensori() throws ParseException {

        comprensorioList = LocalStorage.getListOfComprensori();

        for (District dis : comprensorioList) {
            CheckBox cb = new CheckBox(getContext());
            cb.setText(dis.name);
            cb.setId(dis.id);
            cb.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
                @Override
                public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {

                    if (filtriSelezionatiComprensorioId == null) {
                        filtriSelezionatiComprensorioId = new ArrayList<Integer>();
                    }
                    if (!filtriSelezionatiComprensorioId.contains(buttonView.getId()) &&isChecked) {
                        filtriSelezionatiComprensorioId.add(buttonView.getId());
                    }else if(filtriSelezionatiComprensorioId.contains(buttonView.getId()) && !isChecked){
                        filtriSelezionatiComprensorioId.remove( filtriSelezionatiComprensorioId.indexOf(buttonView.getId()));
                    }

                    if(filtriSelezionatiComprensorioId.size()==0){
                        LocalStorage.setIsSetComprensorio(false);
                    }else{
                        LocalStorage.setIsSetComprensorio(true);
                    }
                    Log.e("FILTRI NUMERO",LocalStorage.getNumberOfFilterSet().toString());
                    setIconaFiltroMenu();
                    LocalStorage.setFiltroComprensorio(filtriSelezionatiComprensorioId);
                    filtraListaEsercenti();
                    setTitleComprensorio();
                    filtroSelezionatoComprensorio.setText(titleFiltroComprensorio);


                }
            });
            comprensorioRadioGroup.addView(cb);

        }

        if (LocalStorage.getFiltriComprensorio().size() == 0) {
            filtroSelezionatoComprensorio.setText(getString(R.string.tutti));
        } else {
            setTitleComprensorio();
            filtroSelezionatoComprensorio.setText(titleFiltroComprensorio);
        }
    }

    public void filtraListaEsercenti(){

        loading.setVisibility(View.VISIBLE);
        FilterHelper.filtraTotale(getActivity());
        loading.setVisibility(View.GONE);


    }



    //region Animazioni
    private static void slideDown(Context ctx, View v) {
        Animation a = AnimationUtils.loadAnimation(ctx, R.anim.slide_down);

        if (a != null) {
            a.reset();
            if (v != null) {
                v.clearAnimation();
                v.startAnimation(a);
            }
        }
    }

    private static void slideUp(Context ctx, View v) {
        Animation a = AnimationUtils.loadAnimation(ctx, R.anim.slide_up);

        if (a != null) {
            a.reset();
            if (v != null) {
                v.clearAnimation();
                v.startAnimation(a);
            }
        }
    }

    private static void rotateFrecciaUp(Context ctx, View v) {
        Animation a = AnimationUtils.loadAnimation(ctx, R.anim.rotate_up);
        a.setFillAfter(true);
        if (a != null) {
            a.reset();
            if (v != null) {
                v.clearAnimation();
                v.startAnimation(a);
            }
        }
    }

    private static void rotateFrecciaDown(Context ctx, View v) {
        Animation a = AnimationUtils.loadAnimation(ctx, R.anim.rotate_down);
        a.setFillAfter(true);
        if (a != null) {
            a.reset();
            if (v != null) {
                v.clearAnimation();
                v.startAnimation(a);
            }
        }
    }
    //endregion
}
