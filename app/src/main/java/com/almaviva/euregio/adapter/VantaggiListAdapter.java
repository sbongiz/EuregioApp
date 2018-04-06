package com.almaviva.euregio.adapter;

/**
 * Created by a.sciarretta on 06/04/2018.
 */

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.almaviva.euregio.R;
import com.almaviva.euregio.model.Agreement;

import java.util.ArrayList;
import java.util.List;


public class VantaggiListAdapter extends BaseAdapter implements View.OnClickListener {

    private ArrayList<Agreement> listOfVantaggi;
    private LayoutInflater inflater;

    public VantaggiListAdapter(Context context, ArrayList<Agreement> listOfVantaggi) {
        this.listOfVantaggi = listOfVantaggi;
        inflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    @Override
    public int getCount() {
        return listOfVantaggi.size();
    }

    @Override
    public Object getItem(int position) {
        return listOfVantaggi.get(position);
    }

    @Override
    public long getItemId(int position) {
        return position;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {

        VantaggiViewHolder holder;
        View view = convertView;

        try{
            if (convertView == null) {
                holder = new VantaggiViewHolder();
                view = inflater.inflate(R.layout.vantaggi_items, null);
                view.setTag(holder);
            } else {
                holder = (VantaggiViewHolder) view.getTag();
            }
            holder.descriptionShort.setText(listOfVantaggi.get(position).getDescriptionShort());

        } catch (Exception e) {
            e.printStackTrace();
        }

        return view;
    }

    @Override
    public void onClick(View view) {

        int resource = view.getId();

        if(resource == R.id.title) {

        }

    }

    public void add(List<Agreement> vantaggi) {
        listOfVantaggi = new ArrayList<>(vantaggi);
    }

    static class VantaggiViewHolder  {
        TextView descriptionShort;
    }
}
