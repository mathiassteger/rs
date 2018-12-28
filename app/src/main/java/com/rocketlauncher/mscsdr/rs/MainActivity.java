package com.rocketlauncher.mscsdr.rs;

import android.bluetooth.BluetoothClass;
import android.bluetooth.BluetoothDevice;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.util.List;

import me.aflak.bluetooth.Bluetooth;

public class MainActivity extends AppCompatActivity {
    Bluetooth bluetooth;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onSingle(View view) {
        bluetooth = new Bluetooth(this);


        bluetooth.enable();
        bluetooth.startScanning();

       List<BluetoothDevice> devices =  bluetooth.getPairedDevices();


       BluetoothDevice device = null;
       for(BluetoothDevice d : devices)
            if(d.getName().equals("raspberrypi"))
                device=d;

        //bluetooth.connectToName("raspberrypi");
        Log.e("error", "Final Device: " + device.getName());
        bluetooth.connectToDevice(device);
        bluetooth.onStart();
        List<BluetoothDevice> list =bluetooth.getPairedDevices();
        for(BluetoothDevice device1 : list)
            Log.e("error", device1.getName());
        Log.e("error", "connected: " + bluetooth.isConnected());
        bluetooth.send("text");

        Intent intent = new Intent(MainActivity.this, SingleActivity.class);
        startActivity(intent);
    }

    public void onMulti(View view) {
        Intent intent = new Intent(MainActivity.this, MultiActivity.class);
        startActivity(intent);
    }
}
