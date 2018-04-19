package com.almaviva.euregio.fragment;


import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.Toast;

import com.almaviva.euregio.MainActivity;
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
    private  String pathFronte;
    private String pathRetro;
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
            image.setVisibility(View.VISIBLE);
            spref = PreferenceManager.getDefaultSharedPreferences(getActivity());

        } catch (Exception e) {
            Log.e(Thread.currentThread().getStackTrace().toString(), e.toString());
        }
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {

        try{
            pathFronte = spref.getString("card_fronte_path", "");
            pathRetro = spref.getString("card_retro_path", "");

            if (pathFronte != "") {
                fileFronte = new File(pathFronte);
            }

            if (pathRetro != "") {
                fileRetro = new File(pathRetro);
            }

            String fronteRetro = spref.getString("fronte_retro", "");

            if(fileFronte != null && fileRetro != null){
                if(fronteRetro.equals("front")){
                    Bitmap myBitmap = BitmapFactory.decodeFile(fileFronte.getAbsolutePath());
                    image.setImageBitmap(myBitmap);
                    isBackOfCardShowing=false;
                }else{
                    Bitmap myBitmap = BitmapFactory.decodeFile(fileRetro.getAbsolutePath());
                    image.setImageBitmap(myBitmap);
                    isBackOfCardShowing = true;
                }
            }

        }catch (Exception e){
            Log.e(Thread.currentThread().getStackTrace().toString(), e.toString());
        }


        super.onViewCreated(view, savedInstanceState);
    }

    private void findComponentInView(View view) {
        try{
            image = (ImageView) view.findViewById(R.id.card_image);
            toMiddle = AnimationUtils.loadAnimation(getActivity(), R.anim.to_middle);
            fromMiddle = AnimationUtils.loadAnimation(getActivity(), R.anim.from_middle);
            toMiddle.setAnimationListener(this);
            fromMiddle.setAnimationListener(this);

            FloatingActionButton fab = (FloatingActionButton) view.findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

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

                    MainActivity main = (MainActivity) getActivity();
                    main.bottomNavigation.setSelectedItemId(R.id.navigation_euregio);

                    Context context = getContext();
                    CharSequence text = getString(R.string.logout);
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();

                }
            });
        }catch (Exception e){
            Log.e(Thread.currentThread().getStackTrace().toString(), e.toString());
        }


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
        try{
            if (animation == toMiddle) {

                image.clearAnimation();
                image.setAnimation(fromMiddle);
                image.startAnimation(fromMiddle);
                SharedPreferences.Editor editor = spref.edit();
                if (isBackOfCardShowing) {

                    editor.putString("fronte_retro","front");
                    editor.commit();
                    Bitmap myBitmap = BitmapFactory.decodeFile(fileFronte.getAbsolutePath());

                    if(myBitmap!=null){
                        image.setImageBitmap(myBitmap);
                    }
                } else {
                    editor.putString("fronte_retro","retro");
                    editor.commit();
                    Bitmap myBitmap = BitmapFactory.decodeFile(fileRetro.getAbsolutePath());
                    if(myBitmap!=null){
                        image.setImageBitmap(myBitmap);
                    }
                }
            } else {
                isBackOfCardShowing = !isBackOfCardShowing;
            }
        }catch (Exception e){
            Log.e(Thread.currentThread().getStackTrace().toString(), e.toString());
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
        try {
            image.clearAnimation();
            image.setAnimation(toMiddle);
            image.startAnimation(toMiddle);
        }catch (Exception e){
            Log.e(Thread.currentThread().getStackTrace().toString(), e.toString());
        }
    }
}
