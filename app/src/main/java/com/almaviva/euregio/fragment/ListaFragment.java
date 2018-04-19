package com.almaviva.euregio.fragment;

import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.res.Resources;
import android.graphics.drawable.TransitionDrawable;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.preference.PreferenceManager;
import android.provider.ContactsContract;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.annotation.RequiresApi;
import android.support.design.widget.BottomSheetBehavior;
import android.support.design.widget.BottomSheetDialogFragment;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.internal.view.SupportMenuItem;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.almaviva.euregio.MainActivity;
import com.almaviva.euregio.R;
import com.almaviva.euregio.adapter.EsercenteListAdapter;
import com.almaviva.euregio.behavior.AnchorBottomSheetBehavior;
import com.almaviva.euregio.helper.BottomSheet3DialogFragment;
import com.almaviva.euregio.helper.FilterHelper;
import com.almaviva.euregio.helper.LocalStorage;
import com.almaviva.euregio.mock.SupplierRestClient;
import com.almaviva.euregio.model.OrdineFiltro;
import com.almaviva.euregio.model.Supplier;
import com.google.gson.Gson;
import com.loopj.android.http.JsonHttpResponseHandler;
import com.loopj.android.http.RequestParams;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.lang.reflect.Field;
import java.text.ParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Map;

import cz.msebera.android.httpclient.Header;


