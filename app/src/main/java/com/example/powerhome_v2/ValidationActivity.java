package com.example.powerhome_v2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ValidationActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_validation);

        TextView goBackToFP = findViewById(R.id.recommencerClick);

        goBackToFP.setOnClickListener(v -> {
            Intent intent = new Intent(ValidationActivity.this, ForgetpwdActivity.class);
            startActivity(intent);
        });
    }
}
