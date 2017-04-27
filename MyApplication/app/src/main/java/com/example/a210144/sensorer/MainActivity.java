package com.example.a210144.sensorer;

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


    public void knappeLytter(View v) {
        Class nyAktivitet = null;

        if (v == findViewById(R.id.buttonListe)) {
            nyAktivitet = SensorListeActivity.class;
        }
        if (v == findViewById(R.id.buttonSensorMaaling)) {
            nyAktivitet = SensorActivity.class;
        }
        if (v == findViewById(R.id.buttonKompass)) {
            //nyAktivitet = KompassActivity.class;
        }

        if (nyAktivitet != null) {
            Intent startIntent = new Intent(getApplicationContext(), nyAktivitet);
            startActivity(startIntent);
        }


    }


}
