package com.almaviva.euregio.fragment;

import android.Manifest;
import android.app.Activity;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
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
import android.support.v4.app.ActivityCompat;
import android.support.v4.app.Fragment;
import android.support.v4.content.ContextCompat;
import android.support.v4.internal.view.SupportMenuItem;
import android.support.v4.view.MenuItemCompat;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.Toolbar;
import android.util.DisplayMetrics;
import android.util.Log;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.view.ViewTreeObserver;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.view.animation.TranslateAnimation;
import android.widget.AdapterView;
import android.widget.AutoCompleteTextView;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.almaviva.euregio.MainActivity;
import com.almaviva.euregio.R;
import com.almaviva.euregio.adapter.EsercenteListAdapter;
import com.almaviva.euregio.behavior.AnchorBottomSheetBehavior;
import com.almaviva.euregio.helper.BottomSheet3DialogFragment;
import com.almaviva.euregio.helper.DebouncedQueryTextListener;
import com.almaviva.euregio.helper.FilterHelper;
import com.almaviva.euregio.helper.LocalStorage;
import com.almaviva.euregio.mock.ImageRestClient;
import com.almaviva.euregio.mock.SupplierRestClient;
import com.almaviva.euregio.model.OrdineFiltro;
import com.almaviva.euregio.model.Product;
import com.almaviva.euregio.model.Supplier;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.gson.Gson;
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
import java.lang.reflect.Field;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.Map;

import cz.msebera.android.httpclient.Header;


public class ListaFragment extends Fragment {

