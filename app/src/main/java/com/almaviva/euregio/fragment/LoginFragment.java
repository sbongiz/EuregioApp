package com.almaviva.euregio.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import com.almaviva.euregio.R;


public class LoginFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private ImageView logo;
    private EditText editCodFisc;
    private EditText editNumCarta;
    private Button buttonAvanti;
    private boolean isfocusedCard=false;
    private boolean isIsfocusedCodf = false;
    public LoginFragment() {
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
       View view = inflater.inflate(R.layout.fragment_login, container, false);


        findComponentInView(view);


        //LISTENER

        buttonAvanti.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                login();
            }
        });



        return view;
    }

    private void login(){
        String codiceFiscale = editCodFisc.getText().toString();
        String numeroCarta = editNumCarta.getText().toString();

    }


    public void findComponentInView(View view){

        logo = view.findViewById(R.id.logo_grande);
        editCodFisc = view.findViewById(R.id.edit_codf);
        editNumCarta = view.findViewById(R.id.edit_cardnumber);
        buttonAvanti = view.findViewById(R.id.button_login);

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


}

