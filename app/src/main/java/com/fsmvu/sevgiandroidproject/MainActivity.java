package com.fsmvu.sevgiandroidproject;

import android.bluetooth.BluetoothDevice;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;


import com.devpaul.bluetoothutillib.abstracts.BaseBluetoothActivity;
import com.devpaul.bluetoothutillib.utils.SimpleBluetoothListener;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.series.DataPoint;
import com.jjoe64.graphview.series.LineGraphSeries;

import java.util.ArrayList;
import java.util.concurrent.Exchanger;


public class MainActivity extends BaseBluetoothActivity implements View.OnClickListener,Runnable {
    boolean isConnected;
    TextView connectionState;
    GraphView graph;
    Button update;
    ArrayList<String> datax = new ArrayList<>();

    @Override
    public SimpleBluetoothListener getSimpleBluetoothListener() {
        return new SimpleBluetoothListener() {
            @Override
            public void onBluetoothDataReceived(byte[] bytes, String data) {
                super.onBluetoothDataReceived(bytes, data);
                connectionState.setText("Data: " + data);
                try{
                    Double.valueOf(data);
                    datax.add(data);
                }catch (Exception e){
                    Log.e("tag", e.getMessage());
                }
                isConnected = false;
            }

            @Override
            public void onDeviceConnected(BluetoothDevice device) {
                super.onDeviceConnected(device);
                connectionState.setText("Connected");
                isConnected = true;
            }

            @Override
            public void onDeviceDisconnected(BluetoothDevice device) {
                super.onDeviceDisconnected(device);
                connectionState.setText("Disconnected");
            }

            @Override
            public void onDiscoveryStarted() {
                super.onDiscoveryStarted();
            }

            @Override
            public void onDiscoveryFinished() {
                super.onDiscoveryFinished();
            }

            @Override
            public void onDevicePaired(BluetoothDevice device) {
                super.onDevicePaired(device);
            }

            @Override
            public void onDeviceUnpaired(BluetoothDevice device) {
                super.onDeviceUnpaired(device);
            }
        };
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        connectionState = (TextView) findViewById(R.id.textx);
        graph = (GraphView) findViewById(R.id.graph);
        graph.setVisibility(View.INVISIBLE);
        update = (Button) findViewById(R.id.buttonUpdate);
        update.setOnClickListener(this);
        new Thread(this).start();
    }

    @Override
    protected void onResume() {
        super.onResume();
    }


    private void setGraph() {
        Log.v("set","graph");

        DataPoint[] a = new DataPoint[datax.size()];
        for (int i = 0; i < a.length; i++) {
            Log.v("hello", "hi: "+ datax.get(i));
            a[i] = new DataPoint(i, Double.valueOf(datax.get(i)));
        }
        LineGraphSeries<DataPoint> series = new LineGraphSeries<>(a);
        graph.addSeries(series);
        graph.setVisibility(View.VISIBLE);
    }
    boolean clicked = false;

    @Override
    public void onClick(View view) {
        setGraph();
        clicked = true;
    }

    @Override
    public void run() {
        while(true){
            try{
                Thread.sleep(1000);
                if(clicked)setGraph();
            }catch (Exception e){

            }
        }
    }
}
