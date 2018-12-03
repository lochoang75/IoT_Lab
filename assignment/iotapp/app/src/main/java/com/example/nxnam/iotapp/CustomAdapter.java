package com.example.nxnam.iotapp;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by nampr on 5/18/2018.
 */

public class CustomAdapter extends ArrayAdapter {
    private Context context;
    private int resource;
    private ArrayList<Data> dataArrayList;

    public CustomAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Data> objects) {
        super(context, resource, objects);
        this.dataArrayList = objects;
        this.resource = resource;
        this.context = context;
    }


    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
        ViewHolder viewHolder = new ViewHolder();

        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(resource, parent, false);
            viewHolder = new ViewHolder();
            viewHolder.txtDate = convertView.findViewById(R.id._date);
            viewHolder.txtHumid = convertView.findViewById(R.id._humid);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (ViewHolder) convertView.getTag();
        }

        Data data = dataArrayList.get(position);

        viewHolder.txtHumid.setText(data.getHumid() + "%");
        SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy HH:mm");
        viewHolder.txtDate.setText(dateFormat.format(new Date(data.getTime())));

        return convertView;
    }

    public class ViewHolder {
        TextView txtHumid, txtDate;
    }
}
