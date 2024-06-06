package com.example.AppManager.Request;

import android.util.Log;

import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

public class GeofenceRequest extends StringRequest {

    // 서버 URL 설정 ( PHP 파일 연동 )
    final static private String URL = "http://ec2-13-209-22-235.ap-northeast-2.compute.amazonaws.com/GolfGeofence.php";

    public GeofenceRequest(Response.Listener<String> listener, Response.ErrorListener errorListener) {
        super(Method.GET, URL, listener, errorListener);
        Log.d("GeofenceRequest", "지오펜스 할당 요청");
    }

}