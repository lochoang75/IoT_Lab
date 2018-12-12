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
import com.jjoe64.graphview.ValueDependentColor;
import com.jjoe64.graphview.helper.StaticLabelsFormatter;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.Date;

public class DistributionChartActivity extends AppCompatActivity {
    private ActionBar actionBar;

    private GraphView graphView;
    BarGraphSeries<DataPoint> series;
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
                double[] distributionData = dataPointProcess(dataArrayList);
                display(distributionData);
            }

            @Override
            public void onFail(Throwable t) {

            }
        });
    }

    private double[] dataPointProcess(ArrayList<Data> dataArrayList) {
        double[] ret = new double[10];
        for (int i = 0; i < 10; i++){
            ret[i] = 0;
        }
        for (int i = 0; i < dataArrayList.size(); i++){

            ret[(int)(dataArrayList.get(i).getHumid() / 10) - (int) (dataArrayList.get(i).getHumid() / 100)]++;
        }
        for (int i = 0; i < 10; i++){
            ret[i] = ret[i] /dataArrayList.size() * 100;
            System.out.print("distrbutionData: " + ret[i] + "\n");
        }
        return ret;
    }

    private void display(double[] distributionData) {
        DataPoint[] dataPoints = new DataPoint[10];

        for (int i = 0; i < 10; i++){
            dataPoints[i] = new DataPoint(i + 1, distributionData[i]);

        }
        series = new BarGraphSeries<>(dataPoints);
        graphView.addSeries(series);

        series.setValueDependentColor(new ValueDependentColor<DataPoint>() {
            @Override
            public int get(DataPoint data) {
                return Color.parseColor("#5735B7");
            }
        });

        graphView.getGridLabelRenderer().setNumHorizontalLabels(11);
        series.setSpacing(50);

        series.setDrawValuesOnTop(true);
        series.setValuesOnTopColor(Color.parseColor("#BB6BD9"));

        StaticLabelsFormatter staticLabelsFormatter = new StaticLabelsFormatter(graphView);
        staticLabelsFormatter.setHorizontalLabels(new String[] {"","0-9", "10-19", "20-29", "30-39", "40-49", "50-59",
                "60-69", "70-79", "80-89", "90-100", ""});
        graphView.getGridLabelRenderer().setLabelFormatter(staticLabelsFormatter);
        graphView.getViewport().setMinX(0);
        graphView.getViewport().setMaxX(11);
        graphView.getViewport().setMinY(0);
        graphView.getViewport().setMaxY(100);
        graphView.getViewport().setXAxisBoundsManual(true);
        graphView.getViewport().setYAxisBoundsManual(true);
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
                Intent linechartIntent = new Intent(DistributionChartActivity.this, LineChartActivity.class);
                linechartIntent.putExtra(HomeActivity.Home_GatewayID, gatewayId);
                linechartIntent.putExtra(HomeActivity.Home_NodeID, nodeId);
                startActivity(linechartIntent);
                finish();
                Toast.makeText(this,"Line Chart",Toast.LENGTH_SHORT).show();
                return true;
            case R.id._home:
                finish();
                Toast.makeText(this,"Home",Toast.LENGTH_SHORT).show();
                return true;
            case R.id._districhart:
                Toast.makeText(this,"Distribution Chart",Toast.LENGTH_SHORT).show();
                return true;
            case R.id._about:
                Toast.makeText(this,"About",Toast.LENGTH_SHORT).show();
                return true;
            case R.id._logout:
                Intent intent = new Intent(DistributionChartActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }
}
