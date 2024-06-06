package com.example.AppManager;

import static androidx.core.content.ContentProviderCompat.requireContext;
import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.os.Build;
import android.os.IBinder;
import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.core.app.NotificationCompat;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.Volley;
import com.example.AppManager.Request.GeofenceRequest;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingClient;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.gms.maps.model.LatLng;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class GeofenceService extends Service {
    private static final String TAG = "GeofenceService";

    private GeofencingClient geofencingClient;
    private GeofenceHelper geofenceHelper;
    private static final int REQUEST_CODE_LOCATION_PERMISSION = 1001;
    private static final float GEOFENCE_RADIUS_IN_METERS = 100.0f;
    private static final long DURATION = 5000L; // milliseconds



    private int FINE_LOCATION_ACCESS_REQUEST_CODE = 10001;
    private int BACKGROUND_LOCATION_ACCESS_REQUEST_CODE = 10002;
    private int notificationID = 1;
    private FusedLocationProviderClient fusedLocationClient;
    private static final String CHANNEL_ID = "GeofenceServiceChannel";
    @Override
    public void onCreate() {
        super.onCreate();
        Log.d(TAG, "지오펜스 서비스 생성됨");
        createNotificationChannel();
    }
    @SuppressLint("MissingPermission")
    private void startLocationUpdates() {
        Log.d(TAG, "startLocationUpdates 동작 중");
        LocationRequest locationRequest = LocationRequest.create();
        locationRequest.setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY);
        locationRequest.setInterval(5000); // 5초마다 업데이트 요청

        LocationCallback locationCallback = new LocationCallback() {
            @Override
            public void onLocationResult(LocationResult locationResult) {
                super.onLocationResult(locationResult);
                if (locationResult != null && locationResult.getLastLocation() != null) {
                    Location location = locationResult.getLastLocation();
                    Log.d("Geofence Location", location.toString());
                }
            }
        };

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this);

        fusedLocationClient.requestLocationUpdates(locationRequest, locationCallback, Looper.getMainLooper());
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        Log.d(TAG, "지오펜스 서비스 시작함");

        geofencingClient = LocationServices.getGeofencingClient(this);
        geofenceHelper = new GeofenceHelper(this);

        startForeground(notificationID, getNotification());

        SettingGeofences();
        startLocationUpdates();
        return START_STICKY;
    }

    private void SettingGeofences() {
        GeofenceRequest geofenceRequest = new GeofenceRequest(
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            // 주어진 JSON 문자열을 JSONObject로 변환
                            JSONArray jsonArray = new JSONArray(response);
                            for (int i = 0; i < jsonArray.length(); i++) {
                                JSONObject data = jsonArray.getJSONObject(i);

                                // 각 필드의 값을 가져오기
                                String placeName = data.getString("placeName");
                                double latitude = data.getDouble("latitude");
                                double longitude = data.getDouble("longitude");
                                float radius = data.getLong("radius");

                                // 가져온 값 출력 또는 다른 처리 수행
                                Log.d(TAG, "장소 이름:" + placeName);
                                Log.d(TAG, "위도:" + latitude);
                                Log.d(TAG, "경도:" + longitude);
                                Log.d(TAG, "반경:" + radius);
                                Log.d(TAG, "");

                                LatLng latLng = new LatLng(latitude, longitude);
                                addGeofence(latLng, radius, placeName);
                            }
                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        Log.e(TAG, "Error fetching geofences", error);
                    }
                }
        );

        RequestQueue queue = Volley.newRequestQueue(this);
        queue.add(geofenceRequest);
    }
    @SuppressLint("MissingPermission")
    private void addGeofence(LatLng latLng, float radius, String ID) {
        Geofence geofence = geofenceHelper.getGeofence(ID, latLng, radius, Geofence.GEOFENCE_TRANSITION_ENTER | Geofence.GEOFENCE_TRANSITION_DWELL | Geofence.GEOFENCE_TRANSITION_EXIT);
        GeofencingRequest geofencingRequest = geofenceHelper.getGeofencingRequest(geofence);
        PendingIntent pendingIntent = geofenceHelper.getPendingIntent();

        geofencingClient.addGeofences(geofencingRequest, pendingIntent)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        Log.d(TAG, "onSuccess: Geofence Added...");
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        String errorMessage = geofenceHelper.getErrorString(e);
                        Log.d(TAG, "onFailure: " + errorMessage);
                    }
                });
    }
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    private Notification getNotification() {
        Intent notificationIntent = new Intent(this, com.example.AppManager.MapsActivity.class);
        PendingIntent pendingIntent = PendingIntent.getActivity(this, 0, notificationIntent, PendingIntent.FLAG_MUTABLE);

        return new NotificationCompat.Builder(this, CHANNEL_ID)
                .setContentTitle("Geofence Service")
                .setContentText("Geofence service is running")
                .setSmallIcon(R.drawable.ic_launcher_foreground) // 적절한 아이콘으로 교체
                .setContentIntent(pendingIntent)
                .build();
    }

    private void createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            NotificationChannel serviceChannel = new NotificationChannel(
                    CHANNEL_ID,
                    "Geofence Service Channel",
                    NotificationManager.IMPORTANCE_DEFAULT
            );

            NotificationManager manager = getSystemService(NotificationManager.class);
            if (manager != null) {
                manager.createNotificationChannel(serviceChannel);
            }
        }
    }
    public void onDestroy() {
        super.onDestroy();
        Log.d(TAG, "지오펜스 서비스 파괴됨");
    }
}