package com.example.AppManager.Request;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.util.HashMap;
import java.util.Map;

public class RegisterRequest extends StringRequest {
    private static final String TAG = "RegisterRequest";
    // 서버 URL 설정 ( PHP 파일 연동 )
    final static private String URL = "http://ec2-13-209-22-235.ap-northeast-2.compute.amazonaws.com/Register.php";
    private Map<String, String> map;


    public RegisterRequest(String userID, String userPassword, String userName, String userPhone, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);

        map = new HashMap<>();
        map.put("userID",userID);
        map.put("userPassword", userPassword);
        map.put("userName", userName);
        map.put("userPhone", userPhone);
        Log.d(TAG, "데이터 서버로 송신 요청");
        Log.d(TAG, userID + userPassword + userName + userPhone);
    }

    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}