    private OnFragmentInteractionListener mListener;
    private AnchorBottomSheetBehavior mBottomSheetBehavior;
    private View bottomSheet;
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
    public static SharedPreferences spref;
    public static ProgressBar progressBarLista;
    private LinearLayout layoutTelefono;
    private LinearLayout layoutMail;
    private LinearLayout layoutSito;
    private LinearLayout layoutIndicazioni;
    public static String immagineEsercentePath;
    private ImageView imageBehind;
    private LinearLayout layoutImmagine;
    public static Context context;

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
        View view = inflater.inflate(R.layout.fragment_lista, container, false);
        try {

            spref = PreferenceManager.getDefaultSharedPreferences(getActivity());
            activity = getContext();
            context = getContext();
            setHasOptionsMenu(true);
            risultati = getString(R.string.risultati);
            risultato = getString(R.string.risultato);
            findComponentInView(view);

            setComponent();
            getEsercenti();

            setFiltriDaImpostazioni();
            mBottomSheetBehavior = AnchorBottomSheetBehavior.from(bottomSheet);
            mBottomSheetBehavior.setState(AnchorBottomSheetBehavior.STATE_HIDDEN);


            mBottomSheetBehavior.setBottomSheetCallback(new AnchorBottomSheetBehavior.BottomSheetCallback() {
                @Override
                public void onStateChanged(@NonNull View bottomSheet, int newState) {

                    if (newState == AnchorBottomSheetBehavior.STATE_EXPANDED) {

                        layoutImmagine.setVisibility(View.VISIBLE);
                        animateImage();
                    }
                    if (newState == AnchorBottomSheetBehavior.STATE_COLLAPSED) {


                    }
                    if (newState == AnchorBottomSheetBehavior.STATE_DRAGGING) {
                        layoutImmagine.setVisibility(View.GONE);
                }
                    if (newState == AnchorBottomSheetBehavior.STATE_SETTLING) {

                    }
                    if (newState == AnchorBottomSheetBehavior.STATE_HIDDEN) {
                        layoutImmagine.setVisibility(View.GONE);
                    }


                }

                @Override
                public void onSlide(@NonNull View bottomSheet, float slideOffset) {
                    // React to dragging events
                }
            });

            bottomSheet.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    return true;
                }
            });

            bottomSheet.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (mBottomSheetBehavior.getState() == AnchorBottomSheetBehavior.STATE_EXPANDED) {
                        layoutImmagine.setVisibility(View.GONE);
                        mBottomSheetBehavior.setState(AnchorBottomSheetBehavior.STATE_HIDDEN);


                    }
                }
            });

            //LISTENER
            esercentiList.setOnItemClickListener(new AdapterView.OnItemClickListener() {
                @Override
                public void onItemClick(AdapterView<?> parent, View view, int position, long id) {

                    Supplier sup = (Supplier) esercentiList.getItemAtPosition(position);
                    setDettaglioComponentInBottomSheet(sup);

                    if (mBottomSheetBehavior.getState() == AnchorBottomSheetBehavior.STATE_HIDDEN) {
                        mBottomSheetBehavior.setState(AnchorBottomSheetBehavior.STATE_EXPANDED);
                    }
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

    public void setDettaglioComponentInBottomSheet(final Supplier supSelected) {



       try {



           if (supSelected.pictureUrl != null) {

               final String idCorrente = String.valueOf(supSelected.id);
               ImageRestClient.get(supSelected.pictureUrl, null, new FileAsyncHttpResponseHandler(getActivity()) {

                   @Override
                   public void onFailure(int statusCode, Header[] headers, Throwable throwable, File file) {
                       String a = "";
                   }

                   @Override
                   public void onSuccess(int statusCode, Header[] headers, File file) {

                       SharedPreferences.Editor editor = spref.edit();


                       File fileNuovo = new File(getContext().getFilesDir(), "immagineEsercente_" + idCorrente);
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


                       } catch (IOException e) {

                       }
                   }
               });
           }


           immagineEsercentePath = getContext().getFilesDir() + "/" + "immagineEsercente_" + supSelected.id;

           TextView esercenteDettaglioTitolo = (TextView) bottomSheet.findViewById(R.id.esercente_dettaglio_titolo);
           final TextView esercenteDettaglioData = (TextView) bottomSheet.findViewById(R.id.esercente_dettaglio_data);
           TextView esercenteDettaglioIndirizzo = (TextView) bottomSheet.findViewById(R.id.esercente_dettaglio_indirizzo);
           LinearLayout esercenteDettaglioLungo = (LinearLayout) bottomSheet.findViewById(R.id.layout_informazioni);
           layoutTelefono = (LinearLayout) bottomSheet.findViewById(R.id.layout_telefono);
           layoutMail = (LinearLayout) bottomSheet.findViewById(R.id.layout_mail);
           layoutSito = (LinearLayout) bottomSheet.findViewById(R.id.layout_sito);
           layoutIndicazioni = (LinearLayout) bottomSheet.findViewById(R.id.layout_indicazioni);

           ArrayList<Product> listaVantaggi = supSelected.products;

           esercenteDettaglioLungo.removeAllViews();

           for (Product vant : listaVantaggi) {

               TextView textTmp = new TextView(getActivity());
               TextView textTmpLunga = new TextView(getActivity());
               textTmp.setText(vant.getDescriptionShort());
               textTmpLunga.setText(vant.description);
               textTmp.setTextColor(getResources().getColor(R.color.colorPrimary));
               textTmpLunga.setTextColor(getResources().getColor(R.color.colorPrimary));
               textTmp.setTextSize(13);
               textTmpLunga.setTextSize(13);

               textTmpLunga.setGravity(Gravity.CENTER_VERTICAL);

               esercenteDettaglioLungo.addView(textTmpLunga);

           }


           esercenteDettaglioTitolo.setText(supSelected.title);

           String data = supSelected.lastUpdate;
           SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssZ");
           Date convertedDate = new Date();
           try {
               convertedDate = sdf.parse(data);
           } catch (ParseException e) {
               // TODO Auto-generated catch block
               e.printStackTrace();
           }


           int var  =convertedDate.getMonth()+1;
           int year = convertedDate.getYear();
           year = year +1900;
           String anno = String.valueOf(year).substring(2);
           String giorno =String.valueOf(convertedDate.getDate());
           if(giorno.length()==1){
               giorno = "0"+giorno;
           }

           String mese = String.valueOf(var);

           if(mese.length()==1){
               mese ="0"+mese;
           }

           String b = String.valueOf(var);

           esercenteDettaglioData.setText(giorno+"/"+mese);
           esercenteDettaglioIndirizzo.setText(supSelected.location.description);


           layoutTelefono.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {

                   if (supSelected != null && supSelected.phone != null) {
                       MainActivity activity = (MainActivity) getActivity();
                       if (activity != null) {
                           if (ActivityCompat.checkSelfPermission(activity, Manifest.permission.CALL_PHONE) == PackageManager.PERMISSION_GRANTED) {
                               Intent callIntent = new Intent(Intent.ACTION_CALL);
                               callIntent.setData(Uri.parse("tel:" + supSelected.phone));
                               startActivity(callIntent);
                           } else {
                               // No explanation needed; request the permission
                               ActivityCompat.requestPermissions(activity,
                                       new String[]{Manifest.permission.CALL_PHONE},
                                       1);
                           }
                       }
                   } else {
                       Context context = getContext();
                       CharSequence text = getString(R.string.errore_funzione);
                       int duration = Toast.LENGTH_SHORT;

                       Toast toast = Toast.makeText(context, text, duration);
                       toast.show();
                   }

               }
           });

           layoutMail.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {


                   if (supSelected != null && supSelected.email != null) {
                       Intent intent = new Intent(Intent.ACTION_SEND);
                       intent.setType("message/rfc822");
                       intent.putExtra(android.content.Intent.EXTRA_EMAIL, new String[]{supSelected.email});
                       intent.putExtra(Intent.EXTRA_SUBJECT, "");
                       intent.putExtra(Intent.EXTRA_TEXT, "");
                       Intent mailer = Intent.createChooser(intent, null);
                       startActivity(mailer);
                   } else {
                       Context context = getContext();
                       CharSequence text = getString(R.string.errore_funzione);
                       int duration = Toast.LENGTH_SHORT;

                       Toast toast = Toast.makeText(context, text, duration);
                       toast.show();
                   }

               }
           });

           layoutSito.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {
                   if (supSelected != null && supSelected.web != null) {
                       String url = "http://" + supSelected.web;
                       Intent i = new Intent(Intent.ACTION_VIEW);
                       i.setData(Uri.parse(url));
                       startActivity(i);
                   } else {
                       Context context = getContext();
                       CharSequence text = getString(R.string.errore_funzione);
                       int duration = Toast.LENGTH_SHORT;

                       Toast toast = Toast.makeText(context, text, duration);
                       toast.show();
                   }

               }
           });

           layoutIndicazioni.setOnClickListener(new View.OnClickListener() {
               @Override
               public void onClick(View view) {

                   if(supSelected.location.lat!=null && supSelected.location.lon!=null){
                       String lat = Double.toString(Double.parseDouble(supSelected.location.lat));
                       String lon = Double.toString(Double.parseDouble(supSelected.location.lon));
                       Uri gmmIntentUri = Uri.parse("google.navigation:q=" + lat + "," + lon);
                       Intent mapIntent = new Intent(Intent.ACTION_VIEW, gmmIntentUri);
                       mapIntent.setPackage("com.google.android.apps.maps");
                       startActivity(mapIntent);
                   }else{
                       Context context = getContext();
                       CharSequence text = getString(R.string.errore_funzione);
                       int duration = Toast.LENGTH_SHORT;

                       Toast toast = Toast.makeText(context, text, duration);
                       toast.show();
                   }

               }
           });
       } catch (Exception e) {
           Log.e(Thread.currentThread().getStackTrace().toString(), e.toString());
       }


    }


    public void animateImage() {
        try {

            if (immagineEsercentePath != null) {
                File fileImmagine = new File(immagineEsercentePath);
                Bitmap myBitmap = BitmapFactory.decodeFile(fileImmagine.getAbsolutePath());
                imageBehind.setImageBitmap(myBitmap);
                Resources r = getResources();
                float px = TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 181, r.getDisplayMetrics());
                Animation animation = new TranslateAnimation(0, 0, px, 0);
                animation.setDuration(1000);
                animation.setFillAfter(true);
                imageBehind.startAnimation(animation);
            }

        } catch (Exception e) {
            Log.e(Thread.currentThread().getStackTrace().toString(), e.toString());
        }

    }


    private void setFiltriDaImpostazioni() {
        try {
            textViewFiltroOrdine.setText(LocalStorage.getFiltriOrdine());
            LocalStorage.setTestoCercato("");
        } catch (Exception e) {
            Log.e(Thread.currentThread().getStackTrace().toString(), e.toString());
        }


    }

    private void animateIconaFiltro() {
        try {
            if (LocalStorage.getIsDecrescente()) {
                rotateDown(getActivity(), iconOrdine);
                LocalStorage.setIsDecrescente(false);
            } else {
                rotateUp(getActivity(), iconOrdine);
                LocalStorage.setIsDecrescente(true);
            }
        } catch (Exception e) {
            Log.e(Thread.currentThread().getStackTrace().toString(), e.toString());
        }

    }

    private void rotateUp(Activity activity, View v) {
        try {
            Animation a = AnimationUtils.loadAnimation(activity, R.anim.rotate_up);
            a.setFillAfter(true);
            if (a != null) {
                a.reset();
                if (v != null) {
                    v.clearAnimation();
                    v.startAnimation(a);
                }
            }
        } catch (Exception e) {
            Log.e(Thread.currentThread().getStackTrace().toString(), e.toString());
        }

    }

    private void rotateDown(Activity activity, View v) {
        try {
            Animation a = AnimationUtils.loadAnimation(activity, R.anim.rotate_down);
            a.setFillAfter(true);
            if (a != null) {
                a.reset();
                if (v != null) {
                    v.clearAnimation();
                    v.startAnimation(a);
                }
            }
        } catch (Exception e) {
            Log.e(Thread.currentThread().getStackTrace().toString(), e.toString());
        }

    }

    public void showModalBottomSheet() {
        try {
            bottomSheetDialogFragment = new BottomSheet3DialogFragment();
            bottomSheetDialogFragment.show(getActivity().getSupportFragmentManager(), bottomSheetDialogFragment.getTag());
        } catch (Exception e) {
            Log.e(Thread.currentThread().getStackTrace().toString(), e.toString());
        }
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
            String linguaAttuale = spref.getString("lingua","");


            if (LocalStorage.getListOfEsercenti().size() == 0 || !(linguaAttuale.equals(LocalStorage.getPrevoiusLanguageLista()))) {
                retriveFilterAsyncTask();
            } else {
                FilterHelper.filtraTotale(getActivity());
            }
        } catch (Exception e) {
            Log.e(Thread.currentThread().getStackTrace().toString(), e.toString());
        }
    }

    private void setComponent() {
        try {
            esercentiList.setDivider(null);
            esercenteListAdapter = new EsercenteListAdapter(getActivity(), new ArrayList<Supplier>());
            esercentiList.setAdapter(esercenteListAdapter);

            swipeLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
                @Override
                public void onRefresh() {
                    retriveFilterAsyncTask();
                }
            });
        } catch (Exception e) {
            Log.e(Thread.currentThread().getStackTrace().toString(), e.toString());
        }


    }

    private void findComponentInView(View view) {
        try {
            esercentiList = (ListView) view.findViewById(R.id.esercenti_list);
            textViewNumeroRisultati = (TextView) view.findViewById(R.id.textView_numero_risultati);
            textViewFiltroOrdine = (TextView) view.findViewById(R.id.textView_filtro_ordine);
            iconOrdine = (ImageView) view.findViewById(R.id.iconFilterAlfabetico);
            textViewFiltroOrdine = (TextView) view.findViewById(R.id.textView_filtro_ordine);
            progressBarLista = (ProgressBar) view.findViewById(R.id.progressBarLista);
            swipeLayout = (SwipeRefreshLayout) view.findViewById(R.id.swipeLayout);
            bottomSheet = view.findViewById(R.id.bottom_sheet);
            layoutImmagine = view.findViewById(R.id.image_dietro);
            imageBehind = layoutImmagine.findViewById(R.id.image_behind_inside);

            final View thisView = view;
            view.getViewTreeObserver().addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
                @Override
                public void onGlobalLayout() {
                    int heightDiff = thisView.getRootView().getHeight() - thisView.getHeight();
                    if (heightDiff > dpToPx(200)) { // if more than 200 dp, it's probably a keyboard...


                    } else {
                        MainActivity activity = (MainActivity) getActivity();
                        if(activity!=null){
                            activity.getWindow().getDecorView().setSystemUiVisibility(
                                    View.SYSTEM_UI_FLAG_HIDE_NAVIGATION
                                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
                        }
                    }
                }
            });
        } catch (Exception e) {
            Log.e(Thread.currentThread().getStackTrace().toString(), e.toString());
        }

    }

    public float dpToPx(float valueInDp) {
        try{
            DisplayMetrics metrics = context.getResources().getDisplayMetrics();
            return TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, valueInDp, metrics);
        }catch (Exception e){
            Log.e(Thread.currentThread().getStackTrace().toString(), e.toString());
        }
        return 0f;
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
        try {
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
                    LocalStorage.setTestoCercato(query);
                    FilterHelper.filtraTotale(getActivity());
                    return false;
                }

                @Override
                public boolean onQueryTextChange(String newText) {
                    if (newText.equals("")) {
                        LocalStorage.setTestoCercato(newText);
                        FilterHelper.filtraTotale(getActivity());
                    }
                    return false;
                }
            });

            //endregion
        } catch (Exception e) {
            Log.e(Thread.currentThread().getStackTrace().toString(), e.toString());
        }


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
        try {
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
        } catch (Exception e) {
            Log.e(Thread.currentThread().getStackTrace().toString(), e.toString());
        }


    }

    private void changeSearchToWhiteMode() {
        try {
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
        } catch (Exception e) {
            Log.e(Thread.currentThread().getStackTrace().toString(), e.toString());
        }

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
        try {
            progressBarLista.setVisibility(View.VISIBLE);
            esercentiArrayList = new ArrayList<Supplier>();

            String lingua = spref.getString("lingua", "");
            RequestParams params = new RequestParams();
            if (lingua.equals("Italiano")) {
                params.put("lang", "it");
            } else {
                params.put("lang", "de");
            }

            SupplierRestClient.get("", params, new JsonHttpResponseHandler() {
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


                        progressBarLista.setVisibility(View.GONE);
                        swipeLayout.setRefreshing(false);

                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });
        } catch (Exception e) {
            Log.e(Thread.currentThread().getStackTrace().toString(), e.toString());
        }


    }
}
