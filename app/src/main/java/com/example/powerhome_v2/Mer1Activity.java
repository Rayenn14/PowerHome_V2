package com.example.powerhome_v2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class Mer1Activity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mer1);

//        faire les raccords ajouterEquipement et ajouter Reservation quand les xml dispo

        ImageButton goToHamburger = findViewById(R.id.menuHamburger);

        goToHamburger.setOnClickListener(v -> {
            Intent intent = new Intent(Mer1Activity.this, NavMenuActivity.class);
            startActivity(intent);
        });
    }
}
