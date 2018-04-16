package com.almaviva.euregio.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.InflateException;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.LinearLayout;
import android.widget.RadioGroup;
import android.widget.Spinner;
import android.widget.TextView;

import com.almaviva.euregio.R;
import com.almaviva.euregio.helper.LocalStorage;
import com.almaviva.euregio.mock.CategoryMock;
import com.almaviva.euregio.model.Category;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;


public class ImpostazioniFragment extends Fragment {


    private OnFragmentInteractionListener mListener;
    private Spinner spinnerLingua;
    private Spinner spinnerHomepage;
    private Spinner spinnerFiltriEsercente;
    private RadioGroup categoryRadioGroup;
    private ArrayList<Category> categoryList;
    private boolean isCategoryListShown = false;
    private View iconaExpand;
    private LinearLayout scrollViewCategorie;
    private ArrayList<Integer> filtriSelezionatiCategoriaId;
    private TextView filtroSelezionato;
    private CharSequence titleFiltroCategorie = "";
    private static View view;
    public ImpostazioniFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment


        if(view != null){
            ViewGroup parent = (ViewGroup) view.getParent();
            if(parent != null){
                parent.removeView(view);
            }
        }
        try{
            view = inflater.inflate(R.layout.fragment_impostazioni, container, false);
            findComponentInView(view);
        }catch (InflateException e){

        }


     return view;


    }


    private void findComponentInView(View view) {


        iconaExpand = view.findViewById(R.id.icona_expand);
        categoryRadioGroup = (RadioGroup) view.findViewById(R.id.radioGroup_category);
        filtroSelezionato = (TextView) view.findViewById(R.id.textView_filtro_selezionato);
        scrollViewCategorie = (LinearLayout) view.findViewById(R.id.scrollViewCategorie);

    }

    private void animateRadioGroup() {
        if (isCategoryListShown) {

            slideUp(getContext(), scrollViewCategorie);
            rotateFrecciaDown(getContext(), iconaExpand);
            categoryRadioGroup.setBackgroundColor(getResources().getColor(R.color.white));
            scrollViewCategorie.setVisibility(View.GONE);
            categoryRadioGroup.setVisibility(View.GONE);
            isCategoryListShown = false;
        }else {
            categoryRadioGroup.setBackgroundColor(getResources().getColor(R.color.light_grey));
            slideDown(getContext(), scrollViewCategorie);
            rotateFrecciaUp(getContext(), iconaExpand);
            scrollViewCategorie.setVisibility(View.VISIBLE);
            categoryRadioGroup.setVisibility(View.VISIBLE);
            isCategoryListShown = true;
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

                    setTitleCategorie();
                    filtroSelezionato.setText(titleFiltroCategorie);


                }
            });
            categoryRadioGroup.addView(cb);

        }


    }

    private void setTitleCategorie() {
        titleFiltroCategorie = "";
        for (Integer i : filtriSelezionatiCategoriaId) {

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

    private void setSpinnerLingua() {
        List<String> spinnerArray = new ArrayList<String>();
        spinnerArray.add("Italiano");
        spinnerArray.add("Deutsche");

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                getActivity(), android.R.layout.simple_spinner_item, spinnerArray);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerLingua.setAdapter(adapter);
    }

    private void setSpinnerHomepage() {
        List<String> spinnerArray = new ArrayList<String>();
        spinnerArray.add(getString(R.string.pagina_esercenti));
        spinnerArray.add(getString(R.string.pagina_euregio));

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                getActivity(), android.R.layout.simple_spinner_item, spinnerArray);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerHomepage.setAdapter(adapter);
    }

    private void setSpinnerFiltriEsercente() {
        List<String> spinnerArray = new ArrayList<String>();
        spinnerArray.add(getString(R.string.title_mappa));
        spinnerArray.add(getString(R.string.lista_alfabetica));
        spinnerArray.add(getString(R.string.lista_data));

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(
                getActivity(), android.R.layout.simple_spinner_item, spinnerArray);

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);

        spinnerFiltriEsercente.setAdapter(adapter);
    }


    // TODO: Rename method, update argument and hook method into UI event
    public void onButtonPressed(Uri uri) {
        if (mListener != null) {
            mListener.onFragmentInteraction(uri);
        }
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        if (context instanceof OnFragmentInteractionListener) {
            mListener = (OnFragmentInteractionListener) context;
        } else {
            throw new RuntimeException(context.toString()
                    + " must implement OnFragmentInteractionListener");
        }
    }

    @Override
    public void onDetach() {
        super.onDetach();
        mListener = null;
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
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
