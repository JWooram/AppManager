package com.example.AppManager;

import androidx.appcompat.app.AppCompatActivity;

import android.net.wifi.ScanResult;
import android.net.wifi.WifiManager;
import android.os.Bundle;

import com.example.AppManager.R;

import java.util.ArrayList;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    private static final String TAG = "MainActivity";
    WifiManager wifiManager;
    List<ScanResult> scanResultList;
    ArrayList<String> top10APId[];
    ArrayList<String> APList[];



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        wifiManager = (WifiManager)getApplicationContext().getSystemService(WIFI_SERVICE);
//
//        BroadcastReceiver wifiReceiver = new BroadcastReceiver() {
//            @Override
//            public void onReceive(Context context, Intent intent) {
//                if (intent.getAction().equals(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION)) {
//                    getWifiInfo(); //받은 스캔 결과 처리
//                }
//            }
//        };
//
//        IntentFilter filter = new IntentFilter();
//        filter.addAction(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
//        registerReceiver(wifiReceiver, filter);
//
//        wifiManager.startScan();

    }
//
//    public void getWifiInfo() {
//        scanResultList = wifiManager.getScanResults();
//        if (wifiManager.getScanResults() != null) {
//            String storeName = "AndroidWifi";
//            int thresHold = -80;
//
//            int size = scanResultList.size();
//            Log.d(TAG, "현재 인식되는 와이파이 수 : " + size);
//            for (int i = 0; i < size; i++) {
//                ScanResult scanResult = scanResultList.get(i);
//
//                if (Objects.equals(scanResult.SSID, storeName)){
//                    Log.d(TAG, "success name");
////                    int level = wifiManager.calculateSignalLevel(scanResult.level);
//                    if(scanResult.level > thresHold){
//                        Log.d(TAG, scanResult.SSID + scanResult.level);
//                    }
//                }
//
//
//                // scanResult.SSID
//                // scanResult.BSSID
//                // scanResult.level
//            }
//        }
//    }
//
//    private void getWifiInfo() {
//        if(!doneWifiScan) { // wifiScan을 한 경우에만 getScanResult를 사용하도록 flag 변수 구현
//            scanResultList = wifiManager.getScanResults();
//
////            ArrayList<String> nowAPId = new ArrayList<String>();
//
//            String str;
//            for(int i=1; i < scanResultList.size(); i++){
//                ScanResult result = scanResultList.get(i);
//                str = result.SSID + result.level;
//                Log.d(TAG, str.toString());
////                nowAPId.add(str);
//            }
//
////            for(int i=1; i< nowAPId.size(); i++){
////                System.out.println(nowAPId[i]);
////            }
////
////            Log.d(TAG, scanResultList.toString());
////            String str;
////            //중복되지 않게 해당 num번째 배열에 AP정보들을 집어넣는다.
////            for (int i = 1; i < scanResultList.size(); i++) {
////                ScanResult result = scanResultList.get(i);
////                // 화면의 TextView에 SSID와 BSSID를 이어붙여서 텍스트로 표시
////                str = result.SSID + result.BSSID;
////                if(!top10APId[num].contains(str))
////                    top10APId[num].add(str);
////            }
////            //모두 처리되고 나면 가장 처음 들어온 AP의 SSID+BSSID를 text로 뿌려준다.
////            if (num == 0) {
////                ap1.setText(top10APId[num].get(0));
////            } else if (num == 1) {
////                ap2.setText(top10APId[num].get(0));
////            } else {
////                ap3.setText(top10APId[num].get(0));
////            }
//            doneWifiScan = true;
//        }
//    }
//

//
//    @Override
//    protected void onResume() {
//        super.onResume();
//
//        // wifi scan 결과 수신을 위한 BroadcastReceiver 등록
//        IntentFilter filter = new IntentFilter(WifiManager.SCAN_RESULTS_AVAILABLE_ACTION);
//        registerReceiver(mReceiver, filter);
//    }
//    private void requestRuntimePermission() {
//        //*******************************************************************
//        // Runtime permission check
//        //*******************************************************************
//        if (ContextCompat.checkSelfPermission(MainActivity.this,
//                Manifest.permission.ACCESS_FINE_LOCATION)
//                != PackageManager.PERMISSION_GRANTED) {
//
//            // Should we show an explanation?
//            if (ActivityCompat.shouldShowRequestPermissionRationale(MainActivity.this,
//                    Manifest.permission.ACCESS_FINE_LOCATION)) {
//
//                // Show an expanation to the user *asynchronously* -- don't block
//                // this thread waiting for the user's response! After the user
//                // sees the explanation, try again to request the permission.
//
//            } else {
//
//                // No explanation needed, we can request the permission.
//
//                ActivityCompat.requestPermissions(MainActivity.this,
//                        new String[]{Manifest.permission.ACCESS_FINE_LOCATION},
//                        MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION);
//            }
//        } else {
//            // ACCESS_FINE_LOCATION 권한이 있는 것
//            isPermitted = true;
//        }
//        //*********************************************************************
//    }
//
//    @Override
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        if (requestCode == MY_PERMISSIONS_REQUEST_ACCESS_FINE_LOCATION) {// If request is cancelled, the result arrays are empty.
//            if (grantResults.length > 0
//                    && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
//
//                // permission was granted, yay! Do the
//                // read_external_storage-related task you need to do.
//
//                // ACCESS_FINE_LOCATION 권한을 얻음
//                isPermitted = true;
//
//            } else {
//
//                // permission denied, boo! Disable the
//                // functionality that depends on this permission.
//
//                // 권한을 얻지 못 하였으므로 location 요청 작업을 수행할 수 없다
//                // 적절히 대처한다
//                isPermitted = false;
//
//            }
//            return;
//
//            // other 'case' lines to check for other
//            // permissions this app might request
//        }
//    }
}
