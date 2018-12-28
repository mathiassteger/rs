package com.rocketlauncher.mscsdr.rs;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.UUID;

public class MainActivity extends AppCompatActivity {
    private static final UUID MY_UUID = UUID.fromString("00001101-0000-1000-8000-00805f9b34fb");
    private BluetoothDevice mmDevice;
    private BluetoothAdapter mBluetoothAdapter;
    private SendReceive sendReceive;

    static final int STATE_LISTENING = 1;
    static final int STATE_CONNECTING = 2;
    static final int STATE_CONNECTED = 3;
    static final int STATE_CONNECTION_FAILED = 4;
    static final int STATE_MESSAGE_RECEIVED = 5;

    int REQUEST_ENABLE_BLUETOOTH = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();

        for (BluetoothDevice d : this.mBluetoothAdapter.getBondedDevices())
            if (d.getName().equals("Irrelev4nt"))
                this.mmDevice = d;

        ClientClass clientClass = new ClientClass(mmDevice);
        clientClass.start();
    }

    Handler handler = new Handler(new Handler.Callback() {
        @Override
        public boolean handleMessage(Message msg) {
            switch (msg.what) {

                default:
                    Log.e("error", "Handler: " + msg.what);
            }
            return true;
        }
    });

    public void onSingle(View view) {
        String string = "Hello!";
        sendReceive.write(string.getBytes());
        Intent intent = new Intent(MainActivity.this, SingleActivity.class);
        startActivity(intent);
    }

    public void onMulti(View view) {
        Intent intent = new Intent(MainActivity.this, MultiActivity.class);
        startActivity(intent);
    }

    private class ClientClass extends Thread {
        private BluetoothDevice device;
        private BluetoothSocket socket;

        public ClientClass(BluetoothDevice device) {
            this.device = device;
            try {
                this.socket = this.device.createRfcommSocketToServiceRecord(MY_UUID);
                Log.e("error", "" + socket);

            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        public void run() {
            try {
                socket.connect();
                Message msg = Message.obtain();
                msg.what = STATE_CONNECTED;
                handler.sendMessage(msg);
                sendReceive = new SendReceive(socket);
                sendReceive.start();
            } catch (IOException e) {
                e.printStackTrace();
                Message msg = Message.obtain();
                msg.what = STATE_CONNECTION_FAILED;
                handler.sendMessage(msg);
            }
        }
    }

    private class SendReceive extends Thread {
        private final BluetoothSocket bluetoothSocket;
        private final InputStream is;
        private final OutputStream os;

        public SendReceive(BluetoothSocket socket) {
            bluetoothSocket = socket;
            OutputStream tempOut = null;
            InputStream tempIn = null;
            try {
                tempOut = bluetoothSocket.getOutputStream();
                tempIn = bluetoothSocket.getInputStream();
            } catch (IOException e) {
                e.printStackTrace();
            }
            os = tempOut;
            is = tempIn;
        }

        public void run() {
            byte[] buffer = new byte[1024];
            int bytes;
            Log.e("error", "Starting WhileLoop");
            while (true) {
                try {
                    bytes = is.read(buffer);
                    handler.obtainMessage(STATE_MESSAGE_RECEIVED, bytes, -1,buffer).sendToTarget();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

        public void write(byte[] bytes) {
            try {
                Log.e("error", "Writing...");
                os.write(bytes);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }
}
