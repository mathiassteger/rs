package com.rocketlauncher.mscsdr.rs;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.rocketlauncher.mscsdr.rs.MyBluetoothService.ConnectedThread;

import java.io.IOException;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    private ConnectedThread ManageConnection;
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
    private BluetoothDevice mmDevice;
    private BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

    private class ConnectThread extends Thread {
        private final BluetoothSocket mmSocket;

        public ConnectThread(BluetoothDevice device) {
            BluetoothSocket tmp = null;
            MainActivity.this.mmDevice = device;
            try {
                tmp = device.createRfcommSocketToServiceRecord(MainActivity.MY_UUID);
            } catch (IOException e) {
                Toast.makeText(MainActivity.this.getApplicationContext(), "Socket's create() method failed", Toast.LENGTH_SHORT).show();
            }
            this.mmSocket = tmp;
        }

        public void run() {
            MainActivity.this.mBluetoothAdapter.cancelDiscovery();
            try {
                this.mmSocket.connect();
                Toast.makeText(MainActivity.this.getApplicationContext(), "Connecting to Device", Toast.LENGTH_SHORT).show();
                MyBluetoothService myBluetoothService = new MyBluetoothService();
                myBluetoothService.getClass();
                ConnectedThread t = new ConnectedThread();
                MainActivity.this.ManageConnection = new ConnectedThread(this.mmSocket);
                MainActivity.this.ManageConnection.start();
            } catch (IOException e) {
                Toast.makeText(MainActivity.this.getApplicationContext(), "Unable to Connect..", Toast.LENGTH_SHORT).show();
                try {
                    this.mmSocket.close();
                } catch (IOException e2) {
                    Toast.makeText(MainActivity.this.getApplicationContext(), "Could not close the client socket", Toast.LENGTH_SHORT).show();
                }
            }
        }

        public void cancel() {
            try {
                this.mmSocket.close();
            } catch (IOException e) {
                Toast.makeText(MainActivity.this.getApplicationContext(), "Could not close the client socket", Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        for (BluetoothDevice d : this.mBluetoothAdapter.getBondedDevices())
            if (d.getName().equals("Irrelev4nt"))
                this.mmDevice = d;
        new Thread(new ConnectThread(MainActivity.this.mmDevice)).run();
    }

    public void onSingle(View view) {
        if (MainActivity.this.ManageConnection != null) {
            MainActivity.this.ManageConnection.write("RedOn");
            return;
        }

        Intent intent = new Intent(MainActivity.this, SingleActivity.class);
        startActivity(intent);
    }

    public void onMulti(View view) {
        Intent intent = new Intent(MainActivity.this, MultiActivity.class);
        startActivity(intent);
    }
}
