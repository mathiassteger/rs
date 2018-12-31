package com.rocketlauncher.mscsdr.rs;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.rocketlauncher.mscsdr.rs.json.Launchinfo;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;

public class MultiActivity extends AppCompatActivity {
    public EditText etValue1;
    public EditText etValue2;
    public EditText etValue3;
    public EditText etValue4;
    public EditText etValue5;
    public EditText etValue6;
    public EditText etValue7;
    public EditText etValue8;
    public EditText etValue9;
    public EditText etValue10;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_multi);
        Model.getInstance().currentContext = this;
        etValue1 = findViewById(R.id.etValue1);
        etValue2 = findViewById(R.id.etValue2);
        etValue3 = findViewById(R.id.etValue3);
        etValue4 = findViewById(R.id.etValue4);
        etValue5 = findViewById(R.id.etValue5);
        etValue6 = findViewById(R.id.etValue6);
        etValue7 = findViewById(R.id.etValue7);
        etValue8 = findViewById(R.id.etValue8);
        etValue9 = findViewById(R.id.etValue9);
        etValue10 = findViewById(R.id.etValue10);
    }


    public void onGo(View view) {
        double[] extractedValues = extractValues();
        HashMap<Double, ArrayList<Integer>> delayMap = makeDelayMap(extractedValues);

        ArrayList<Launchinfo> launchinfos = new ArrayList<>();
        while (delayMap.size() > 0) {
            Double minDelay = Collections.min(delayMap.keySet());
            ArrayList<Integer> rocketsA = delayMap.remove(minDelay);
            Integer[] rockets = rocketsA.toArray(new Integer[rocketsA.size()]);
            int[] pins = getPinsFor(rockets);

            launchinfos.add(new Launchinfo(pins, minDelay));
        }

        for (Launchinfo li : launchinfos) {
            Log.i(Model.LOG_TAG, "Sending: " + li.toString());
            Model.getInstance().send(Model.getInstance().gson.toJson(li).getBytes());
            try {
                Thread.sleep(100);
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    private double[] extractValues() {
        double[] extractedValues = new double[10];
        extractedValues[0] = Double.parseDouble(etValue1.getText().toString());
        extractedValues[1] = Double.parseDouble(etValue2.getText().toString());
        extractedValues[2] = Double.parseDouble(etValue3.getText().toString());
        extractedValues[3] = Double.parseDouble(etValue4.getText().toString());
        extractedValues[4] = Double.parseDouble(etValue5.getText().toString());
        extractedValues[5] = Double.parseDouble(etValue6.getText().toString());
        extractedValues[6] = Double.parseDouble(etValue7.getText().toString());
        extractedValues[7] = Double.parseDouble(etValue8.getText().toString());
        extractedValues[8] = Double.parseDouble(etValue9.getText().toString());
        extractedValues[9] = Double.parseDouble(etValue10.getText().toString());
        return extractedValues;
    }

    private HashMap<Double, ArrayList<Integer>> makeDelayMap(double[] extractedValues) {
        HashMap<Double, ArrayList<Integer>> delayMap = new HashMap<>();

        for (int i = 0; i < 10; i++) {
            double d = extractedValues[i];

            if (d == -1) {
                continue;
            }

            if (delayMap.containsKey(d)) {
                delayMap.get(d).add(i);
            } else {
                delayMap.put(d, new ArrayList<Integer>());
                delayMap.get(d).add(i);
            }
        }

        return delayMap;
    }

    private int[] getPinsFor(Integer[] rockets) {
        int[] pins = new int[rockets.length];

        for (int i = 0; i < rockets.length; i++) {
            pins[i] = Model.pinMapping.get(rockets[i]);
        }

        return pins;
    }
}
