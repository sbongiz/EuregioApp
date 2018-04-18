package com.almaviva.euregio.fragment;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.almaviva.euregio.R;

import java.io.File;


public class EuregioFragment extends Fragment implements Animation.AnimationListener {


    private OnFragmentInteractionListener mListener;
    private ImageView image;
    private Animation toMiddle;
    private Animation fromMiddle;
    private boolean isBackOfCardShowing;
    private File fileFronte;
    private File fileRetro;
    private SharedPreferences spref;

    public EuregioFragment() {
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

        View view;


        view = inflater.inflate(R.layout.fragment_euregio, container, false);


        try {

            findComponentInView(view);


            spref = PreferenceManager.getDefaultSharedPreferences(getActivity());

            String pathFronte = spref.getString("card_fronte_path", "");
            String pathRetro = spref.getString("card_retro_path", "");

            if (pathFronte != "") {
                fileFronte = new File(pathFronte);
            }

            if (pathRetro != "") {
                fileRetro = new File(pathRetro);
            }

            String fronteRetro = spref.getString("fronte_retro", "");

            if(fronteRetro.equals("front")){
                Bitmap myBitmap = BitmapFactory.decodeFile(fileFronte.getAbsolutePath());
                image.setImageBitmap(myBitmap);
            }else{
                Bitmap myBitmap = BitmapFactory.decodeFile(fileRetro.getAbsolutePath());
                image.setImageBitmap(myBitmap);
            }





        } catch (Exception e) {
            Log.e(Thread.currentThread().getStackTrace().toString(), e.toString());
        }
        return view;
    }


    private void findComponentInView(View view) {
        image = (ImageView) view.findViewById(R.id.card_image);
        toMiddle = AnimationUtils.loadAnimation(getActivity(), R.anim.to_middle);
        fromMiddle = AnimationUtils.loadAnimation(getActivity(), R.anim.from_middle);
        toMiddle.setAnimationListener(this);
        fromMiddle.setAnimationListener(this);

        FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
        fab.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //image.setImageResource(R.drawable.card_retro);
                flipIt(image);
            }
        });

        FloatingActionButton fabLogout = (FloatingActionButton) view.findViewById(R.id.fabLogout);
        fabLogout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                SharedPreferences.Editor editor = spref.edit();
                editor.putBoolean("isLogged", false);
                editor.commit();


                editor.putString("card_fronte_path", "");
                editor.commit();
                editor.putString("card_retro_path", "");
                editor.commit();

            }
        });

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

    @Override
    public void onAnimationStart(Animation animation) {

    }

    @Override
    public void onAnimationEnd(Animation animation) {
        if (animation == toMiddle) {

            image.clearAnimation();
            image.setAnimation(fromMiddle);
            image.startAnimation(fromMiddle);
            if (isBackOfCardShowing) {
                Bitmap myBitmap = BitmapFactory.decodeFile(fileFronte.getAbsolutePath());
                image.setImageBitmap(myBitmap);
            } else {
                Bitmap myBitmap = BitmapFactory.decodeFile(fileRetro.getAbsolutePath());
                image.setImageBitmap(myBitmap);
            }
        } else {
            isBackOfCardShowing = !isBackOfCardShowing;
        }
    }

    @Override
    public void onAnimationRepeat(Animation animation) {

    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }

    private void flipIt(final View viewToFlip) {
        image.clearAnimation();
        image.setAnimation(toMiddle);
        image.startAnimation(toMiddle);
    }
}
