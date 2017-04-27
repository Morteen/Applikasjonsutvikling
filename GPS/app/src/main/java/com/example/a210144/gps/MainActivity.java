package com.example.a210144.gps;

import android.Manifest;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import java.io.IOException;
import java.util.List;

// Bruker AppComatActivity for å være kompatibel før Android 6.0 API 23
public class MainActivity extends AppCompatActivity {

    public final static int MY_REQUEST_LOCATION = 1;

    TextView mDisplayLengdeTextView = null;
    TextView mDisplayBreddeTextView = null;
    TextView displayAdrView = null;
    Spinner adresseListeSpinner = null;
    Button kartButton = null;

    LocationManager locationManager;
    Location myLocation = null;
    String locationProvider = LocationManager.GPS_PROVIDER;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDisplayLengdeTextView = (TextView) findViewById(R.id.displayLengdeView);
        mDisplayBreddeTextView = (TextView) findViewById(R.id.displayBreddeView);
        kartButton = (Button) findViewById(R.id.kart_button);
        displayAdrView=(TextView)findViewById(R.id.displayAdresseView);
        adresseListeSpinner=(Spinner)findViewById(R.id.spinnerAdresser);


    }

    public void onStart() {
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
                locationManager.requestLocationUpdates(locationProvider, 500, 0, locationListntener);
            }
        } else {
            Toast.makeText(getApplicationContext(),
                    "Aktiver " + locationProvider + " under Location i Settings", Toast.LENGTH_LONG).show();
        }
        super.onStart();
    }


    LocationListener locationListntener = new LocationListener() {

        @Override
        public void onLocationChanged(Location location) {
            myLocation = location;
            visPosition(myLocation);
        }


        @Override
        public void onStatusChanged(String provider, int status, Bundle extras) {

        }

        @Override
        public void onProviderEnabled(String provider) {

        }

        @Override
        public void onProviderDisabled(String provider) {

        }

        @Override
        public int hashCode() {
            return super.hashCode();
        }

        public void onlocationChanged() {

        }
    };


    @Override
    public void onStop() {
        if (locationManager.isProviderEnabled(locationProvider)) {
            locationManager.removeUpdates(locationListntener);
        }
        super.onStop();
    }


    // Callback-metode som kalles når bruker har svart på spørsmål om tillatelse
    @Override
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == MY_REQUEST_LOCATION) {
            // Hvis bruker avviser tillatelsen vil arrayen grantResults være tom.
            if (grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                try {
                    // Tillatelse er gitt, ok å lese siste posisjon
                    myLocation = locationManager.getLastKnownLocation(locationProvider);
                    visPosition(myLocation);
                } catch (SecurityException e) {
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
            String breddeGradString = Location.convert(location.getLatitude(), Location.FORMAT_SECONDS) + " Nord";
            String lengdeGradString = Location.convert(location.getLongitude(), Location.FORMAT_SECONDS) + " Øst";
            mDisplayBreddeTextView.setText(breddeGradString);
            mDisplayLengdeTextView.setText(lengdeGradString);
        }
    }

    // Lyttemetode for knappen Vis i kart
    public void visIKart(View v) {
        // Lyttemetode for knappen "Vis i kart" (R.id.kart_button)

        if (myLocation != null) {
            String geoURL = "geo:" + myLocation.getLatitude() + ","
                    + myLocation.getLongitude() + "?z=14";
            Uri geoURI = Uri.parse(geoURL);
            Intent geoMap = new Intent(Intent.ACTION_VIEW, geoURI);
            startActivity(geoMap);

        }
    }

    // Lyttemetode for knappen Finn adresse
    public void finnAdresser(View v) {
        if (myLocation != null) {
            Geocoder coder = new Geocoder(getApplicationContext());
            List<Address> geocodeResults = null;
            try {
                if (coder.isPresent()) {
                    geocodeResults = coder.getFromLocation(myLocation.getLatitude(), myLocation.getLongitude(), 5);
                    if (geocodeResults != null && geocodeResults.size() > 0) {
                        Address adresse = geocodeResults.get(0);
                        displayAdrView.setText(fullAdresse(adresse));
// Legg alle adressene inn i Spinneren
                        adresseListeSpinner.setAdapter(new ArrayAdapter<Address>(
                                this, android.R.layout.simple_spinner_item, geocodeResults));
                    } else {
                        displayAdrView.setText("Ingen svar fra geoCoder." + geocodeResults.size());
                    }
                } else {
                    displayAdrView.setText("Ingen geokoder tilgjengelig.");
                }
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }


    private String fullAdresse(Address a) {
        String adresse = "";
        if (a != null) {
            for (int i = 0; i <= a.getMaxAddressLineIndex(); i++)
                adresse += (a.getAddressLine(i) + " ");
        }
        return adresse;
    }
}



