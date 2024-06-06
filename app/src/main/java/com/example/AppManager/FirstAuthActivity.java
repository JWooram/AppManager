package com.example.AppManager;

import android.content.Intent;
import android.os.Bundle;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;

public class FirstAuthActivity extends AppCompatActivity {
    private Intent intent;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_first_auth);

        if(SaveSharedPreference.getUserID(FirstAuthActivity.this).isEmpty()) {
            // call Login Activity
            intent = new Intent(FirstAuthActivity.this, LoginActivity.class);
        } else {
            // Call Next Activity
            intent = new Intent(FirstAuthActivity.this ,MapsActivity.class);
            intent.putExtra("STD_NUM", SaveSharedPreference.getUserID(this).toString());
        }
        startActivity(intent);
        this.finish();
    }
}
