package com.example.AppManager;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.fragment.app.Fragment;
import androidx.localbroadcastmanager.content.LocalBroadcastManager;

import android.Manifest;
import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.location.Location;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Looper;
import android.provider.Settings;
import android.util.Log;
import android.widget.Toast;

import com.example.AppManager.GeofenceService;
import com.example.AppManager.Fragment.HomeFragment;
import com.example.AppManager.Fragment.ProfileFragment;
import com.example.AppManager.Fragment.SettingFragment;
import com.google.android.gms.location.FusedLocationProviderClient;
import com.google.android.gms.location.LocationCallback;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationResult;
import com.google.android.gms.location.LocationServices;
import com.google.android.material.navigation.NavigationBarView;

public class MapsActivity extends AppCompatActivity {

    private static final String TAG = "MapsActivity";
    private HomeFragment fragmentHome = new HomeFragment();
    private SettingFragment fragmentSetting = new SettingFragment();
    private ProfileFragment fragmentProfile = new ProfileFragment();
    private Fragment activeFragment;

    public static final int REQUEST_CODE_BACKGROUND_LOCATION_PERMISSION = 102;
    public static final int SYSTEM_ALERT_WINDOW_PERMISSION_REQUEST_CODE = 101;
    private static final int SYSTEM_ALERT_WINDOW_PERMISSION = 2084;
    private int REQUEST_CODE_LOCATION_PERMISSION = 10001;
    private int BACKGROUND_LOCATION_ACCESS_REQUEST_CODE = 10002;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_maps);

        requestLocationPermissions();
//        startGeofenceService();
        getSupportFragmentManager().beginTransaction()
                .add(R.id.fragment_container, fragmentSetting, "fragmentSetting").hide(fragmentSetting)
                .add(R.id.fragment_container, fragmentProfile, "fragmentProfile").hide(fragmentProfile)
                .add(R.id.fragment_container, fragmentHome, "fragmentHome")
                .commit();

//        getSupportFragmentManager().beginTransaction().replace(R.id.fragment_container, fragmentHome).commit();
        activeFragment = fragmentHome;

        NavigationBarView navigationBarView = findViewById(R.id.menu_bottom_navigation);
        navigationBarView.setSelectedItemId(R.id.menu_home);
        navigationBarView.setOnItemSelectedListener(navListener);

    }
    private final NavigationBarView.OnItemSelectedListener navListener = item -> {
        Fragment selectedFragment = null;
        int itemId = item.getItemId();
        if (itemId == R.id.menu_home) {
            selectedFragment = fragmentHome;
            Log.d(TAG, "fragmentHome: ");
        } else if (itemId == R.id.menu_wishlists) {
            selectedFragment = fragmentSetting;
            Log.d(TAG, "fragmentSetting: ");
        } else if (itemId == R.id.menu_profile) {
            selectedFragment = fragmentProfile;
            Log.d(TAG, "fragmentProfile: ");
        }

        if (selectedFragment != null && selectedFragment != activeFragment) {
            getSupportFragmentManager().beginTransaction().hide(activeFragment).show(selectedFragment).commit();
            Log.d(TAG, "activeFragment: "+ activeFragment);
            activeFragment = selectedFragment;
        }
        return true;
    };


    private void startGeofenceService() {
        Intent serviceIntent = new Intent(this, GeofenceService.class);

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            Log.d(TAG, "포어그라운드 서비스 실행합니다");
            startForegroundService(serviceIntent);
        } else {
            startService(serviceIntent);
        }
    }

    private void requestLocationPermissions() {
        if (checkSelfPermission(Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED ||
                checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            Log.d(TAG, "Q 버전 이상 권한 요청");
            ActivityCompat.requestPermissions(this,
                    new String[]{Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION},
                    REQUEST_CODE_LOCATION_PERMISSION);
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_CODE_LOCATION_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                BackgroundLoactionPermissionDialog();
                Toast.makeText(this, "Fine location permission granted", Toast.LENGTH_SHORT).show();
            } else {
                // 권한이 거부된 경우
                Toast.makeText(this, "Fine location permission denied", Toast.LENGTH_SHORT).show();
            }
        }
        else if(requestCode == REQUEST_CODE_BACKGROUND_LOCATION_PERMISSION){
            if(grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
//                SystemAlertPermissionDialog();
                startGeofenceService();
                Log.d(TAG, "지오펜스 서비스 시작");

            }
            else{
                Toast.makeText(this, "Background location permission denied", Toast.LENGTH_SHORT).show();
            }
        }

    }

    private void backgroundPermission() {
        ActivityCompat.requestPermissions(
                this,
                new String[]{android.Manifest.permission.ACCESS_BACKGROUND_LOCATION},
                REQUEST_CODE_BACKGROUND_LOCATION_PERMISSION);
    }

    private void BackgroundLoactionPermissionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("백그라운드 위치 권한을 위해 항상 허용으로 설정해주세요.");

        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == DialogInterface.BUTTON_POSITIVE) {
                    backgroundPermission();
                }
            }
        };

        builder.setPositiveButton("Allow all the time", listener);
        builder.setNegativeButton("Deny", null);

        builder.show();
    }

    public void requestSystemAlertWindowPermission() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            if (!Settings.canDrawOverlays(this)) {
                // 시스템 알림창 권한이 없는 경우 권한 요청 다이얼로그 표시
                Intent intent = new Intent(Settings.ACTION_MANAGE_OVERLAY_PERMISSION, Uri.parse("package:" + getPackageName()));
                startActivityForResult(intent, SYSTEM_ALERT_WINDOW_PERMISSION_REQUEST_CODE);
            }
        }
    }

    private void SystemAlertPermissionDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("앱 자동 실행을 위해 항상 허용으로 설정해주세요.");

        DialogInterface.OnClickListener listener = new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                if (which == DialogInterface.BUTTON_POSITIVE) {
                    requestSystemAlertWindowPermission();
                }
            }
        };

        builder.setPositiveButton("허용", listener);
        builder.setNegativeButton("거부", null);

        builder.show();
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == SYSTEM_ALERT_WINDOW_PERMISSION_REQUEST_CODE) {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
                if (Settings.canDrawOverlays(this)) {

                } else {
                    // 권한이 거부된 경우 사용자에게 메시지 표시
                    Toast.makeText(this, "SYSTEM_ALERT_WINDOW 권한이 거부되었습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        }
    }
}
