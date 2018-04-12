package com.almaviva.euregio.helper;

import android.app.Activity;
import android.app.Dialog;
import android.content.Context;
import android.support.annotation.IdRes;
import android.support.v4.app.Fragment;
import android.support.v4.widget.NestedScrollView;
import android.util.Log;
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
import com.almaviva.euregio.mock.CategoryMock;
import com.almaviva.euregio.mock.DistrictMock;
import com.almaviva.euregio.mock.OrdineMock;
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
    private Integer filtriSelezionatiComprensorioId;
    private ProgressBar loading;



    private CharSequence titleFiltroCategorie = "";

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
                LocalStorage.setFiltroComprensorio(0);
                LocalStorage.setTestoCercato("");
                CheckBox selectedRadioButton = (CheckBox) comprensorioRadioGroup.findViewById(0);
                filtroSelezionatoComprensorio.setText(selectedRadioButton.getText());

                FilterHelper.filtraTotale(getActivity());

                LocalStorage.setIsSetCategoria(false);
                LocalStorage.setIsSetComprensorio(false);

                Log.e("FILTRI NUMERO: ", LocalStorage.getNumberOfFilterSet().toString());

            }
        });

        confermaButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                getDialog().hide();


            }
        });


        comprensorioRadioGroup.setOnCheckedChangeListener(new RadioGroup.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(RadioGroup group, int checkedId) {

                RadioButton selectedRadioButton = (RadioButton) group.findViewById(checkedId);
                filtroSelezionatoComprensorio.setText(selectedRadioButton.getText());

                LocalStorage.setFiltroComprensorio(checkedId);
                if(!LocalStorage.getIsSetComprensorio()){
                    LocalStorage.setIsSetComprensorio(true);
                }
                filtraListaEsercenti();
                Log.e("FILTRI NUMERO",LocalStorage.getNumberOfFilterSet().toString());

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

        Integer index = LocalStorage.getFiltriComprensorio();
        if(index!= null){
            RadioButton selectedRadioButton = (RadioButton) comprensorioRadioGroup.findViewById(index);
            selectedRadioButton.setChecked(false);

        }


        filtriSelezionatiComprensorioId = null;
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
            isComprensorioListShown = false;
        }


    }

    private void getCategory() throws ParseException {


        categoryList = CategoryMock.getListMock();

        for (Category cat : categoryList) {
            CheckBox cb = new CheckBox(getContext());
            cb.setText(cat.properties.name);
            cb.setId(cat.properties.id);
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

    private void getComprensori() throws ParseException {

        comprensorioList = DistrictMock.getListMock();

        final RadioButton[] rb = new RadioButton[comprensorioList.size() + 1];
        comprensorioRadioGroup.setOrientation(RadioGroup.VERTICAL);

        int i = 1;



        rb[0] = new RadioButton(getContext());
        rb[0].setText(getString(R.string.tutti));
        rb[0].setId(0);
        RadioGroup.LayoutParams params = new RadioGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        params.setMargins(0, 15, 0, 15);
        rb[0].setLayoutParams(params);
        rb[0].setTextColor(getResources().getColor(R.color.greyText));
        comprensorioRadioGroup.addView(rb[0]);

        for (District dis : comprensorioList) {

            rb[i] = new RadioButton(getContext());
            rb[i].setText(dis.properties.name);
            rb[i].setId(dis.properties.id);
            RadioGroup.LayoutParams params2 = new RadioGroup.LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
            params2.setMargins(0, 15, 0, 15);
            rb[i].setLayoutParams(params);
            comprensorioRadioGroup.addView(rb[i]);
            i++;
        }

        RadioButton selectedRadioButton;
        if (LocalStorage.getFiltriComprensorio() == 0) {
             selectedRadioButton = rb[0];
        } else {
            comprensorioRadioGroup.check(LocalStorage.getFiltriComprensorio());
             selectedRadioButton = (RadioButton) comprensorioRadioGroup.findViewById(LocalStorage.getFiltriComprensorio());

        }
        filtroSelezionatoComprensorio.setText(selectedRadioButton.getText());
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
