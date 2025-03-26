package com.example.powerhome_v2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.powerhome_v2.R;
import com.example.powerhome_v2.SigninActivity;

public class ProfilActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_profil);

        ImageButton goToHamburger = findViewById(R.id.menuHamburger);

        goToHamburger.setOnClickListener(v -> {
            Intent intent = new Intent(ProfilActivity.this, NavMenuActivity.class);
            startActivity(intent);
        });


    }
}