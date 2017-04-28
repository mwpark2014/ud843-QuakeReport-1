package com.example.android.quakereport;

import android.content.Context;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.util.List;

/**
 * Created by Mason on 4/28/2017.
 */

public class EarthquakeAdapter extends ArrayAdapter {

    public EarthquakeAdapter(@NonNull Context context, @LayoutRes int resource, @NonNull List objects) {
        super(context, resource, objects);
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        //Check if there is an existing list item view (called convertView) that we can reuse
        //otherwise, if convertView is null, then inflate a new list item layout

        View rootView = convertView;
        if (rootView == null) {
            rootView = LayoutInflater.from(getContext()).inflate(R.layout.list_item, parent, false);
        }

        Earthquake eq = (Earthquake)getItem(position);

        TextView magnitude = (TextView)rootView.findViewById(R.id.magText);
        magnitude.setText(eq.getMagnitude() + "");

        TextView city = (TextView)rootView.findViewById(R.id.cityText);
        city.setText(eq.getCity());

        TextView date = (TextView)rootView.findViewById(R.id.dateText);
        date.setText(eq.getDate());

        return rootView;
    }
}