public class ListaFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private BottomSheetBehavior mBottomSheetBehavior;
    private FrameLayout bottomSheet;
    private ListView esercentiList;
    public static TextView textViewNumeroRisultati;
    private TextView textViewFiltroOrdine;
    private ImageView iconOrdine;
    private TextView TextViewFiltroOrdine;
    public static EsercenteListAdapter esercenteListAdapter;
    public static ArrayList<Supplier> esercentiArrayList;
    private Toolbar toolbar;
    private SearchManager searchManager;
    private SearchView searchView;
    public static MenuItem filter;
    private ImageView searchClose;
    private TransitionDrawable transition;
    private boolean isFragmentShowing = false;
    public boolean isSearchWhite = false;
    private BottomSheetDialogFragment bottomSheetDialogFragment;
    public static Context activity;
    public static String risultati;
    public static String risultato;
    public static SwipeRefreshLayout swipeLayout;
    public static  SharedPreferences spref;


    public ListaFragment() {
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

        spref = PreferenceManager.getDefaultSharedPreferences(getActivity());
        activity= getContext();
        View view = inflater.inflate(R.layout.fragment_lista, container, false);
        try {
            setHasOptionsMenu(true);
            risultati = getString(R.string.risultati);
            risultato = getString(R.string.risultato);
            findComponentInView(view);
            setComponent();
            getEsercenti();

            setFiltriDaImpostazioni();


            //LISTENER
            esercentiList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    //  Esercente lesson = (Esercente) esercenteListAdapter.getItem(position);

                    //  Intent intent = new Intent();
                    //  intent.putExtra("SelectedLesson",lesson);
                    //  setResult(Activity.RESULT_OK, intent);

                    //  finish();
                }
            });

            iconOrdine.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    ArrayList<Supplier> afterFilterList = new ArrayList<Supplier>();

                    animateIconaFiltro();

                    FilterHelper.filtraTotale(getActivity());

                }
            });

            textViewFiltroOrdine.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    ArrayList<Supplier> afterFilterList = new ArrayList<Supplier>();
                    if (textViewFiltroOrdine.getText() == getString(R.string.alfabetico)) {
                        textViewFiltroOrdine.setText(getString(R.string.data_aggiornamento));
                        LocalStorage.setFiltroOrdine(getString(R.string.data_aggiornamento));
                        FilterHelper.filtraTotale(getActivity());
                    } else {
                        textViewFiltroOrdine.setText(getString(R.string.alfabetico));
                        LocalStorage.setFiltroOrdine(getString(R.string.alfabetico));
                        FilterHelper.filtraTotale(getActivity());
                    }

                }
            });

        } catch (Exception e) {
            Log.e(Thread.currentThread().getStackTrace().toString(), e.toString());
        }
        return view;
    }


    private void setFiltriDaImpostazioni() {

        textViewFiltroOrdine.setText(LocalStorage.getFiltriOrdine());
        LocalStorage.setTestoCercato("");

    }

    private void animateIconaFiltro() {

        if (LocalStorage.getIsDecrescente()) {
            rotateDown(getActivity(), iconOrdine);
            LocalStorage.setIsDecrescente(false);
        } else {
            rotateUp(getActivity(), iconOrdine);
            LocalStorage.setIsDecrescente(true);
        }
    }

    private void rotateUp(Activity activity, View v) {
        Animation a = AnimationUtils.loadAnimation(activity, R.anim.rotate_up);
        a.setFillAfter(true);
        if (a != null) {
            a.reset();
            if (v != null) {
                v.clearAnimation();
                v.startAnimation(a);
            }
        }
    }

    private void rotateDown(Activity activity, View v) {
        Animation a = AnimationUtils.loadAnimation(activity, R.anim.rotate_down);
        a.setFillAfter(true);
        if (a != null) {
            a.reset();
            if (v != null) {
                v.clearAnimation();
                v.startAnimation(a);
            }
        }
    }

    public void showModalBottomSheet() {
        bottomSheetDialogFragment = new BottomSheet3DialogFragment();
        bottomSheetDialogFragment.show(getActivity().getSupportFragmentManager(), bottomSheetDialogFragment.getTag());
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
            esercentiArrayList = new ArrayList<Supplier>();


            if (LocalStorage.getListOfEsercenti().size() == 0) {
                retriveFilterAsyncTask();
            }else{
                FilterHelper.filtraTotale(getActivity());
            }




        } catch (Exception e) {
            Log.e(Thread.currentThread().getStackTrace().toString(), e.toString());
        }
    }

    private void setComponent() {
        esercentiList.setDivider(null);
        esercenteListAdapter = new EsercenteListAdapter(getActivity(), new ArrayList<Supplier>());
        esercentiList.setAdapter(esercenteListAdapter);


        swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                retriveFilterAsyncTask();
            }
        });

    }

    private void findComponentInView(View view) {
        esercentiList = (ListView) view.findViewById(R.id.esercenti_list);
        textViewNumeroRisultati = (TextView) view.findViewById(R.id.textView_numero_risultati);
        textViewFiltroOrdine = (TextView) view.findViewById(R.id.textView_filtro_ordine);
        iconOrdine = (ImageView) view.findViewById(R.id.iconFilterAlfabetico);
        textViewFiltroOrdine = (TextView) view.findViewById(R.id.textView_filtro_ordine);
        //progressBarLista = (ProgressBar) view.findViewById(R.id.progressBarLista);
        swipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeLayout);
    }

    private void setMenuComponent(Menu menu) {
        try {
            searchManager = (SearchManager) getActivity().getSystemService(Context.SEARCH_SERVICE);
            searchView = (SearchView) menu.findItem(R.id.searchView).getActionView();
            toolbar = (Toolbar) getActivity().findViewById(R.id.toolbar);
            filter = menu.findItem(R.id.filter);
            searchClose = (ImageView) searchView.findViewById(android.support.v7.appcompat.R.id.search_close_btn);

        } catch (Exception e) {
            Log.e(Thread.currentThread().getStackTrace().toString(), e.toString());
        }
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        // Inflate the menu; this adds items to the action bar if it is present.
        inflater.inflate(R.menu.search, menu);

        setMenuComponent(menu);

        customizeSearchView();

        changeSearchToNormalMode();
        //region MENU LISTENER
        final SupportMenuItem menuNonCompat = (SupportMenuItem) menu.findItem(R.id.searchView);


        menuNonCompat.setSupportOnActionExpandListener(new MenuItem.OnActionExpandListener() {
            @Override
            public boolean onMenuItemActionCollapse(MenuItem item) {
                Log.d("TAG", "Collapsed");
                changeSearchToNormalMode();
                return true;
            }

            @Override
            public boolean onMenuItemActionExpand(MenuItem item) {
                Log.d("TAG", "Expanded");
                changeSearchToWhiteMode();
                return true;
            }
        });


        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String query) {


                final String textCercato = query;

                // Remove all previous callbacks.
                Handler handler = new Handler();


                Runnable runnable = new Runnable() {
                    @Override
                    public void run() {

                        LocalStorage.setTestoCercato(textCercato);
                        FilterHelper.filtraTotale(getActivity());
                    }
                };

                handler.removeCallbacks(runnable);
                handler.postDelayed(runnable, 5000);
                return false;

            }

        });

        //endregion

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle item selection
        switch (item.getItemId()) {
            case R.id.searchView:

                return true;
            case R.id.filter:
                showModalBottomSheet();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }
    }


    private void changeSearchToNormalMode() {
        Integer numero_risultati = LocalStorage.getNumberOfFilterSet();

        if (numero_risultati == 1) {
            filter.setIcon(R.drawable.icon_settingswhite1);
        } else if (numero_risultati == 2) {
            filter.setIcon(R.drawable.icon_settingswhite2);
        } else {
            filter.setIcon(R.drawable.icon_settingswhite);
        }

        if (transition == null) {
            transition = (TransitionDrawable) toolbar.getBackground();
        }

        if (isSearchWhite) {
            transition.reverseTransition(300);
        }
        isSearchWhite = false;
    }

    private void changeSearchToWhiteMode() {

        Integer numero_risultati = LocalStorage.getNumberOfFilterSet();

        if (numero_risultati == 1) {
            filter.setIcon(R.drawable.icon_settingsgray1);
        } else if (numero_risultati == 2) {
            filter.setIcon(R.drawable.icon_settingsgray2);
        } else {
            filter.setIcon(R.drawable.icon_settingsgrey);
        }
        if (transition == null) {
            transition = (TransitionDrawable) toolbar.getBackground();
        }

        transition.startTransition(300);
        isSearchWhite = true;

    }

    private void customizeSearchView() {
        try {
            searchView.setSearchableInfo(searchManager.getSearchableInfo(getActivity().getComponentName()));

            searchClose.setImageResource(R.drawable.icon_remove);

            View searchPlate = searchView.findViewById(android.support.v7.appcompat.R.id.search_plate);
            if (searchPlate != null) {
                TextView searchText = (TextView) searchPlate.findViewById(android.support.v7.appcompat.R.id.search_src_text);
                if (searchText != null) {
                    searchText.setTextColor(ContextCompat.getColor(getActivity(), R.color.greyText));
                    searchText.setHintTextColor(ContextCompat.getColor(getActivity(), R.color.greyText));
                }
            }

            AutoCompleteTextView searchTextView = (AutoCompleteTextView) searchView.findViewById(android.support.v7.appcompat.R.id.search_src_text);
            try {
                Field mCursorDrawableRes = TextView.class.getDeclaredField("mCursorDrawableRes");
                mCursorDrawableRes.setAccessible(true);
                //  mCursorDrawableRes.set(searchTextView, R.drawable.search_cursor); //This sets the cursor resource ID to 0 or @null which will make it visible on white background
            } catch (Exception e) {
                e.printStackTrace();
            }
        } catch (Exception e) {
            Log.e(Thread.currentThread().getStackTrace().toString(), e.toString());
        }

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
                changeSearchToNormalMode();
            }
            isFragmentShowing = false;
        }
    }


    public static void retriveFilterAsyncTask() {

        // progressBarLista.setVisibility(View.VISIBLE);
        esercentiArrayList = new ArrayList<Supplier>();

        String lingua = spref.getString("lingua", "");
        RequestParams params = new RequestParams();
        if(lingua.equals("Italiano")){
            params.put("lang","it");
        }else{
            params.put("lang","de");
        }

        SupplierRestClient.get("", null, new JsonHttpResponseHandler() {
            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONObject response) {
                // If the response is JSONObject instead of expected JSONArray
            }

            @Override
            public void onSuccess(int statusCode, Header[] headers, JSONArray timeline) {
                // Pull out the first event on the public timeline
                JSONObject firstEvent = null;
                try {


                    for (int i = 0; i < timeline.length(); i++) {
                        firstEvent = (JSONObject) timeline.get(i);
                        Supplier data = new Gson().fromJson(firstEvent.toString(), Supplier.class);
                        esercentiArrayList.add(data);
                    }

                    LocalStorage.setListOfEsercenti(esercentiArrayList);


                    FilterHelper.filtraTotale(activity);

                    esercenteListAdapter.add(LocalStorage.getListOfEsercentiFiltrata());
                    esercenteListAdapter.notifyDataSetChanged();


                    //   progressBarLista.setVisibility(View.GONE);
                    swipeLayout.setRefreshing(false);

                } catch (JSONException e) {
                    e.printStackTrace();
                }

            }
        });

    }
}
