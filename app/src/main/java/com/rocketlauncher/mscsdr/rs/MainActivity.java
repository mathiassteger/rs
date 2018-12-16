package com.rocketlauncher.mscsdr.rs;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
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
