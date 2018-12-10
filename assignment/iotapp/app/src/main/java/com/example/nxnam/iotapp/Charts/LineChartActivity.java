package com.example.nxnam.iotapp.Charts;

import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;

import com.example.nxnam.iotapp.R;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.Random;

public class LineChartActivity extends AppCompatActivity {

    private DateFormat dateFormat;
    private Calendar calendar;
    private Date[] date;
    GraphView graphView;
    DataPoint[] dataPoints1, dataPoints2;
    LineGraphSeries<DataPoint> series1, series2;
    Random random;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.line_chart_activity);

        dateFormat = new SimpleDateFormat("HH:mm");
        calendar = Calendar.getInstance();
        date = new Date[14];
        graphView = findViewById(R.id.graph);
        dataPoints1 = new DataPoint[10];
        dataPoints2 = new DataPoint[4];
        random = new Random();

        //Init data
        for (int i = 0; i < 14; i++)
        {
            date[i] = calendar.getTime();
            if (i != 9){
                calendar.add(Calendar.MINUTE,15);
            }
        }
        for (int i = 0; i < 10; i++){
            dataPoints1[i] = new DataPoint(date[i], 30 + random.nextInt(70));
        }
        dataPoints2[0] = dataPoints1[9];
        for (int i = 1; i < 4; i++){
            dataPoints2[i] = new DataPoint(date[i+10], 30 + random.nextInt(70));
        }

        series1 = new LineGraphSeries<>(dataPoints1);
        series2 = new LineGraphSeries<>(dataPoints2);
        series2.setColor(Color.argb(255, 255, 60, 60));
        graphView.addSeries(series1);
        graphView.addSeries(series2);

        graphView.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(this, dateFormat));
        graphView.getGridLabelRenderer().setNumHorizontalLabels(9);

        graphView.getViewport().setMinX(date[0].getTime());
        graphView.getViewport().setMaxX(date[9].getTime());
        graphView.getViewport().setXAxisBoundsManual(true);

        graphView.getGridLabelRenderer().setHumanRounding(false);
        graphView.getViewport().setScrollable(true);
    }
}
