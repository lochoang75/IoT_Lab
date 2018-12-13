package com.example.nxnam.iotapp.CustomAdapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.example.nxnam.iotapp.Api.Data;
import com.example.nxnam.iotapp.R;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class IdAdapter extends ArrayAdapter {

    private Context context;
    private int resource;
    private ArrayList<Integer> dataArrayList;


    public IdAdapter(@NonNull Context context, int resource, @NonNull ArrayList<Integer> objects) {
        super(context, resource, objects);
        this.context = context;
        this.resource = resource;
        this.dataArrayList = objects;
    }

    @NonNull
    @Override
    public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {

        ViewHolder viewHolder = new ViewHolder();
        if (convertView == null) {
            convertView = LayoutInflater.from(context).inflate(resource, parent, false);
            viewHolder = new IdAdapter.ViewHolder();
            viewHolder.id = convertView.findViewById(R.id._id);
            convertView.setTag(viewHolder);
        } else {
            viewHolder = (IdAdapter.ViewHolder) convertView.getTag();
        }

        Integer data = dataArrayList.get(position);

        viewHolder.id.setText(data.toString());

        return convertView;
    }

    public class ViewHolder {
        TextView id;
    }

}

