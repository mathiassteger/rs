package com.rocketlauncher.mscsdr.rs;

import android.annotation.SuppressLint;
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
import android.view.inputmethod.InputMethodManager;
import android.widget.TextView;
import android.widget.Toast;

public class MainActivity extends AppCompatActivity {
    public TextView mTitle;
    private String mConnectedDeviceName = null;
    private BluetoothAdapter mBluetoothAdapter = null;
    private static BluetoothSerialService mSerialService = null;
    private boolean mLocalEcho = false;
    private BluetoothDevice mmDevice;
    public static final String LOG_TAG = "ROCKETS";

    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;
    public static final int MESSAGE_DEVICE_NAME = 4;
    public static final int MESSAGE_TOAST = 5;

    private int mOutgoingEoL_0D = 0x0D;
    private int mOutgoingEoL_0A = 0x0A;

    // Key names received from the BluetoothChatService Handler
    public static final String DEVICE_NAME = "device_name";
    public static final String TOAST = "toast";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mTitle = findViewById(R.id.txtTitle);
        mTitle.setText(R.string.app_name);

        mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
    }

    private byte[] handleEndOfLineChars(int outgoingEoL) {
        byte[] out;

        if (outgoingEoL == 0x0D0A) {
            out = new byte[2];
            out[0] = 0x0D;
            out[1] = 0x0A;
        } else {
            if (outgoingEoL == 0x00) {
                out = new byte[0];
            } else {
                out = new byte[1];
                out[0] = (byte) outgoingEoL;
            }
        }

        return out;
    }

    public void send(byte[] out) {

        if (out.length == 1) {

            if (out[0] == 0x0D) {
                out = handleEndOfLineChars(mOutgoingEoL_0D);
            } else {
                if (out[0] == 0x0A) {
                    out = handleEndOfLineChars(mOutgoingEoL_0A);
                }
            }
        }

        if (out.length > 0) {
            mSerialService.write(out);
        }
    }

    @SuppressLint("HandlerLeak")
    private final Handler mHandlerBT = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_STATE_CHANGE:
                    if (true) Log.i(LOG_TAG, "MESSAGE_STATE_CHANGE: " + msg.arg1);
                    switch (msg.arg1) {
                        case BluetoothSerialService.STATE_CONNECTED:

                            mTitle.setText("Connected to");
                            mTitle.append(" " + mConnectedDeviceName);
                            break;

                        case BluetoothSerialService.STATE_CONNECTING:
                            mTitle.setText("Connecting...");
                            break;

                        case BluetoothSerialService.STATE_LISTEN:
                        case BluetoothSerialService.STATE_NONE:
                            mTitle.setText("Not connected");

                            break;
                    }
                    break;
                case MESSAGE_WRITE:
                    if (mLocalEcho) {
                        byte[] writeBuf = (byte[]) msg.obj;
                    }

                    break;
/*
            case MESSAGE_READ:
                byte[] readBuf = (byte[]) msg.obj;
                mEmulatorView.write(readBuf, msg.arg1);

                break;
*/
                case MESSAGE_DEVICE_NAME:
                    // save the connected device's name
                    mConnectedDeviceName = msg.getData().getString(DEVICE_NAME);
                    Toast.makeText(getApplicationContext(), "Connected to" + " "
                            + mConnectedDeviceName, Toast.LENGTH_SHORT).show();
                    break;
                case MESSAGE_TOAST:
                    Toast.makeText(getApplicationContext(), msg.getData().getString(TOAST), Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };


    public void onSingle(View view) {
        if (mSerialService != null) {
            Log.i(LOG_TAG, "Resetting mSerialService");
            mSerialService.stop();
            mSerialService = null;
        }
        for (BluetoothDevice d : mBluetoothAdapter.getBondedDevices())
            if (d.getName().equals("Irrelev4nt"))
                mmDevice = d;

        //this.mConnectedDeviceName = mmDevice.getName();
        mSerialService = new BluetoothSerialService(this, mHandlerBT);
        mSerialService.start();           // DAS HIER MUSS
        mSerialService.connect(mmDevice); // VOR DAS HIER!!!! SONST IST DER SOCKET ZU

//        Intent intent = new Intent(MainActivity.this, SingleActivity.class);
//        startActivity(intent);
    }

    public void onMulti(View view) {
        send("Hallo".getBytes());
//        Intent intent = new Intent(MainActivity.this, MultiActivity.class);
//        startActivity(intent);
    }
}
