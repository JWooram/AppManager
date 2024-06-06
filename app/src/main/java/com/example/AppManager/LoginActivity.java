package com.example.AppManager;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.toolbox.Volley;
import com.example.AppManager.Request.LoginRequest;

import org.json.JSONException;
import org.json.JSONObject;

public class LoginActivity extends AppCompatActivity {
    private static final String TAG = "LoginActivity";
    private EditText et_id, et_pass;
    @Override 
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_login);

        et_id = findViewById(R.id.username_input);
        et_pass = findViewById(R.id.password_input);
        Button btn_signin = findViewById(R.id.signIn_btn);
        TextView btn_signup = findViewById(R.id.SignUp_btn);

//        Map<String, String> loginInfo = SharedPreferencesManager.getLoginInfo(this);
//        if (!loginInfo.isEmpty()){
//            String email    = loginInfo.get("email");
//            String password = loginInfo.get("password");
//        }
        btn_signup.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(LoginActivity.this, RegisterActivity.class);
                startActivity(intent);
            }
        });

        btn_signin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String userID = et_id.getText().toString();
                String userPass = et_pass.getText().toString();

                Response.Listener<String> responseListener = new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {
                        try {
                            System.out.println(response);
                            JSONObject jsonObject = new JSONObject(response);
                            boolean success = jsonObject.getBoolean("success");
                            if (success) { // 로그인에 성공한 경우
                                Log.d(TAG, "login success");
                                String userID1 = jsonObject.getString("userID");
                                String userPass1 = jsonObject.getString("userPassword");
                                String userName = jsonObject.getString("userName");

//                                Toast.makeText(getApplicationContext(), "로그인에 성공하였습니다.", Toast.LENGTH_SHORT).show();
                                Intent intent = new Intent(LoginActivity.this, MapsActivity.class);

                                SaveSharedPreference.setUserID(LoginActivity.this, userID1);
                                SaveSharedPreference.setUserName(LoginActivity.this, userName);

                                Log.d(TAG, "User ID :" + userID1);
                                Log.d(TAG, "User Password :" + userPass1);
                                Log.d(TAG, "user Name :" + userName);

                                startActivity(intent);
                            } else { // 로그인에 실패한 경우
                                Log.d(TAG, "login fail");
//                                Toast.makeText(getApplicationContext(), "로그인에 실패하였습니다.", Toast.LENGTH_SHORT).show();
                                return;
                            }
                        } catch (JSONException e) {
                            Log.d(TAG, "exception");
                            e.printStackTrace();
                        }
                    }
                };

                LoginRequest loginRequest = new LoginRequest(userID, userPass, responseListener);
                RequestQueue queue = Volley.newRequestQueue(LoginActivity.this);
                queue.add(loginRequest);
            }
        });


    }


}

