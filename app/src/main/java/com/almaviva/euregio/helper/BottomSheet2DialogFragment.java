package com.almaviva.euregio.helper;

import android.app.Dialog;
import android.content.Context;
import android.support.v4.app.Fragment;
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
import com.almaviva.euregio.model.Category;
import com.almaviva.euregio.model.District;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;

/**
 * Created by a.sciarretta on 13/04/2018.
 */

public class BottomSheet2DialogFragment extends android.support.design.widget.BottomSheetDialogFragment {

    private RadioGroup categoryRadioGroup;
    private ArrayList<Category> categoryList;
    private boolean isCategoryListShown = false;
    private TextView filtroSelezionato;
    private TextView resetButton;
    private TextView confermaButton;
    private View iconaExpand;
    private LinearLayout scrollViewCategorie;
    private ArrayList<Integer> filtriSelezionatiCategoriaId;
    private ProgressBar loading;


    private CharSequence titleFiltroCategorie = "";

    @Override
    public void setupDialog(final Dialog dialog, int style) {
        super.setupDialog(dialog, style);
        View contentView = View.inflate(getContext(), R.layout.filter_singolo_bottom_sheet, null);
        dialog.setContentView(contentView);
        getComponentInView(contentView);
        categoryRadioGroup.setVisibility(View.GONE);
        loading.setVisibility(View.GONE);

        try {
            getCategory();
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


        resetButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                animateReset();
                try {
                    uncheckAllSelectedCheckBox();
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                setTitleCategorie();
                LocalStorage.setTestoCercatoMappa("");

                FilterHelper.filtraSuMappa(getActivity());

                LocalStorage.setIsSetCategoriaMappa(false);

                Log.e("FILTRI NUMERO: ", LocalStorage.getNumberOfFilterSetMappa().toString());
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

    private void getComponentInView(View contentView) {
        iconaExpand = contentView.findViewById(R.id.icona_expand);
        categoryRadioGroup = (RadioGroup) contentView.findViewById(R.id.radioGroup_category);
        filtroSelezionato = (TextView) contentView.findViewById(R.id.textView_filtro_selezionato);
        resetButton = (TextView) contentView.findViewById(R.id.button_reset);
        confermaButton = (TextView) contentView.findViewById(R.id.button_conferma);
        scrollViewCategorie = (LinearLayout) contentView.findViewById(R.id.scrollViewCategorie);
        loading = (ProgressBar) contentView.findViewById(R.id.loading);
    }

    private void uncheckAllSelectedCheckBox() throws ParseException {

        ArrayList<Integer> a = new ArrayList<Integer>();
        a.addAll(LocalStorage.getFiltriCategoriaMappa());
        for (Integer i : a) {
            CheckBox selectedRadioButton = (CheckBox) categoryRadioGroup.findViewById(i);
            selectedRadioButton.setChecked(false);
        }

        filtriSelezionatiCategoriaId = new ArrayList<Integer>();
        LocalStorage.setFiltriCategoriaMappa(filtriSelezionatiCategoriaId);
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


    }

    private void getCategory() throws ParseException {


        categoryList = CategoryMock.getListMock();

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
                    if (!filtriSelezionatiCategoriaId.contains(buttonView.getId()) && isChecked) {
                        filtriSelezionatiCategoriaId.add(buttonView.getId());
                    } else if (filtriSelezionatiCategoriaId.contains(buttonView.getId()) && !isChecked) {
                        filtriSelezionatiCategoriaId.remove(filtriSelezionatiCategoriaId.indexOf(buttonView.getId()));
                    }

                    if (filtriSelezionatiCategoriaId.size() == 0) {
                        LocalStorage.setIsSetCategoriaMappa(false);
                    } else {
                        LocalStorage.setIsSetCategoriaMappa(true);
                    }
                    Log.e("FILTRI NUMERO", LocalStorage.getNumberOfFilterSetMappa().toString());
                    setIconaFiltroMenu();
                    LocalStorage.setFiltriCategoriaMappa(filtriSelezionatiCategoriaId);
                    filtraListaEsercenti();
                    setTitleCategorie();
                    filtroSelezionato.setText(titleFiltroCategorie);


                }
            });
            categoryRadioGroup.addView(cb);

        }

        if (LocalStorage.getFiltriCategoriaMappa().size() == 0) {
            filtroSelezionato.setText(getString(R.string.tutte));
        } else {
            setTitleCategorie();
            filtroSelezionato.setText(titleFiltroCategorie);
        }

    }


    private void setIconaFiltroMenu() {

        int numero_filtri_impostati = LocalStorage.getNumberOfFilterSetMappa();

        MainActivity mainActivity = (MainActivity) getActivity();
        List<Fragment> fra = mainActivity.getSupportFragmentManager().getFragments();
        MapFragment mapFragment = null;


        for (Fragment f : fra) {

            if (f!=null && f.getClass().toString().equals("class com.almaviva.euregio.fragment.MapFragment")) {
                mapFragment = (MapFragment) f;
            }
        }
        if (mapFragment != null) {
            if (numero_filtri_impostati == 1) {
                if (mapFragment.isSearchWhite) {
                    mapFragment.filter.setIcon(R.drawable.icon_settingsgray1);
                } else {
                    mapFragment.filter.setIcon(R.drawable.icon_settingswhite1);
                }
            } else {
                if (mapFragment.isSearchWhite) {
                    mapFragment.filter.setIcon(R.drawable.icon_settingsgrey);
                } else {
                    mapFragment.filter.setIcon(R.drawable.icon_settingswhite);
                }
            }
        }
    }

    private void setTitleCategorie() {
        titleFiltroCategorie = "";
        for (Integer i : LocalStorage.getFiltriCategoriaMappa()) {

            CheckBox selectedRadioButton = (CheckBox) categoryRadioGroup.findViewById(i);

            if (titleFiltroCategorie == "") {
                titleFiltroCategorie = selectedRadioButton.getText();
            } else {
                titleFiltroCategorie = titleFiltroCategorie.toString() + ", " + selectedRadioButton.getText();
            }

            selectedRadioButton.setChecked(true);
        }
        if (titleFiltroCategorie == "") {
            titleFiltroCategorie = getString(R.string.tutte);
        }
    }


    public void filtraListaEsercenti() {

        loading.setVisibility(View.VISIBLE);
        FilterHelper.filtraSuMappa(getActivity());
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
