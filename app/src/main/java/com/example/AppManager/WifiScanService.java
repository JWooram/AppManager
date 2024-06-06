package com.example.AppManager;

import android.app.Service;
import android.content.Intent;
import android.content.IntentFilter;
import android.net.wifi.WifiManager;
import android.os.Handler;
import android.os.IBinder;
import android.util.Log;

import androidx.annotation.Nullable;

public class WifiScanService extends Service {
    private static final String TAG = "WifiScanService";
    WifiManager wifiManager;
    private WifiBroadcastReceiver wifiReceiver;
    private Handler handler;


    @Override
    public void onCreate() {
        super.onCreate();
        wifiManager = (WifiManager)getApplicationContext().getSystemService(WIFI_SERVICE);
        wifiReceiver = new WifiBroadcastReceiver();

        IntentFilter filter = new IntentFilter();
        filter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
        registerReceiver(wifiReceiver, filter);

        handler = new Handler();
        startWifiScanPeriodically();
    }

    @Nullable
    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }


    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        if(intent==null){

        }
        return START_NOT_STICKY;
    }
//    @Override
//    public int onStartCommand(Intent intent, int flags, int startId) {
//        WifiManager wifiManager = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
//        if (wifiManager != null) {
//            wifiManager.startScan();
//            Log.d(TAG, "Wi-Fi scan started");
//        } else {
//            Log.e(TAG, "WifiManager is null");
//        }
//        return START_NOT_STICKY;
//    }
    private void startWifiScanPeriodically() {
        handler.postDelayed(new Runnable() {
            @Override
            public void run() {
                if (wifiManager != null) {
                    wifiManager.startScan();
                    Log.d(TAG, "Wi-Fi scan started");
                }
                // 주기적으로 Wi-Fi 스캔을 수행할 시간 간격 (예: 1분)
                handler.postDelayed(this, 5 * 1000); // 1분 (1분 = 60초 * 1000밀리초)
            }
        }, 0); // 초기 실행 시간 (즉시 시작)
    }
    public void stopWifiScan() {
        handler.removeCallbacksAndMessages(null);
        stopSelf();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        handler.removeCallbacksAndMessages(null);
    }
}