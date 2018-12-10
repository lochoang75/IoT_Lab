package com.example.nxnam.iotapp;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.nxnam.iotapp.Api.ApiController;
import com.example.nxnam.iotapp.Api.ApiData;
import com.example.nxnam.iotapp.Api.Data;
import com.example.nxnam.iotapp.Api.Gateway;
import com.example.nxnam.iotapp.Api.Node;
import com.example.nxnam.iotapp.CustomAdapter.CustomAdapter;
import com.example.nxnam.iotapp.CustomAdapter.IdAdapter;
import com.example.nxnam.iotapp.IdSelection.IdSelection;
import com.example.nxnam.iotapp.Login.MainActivity;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity implements SwipeRefreshLayout.OnRefreshListener {

    public static final int UNDEFINED_ID = -1;
    private int nodeId = UNDEFINED_ID;
    private int gatewayId = UNDEFINED_ID;

    private TextView gatewayIdTextView, nodeIdTextView;
    //private Spinner gatewaySpinner, nodeSpinner;

    private ListView listView;
    private CustomAdapter customAdapter;

    private ApiController apiController;

    private ArrayList<Data> dataArrayList = new ArrayList<Data>();
    private ArrayList<Gateway> gatewayArrayList;
    private ArrayList<Integer> gatewayIds, nodeIds;
    private IdAdapter gatewayIdAdapter, nodeIdAdapter;

    private SwipeRefreshLayout swipeRefreshLayout;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);

        gatewayIds = new ArrayList<Integer>();
        gatewayIdAdapter = new IdAdapter(this,R.layout.id_select_activity,gatewayIds);
        //gatewaySpinner = findViewById(R.id._gateway_spin);
        //gatewayIdAdapter = new ArrayAdapter<Integer>(this, R.layout.support_simple_spinner_dropdown_item,gatewayIds);
        //gatewayIdAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);

        nodeIds = new ArrayList<Integer>();
        nodeIdAdapter = new IdAdapter(this,R.layout.id_select_activity, nodeIds);
        //nodeSpinner = findViewById(R.id._node_id_spin);
        //nodeIdAdapter = new ArrayAdapter<Integer>(this, R.layout.support_simple_spinner_dropdown_item,nodeIds);
        //nodeIdAdapter.setDropDownViewResource(R.layout.support_simple_spinner_dropdown_item);

        gatewayIdTextView = findViewById(R.id._gateway_id);
        gatewayIdTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                gatewayId = UNDEFINED_ID;
                nodeId = UNDEFINED_ID;
                gatewayIdTextView.setText("Select a gateway ID");
                nodeIdTextView.setText(null);
                Toast.makeText(HomeActivity.this, "Select gateway id",Toast.LENGTH_LONG).show();
                displayData();
            }
        });

        nodeIdTextView = findViewById(R.id._node_id);
        nodeIdTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                nodeId = UNDEFINED_ID;
                if (gatewayId == UNDEFINED_ID) {
                    nodeIdTextView.setText(null);
                    Toast.makeText(HomeActivity.this, "Select gateway id first",Toast.LENGTH_LONG).show();
                }
                else {
                    nodeIdTextView.setText("Select a node ID");
                    displayData();
                }
            }
        });

        listView = findViewById(R.id._l_view);

        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                if (gatewayId == UNDEFINED_ID) {
                    gatewayId = (int) parent.getItemAtPosition(position);
                    gatewayIdTextView.setText(String.valueOf(gatewayId));
                    nodeId = UNDEFINED_ID;
                    nodeIdTextView.setText("Slect an node ID");
                    displayData();
                } else if (nodeId == UNDEFINED_ID) {
                    nodeId = (int) parent.getItemAtPosition(position);
                    nodeIdTextView.setText(String.valueOf(nodeId));
                    displayData();
                }
            }
        });
        customAdapter = new CustomAdapter(this,R.layout.data_in_list,dataArrayList);


        apiController = new ApiController();

        /*gatewaySpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                gatewayId = gatewaySpinner.getSelectedItemPosition();
                System.out.print("[mDEBUG] HomeActivity onGateway " + gatewayId + "\n");
                displayData();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/

        /*nodeSpinner.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                nodeId = nodeSpinner.getSelectedItemPosition();
                displayData();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });*/

        swipeRefreshLayout = findViewById(R.id.swiperefresh);
        swipeRefreshLayout.setOnRefreshListener(this);
    }


    private void displayData() {
        apiController.apiGetData().setApiListener(new ApiController.ApiListener() {
            @Override
            public void onGetData(ApiData data) {
                if (data != null){
                    System.out.print("[mDEBUG] HomeActivity getData\n");
                    if (dataArrayList != null) {

                        dataArrayList.clear();
                        gatewayIds.clear();
                        nodeIds.clear();

                        for (int i = 0; i < data.getApiData().size(); i++){

                            gatewayIds.add(data.getApiData().get(i).getGatewayId());
                            if (data.getApiData().get(i).getGatewayId() == gatewayId){

                                ArrayList<Node> nodes = data.getApiData().get(i).getNodeData();

                                for (int j = 0; j < nodes.size(); j++){

                                    nodeIds.add(nodes.get(j).getNodeId());
                                    if (nodes.get(j).getNodeId() == nodeId) {

                                        for (int k = 0; k < nodes.get(j).getData().size(); k++){

                                            dataArrayList.add(nodes.get(j).getData().get(k));
                                            System.out.print("[mDEBUG] " + nodes.get(j).getData().get(k).getTime() + " "
                                                + nodes.get(j).getData().get(k).getHumid() + "\n");
                                        }
                                        customAdapter.notifyDataSetChanged();
                                        listView.setAdapter(customAdapter);
                                        System.out.print("[mDEBUG]: dataArrayList size " + dataArrayList.size() + "\n");
                                    }
                                }
                                nodeIdAdapter.notifyDataSetChanged();
                                if (nodeId == UNDEFINED_ID)
                                    listView.setAdapter(nodeIdAdapter);
                                //nodeSpinner.setAdapter(nodeIdAdapter);
                                System.out.print("[mDEBUG]: node ids size " + nodeIds.size() + "\n");
                            }
                        }
                        gatewayIdAdapter.notifyDataSetChanged();
                        if (gatewayId == UNDEFINED_ID)
                            listView.setAdapter(gatewayIdAdapter);
                        System.out.print("[mDEBUG]: gatewayids size " + gatewayIds.size() + "\n");
                        //gatewaySpinner.setAdapter(gatewayIdAdapter);
                    }
                }
            }

            @Override
            public void onFail(Throwable t) {

            }
        });
    }
    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }
    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        switch (id) {
            case R.id._linechart:
                Toast.makeText(this,"Line Chart",Toast.LENGTH_SHORT).show();
                return true;
            case R.id._home:
                Toast.makeText(this,"Home",Toast.LENGTH_SHORT).show();
                return true;
            case R.id._districhart:
                Toast.makeText(this,"Distribution Chart",Toast.LENGTH_SHORT).show();
                return true;
            case R.id._about:
                Toast.makeText(this,"About",Toast.LENGTH_SHORT).show();
                return true;
            case R.id._logout:
                Intent intent = new Intent(HomeActivity.this,MainActivity.class);
                startActivity(intent);
                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onRefresh() {
        displayData();
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(false);
            }
        }, 2000);
    }


}
