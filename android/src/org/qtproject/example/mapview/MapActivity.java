package org.qtproject.example.mapview;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.pm.PackageManager;
import android.location.Address;
import android.location.Geocoder;
import android.location.Location;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.SupportMapFragment;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.Marker;
import com.google.android.gms.maps.model.MarkerOptions;
//import com.example.maps.databinding.ActivityMapsBinding;
import com.google.android.libraries.places.api.Places;
import com.google.android.libraries.places.api.net.PlacesClient;

import java.io.IOException;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

public class MapActivity {

    private static final int PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION = 101;
    private static final int DEFAULT_ZOOM = 15;
    private final Activity mactivity;
    private final ViewGroup mviewGroup;
    private MapView mapView;
    private GoogleMap mMap;
//    private ActivityMapsBinding binding;
    private boolean locationPermissionGranted = false;
    private FusedLocationProviderClient fusedLocationClient;
    private Location currentLocation;
    private PlacesClient placesClient;

    public MapActivity(Activity instance) {
        mactivity = instance;
        mviewGroup = (ViewGroup) mactivity.getWindow().getDecorView().getRootView();
        getLocationPermission();
    }

//    @Override
//    protected void onCreate(Bundle savedInstanceState) {
//        super.onCreate(savedInstanceState);
//        binding = ActivityMapsBinding.inflate(getLayoutInflater());
//        setContentView(binding.getRoot());
//        getLocationPermission();
//        SupportMapFragment mapFragment = (SupportMapFragment) getSupportFragmentManager().findFragmentById(R.id.map);
//        mapFragment.getMapAsync(this);
//        mapView = mactivity.findViewById(R.id.map_view);
//        mapView.getMapAsync(this);
//        mapView.onCreate(savedInstanceState);
//
//        Places.initialize(mactivity, mactivity.getString(R.string.maps_api_key));
//        placesClient = Places.createClient(mactivity);
//
//        fusedLocationClient = LocationServices.getFusedLocationProviderClient(mactivity);
//    }

    @SuppressLint("MissingPermission")
    public void initializeMap() {
        mactivity.runOnUiThread(() -> {
            mapView = new MapView(mactivity);
            mapView.onCreate(Bundle.EMPTY);

            final FrameLayout.LayoutParams LayoutParams = new FrameLayout.LayoutParams(
                    FrameLayout.LayoutParams.MATCH_PARENT,
                    FrameLayout.LayoutParams.MATCH_PARENT);
            mapView.setLayoutParams(LayoutParams);
//            mviewGroup.addView(mapView);
            LayoutParams.setMargins(0, 200, 0, 200);

            mviewGroup.addView(mapView);

            mapView.getMapAsync(googleMap -> {
                mMap = googleMap;
                //mMap.getUiSettings().setMyLocationButtonEnabled(false);
                if(locationPermissionGranted) {
                    mMap.setMyLocationEnabled(true);
                    fusedLocationClient.getLastLocation().addOnSuccessListener(mactivity, location -> {
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
                    fusedLocationClient.getLastLocation().addOnSuccessListener(mactivity, location -> {
                        if (location != null) {
                            currentLocation = location;
                        }
                    });
                    return false;
                });
            });
        });

        //mapView = mactivity.findViewById(R.id.map_view);
//        mapView.setLayoutParams(LayoutParams);
//        mviewGroup.addView(mapView);
        //mapView.getMapAsync(this);
        //mapView.onCreate(savedPrefs);

        Places.initialize(mactivity, "AIzaSyAJSOe2hHyMrYe0o4p0MjokdYQmyqb--ZY");
        placesClient = Places.createClient(mactivity);

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(mactivity);
    }

    private HashMap<String, String> getLocationData(LatLng point) {
        Geocoder geocoder;
        List<Address> addresses;
        geocoder = new Geocoder(mactivity, Locale.getDefault());

        try {
            addresses = geocoder.getFromLocation(point.latitude, point.longitude, 1);
            String address = addresses.get(0).getAddressLine(0);
            String city = addresses.get(0).getLocality();
            String state = addresses.get(0).getAdminArea();
            String country = addresses.get(0).getCountryName();
            String postalCode = addresses.get(0).getPostalCode();
            String knownName = addresses.get(0).getFeatureName();
            Log.d("TAG", address + " : " + city + " : " + country);
            return null;
        } catch (IOException e) {
            e.printStackTrace();
        }
        return null;
    }
    private void getLocationPermission() {
        if (mactivity.checkSelfPermission(android.Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            locationPermissionGranted = true;
        } else {
            mactivity.requestPermissions(new String[]{android.Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION}, PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
        }
    }
}
