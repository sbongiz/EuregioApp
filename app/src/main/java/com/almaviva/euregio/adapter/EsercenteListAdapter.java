package com.almaviva.euregio.adapter;

/**
 * Created by a.sciarretta on 16/03/2018.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.almaviva.euregio.R;
import com.almaviva.euregio.model.Esercente;

import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;


/**
 * Created by a.sciarretta on 09/10/2017.
 */

public class EsercenteListAdapter extends BaseAdapter implements View.OnClickListener {

    private ArrayList<Esercente> listOfEsercenti;
    private LayoutInflater inflater;

    public EsercenteListAdapter(Context context, ArrayList<Esercente> listOfEsercenti) {
        this.listOfEsercenti = listOfEsercenti;
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
            holder.start = (TextView) view.findViewById(R.id.start);
            holder.finish = (TextView) view.findViewById(R.id.finish);
            holder.room = (TextView) view.findViewById(R.id.room);
            holder.roomPreview = (TextView) view.findViewById(R.id.roomPreview);
            view.setTag(holder);
        } else {
            holder = (EsercenteViewHolder) view.getTag();
        }
        holder.title.setText(listOfEsercenti.get(position).getLesson());
        try {
            holder.start.setText(listOfEsercenti.get(position).getStartLesson());
            holder.finish.setText(listOfEsercenti.get(position).getEndLesson());
        } catch (ParseException e) {
            e.printStackTrace();
        }

        holder.room.setText(listOfEsercenti.get(position).getRoom());


        String tmp = listOfEsercenti.get(position).getRoom();
        if(tmp.contains("Aula ")){
            holder.roomPreview.setText(tmp.replace("Aula ",""));
        }else if (tmp.contains("LD")){
            holder.roomPreview.setText(tmp.replace("LD",""));
        }else if (tmp.contains("spazio")){
            holder.roomPreview.setText(tmp.replace("Aula ","").replace("(spazio polifunzionale)",""));
        }


        holder.room.setOnClickListener(this);

        return view;
    }

    @Override
    public void onClick(View view) {

        int resource = view.getId();

        if(resource == R.id.title) {

        } else if(resource == R.id.room) {

        }

    }

    public void add(List<Esercente> esercenti) {
        listOfEsercenti = new ArrayList<>(esercenti);
    }

    static class EsercenteViewHolder  {
        TextView title;
        TextView start;
        TextView finish;
        TextView room;
        TextView roomPreview;
    }
}
