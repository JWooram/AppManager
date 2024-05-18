package com.example.spoting;
//implements OnMapReadyCallback, GoogleMap.OnMapLongClickListener
import android.Manifest;
import android.annotation.SuppressLint;
import android.app.PendingIntent;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.location.Location;
import android.os.Build;
import android.os.Bundle;

import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.maps.CameraUpdateFactory;
import com.google.android.gms.maps.GoogleMap;
import com.google.android.gms.maps.MapView;
import com.google.android.gms.maps.OnMapReadyCallback;
import com.google.android.gms.maps.model.CircleOptions;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.maps.model.MarkerOptions;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import androidx.annotation.NonNull;
public class MapFragment extends Fragment{

    private static final String TAG = "MapsFragment";
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER


    private GoogleMap mMap;
    MapView mView;
    private GeofencingClient geofencingClient;
//    private GeofenceHelper geofenceHelper;
    private FusedLocationProviderClient fusedLocationClient;

    private float GEOFENCE_RADIUS = 150;
    private String GEOFENCE_ID = "SOME_GEOFENCE_ID";

    private int FINE_LOCATION_ACCESS_REQUEST_CODE = 10001;
    private int BACKGROUND_LOCATION_ACCESS_REQUEST_CODE = 10002;


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        ViewGroup rootView = (ViewGroup)inflater.inflate(R.layout.fragment_map, container, false);

//        mView = rootView.findViewById(R.id.mapView);
//        mView.onCreate(savedInstanceState);
//        mView.getMapAsync((OnMapReadyCallback) this);

        return rootView;
    }


//
//    @SuppressLint("MissingPermission")
//    private void getLastLocation() {
//        Task<Location> locationTask = fusedLocationClient.getLastLocation();
//
//        locationTask.addOnSuccessListener(new OnSuccessListener<Location>() {
//            @Override
//            public void onSuccess(Location location) {
//                if (location != null) {
//                    Log.d(TAG, "onSuccess: " + location.getLongitude());
//                    Log.d(TAG, "onSuccess: " + location.getLongitude());
//                } else {
//                    Log.d(TAG, "find your location..");
//                }
//            }
//        });
//        locationTask.addOnFailureListener(new OnFailureListener() {
//            @Override
//            public void onFailure(@NonNull Exception e) {
//                Log.d(TAG, "onFailure..");
//            }
//        });
//    }
//
//
//
//    private void enableUserLocation() {
//        if (ContextCompat.checkSelfPermission(requireContext(), Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//            mMap.setMyLocationEnabled(true);
//        } else {
//            //Ask for permission
//            if (shouldShowRequestPermissionRationale(Manifest.permission.ACCESS_FINE_LOCATION)) {
//                //We need to show user a dialog for displaying why the permission is needed and then ask for the permission...
//                requestPermissions(new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, FINE_LOCATION_ACCESS_REQUEST_CODE);
//            } else {
//                requestPermissions(new String[] {Manifest.permission.ACCESS_FINE_LOCATION}, FINE_LOCATION_ACCESS_REQUEST_CODE);
//            }
//        }
////    }
//
//    @SuppressLint("MissingPermission")
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        if (requestCode == FINE_LOCATION_ACCESS_REQUEST_CODE) {
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                //We have the permission
//                mMap.setMyLocationEnabled(true);
//            } else {
//                //We do not have the permission..
//
//            }
//        }
//
//        if (requestCode == BACKGROUND_LOCATION_ACCESS_REQUEST_CODE) {
//            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//                //We have the permission
//                Toast.makeText(getContext(), "You can add geofences...", Toast.LENGTH_SHORT).show();
//            } else {
//                //We do not have the permission..
//                Toast.makeText(getContext(), "Background location access is neccessary for geofences to trigger...", Toast.LENGTH_SHORT).show();
//            }
//        }
//    }


//    @SuppressLint("MissingPermission")
//    private void addGeofence(LatLng latLng, float radius) {
//
//        Geofence geofence = geofenceHelper.getGeofence(GEOFENCE_ID, latLng, radius, Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_DWELL | Geofence.GEOFENCE_TRANSITION_EXIT);
//        GeofencingRequest geofencingRequest = geofenceHelper.getGeofencingRequest(geofence);
//        PendingIntent pendingIntent = geofenceHelper.getPendingIntent();
//
//        geofencingClient.addGeofences(geofencingRequest, pendingIntent)
//                .addOnSuccessListener(new OnSuccessListener<Void>() {
//                    @Override
//                    public void onSuccess(Void aVoid) {
//                        Log.d(TAG, "onSuccess: Geofence Added...");
//                    }
//                })
//                .addOnFailureListener(new OnFailureListener() {
//                    @Override
//                    public void onFailure(@NonNull Exception e) {
//                        String errorMessage = geofenceHelper.getErrorString(e);
//                        Log.d(TAG, "onFailure: " + errorMessage);
//                    }
//                });
//    }

    @SuppressLint("MissingPermission")
    public void onMapReady(GoogleMap googleMap) {
        mMap = googleMap;
        Task<Location> locationTask = fusedLocationClient.getLastLocation();

        locationTask.addOnSuccessListener(new OnSuccessListener<Location>() {
            @Override
            public void onSuccess(Location location) {
                if (location != null) {
                    LatLng latLng = new LatLng(location.getLatitude(), location.getLongitude());
                    mMap.moveCamera(CameraUpdateFactory.newLatLngZoom(latLng, 15));
                } else {
                    Log.d(TAG, "find your location..");
                }
            }
        });
//        enableUserLocation();
//        mMap.setOnMapLongClickListener(this);
    }
//
//
//    private void handleMapLongClick(LatLng latLng) {
//        mMap.clear();
//        addMarker(latLng);
//        addCircle(latLng, GEOFENCE_RADIUS);
//        addGeofence(latLng, GEOFENCE_RADIUS);
//    }
//
//    @Override
//    public void onMapLongClick(LatLng latLng) {
//        if (Build.VERSION.SDK_INT >= 29) {
//            //We need background permission
//            if (ContextCompat.checkSelfPermission(requireContext(), android.Manifest.permission.ACCESS_BACKGROUND_LOCATION) == PackageManager.PERMISSION_GRANTED) {
//                handleMapLongClick(latLng);
//            } else {
//                if (shouldShowRequestPermissionRationale(android.Manifest.permission.ACCESS_BACKGROUND_LOCATION)) {
//                    //We show a dialog and ask for permission
//                    requestPermissions(new String[] {android.Manifest.permission.ACCESS_BACKGROUND_LOCATION}, BACKGROUND_LOCATION_ACCESS_REQUEST_CODE);
//                } else {
//                    requestPermissions(new String[] {Manifest.permission.ACCESS_BACKGROUND_LOCATION}, BACKGROUND_LOCATION_ACCESS_REQUEST_CODE);
//                }
//            }
//
//        } else {
//            handleMapLongClick(latLng);
//        }
//
//    }
//
//    private void addMarker(LatLng latLng) {
//        MarkerOptions markerOptions = new MarkerOptions().position(latLng);
//        mMap.addMarker(markerOptions);
//    }
//
//    private void addCircle(LatLng latLng, float radius) {
//        CircleOptions circleOptions = new CircleOptions();
//        circleOptions.center(latLng);
//        circleOptions.radius(radius);
//        circleOptions.strokeColor(Color.argb(255, 255, 0,0));
//        circleOptions.fillColor(Color.argb(64, 255, 0,0));
//        circleOptions.strokeWidth(4);
//        mMap.addCircle(circleOptions);
//    }
}