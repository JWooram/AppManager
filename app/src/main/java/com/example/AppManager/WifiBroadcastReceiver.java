package com.example.AppManager;

import static android.content.Context.WIFI_SERVICE;

import android.annotation.SuppressLint;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.util.Log;

import java.util.ArrayList;
import java.util.List;

public class WifiBroadcastReceiver extends BroadcastReceiver {
    private static final String TAG = "WifiBroadcastReceiver";
    WifiManager wifiManager;
    List<ScanResult> scanResultList;

    @Override
    public void onReceive(Context context, Intent intent) {
        NotificationHelper notificationHelper = new NotificationHelper(context);
        wifiManager = (WifiManager) context.getApplicationContext().getSystemService(WIFI_SERVICE);
        if (intent.getAction() != null && intent.getAction().equals(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)) {
            getWifiInfo(context);
        }
    }

    @SuppressLint("MissingPermission")
    public void getWifiInfo(Context context) {
        Log.d(TAG, "receive success!!");
        scanResultList = wifiManager.getScanResults();
        ArrayList<String> storeNames = new ArrayList<>();
        if (scanResultList != null) {
            storeNames.add("AndroidWifi");
            int thresHold = -60;

            int size = scanResultList.size();
            Log.d(TAG, "현재 인식되는 와이파이 수 : " + size);
            for (int i = 0; i < size; i++) {
                ScanResult scanResult = scanResultList.get(i);

                if (storeNames.contains(scanResult.SSID)){
                    if(scanResult.level > thresHold){
                        Log.d(TAG, "Wi-Fi AP SSID : " + scanResult.SSID);
                        Log.d(TAG, "Wi-Fi AP RSSI : " + scanResult.level);

                        return;
                    }

                }
            }
        }
    }


}
