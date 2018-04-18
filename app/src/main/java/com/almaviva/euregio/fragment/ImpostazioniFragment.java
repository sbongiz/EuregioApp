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
import com.almaviva.euregio.helper.FilterHelper;
import com.almaviva.euregio.helper.LocalStorage;
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
    private boolean isFragmentShowing=false;
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
        }catch (InflateException e){

        }


     return view;


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

    @Override
    public void setUserVisibleHint(boolean isVisibleToUser) {
        super.setUserVisibleHint(isVisibleToUser);

        if (isVisibleToUser) {
            isFragmentShowing = true;
        } else {
            if (isFragmentShowing) {
                FilterHelper.aggiornaFiltriImpostati(getActivity(),true);
            }
            isFragmentShowing = false;
        }
    }

}
