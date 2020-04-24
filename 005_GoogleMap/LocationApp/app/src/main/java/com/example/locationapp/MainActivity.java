package com.example.locationapp;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.os.Looper;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.common.api.ResolvableApiException;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.location.LocationSettingsRequest;
import com.google.android.gms.location.LocationSettingsResponse;
import com.google.android.gms.location.LocationSettingsStatusCodes;
import com.google.android.gms.location.SettingsClient;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.BitmapDescriptorFactory;
import com.google.android.gms.maps.model.GroundOverlayOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.maps.model.PointOfInterest;
import com.google.android.gms.maps.model.Polyline;
import com.google.android.gms.maps.model.PolylineOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;

import java.io.IOException;
import java.util.List;

public class MainActivity extends AppCompatActivity implements OnMapReadyCallback{

    private static final int LOCATION_PERMISSION_REQUEST = 100;
    private static final int CHECK_LOCATION_SETTINGS_REQUEST = 101;

    private FusedLocationProviderClient fusedLocationProviderClient;
    private LocationRequest locationRequest;
    private Geocoder geocoder;

    private TextView lngTextView;
    private TextView latTextView;
    private TextView addressTextView;

    private GoogleMap mMap;
    private Marker mUserMarker;
    private Polyline mRoute;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        lngTextView = findViewById(R.id.lng_textview);
        latTextView = findViewById(R.id.lat_textview);
        addressTextView = findViewById(R.id.address_textview);

        fusedLocationProviderClient = LocationServices.getFusedLocationProviderClient(this);
        locationRequest = getLocationRequest();
        geocoder = new Geocoder(this);

        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager()
                .findFragmentById(R.id.map);

        mapFragment.getMapAsync(this);

    }

    @Override
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        mMap.getUiSettings().setMapToolbarEnabled(true);
        mMap.setOnPoiClickListener(new GoogleMap.OnPoiClickListener() {
            @Override
            public void onPoiClick(PointOfInterest poi) {

                Toast.makeText(getApplicationContext(), "Clicked: " +
                                poi.name + "\nPlace ID:" + poi.placeId +
                                "\nLatitude:" + poi.latLng.latitude +
                                " Longitude:" + poi.latLng.longitude,
                        Toast.LENGTH_SHORT).show();
            }
        });

        // Add a marker in Sydney, Australia, and move the camera.
        LatLng islamicUniv = new LatLng(31.514958, 34.441759);
        mUserMarker = mMap.addMarker(new MarkerOptions().position(islamicUniv)
                                            .title("Moving...")
                                        .icon(BitmapDescriptorFactory.fromResource(R.drawable.location)));

        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(islamicUniv, 13));
        mRoute = mMap.addPolyline(new PolylineOptions());
        mMap.addGroundOverlay(new GroundOverlayOptions()
                .position(islamicUniv, 600, 800)
                .image(BitmapDescriptorFactory.fromResource(R.drawable.googleg_standard_color_18)));
    }


    @Override
    protected void onStart() {
        super.onStart();

        if(checkLocationPermission()){
            getLastKnownLocation();
            checkUserLocationSettings();
        }
    }

    private boolean checkLocationPermission(){

        if (ContextCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION)
                != PackageManager.PERMISSION_GRANTED) {

            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
                    LOCATION_PERMISSION_REQUEST);

        } else {
            // Permission has already been granted
            return true;
        }

        return false;
    }

    @Override
    public void onRequestPermissionsResult(int requestCode,
                                           String[] permissions, int[] grantResults) {
        switch (requestCode) {
            case LOCATION_PERMISSION_REQUEST: {
                // If request is cancelled, the result arrays are empty.
                if (grantResults.length > 0
                        && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                    // permission was granted
                    getLastKnownLocation();
                    checkUserLocationSettings();
                }
                return;
            }
        }
    }

    @Override
    protected void onStop() {
        super.onStop();
        stopTracking();
    }

    private void getLastKnownLocation(){
        Task<Location> task = fusedLocationProviderClient.getLastLocation();
        task.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {

                if(location != null){
                    updateUI(location);
                }
            }
        });
    }

    private void startTracking(){
        fusedLocationProviderClient.requestLocationUpdates(locationRequest, mLocationCallback, Looper.getMainLooper());
    }

    private void stopTracking(){
        fusedLocationProviderClient.removeLocationUpdates(mLocationCallback);
    }

    private LocationRequest getLocationRequest() {
        LocationRequest locationRequest = new LocationRequest();
        locationRequest.setInterval(1000);
        locationRequest.setFastestInterval(500);
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        return locationRequest;
    }

    private LocationCallback mLocationCallback = new LocationCallback(){
        @Override
        public void onLocationResult(LocationResult locationResult) {
            super.onLocationResult(locationResult);

            Location location = locationResult.getLastLocation();
            if(location != null){
                updateUI(location);
            }
        }
    };

    private void updateUI(Location location){
        lngTextView.setText(getString(R.string.lng_text, location.getLongitude()));
        latTextView.setText(getString(R.string.lat_text, location.getLatitude()));
        addressTextView.setText(getString(R.string.address_text, getAddressLine(location)));
        mUserMarker.setPosition(new LatLng(location.getLatitude(), location.getLongitude()));
        List<LatLng> points = mRoute.getPoints();
        points.add(new LatLng(location.getLatitude(), location.getLongitude()));
        mRoute.setPoints(points);

    }

    private String getAddressLine(Location location){

        String addressLine = "";
        try {
            List<Address> addressList = geocoder.getFromLocation(location.getLatitude(), location.getLongitude(), 1);
            if(!addressList.isEmpty()){
                Address address = addressList.get(0);
                addressLine =  address.getAddressLine(0);
            }
            return addressLine;
        } catch (IOException e) {
            e.printStackTrace();
            return addressLine;
        }
    }

    private void checkUserLocationSettings(){

        LocationSettingsRequest settingsRequest = new LocationSettingsRequest.Builder()
                .addLocationRequest(locationRequest).build();

        SettingsClient client = LocationServices.getSettingsClient(this);
        Task<LocationSettingsResponse> task = client.checkLocationSettings(settingsRequest);
        task.addOnSuccessListener(new OnSuccessListener<LocationSettingsResponse>() {
            @Override
            public void onSuccess(LocationSettingsResponse locationSettingsResponse) {
                    startTracking();
            }
        });

        task.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                int statusCode = ((ApiException) e).getStatusCode();
                if (statusCode == LocationSettingsStatusCodes.RESOLUTION_REQUIRED) {
                    // Location settings are not satisfied, but this can
                    // be fixed by showing the user a dialog
                    try {
                        // Show the dialog by calling
                        // startResolutionForResult(), and check the
                        // result in onActivityResult()
                        ResolvableApiException resolvable = (ResolvableApiException) e;
                        resolvable.startResolutionForResult(MainActivity.this, CHECK_LOCATION_SETTINGS_REQUEST);
                    } catch (IntentSender.SendIntentException sendEx) {
                        // Ignore the error
                    }
                }
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CHECK_LOCATION_SETTINGS_REQUEST) {
            if (resultCode == RESULT_CANCELED) {
                stopTracking();
            } else if (resultCode == RESULT_OK) {
                startTracking();
            }
        }
    }


}
