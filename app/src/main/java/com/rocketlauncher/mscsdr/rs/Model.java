package com.rocketlauncher.mscsdr.rs;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.widget.Toast;

import com.google.gson.Gson;
import com.rocketlauncher.mscsdr.rs.PropertyChanges.RadioString;

import java.util.HashMap;

/**
 * Created by Mortif3r on 29.12.2018.
 */

public class Model {
    private static final Model INSTANCE = new Model();
    public Gson gson = new Gson();
    private static String mConnectedDeviceName = null;
    public BluetoothSerialService mSerialService = null;
    private static boolean mLocalEcho = false;
    public volatile Context currentContext = null;

    public final RadioString titleText = new RadioString();

    public static final int MESSAGE_STATE_CHANGE = 1;
    public static final int MESSAGE_READ = 2;
    public static final int MESSAGE_WRITE = 3;
    public static final int MESSAGE_DEVICE_NAME = 4;
    public static final int MESSAGE_TOAST = 5;

    private int mOutgoingEoL_0D = 0x0D;
    private int mOutgoingEoL_0A = 0x0A;

    public static final String LOG_TAG = "ROCKETS";

    public static final String DEVICE_NAME = "device_name";
    public static final String TOAST = "toast";

    public static final HashMap<Integer, Integer> pinMapping = new HashMap<Integer, Integer>() { // ROCKET -> PIN
        {
            this.put(0, 8);
            this.put(1, 10);
            this.put(2, 12);
            this.put(3, 16);
            this.put(4, 18);
            this.put(5, 22);
            this.put(6, 24);
            this.put(7, 26);
            this.put(8, 28);
            this.put(9, 32);
        }
    };

    private Model() {
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
    public final Handler mHandlerBT = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case MESSAGE_STATE_CHANGE:
                    if (true) Log.i(LOG_TAG, "MESSAGE_STATE_CHANGE: " + msg.arg1);
                    switch (msg.arg1) {
                        case BluetoothSerialService.STATE_CONNECTED:
                            titleText.setValue("Connected to " + mConnectedDeviceName);
                            break;

                        case BluetoothSerialService.STATE_CONNECTING:
                            titleText.setValue("Connecting...");
                            break;

                        case BluetoothSerialService.STATE_LISTEN:
                        case BluetoothSerialService.STATE_NONE:
                            titleText.setValue("Not connected!");
                            break;
                    }
                    break;
                case MESSAGE_WRITE:
                    if (mLocalEcho) {
                        byte[] writeBuf = (byte[]) msg.obj;
                    }

                    break;
                case MESSAGE_DEVICE_NAME:
                    // save the connected device's name
                    mConnectedDeviceName = msg.getData().getString(DEVICE_NAME);
                    Toast.makeText(currentContext, "Connected to" + " "
                            + mConnectedDeviceName, Toast.LENGTH_SHORT).show();
                    break;
                case MESSAGE_TOAST:
                    Toast.makeText(currentContext, msg.getData().getString(TOAST), Toast.LENGTH_SHORT).show();
                    break;
            }
        }
    };

    public static Model getInstance() {
        return INSTANCE;
    }

}
