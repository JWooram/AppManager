package com.example.AppManager;

import android.app.PendingIntent;
import android.content.Context;
import android.content.ContextWrapper;
import android.content.Intent;
import android.util.Log;

import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofenceStatusCodes;
import com.google.android.gms.location.GeofencingRequest;
import com.google.android.gms.maps.model.LatLng;

public class GeofenceHelper extends ContextWrapper {

    private static final String TAG = "GeofenceHelper";
    private PendingIntent pendingIntent;

    public GeofenceHelper(Context base) {
        super(base);
    }

    /**
     * GeofencingRequest를 생성합니다.
     * @param geofence Geofence 객체
     * @return GeofencingRequest 객체
     */
    public GeofencingRequest getGeofencingRequest(Geofence geofence) {
        Log.d(TAG, "지오펜스 요청");
        return new GeofencingRequest.Builder()
                .addGeofence(geofence)
                .setInitialTrigger(GeofencingRequest.INITIAL_TRIGGER_ENTER)
                .build();
    }

    /**
     * Geofence 객체를 생성합니다.
     * @param ID Geofence ID
     * @param latLng Geofence의 중심 좌표
     * @param radius Geofence의 반경
     * @param transitionTypes Geofence 전환 타입
     * @return Geofence 객체
     */
    public Geofence getGeofence(String ID, LatLng latLng, float radius, int transitionTypes) {
        Log.d(TAG, "지오펜스 Get");
        Log.d(TAG, String.valueOf(latLng.latitude)+ String.valueOf(latLng.longitude));
        return new Geofence.Builder()
                .setCircularRegion(latLng.latitude, latLng.longitude, radius)
                .setRequestId(ID)
                .setTransitionTypes(transitionTypes)
                .setLoiteringDelay(5000) // 체류 시간
                .setExpirationDuration(Geofence.NEVER_EXPIRE)
                .build();
    }

    /**
     * PendingIntent를 생성하거나 반환합니다.
     * @return PendingIntent 객체
     */
    public PendingIntent getPendingIntent() {
        Log.d(TAG, "인텐트 얻기");
        if (pendingIntent != null) {
            Log.d(TAG, "반환?");
            return pendingIntent;
        }
        Intent intent = new Intent(this, GeofenceBroadcastReceiver.class);
        pendingIntent = PendingIntent.getBroadcast(this, 2607, intent, PendingIntent.FLAG_MUTABLE);
        return pendingIntent;
    }

    /**
     * Geofence 관련 에러 메시지를 반환합니다.
     * @param e Exception 객체
     * @return 에러 메시지 문자열
     */
    public String getErrorString(Exception e) {
        if (e instanceof ApiException) {
            ApiException apiException = (ApiException) e;
            switch (apiException.getStatusCode()) {
                case GeofenceStatusCodes.GEOFENCE_NOT_AVAILABLE:
                    return "GEOFENCE_NOT_AVAILABLE";
                case GeofenceStatusCodes.GEOFENCE_TOO_MANY_GEOFENCES:
                    return "GEOFENCE_TOO_MANY_GEOFENCES";
                case GeofenceStatusCodes.GEOFENCE_TOO_MANY_PENDING_INTENTS:
                    return "GEOFENCE_TOO_MANY_PENDING_INTENTS";
                default:
                    return apiException.getLocalizedMessage();
            }
        }
        return e.getLocalizedMessage();
    }
}
