package org.qtproject.example.mapview;

import androidx.annotation.NonNull;
import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.PlacesClient;
import org.qtproject.qt.android.bindings.QtActivity;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class MapActivity extends QtActivity{

    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 101;
    private static final int DEFAULT_ZOOM = 15;
    private MapView mapView;
    private GoogleMap mMap;
    private boolean locationPermissionGranted = false;
    private FusedLocationProviderClient fusedLocationClient;
    private Location currentLocation;
    private PlacesClient placesClient;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        getLocationPermission();
        initializeMap();
    }

    @Override
    protected void onStart() {
        super.onStart();
        mapView.onStart();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        mapView.onSaveInstanceState(outState);
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onStop() {
        super.onStop();
        mapView.onStop();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mapView.onDestroy();
    }

    @SuppressLint({"MissingPermission", "ResourceType"})
    public void initializeMap() {

        mapView = new MapView(this);
        ((ViewGroup) getWindow().getDecorView().getRootView()).addView(mapView);
        final FrameLayout.LayoutParams LayoutParams = new FrameLayout.LayoutParams(
                            FrameLayout.LayoutParams.MATCH_PARENT,
                            FrameLayout.LayoutParams.MATCH_PARENT);
                    mapView.setLayoutParams(LayoutParams);
        //            mviewGroup.addView(mapView);
                    LayoutParams.setMargins(0, 200, 0, 200);
        mapView.onCreate(Bundle.EMPTY);

        mapView.getMapAsync(googleMap -> {
            mMap = googleMap;
            //mMap.getUiSettings().setMyLocationButtonEnabled(false);
            if(locationPermissionGranted) {
                mMap.setMyLocationEnabled(true);
                fusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> {
                    if (location != null) {
                        currentLocation = location;
                        mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(new LatLng(location.getLatitude(), location.getLongitude()), DEFAULT_ZOOM));
                    }
                });
            }

            mMap.setOnMapClickListener(point -> {
                mMap.clear();
                mMap.addMarker(new MarkerOptions().position(point)).setDraggable(true);
                getLocationData(point);
            });

            mMap.setOnMarkerDragListener(new GoogleMap.OnMarkerDragListener() {

                @Override
                public void onMarkerDragEnd(@NonNull Marker marker) {
                    getLocationData(marker.getPosition());
                }
                @Override
                public void onMarkerDrag(@NonNull Marker marker) {}
                @Override
                public void onMarkerDragStart(@NonNull Marker marker) {}
            });

            mMap.setOnMyLocationButtonClickListener(() ->{
                fusedLocationClient.getLastLocation().addOnSuccessListener(this, location -> {
                    if (location != null) {
                        currentLocation = location;
                    }
                });
                return false;
            });
        });

        Places.initialize(this, "AIzaSyAJSOe2hHyMrYe0o4p0MjokdYQmyqb--ZY");
        placesClient = Places.createClient(this);
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

    }

    private HashMap<String, String> getLocationData(LatLng point) {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(this, Locale.getDefault());
        try {
            addresses = geocoder.getFromLocation(point.latitude, point.longitude, 1);
            if(addresses.size() > 0) {
                String address = addresses.get(0).getAddressLine(0);
                String city = addresses.get(0).getLocality();
                String state = addresses.get(0).getAdminArea();
                String country = addresses.get(0).getCountryName();
                String postalCode = addresses.get(0).getPostalCode();
                String knownName = addresses.get(0).getFeatureName();
                Log.d("TAG", address + " : " + city + " : " + country);
            }
            return null;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    private void getLocationPermission() {
        if (checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true;
        } else {
            requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }
}
