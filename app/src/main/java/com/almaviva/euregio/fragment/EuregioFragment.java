package com.almaviva.euregio.fragment;

import android.animation.Animator;
import android.animation.ObjectAnimator;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.design.widget.FloatingActionButton;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.Button;
import android.widget.ImageView;

import com.almaviva.euregio.R;


public class EuregioFragment extends Fragment implements Animation.AnimationListener {


    private OnFragmentInteractionListener mListener;
    private ImageView image;
    private Animation toMiddle;
    private Animation fromMiddle;
    private boolean isBackOfCardShowing;
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
        boolean isLogged = true;
        if (isLogged) {
            view =  inflater.inflate(R.layout.fragment_euregio, container, false);
        } else {
            view = inflater.inflate(R.layout.fragment_login, container, false);
        }

        try {
            image = view.findViewById(R.id.card_image);
            toMiddle = AnimationUtils.loadAnimation(getActivity(),R.anim.to_middle);
            fromMiddle = AnimationUtils.loadAnimation(getActivity(),R.anim.from_middle);
            toMiddle.setAnimationListener(this);
            fromMiddle.setAnimationListener(this);
            image.setRotation(-90);
            FloatingActionButton fab = view.findViewById(R.id.fab);
            fab.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    //image.setImageResource(R.drawable.card_retro);
                    flipIt(image);
                }
            });
        }catch(Exception e){
            Log.e(Thread.currentThread().getStackTrace().toString(), e.toString());
        }
        return view;
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
        if (animation==toMiddle) {

            image.clearAnimation();
            image.setAnimation(fromMiddle);
            image.startAnimation(fromMiddle);
            if (isBackOfCardShowing) {
                image.setImageResource(R.drawable.card_fronte);
            } else {
                image.setImageResource(R.drawable.card_retro);
            }
        } else {
            isBackOfCardShowing=!isBackOfCardShowing;
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
