package com.example.powerhome_v2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

import com.example.powerhome_v2.R;
import com.example.powerhome_v2.SigninActivity;

public class NavMenuActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navmenu);

        TextView editerProfil = findViewById(R.id.nav_editprofile);

        editerProfil.setOnClickListener(v -> {
            Intent intent = new Intent(NavMenuActivity.this, ProfilActivity.class);
            startActivity(intent);
        });

        TextView goBackToNotif = findViewById(R.id.nav_bell);

        // FAIRE CENTRE DE NOTIF !!!!!!!!!!!!!!!

        editerProfil.setOnClickListener(v -> {
            Intent intent = new Intent(NavMenuActivity.this, ProfilActivity.class); // CE NEST PAS ProfilActivity.class, il faut rediriger vers un centre de notifs
            startActivity(intent);
        });

        TextView goBackToPreferences = findViewById(R.id.nav_bookmark);

        // FAIRE CENTRE DE PREFERENCES

        goBackToPreferences.setOnClickListener(v -> {
            Intent intent = new Intent(NavMenuActivity.this, ProfilActivity.class); // CE NEST PAS ProfilActivity.class, il faut rediriger vers un centre de preference
            startActivity(intent);
        });

        TextView goBackToListeHabitants = findViewById(R.id.nav_home);

        goBackToListeHabitants.setOnClickListener(v -> {
            Intent intent = new Intent(NavMenuActivity.this, ResidentActivity.class);
            startActivity(intent);
        });

        TextView goBackToMyAccount = findViewById(R.id.nav_user);

        editerProfil.setOnClickListener(v -> {
            Intent intent = new Intent(NavMenuActivity.this, ElectromenagerActivity.class);
            startActivity(intent);
        });

    }
}