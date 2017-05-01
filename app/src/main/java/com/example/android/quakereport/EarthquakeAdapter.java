package com.example.android.quakereport;

import android.content.Context;
import android.content.Intent;
import android.graphics.drawable.GradientDrawable;
import android.net.Uri;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.List;

/**
 * Created by Mason on 4/28/2017.
 */

public class EarthquakeAdapter extends ArrayAdapter {
    private Earthquake eq;

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

        //Set the OnClickListener to direct user to website for more details
        eq = (Earthquake)getItem(position);
        rootView.setOnClickListener(new View.OnClickListener() {
            public void onClick(View v) {
                openEarthquakePage();
            }
        });

        String magnitudeOutput = new DecimalFormat("0.0").format(eq.getMagnitude());
        TextView magnitudeView = (TextView)rootView.findViewById(R.id.magText);
        magnitudeView.setText(magnitudeOutput);

        // Set the proper background color on the magnitude circle.
        // Fetch the background from the TextView, which is a GradientDrawable.
        GradientDrawable magnitudeCircle = (GradientDrawable) magnitudeView.getBackground();
        // Set the color on the magnitude circle
        magnitudeCircle.setColor(getMagnitudeColor(eq.getMagnitude()));

        String cityString = eq.getCity();
        String locationQualifier = "Near the ";
        if(cityString.contains(" of ")) {
            locationQualifier = cityString.substring(0, cityString.indexOf(" of ") + 3);
            cityString = cityString.substring(cityString.indexOf(" of ") + 4);
        }
        TextView cityView = (TextView)rootView.findViewById(R.id.cityText);
        cityView.setText(locationQualifier + "\n" + cityString);

        String dateOutput = new SimpleDateFormat("yyyy-MM-dd \nhh:mm:ss a").format(eq.getDate());
        TextView dateView = (TextView)rootView.findViewById(R.id.dateText);
        dateView.setText(dateOutput);

        return rootView;
    }

    private int getMagnitudeColor(double magnitude) {
        int[] magColorGradient = new int[]{R.color.magnitude1, R.color.magnitude2,
                R.color.magnitude3,R.color.magnitude4, R.color.magnitude5,
                R.color.magnitude6, R.color.magnitude7, R.color.magnitude8,
                R.color.magnitude9, R.color.magnitude10plus, R.color.magnitude10plus};
        return ContextCompat.getColor(getContext(), magColorGradient[(int)magnitude]);
    }

    private void openEarthquakePage() {
        Intent i = new Intent(Intent.ACTION_VIEW);
        i.setData(Uri.parse(eq.getURL()));
        getContext().startActivity(i);
    }
}

