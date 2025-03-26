package com.example.powerhome_v2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class NavMenuActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navmenu);

        // Redirection vers ProfilActivity
        TextView editerProfil = findViewById(R.id.nav_editprofile);
        editerProfil.setOnClickListener(v -> {
            Intent intent = new Intent(NavMenuActivity.this, ProfilActivity.class);
            startActivity(intent);
        });

        // Redirection vers Notifications (à compléter)
        // TextView goBackToNotif = findViewById(R.id.nav_bell);
        // goBackToNotif.setOnClickListener(v -> {
        // Intent intent = new Intent(NavMenuActivity.this, NotificationActivity.class);
        // startActivity(intent);
        // });

        // Redirection vers Mes Préférences
        TextView goBackToPreferences = findViewById(R.id.nav_bookmark);
        goBackToPreferences.setOnClickListener(v -> {
            Intent intent = new Intent(NavMenuActivity.this, ResidentActivity.class); // Remplacer par l'activité correcte
            startActivity(intent);
        });

        // Redirection vers Liste des habitants
        TextView goBackToListeHabitants = findViewById(R.id.nav_home);
        goBackToListeHabitants.setOnClickListener(v -> {
            Intent intent = new Intent(NavMenuActivity.this, ResidentActivity.class);
            startActivity(intent);
        });

        // Redirection vers Mon Habitat
        TextView goBackToMyAccount = findViewById(R.id.nav_user);
        goBackToMyAccount.setOnClickListener(v -> {
            Intent intent = new Intent(NavMenuActivity.this, ResidentActivity.class); // Remplacer par l'activité correcte
            startActivity(intent);
        });
    }
}
