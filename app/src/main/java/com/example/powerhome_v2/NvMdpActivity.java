package com.example.powerhome_v2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;

import androidx.appcompat.app.AppCompatActivity;

public class NvMdpActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_nv_mdp);

        Button btnValider = findViewById(R.id.btnValider);

        btnValider.setOnClickListener(v -> {
            Intent intent = new Intent(NvMdpActivity.this, SigninActivity.class);
            startActivity(intent);
            finish();
        });
    }
}
