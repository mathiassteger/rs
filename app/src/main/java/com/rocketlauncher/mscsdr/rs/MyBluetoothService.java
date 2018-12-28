package com.rocketlauncher.mscsdr.rs;

import android.bluetooth.BluetoothSocket;
import android.os.Handler;
import android.util.Log;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class MyBluetoothService {
    private static final String TAG = "MY_APP_DEBUG_TAG";
    private Handler mHandler;

    public class ConnectedThread extends Thread {
        private byte[] mmBuffer;
        private final InputStream mmInStream;
        private final OutputStream mmOutStream;
        private final BluetoothSocket mmSocket;

        public ConnectedThread(BluetoothSocket socket) {
            this.mmSocket = socket;
            InputStream tmpIn = null;
            OutputStream tmpOut = null;
            try {
                tmpIn = socket.getInputStream();
            } catch (IOException e) {
                Log.e(MyBluetoothService.TAG, "Error occurred when creating input stream", e);
            }
            try {
                tmpOut = socket.getOutputStream();
            } catch (IOException e2) {
                Log.e(MyBluetoothService.TAG, "Error occurred when creating output stream", e2);
            }
            this.mmInStream = tmpIn;
            this.mmOutStream = tmpOut;
        }

        public void run() {
            this.mmBuffer = new byte[1024];
            while (true) {
                try {
                    MyBluetoothService.this.mHandler.obtainMessage(0, this.mmInStream.read(this.mmBuffer), -1, this.mmBuffer).sendToTarget();
                } catch (Exception e) {
                    Log.d(MyBluetoothService.TAG, "Input stream was disconnected", e);
                    return;
                }
            }
        }

        public void write(String input) {
            try {
                this.mmOutStream.write(input.getBytes());
            } catch (IOException e) {
            }
        }

        public void cancel() {
            try {
                this.mmSocket.close();
            } catch (IOException e) {
                Log.e(MyBluetoothService.TAG, "Could not close the connect socket", e);
            }
        }
    }

    private interface MessageConstants {
        public static final int MESSAGE_READ = 0;
        public static final int MESSAGE_TOAST = 2;
        public static final int MESSAGE_WRITE = 1;
    }
}
