package com.example.AppManager.Request;

import android.util.Log;

import com.android.volley.AuthFailureError;
import com.android.volley.Response;
import com.android.volley.toolbox.StringRequest;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class ReservationRequest extends StringRequest {
    private static final String TAG = "ReservationRequest";
    // 서버 URL 설정 ( PHP 파일 연동 )
    final static private String URL = "http://ec2-13-209-22-235.ap-northeast-2.compute.amazonaws.com/Reservation.php";
    private Map<String, String> map;


    public ReservationRequest(String userID, Response.Listener<String> listener) {
        super(Method.POST, URL, listener, null);

        // 현재 시각 가져오기
        String currentDateTime = getCurrentDateTime();
        // 1시간 후의 시각 가져오기
        String oneHourAfterDateTime = getOneHourAfterDateTime();

        map = new HashMap<>();
        map.put("user_id",userID);
        map.put("reservation_date", currentDateTime);
        map.put("reservation_date_after", oneHourAfterDateTime);
        Log.d(TAG, "데이터 서버로 송신 요청");
        Log.d(TAG, map.toString());
    }

    // 현재 시각을 가져오는 메소드
    private String getCurrentDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date currentTime = Calendar.getInstance().getTime();
        Log.d(TAG, currentTime.toString());
        return dateFormat.format(currentTime);
    }

    // 1시간 후의 시각을 계산하는 메소드
    private String getOneHourAfterDateTime() {
        SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Calendar calendar = Calendar.getInstance();
        calendar.add(Calendar.HOUR_OF_DAY, 1); // 1시간 추가
        Date oneHourLaterTime = calendar.getTime();
        Log.d(TAG, oneHourLaterTime.toString());
        return dateFormat.format(oneHourLaterTime);
    }
    @Override
    protected Map<String, String> getParams() throws AuthFailureError {
        return map;
    }
}