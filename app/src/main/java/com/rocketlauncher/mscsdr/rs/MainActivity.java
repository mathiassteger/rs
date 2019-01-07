package com.rocketlauncher.mscsdr.rs;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.TextView;

import java.beans.PropertyChangeEvent;
import java.beans.PropertyChangeListener;

public class MainActivity extends AppCompatActivity {
    public TextView mTitle;
    private PropertyChangeListener pcl;
    private BluetoothAdapter mBluetoothAdapter = null;
    private BluetoothDevice mmDevice;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Model.getInstance().currentContext = this;
        initPropertyChangeListeners();
        mTitle = findViewById(R.id.txtTitle);
        mTitle.setText(Model.getInstance().titleText.getValue());

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    private void initPropertyChangeListeners() {
        PropertyChangeListener pcl = new PropertyChangeListener() {
            @Override
            public void propertyChange(PropertyChangeEvent evt) {
                MainActivity.this.mTitle.setText((String) evt.getNewValue());
            }
        };
        this.pcl = pcl;
        Model.getInstance().titleText.subscribeToValue(pcl);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        Model.getInstance().titleText.unsubcribeFromValue(pcl);
    }

    public void onConnect(View view) {
        if (Model.getInstance().mSerialService != null) {
            Log.i(Model.LOG_TAG, "Resetting mSerialService");
            Model.getInstance().mSerialService.stop();
            Model.getInstance().mSerialService = null;
        }

        for (BluetoothDevice d : mBluetoothAdapter.getBondedDevices())
            if (d.getName().equals("Irrelev4nt"))
                mmDevice = d;

        Model.getInstance().mSerialService = new BluetoothSerialService(Model.getInstance().mHandlerBT);
        Model.getInstance().mSerialService.start();           // DAS HIER MUSS
        Model.getInstance().mSerialService.connect(mmDevice); // VOR DAS HIER!!!! SONST IST DER SOCKET ZU
    }

    public void onSingle(View view) {
        Intent intent = new Intent(MainActivity.this, SingleActivity.class);
        startActivity(intent);
    }

    public void onMulti(View view) {
        Intent intent = new Intent(MainActivity.this, MultiActivity.class);
        startActivity(intent);
    }
}
