package com.example.nxnam.iotapp;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.util.ArrayList;

public class HomeActivity extends AppCompatActivity {

    private String id = "101";
    private TextView textView;
    private ListView listView;

    private Device device = new Device(id);
    private ArrayList<Data> dataArrayList;

    private CustomAdapter customAdapter;
    private ApiController apiController;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_activity);

        textView = findViewById(R.id._node_id);
        apiController = new ApiController();
//        listView = findViewById(R.id._l_view);
//
//        customAdapter = new CustomAdapter(this,R.layout.data_in_list,device.getDataArrayList());
        //listView.setAdapter(customAdapter);

        //getData();
        //displayData();
        
    }

    private void displayData() {
        customAdapter.notifyDataSetChanged();
        textView.setText(device.getId());
        listView.setAdapter(customAdapter);
    }

    private void getData(){
        apiController.getData().addListener(new ApiController.ApiListener() {
            @Override
            public void onGetData(ApiResponse data) {
                for (int i = 0; i < data.response.size(); i++){
                    if (data.response.get(i).getId().compareTo(id) == 0){
                       device = data.response.get(i);
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
}
