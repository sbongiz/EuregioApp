package com.almaviva.euregio.adapter;

/**
 * Created by a.sciarretta on 16/03/2018.
 */

import android.app.Application;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import com.almaviva.euregio.R;
import com.almaviva.euregio.model.Agreement;
import com.almaviva.euregio.model.Esercente;
import com.almaviva.euregio.model.Supplier;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by a.sciarretta on 09/10/2017.
 */

public class EsercenteListAdapter extends BaseAdapter implements View.OnClickListener {

    private ArrayList<Supplier> listOfEsercenti;
    private LayoutInflater inflater;
    private Context context;


    public EsercenteListAdapter(Context context, ArrayList<Supplier> listOfEsercenti) {
        this.listOfEsercenti = listOfEsercenti;
        this.context = context;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return listOfEsercenti.size();
    }

    @Override
    public Object getItem(int position) {
        return listOfEsercenti.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        EsercenteViewHolder holder;
        View view = convertView;

        if (convertView == null) {
            holder = new EsercenteViewHolder();
            view = inflater.inflate(R.layout.esercente_item, null);

            holder.title = (TextView) view.findViewById(R.id.title);
            holder.data = (TextView) view.findViewById(R.id.data);
            holder.indirizzo = (TextView) view.findViewById(R.id.indirizzo);
            holder.linearLayoutVantaggi = (LinearLayout) view.findViewById(R.id.vantaggi_linear_layout);

            view.setTag(holder);
        } else {
            holder = (EsercenteViewHolder) view.getTag();
        }


        holder.title.setText(listOfEsercenti.get(position).getTitle());
        holder.data.setText(listOfEsercenti.get(position).getDate());
        holder.indirizzo.setText(listOfEsercenti.get(position).getIndirizzo());

        LinearLayout a = new LinearLayout(context);
        a.setOrientation(LinearLayout.VERTICAL);

        for (Agreement vantaggio: listOfEsercenti.get(position).properties.agreements){
        TextView textTmp = new TextView(context);
            textTmp.setText(vantaggio.getDescriptionShort());
            textTmp.setTextColor(context.getResources().getColor(R.color.colorPrimary));
        a.addView(textTmp);
        }

        holder.linearLayoutVantaggi.addView(a);



        return view;
    }

    @Override
    public void onClick(View view) {

        int resource = view.getId();

        if(resource == R.id.title) {

        }

    }

    public void add(List<Supplier> esercenti) {
        listOfEsercenti = new ArrayList<>(esercenti);
    }

    static class EsercenteViewHolder  {
        TextView title;
        TextView indirizzo;
        TextView data;
        LinearLayout linearLayoutVantaggi;

    }
}
