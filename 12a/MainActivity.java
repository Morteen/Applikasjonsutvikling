package com.example.jon.geo_demo;

import android.Manifest;
import android.content.Context;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


// Bruker AppComatActivity for å være kompatibel før Android 6.0 API 23
public class MainActivity extends AppCompatActivity {

    public final static int MY_REQUEST_LOCATION = 1;

    TextView mDisplayLengdeTextView = null;
    TextView mDisplayBreddeTextView = null;
    TextView displayAdrView = null;
    Spinner adresseListeSpinner = null;
    Button kartButton = null;

    LocationManager locationManager;
    Location myLocation=null;
    String locationProvider = LocationManager.GPS_PROVIDER;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDisplayLengdeTextView = (TextView) findViewById(R.id.displayLengdeView);
        mDisplayBreddeTextView = (TextView) findViewById(R.id.displayBreddeView);

        // Bruk LocationManager for å finne siste kjente posisjon
        locationManager = (LocationManager) this.getSystemService(Context.LOCATION_SERVICE);
        if (locationManager.isProviderEnabled(locationProvider)) {
            //** Må sjekke om bruker har gitt tillatelse til å bruke GPS
            // Denne fungerer også før API 23 med AppCompatActivity:
            int permissionCheck = ContextCompat.checkSelfPermission(getApplicationContext(), Manifest.permission.ACCESS_FINE_LOCATION);
            // Denne fungerer bare fra og med  API 23 med Activity:
            //int permissionCheck = getPackageManager().checkPermission(Manifest.permission.ACCESS_FINE_LOCATION, getPackageName());
            if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
                //** Hvis tillatelse ikke er gitt må programmet spørre brukeren
                // Denne fungerer også før API 23 med AppCompatActivity:
                ActivityCompat.requestPermissions(this, new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_REQUEST_LOCATION);
                // Denne fungerer bare fra og med  API 23 med Activity:
                //this.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, MY_REQUEST_LOCATION);
            } else {
                // Hent siste kjente posisjon
                myLocation = locationManager.getLastKnownLocation(locationProvider);
                visPosition(myLocation);
            }
        } else {
            Toast.makeText(getApplicationContext(),
                    "Aktiver " + locationProvider + " under Location i Settings", Toast.LENGTH_LONG).show();
        }
    }

    // Callback-metode som kalles når bruker har svart på spørsmål om tillatelse
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == MY_REQUEST_LOCATION) {
            // Hvis bruker avviser tillatelsen vil arrayen grantResults være tom.
            if(grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                try {
                    // Tillatelse er gitt, ok å lese siste posisjon
                    myLocation = locationManager.getLastKnownLocation(locationProvider);
                    visPosition(myLocation);
                }
                catch (SecurityException e) {
                    e.printStackTrace();
                }
            } else {
                // Bruker avviste spørsmål om tillatelse
                Toast.makeText(getApplicationContext(),
                        "Kan ikke vise posisjon uten brukertillatelse.", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void visPosition(Location location) {
        if (location != null) {
            String breddeGradString = Location.convert(location.getLatitude(),Location.FORMAT_SECONDS) + " Nord";
            String lengdeGradString = Location.convert(location.getLongitude(),Location.FORMAT_SECONDS)+ " Øst";
            mDisplayBreddeTextView.setText(breddeGradString);
            mDisplayLengdeTextView.setText(lengdeGradString);
        }
    }

    // Lyttemetode for knappen Vis i kart
    public void visIKart(View v) {
        Toast.makeText(getApplicationContext(), "Ikke implementert ennå.", Toast.LENGTH_LONG).show();
    }

    // Lyttemetode for knappen Finn adresse
    public void finnAdresser(View v) {
        Toast.makeText(getApplicationContext(), "Ikke implementert ennå.", Toast.LENGTH_LONG).show();
    }

}
