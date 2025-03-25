package com.example.powerhome_v2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class SigninActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);

        TextView creerCompte = findViewById(R.id.Creeruncompteclick);

        creerCompte.setOnClickListener(v -> {
            Intent intent = new Intent(SigninActivity.this, RegisterActivity.class);
            startActivity(intent);
        });

        TextView mdpOublie = findViewById(R.id.Mdpoublieeclick);

        mdpOublie.setOnClickListener(v -> {
            Intent intent = new Intent(SigninActivity.this, ForgetpwdActivity.class);
            startActivity(intent);
        });

        ImageButton goBackTo = findViewById(R.id.RetourSplash);

        goBackTo.setOnClickListener(v -> {
            Intent intent = new Intent(SigninActivity.this, SplashActivity.class);
            startActivity(intent);
        });

    }
}
