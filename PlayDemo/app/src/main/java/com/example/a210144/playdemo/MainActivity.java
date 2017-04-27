package com.example.a210144.playdemo;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;


import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.location.LocationListener;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResult;

import java.util.Date;


// Bruk av Google Play Services for Location API
public class MainActivity extends AppCompatActivity
        implements GoogleApiClient.ConnectionCallbacks,
                   GoogleApiClient.OnConnectionFailedListener,LocationListener
{

    private TextView mDisplayLengdeTextView = null;
    private TextView mDisplayBreddeTextView = null;
    private TextView mDisplayTimeTextView = null;
    private TextView mDisplayAccuracyTextView = null;
    LocationRequest mLocationRequest =null;
    private boolean mRequestingLocationUpdates = false;

    private Location myLocation = null;

    // Klient som kommuniserer med Google Play API
    private GoogleApiClient mGoogleApiClient = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mDisplayLengdeTextView   = (TextView) findViewById(R.id.displayLengdeView);
        mDisplayBreddeTextView   = (TextView) findViewById(R.id.displayBreddeView);
        mDisplayAccuracyTextView = (TextView) findViewById(R.id.displayAccuracyView);
        mDisplayTimeTextView     = (TextView) findViewById(R.id.displayTimeView);

        // Create an instance of GoogleAPIClient.
        if (mGoogleApiClient == null) {
            GoogleApiClient.Builder apiBuilder = new GoogleApiClient.Builder(this);
            apiBuilder.addConnectionCallbacks(this);        /* ConnectionCallbacks-objekt */
            apiBuilder.addOnConnectionFailedListener(this); /* OnConnectionFailedListener-objekt */
            apiBuilder.addApi(LocationServices.API);        /* Velg Play Service API */
            mGoogleApiClient = apiBuilder.build();
        }
    }
    @Override
    public void onConnectionFailed(ConnectionResult result) {
        // An unresolvable error has occurred and a connection to Google APIs
        // could not be established. Display an error message, or handle the failure silently
        Toast.makeText(getApplicationContext(),
                "Klarte ikke å koble til Google Play Services", Toast.LENGTH_LONG).show();
    }



    public final static int REQUEST_LOCATION = 1;
    // Implementasjon av metode fra interface GoogleApiClient.ConnectionCallbacks
   @Override
    public void onConnected(Bundle connectionHint) {
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            ActivityCompat.requestPermissions(this, new String[]{
                    Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
        } else {
            mLocationRequest = new LocationRequest();
            mLocationRequest.setInterval(5000);
            mLocationRequest.setFastestInterval(1000);
            mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
            builder.addLocationRequest(mLocationRequest);
            //LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
            PendingResult<LocationSettingsResult> result= LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient,builder.build());

            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest,  this);

            //mRequestingLocationUpdates=true;
        }
    }


    public void onLocationChanged(Location location) {
        myLocation = location;
        visPosisjon(myLocation);
    }
    @Override
    protected void onPause() {
        if (mRequestingLocationUpdates) {
            LocationServices.FusedLocationApi.removeLocationUpdates(mGoogleApiClient,  this);
            mRequestingLocationUpdates = false;
        }
        super.onPause();
    }
    @Override
    public void onResume() {
        super.onResume();
        if (mGoogleApiClient.isConnected() && !mRequestingLocationUpdates) {
            try {
                LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest,  this);
                mRequestingLocationUpdates=true;
            } catch (SecurityException e) { e.printStackTrace(); }
        }
    }



    @Override
    protected void onStart() {
        super.onStart();
        // Connect the ApiClient to Google Services
        mGoogleApiClient.connect();
    }

    @Override
    protected void onStop() {
        // Disconnect the ApiClient
        mGoogleApiClient.disconnect();
        super.onStop();
    }


    // Implementasjon av metode fra interface GoogleApiClient.ConnectionCallbacks
    // Kalles når Api-klienten har fått kontakt
   // public final static int REQUEST_LOCATION = 1;
   /* @Override
    public void onConnected(Bundle connectionHint) {
        // Denne fungerer også før API 23 med AppCompatActivity:
        int permissionCheck = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION);
        // Denne fungerer bare fra og med  API 23 med Activity:
        //int permissionCheck = this.getPackageManager().checkPermission(Manifest.permission.ACCESS_FINE_LOCATION, getPackageName());
        if (permissionCheck != PackageManager.PERMISSION_GRANTED) {
            //** Spør bruker om å gi appen tillatelsen ACCESS_FINE_LOCATION
            // Denne fungerer også før API 23 med AppCompatActivity:
            ActivityCompat.requestPermissions(this,new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, REQUEST_LOCATION);
            // Denne fungerer bare fra og med  API 23 med Activity:
            //this.requestPermissions(new String[]{Manifest.permission.ACCESS_FINE_LOCATION}, 1);
        } else {
            // mLocationRequest = new LocationRequest();
            mLocationRequest.setInterval(5000);
            mLocationRequest.setFastestInterval(1000);
            mLocationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
            LocationSettingsRequest.Builder builder = new LocationSettingsRequest.Builder();
            builder.addLocationRequest(mLocationRequest);
            //LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, this);
            PendingResult<LocationSettingsResult> result= LocationServices.SettingsApi.checkLocationSettings(mGoogleApiClient,builder.build());

            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest,  this);

        }
    }*/

    @Override
    public void onConnectionSuspended(int i) {

    }

    // Callbackmetode som kalles etter at bruker har svart på spørsmål om rettigheter
    public void onRequestPermissionsResult(int requestCode, String[] permissions, int[] grantResults) {
        if (requestCode == REQUEST_LOCATION) {
            if(grantResults.length == 1 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                try {
                    myLocation = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);
                    this.visPosisjon(myLocation);
                }
                catch (SecurityException e) {
                    e.printStackTrace();
                }
            } else {
                // Permission was denied or request was cancelled
                Toast.makeText(getApplicationContext(),
                        "Kan ikke vise posisjon uten tillatelse", Toast.LENGTH_LONG).show();
            }
        }
    }

    private void visPosisjon(Location posisjon) {
        if (posisjon != null) {
            String lengdeGradString = Location.convert(posisjon.getLongitude(), Location.FORMAT_SECONDS);
            String breddeGradString = Location.convert(posisjon.getLatitude(), Location.FORMAT_SECONDS);

            mDisplayBreddeTextView.setText(lengdeGradString + " Nord");
            mDisplayLengdeTextView.setText(breddeGradString + " Øst");
            mDisplayAccuracyTextView.setText(posisjon.getAccuracy() + " meter");
            mDisplayTimeTextView.setText(new Date(posisjon.getTime()).toString());
        }
    }

    // Lyttemetode for knappen Vis i kart
    public void visIKart(View v) {
        if (myLocation != null) {
            String geoURL = "geo:" + myLocation.getLatitude() + ","
                    + myLocation.getLongitude() + "?z=14" ;
            Uri geoURI = Uri.parse(geoURL);
            Intent geoMap = new Intent(Intent.ACTION_VIEW, geoURI);
            startActivity(geoMap);
        }
    }
    // Lyttemetode for knappen Finn adresse
    public void finnAdresser(View v) {
        Toast.makeText(getApplicationContext(), "Ikke implementert ennå.", Toast.LENGTH_LONG).show();
    }

}

