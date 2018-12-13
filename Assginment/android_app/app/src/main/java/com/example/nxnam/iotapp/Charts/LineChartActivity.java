package com.example.nxnam.iotapp.Charts;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.Toast;

import com.example.nxnam.iotapp.Api.ApiController;
import com.example.nxnam.iotapp.Api.ApiData;
import com.example.nxnam.iotapp.Api.Data;
import com.example.nxnam.iotapp.Api.Node;
import com.example.nxnam.iotapp.HomeActivity;
import com.example.nxnam.iotapp.Login.MainActivity;
import com.example.nxnam.iotapp.R;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;

public class LineChartActivity extends AppCompatActivity {

    private ActionBar actionBar;
    private DateFormat dateFormat;
    private GraphView graphView;
    private LineGraphSeries<DataPoint> series;
    private Intent intent;
    private int gatewayId = HomeActivity.UNDEFINED_ID, nodeId = HomeActivity.UNDEFINED_ID;
    private ApiController apiController;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.line_chart_activity);

        actionBar = getSupportActionBar();
        actionBar.setBackgroundDrawable(new ColorDrawable(Color.parseColor("#9040E0")));

        graphView = findViewById(R.id.graph);
        dateFormat = new SimpleDateFormat("HH:mm");
        intent = getIntent();
        apiController = new ApiController();

        gatewayId = intent.getIntExtra(HomeActivity.Home_GatewayID,HomeActivity.UNDEFINED_ID);
        nodeId = intent.getIntExtra(HomeActivity.Home_NodeID, HomeActivity.UNDEFINED_ID);

        apiController.apiGetData().setApiListener(new ApiController.ApiListener() {
            @Override
            public void onGetData(ApiData data) {
                ArrayList<Node> nodes = new ArrayList<Node>();
                ArrayList<Data> dataArrayList = new ArrayList<Data>();
                for (int i = 0; i < data.getApiData().size(); i++){
                    if (data.getApiData().get(i).getGatewayId() == gatewayId){
                        nodes = data.getApiData().get(i).getNodeData();
                        break;
                    }
                }
                for (int j = 0; j < nodes.size(); j++){
                    if (nodes.get(j).getNodeId() == nodeId){
                        dataArrayList = nodes.get(j).getData();
                        break;
                    }
                }
                display(dataArrayList);
            }
            @Override
            public void onFail(Throwable t) {

            }
        });
    }

    private void display(ArrayList<Data> objects) {
        int dataPointSize = objects.size() > 20 ? 20 : objects.size();
        int offset = objects.size() > 20 ? objects.size() - 20 : 0;
        DataPoint[] dataPoints = new DataPoint[dataPointSize];
        for (int i = 0; i < dataPointSize; i++){
            dataPoints[i] = new DataPoint(new Date(objects.get(i + offset).getTime() * 1000), objects.get(i + offset).getHumid());

        }
        series = new LineGraphSeries<>(dataPoints);
        series.setColor(Color.parseColor("#BB6BD9"));
        graphView.addSeries(series);

        graphView.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(this, dateFormat));
        graphView.getGridLabelRenderer().setNumHorizontalLabels(6);

        graphView.getViewport().setMinX(dataPoints[0].getX());
        graphView.getViewport().setMaxX(dataPoints[0].getX() + 3600000);
        graphView.getViewport().setMinY(0);
        graphView.getViewport().setMaxY(100);
        graphView.getViewport().setXAxisBoundsManual(true);
        graphView.getViewport().setYAxisBoundsManual(true);

        graphView.getViewport().setScrollable(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id._linechart:
                Toast.makeText(this,"Line Chart",Toast.LENGTH_SHORT).show();
                return true;
            case R.id._home:
                finish();
                Toast.makeText(this,"Home",Toast.LENGTH_SHORT).show();
                return true;
            case R.id._districhart:
                Intent distributionChartIntent = new Intent(LineChartActivity.this, DistributionChartActivity.class);
                distributionChartIntent.putExtra(HomeActivity.Home_GatewayID, gatewayId);
                distributionChartIntent.putExtra(HomeActivity.Home_NodeID, nodeId);
                startActivity(distributionChartIntent);
                finish();
                Toast.makeText(this,"Distribution Chart",Toast.LENGTH_SHORT).show();
                return true;
            case R.id._about:
                Toast.makeText(this,"About",Toast.LENGTH_SHORT).show();
                return true;
            case R.id._logout:
                Intent intent = new Intent(LineChartActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
