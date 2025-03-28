package com.example.powerhome_v2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ForgetpwdActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_forgetpwd);


        TextView goBackToSignIn = findViewById(R.id.seConnecter_forgetpwd);

        goBackToSignIn.setOnClickListener(v -> {
            Intent intent = new Intent(ForgetpwdActivity.this, SigninActivity.class);
            startActivity(intent);
        });

    }
}
