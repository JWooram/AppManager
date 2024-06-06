package com.example.AppManager.Request;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class ReservationUpdate extends StringRequest {
    private static final String TAG = "ReservationUpdate";
    // 서버 URL 설정 ( PHP 파일 연동 )
    final static private String URL = "http://ec2-13-209-22-235.ap-northeast-2.compute.amazonaws.com/ReservationUpdate.php";
    private Map<String, String> map;


    public ReservationUpdate(String userID, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);


        map = new HashMap<>();
        map.put("user_id",userID);
        Log.d(TAG, "데이터 서버로 송신 요청");
        Log.d(TAG, map.toString());
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}