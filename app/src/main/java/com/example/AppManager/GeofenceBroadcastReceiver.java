package com.example.AppManager;

import android.annotation.SuppressLint;
import android.content.ActivityNotFoundException;
import android.content.BroadcastReceiver;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.location.Location;
import android.util.Log;
import android.widget.Toast;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.AppManager.Request.LockerRequest;
import com.example.AppManager.Request.ReservationRequest;
import com.google.android.gms.location.Geofence;
import com.google.android.gms.location.GeofencingEvent;

import org.json.JSONException;
import org.json.JSONObject;

import java.util.List;

public class GeofenceBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = "GeofenceBroadcastReceiver";

    @SuppressLint("LongLogTag")
    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationHelper notificationHelper = new NotificationHelper(context);

        GeofencingEvent geofencingEvent = GeofencingEvent.fromIntent(intent);

        if (geofencingEvent.hasError()) {
            Log.d(TAG, "onReceive: Error receiving geofence event...");
            return;
        }

        List<Geofence> geofenceList = geofencingEvent.getTriggeringGeofences();
        for (Geofence geofence : geofenceList) {
            Log.d(TAG, "onReceive: " + geofence.getRequestId());
        }

        Location location = geofencingEvent.getTriggeringLocation();
        int transitionType = geofencingEvent.getGeofenceTransition();

        switch (transitionType) {
            case Geofence.GEOFENCE_TRANSITION_ENTER:
                Toast.makeText(context, "GEOFENCE_TRANSITION_ENTER", Toast.LENGTH_SHORT).show();
                notificationHelper.sendHighPriorityNotification("GEOFENCE_TRANSITION_ENTER", "", MapsActivity.class);
                break;
            case Geofence.GEOFENCE_TRANSITION_DWELL:
                Toast.makeText(context, "GEOFENCE_TRANSITION_DWELL", Toast.LENGTH_SHORT).show();
                notificationHelper.sendHighPriorityNotification("GEOFENCE_TRANSITION_DWELL", "", MapsActivity.class);
                CheckLocker(context);
                break;
            case Geofence.GEOFENCE_TRANSITION_EXIT:
                Toast.makeText(context, "GEOFENCE_TRANSITION_EXIT", Toast.LENGTH_SHORT).show();
                notificationHelper.sendHighPriorityNotification("GEOFENCE_TRANSITION_EXIT", "", MapsActivity.class);
//                context.stopService(new Intent(context, WifiScanService.class));
//                Log.d(TAG, "Scan Stop()");
                break;
        }
    }

    private void CheckReservation(Context context) {
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    System.out.println(response);
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");
                    if (success) { // 로그인에 성공한 경우
                        Log.d(TAG, "예약 정보 확인 성공");
//                        String reservationNum = jsonObject.getString("reservation_num");
//                        String name = jsonObject.getString("name");
//                        String email = jsonObject.getString("email");
//                        String reservation_date = jsonObject.getString("reservation_date");
//
//                        Log.d(TAG, "예약 번호 :" + reservationNum);
//                        Log.d(TAG, "예약자 성함 :" + name);
//                        Log.d(TAG, "예약자 ID :" + email);
//                        Log.d(TAG, "예약 일시 :" + reservation_date);

                        CheckLocker(context);
//                        startActivity(intent);
                    } else { // 로그인에 실패한 경우
                        Log.d(TAG, "login fail");
                        return;
                    }
                } catch (JSONException e) {
                    Log.d(TAG, "exception");
                    e.printStackTrace();
                }
            }
        };

        String userID = SaveSharedPreference.getUserID(context);
        ReservationRequest resevationRequest = new ReservationRequest(userID, responseListener);
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(resevationRequest);
    }

    private void CheckLocker(Context context) {
        Response.Listener<String> responseListener = new Response.Listener<String>() {
            @Override
            public void onResponse(String response) {
                try {
                    System.out.println(response);
                    JSONObject jsonObject = new JSONObject(response);
                    boolean success = jsonObject.getBoolean("success");
                    if (success) { // 로그인에 성공한 경우
                        String lockerID = jsonObject.getString("locker_id");
                        Log.d(TAG, "할당된 라커 ID :" + lockerID);
                        launchExternalApp(context);
//                        Intent localIntent = new Intent("com.example.ACTION_DATA_AVAILABLE");
//                        localIntent.putExtra("lockerID", lockerID);
//                        Log.d(TAG, localIntent.toString());
//                        LocalBroadcastManager.getInstance(context).sendBroadcast(localIntent);
                    } else {
                        Log.d(TAG, "남은 라커 없음");
                        return;
                    }
                } catch (JSONException e) {
                    Log.d(TAG, "exception");
                    e.printStackTrace();
                }
            }
        };

        String userID = SaveSharedPreference.getUserID(context);
        LockerRequest lockerRequest = new LockerRequest(userID, responseListener);
        RequestQueue queue = Volley.newRequestQueue(context);
        queue.add(lockerRequest);
    }

    private void launchExternalApp(Context context) {
        String packageName = "com.example.spoting";
        String activityName = "com.example.spoting.MapsActivity";

        Intent launchIntent = context.getPackageManager().getLaunchIntentForPackage(packageName);

        if (launchIntent != null) {
            launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
            context.startActivity(launchIntent);
        } else {
            try {
                // 명시된 액티비티를 직접 실행하도록 인텐트를 설정
                launchIntent = new Intent();
                launchIntent.setComponent(new ComponentName(packageName, activityName));
                launchIntent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                context.startActivity(launchIntent);
            } catch (ActivityNotFoundException e) {
                Log.e(TAG, "Activity not found", e);
            }
        }
    }

}
