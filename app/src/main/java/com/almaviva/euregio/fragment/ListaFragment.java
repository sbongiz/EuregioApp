package com.almaviva.euregio.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;

import com.almaviva.euregio.R;
import com.almaviva.euregio.adapter.EsercenteListAdapter;
import com.almaviva.euregio.model.Esercente;

import java.text.ParseException;
import java.util.ArrayList;


public class ListaFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private ListView esercentiList;
    private EsercenteListAdapter esercenteListAdapter;
    private ArrayList<Esercente> esercentiArrayList;

    public ListaFragment() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @RequiresApi(api = Build.VERSION_CODES.M)
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_lista, container, false);
        try {
            esercentiList = (ListView) view.findViewById(R.id.esercenti_list);
            esercenteListAdapter = new EsercenteListAdapter(getActivity(), new ArrayList<Esercente>());
            esercentiList.setAdapter(esercenteListAdapter);
            getEsercenti();
            esercentiList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    //        Esercente lesson = (Esercente) esercenteListAdapter.getItem(position);

                    //  Intent intent = new Intent();
                    //  intent.putExtra("SelectedLesson",lesson);
                    //  setResult(Activity.RESULT_OK, intent);

                    //  finish();
                }
            });
        } catch (Exception e) {
            Log.e(Thread.currentThread().getStackTrace().toString(), e.toString());
        }
        return view;
    }

    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

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

    public void getEsercenti() throws ParseException {
        try {
            esercentiArrayList = new ArrayList<Esercente>();
            for (int x = 0; x < 6; x++) {
                Esercente eser = new Esercente("Lesson" + x, "start " + x, "end " + x, "room" + x);
                esercentiArrayList.add(eser);

                esercenteListAdapter.add(esercentiArrayList);
                esercenteListAdapter.notifyDataSetChanged();
            }
            Log.d("LISTA", esercentiArrayList.toString());
        } catch (Exception e) {
            Log.e(Thread.currentThread().getStackTrace().toString(), e.toString());
        }
    }

    public interface OnFragmentInteractionListener {
        // TODO: Update argument type and name
        void onFragmentInteraction(Uri uri);
    }
}
