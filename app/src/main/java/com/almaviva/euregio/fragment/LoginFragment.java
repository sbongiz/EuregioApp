package com.almaviva.euregio.fragment;

import android.content.Context;
import android.content.SharedPreferences;
import android.net.Uri;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.almaviva.euregio.MainActivity;
import com.almaviva.euregio.R;
import com.almaviva.euregio.helper.LocalStorage;
import com.almaviva.euregio.mock.CardRestClient;
import com.almaviva.euregio.mock.SupplierRestClient;
import com.almaviva.euregio.model.Supplier;
import com.google.gson.Gson;
import com.loopj.android.http.Base64;
import com.loopj.android.http.FileAsyncHttpResponseHandler;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

import cz.msebera.android.httpclient.Header;


public class LoginFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private ImageView logo;
    private EditText editCodFisc;
    private EditText editNumCarta;
    private Button buttonAvanti;
    private boolean isfocusedCard = false;
    private boolean isIsfocusedCodf = false;
    private SharedPreferences spref;
    private RequestParams loginParams;
    private static Context context;
    public static ProgressBar progressBar;
    private static boolean isOtherDownloaded = false;

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
        try {
            context = getContext();
            findComponentInView(view);


            //LISTENER

            buttonAvanti.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    progressBar.setVisibility(View.VISIBLE);
                    login();
                }
            });

        } catch (Exception e) {
            Log.e(Thread.currentThread().getStackTrace().toString(), e.toString());
        }


        return view;
    }

    private void login() {
        try {
            String codiceFiscale = editCodFisc.getText().toString();
            String numeroCarta = editNumCarta.getText().toString();

            spref = PreferenceManager.getDefaultSharedPreferences(getActivity());

            loginParams = new RequestParams();

            codiceFiscale = codiceFiscale.trim();
            numeroCarta = numeroCarta.trim();
            loginParams.put("fc", codiceFiscale);
            loginParams.put("cardNumber", numeroCarta);
            loginParams.put("side", "front");

            CardRestClient.get("", loginParams, new FileAsyncHttpResponseHandler(getActivity()) {

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, File file) {
                    Context context = getContext();
                    CharSequence text = getString(R.string.errore_login);
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();

                    SharedPreferences.Editor editor = spref.edit();
                    editor.putBoolean("isLogged", false);
                    editor.commit();

                    progressBar.setVisibility(View.GONE);
                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, File file) {

                    SharedPreferences.Editor editor = spref.edit();


                    File fileNuovo = new File(getContext().getFilesDir(), "cardFronte");

                    try {

                        InputStream is = new FileInputStream(file);
                        OutputStream os = new FileOutputStream(fileNuovo);
                        byte[] buff = new byte[1024];
                        int len;
                        while ((len = is.read(buff)) > 0) {
                            os.write(buff, 0, len);
                        }
                        is.close();
                        os.close();

                        editor.putString("card_fronte_path", fileNuovo.getAbsolutePath());
                        editor.commit();

                        editor.putBoolean("isLogged", true);
                        editor.commit();


                        if (isOtherDownloaded) {
                            MainActivity main = (MainActivity) getActivity();
                            main.bottomNavigation.setSelectedItemId(R.id.navigation_euregio);
                        }

                        isOtherDownloaded = true;


                    } catch (IOException e) {

                    }
                }
            });

            loginParams.put("fc", codiceFiscale);
            loginParams.put("cardNumber", numeroCarta);
            loginParams.put("side", "back");

            CardRestClient.get("", loginParams, new FileAsyncHttpResponseHandler(getActivity()) {

                @Override
                public void onFailure(int statusCode, Header[] headers, Throwable throwable, File file) {
                    Context context = getContext();
                    CharSequence text = getString(R.string.errore_login);
                    int duration = Toast.LENGTH_SHORT;

                    Toast toast = Toast.makeText(context, text, duration);
                    toast.show();

                    SharedPreferences.Editor editor = spref.edit();
                    editor.putBoolean("isLogged", false);
                    editor.commit();

                    progressBar.setVisibility(View.GONE);

                }

                @Override
                public void onSuccess(int statusCode, Header[] headers, File file) {
                    SharedPreferences.Editor editor = spref.edit();


                    File fileNuovo = new File(getContext().getFilesDir(), "cardRetro");
                    try {

                        InputStream is = new FileInputStream(file);
                        OutputStream os = new FileOutputStream(fileNuovo);
                        byte[] buff = new byte[1024];
                        int len;
                        while ((len = is.read(buff)) > 0) {
                            os.write(buff, 0, len);
                        }
                        is.close();
                        os.close();

                        editor.putString("card_retro_path", fileNuovo.getAbsolutePath());
                        editor.commit();


                        editor.putBoolean("isLogged", true);
                        editor.commit();

                        progressBar.setVisibility(View.GONE);

                        if (isOtherDownloaded) {
                            MainActivity main = (MainActivity) getActivity();
                            main.bottomNavigation.setSelectedItemId(R.id.navigation_euregio);
                        }

                        isOtherDownloaded = true;
                    } catch (IOException e) {

                    }
                }
            });


        } catch (Exception e) {
            Log.e(Thread.currentThread().getStackTrace().toString(), e.toString());
        }

    }


    public void findComponentInView(View view) {
        try {
            logo = view.findViewById(R.id.logo_grande);
            editCodFisc = view.findViewById(R.id.edit_codf);
            editNumCarta = view.findViewById(R.id.edit_cardnumber);
            buttonAvanti = view.findViewById(R.id.button_login);
            progressBar = view.findViewById(R.id.progressBar);
            final View thisView = view;
            view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    int heightDiff = thisView.getRootView().getHeight() - thisView.getHeight();
                    if (heightDiff > dpToPx(200)) { // if more than 200 dp, it's probably a keyboard...

                        logo.setVisibility(View.GONE);
                    } else {
                        logo.setVisibility(View.VISIBLE);
                    }
                }
            });
        } catch (Exception e) {
            Log.e(Thread.currentThread().getStackTrace().toString(), e.toString());
        }

    }

    public static float dpToPx(float valueInDp) {

        try {
            DisplayMetrics metrics = context.getResources().getDisplayMetrics();
            return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, valueInDp, metrics);
        } catch (Exception e) {
            Log.e(Thread.currentThread().getStackTrace().toString(), e.toString());
        }
       return 0f;
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

