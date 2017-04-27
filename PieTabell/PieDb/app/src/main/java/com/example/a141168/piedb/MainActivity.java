package com.example.a141168.piedb;

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

    public void lagreData(View v) {
        Intent nyIntent =
                new Intent(getApplicationContext(), DatabaseActivity.class);
        startActivity(nyIntent);
    }
    public void visData(View v) {
        Intent nyIntent =
                new Intent(getApplicationContext(), ShowPiesActivity.class);
        startActivity(nyIntent);
    }
}
