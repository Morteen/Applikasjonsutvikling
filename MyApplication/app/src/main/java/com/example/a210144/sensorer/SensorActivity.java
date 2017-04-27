package com.example.a210144.sensorer;

import android.app.Activity;
import android.content.Context;
import android.hardware.Sensor;
import android.hardware.SensorEvent;
import android.hardware.SensorEventListener;
import android.hardware.SensorManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.widget.TextView;

public class SensorActivity extends Activity implements SensorEventListener {
    private SensorManager sensorManager;
    private Sensor sensor;
    private int sensorType = Sensor.TYPE_ACCELEROMETER;
    private String typeNavn=Sensor.STRING_TYPE_ACCELEROMETER;
    //private int sensorType = Sensor. TYPE_PROXIMITY ;
    private TextView textViewSensorVerdi, textViewSensorNavn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sensor);

        TextView textViewSensorNavn = (TextView) findViewById(R.id.textViewSensorNavn);
        TextView  textViewSensorVerdi = (TextView) findViewById(R.id.textViewSensorVerdi);
         sensorManager = (SensorManager) getSystemService(Context.SENSOR_SERVICE);
         sensor = sensorManager.getDefaultSensor(sensorType);
        if (sensor!=null)
            textViewSensorNavn.setText("Sensor: \n "+typeNavn );
        else
            textViewSensorNavn.setText("Sensoren er ikke tilgjengelig.");
    }


    protected void onResume() {
        super.onResume();
        if (sensor!=null)
            sensorManager.registerListener(this, sensor, SensorManager.SENSOR_DELAY_NORMAL);
    }
    protected void onPause() {
        super.onPause();
        if (sensor!=null) sensorManager.unregisterListener(this);
    }

    @Override
    public final void onSensorChanged(SensorEvent sensorMaaling) {
// Sensorer med èn måleverdi returnerer denne i første element i tabellen
        float sensorVerdi = sensorMaaling.values[0];
        textViewSensorVerdi=(TextView)findViewById(R.id.textViewSensorNavn);
        textViewSensorVerdi.setText("Sensorverdi:" + "\n " +
                "x=" + sensorMaaling.values[0] + " " +
                "y=" + sensorMaaling.values[1] + " " +
                "z=" + sensorMaaling.values[2]);
    }
    @Override
    public final void onAccuracyChanged(Sensor sensor, int accuracy) {
// Do something here if sensor accuracy changes.
        textViewSensorVerdi=(TextView)findViewById(R.id.textViewSensorVerdi);
        textViewSensorVerdi.setText("Sensoren endret nøyaktighet til" + accuracy + ".");
    }
}

