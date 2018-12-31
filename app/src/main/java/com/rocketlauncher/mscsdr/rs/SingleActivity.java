package com.rocketlauncher.mscsdr.rs;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.rocketlauncher.mscsdr.rs.json.Launchinfo;

public class SingleActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_single);
        Model.getInstance().currentContext = this;
    }

    public void on1(View view) {
        Launchinfo launchinfo = new Launchinfo(new int[]{8}, 0);
        Model.getInstance().send(Model.getInstance().gson.toJson(launchinfo).getBytes());
    }

    public void on2(View view) {
        Launchinfo launchinfo = new Launchinfo(new int[]{10}, 0);
        Model.getInstance().send(Model.getInstance().gson.toJson(launchinfo).getBytes());
    }

    public void on3(View view) {
        Launchinfo launchinfo = new Launchinfo(new int[]{12}, 0);
        Model.getInstance().send(Model.getInstance().gson.toJson(launchinfo).getBytes());
    }

    public void on4(View view) {
        Launchinfo launchinfo = new Launchinfo(new int[]{16}, 0);
        Model.getInstance().send(Model.getInstance().gson.toJson(launchinfo).getBytes());
    }

    public void on5(View view) {
        Launchinfo launchinfo = new Launchinfo(new int[]{18}, 0);
        Model.getInstance().send(Model.getInstance().gson.toJson(launchinfo).getBytes());
    }

    public void on6(View view) {
        Launchinfo launchinfo = new Launchinfo(new int[]{22}, 0);
        Model.getInstance().send(Model.getInstance().gson.toJson(launchinfo).getBytes());
    }

    public void on7(View view) {
        Launchinfo launchinfo = new Launchinfo(new int[]{24}, 0);
        Model.getInstance().send(Model.getInstance().gson.toJson(launchinfo).getBytes());
    }

    public void on8(View view) {
        Launchinfo launchinfo = new Launchinfo(new int[]{26}, 0);
        Model.getInstance().send(Model.getInstance().gson.toJson(launchinfo).getBytes());
    }

    public void on9(View view) {
        Launchinfo launchinfo = new Launchinfo(new int[]{28}, 0);
        Model.getInstance().send(Model.getInstance().gson.toJson(launchinfo).getBytes());
    }

    public void on10(View view) {
        Launchinfo launchinfo = new Launchinfo(new int[]{32}, 0);
        Model.getInstance().send(Model.getInstance().gson.toJson(launchinfo).getBytes());
    }
}