package com.example.AppManager.Request;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class LoginRequest extends StringRequest {

    // 서버 URL 설정 ( PHP 파일 연동 )
    final static private String URL = "http://ec2-13-209-22-235.ap-northeast-2.compute.amazonaws.com/Logtest.php";
    private Map<String, String> map;


    public LoginRequest(String userID, String userPassword, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);
        map = new HashMap<>();
        map.put("user_id",userID);
        map.put("password", userPassword);
        Log.d("Login request", userID);
        Log.d("Login request", userPassword);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        Log.d("Login request", "go");
        return map;
    }
}