package com.example.a210144.sensorer;

import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.ListView;

import java.util.List;

public class SensorListeActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor_liste2);
        SensorManager sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
        ListView sensorListView = (ListView) findViewById (R.id.sensorListView);
        List<Sensor> alleSensorer = sensorManager.getSensorList(Sensor.TYPE_ALL);
        sensorListView.setAdapter(new ArrayAdapter<Sensor>(SensorListeActivity.this, android.R.layout.simple_list_item_1, alleSensorer));
    }
}
