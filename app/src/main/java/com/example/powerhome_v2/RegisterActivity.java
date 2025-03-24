package com.example.powerhome_v2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class RegisterActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        TextView goBackToSignIn = findViewById(R.id.Seconnecterclick);

        goBackToSignIn.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterActivity.this, SigninActivity.class);
            startActivity(intent);
        });
    }
}
