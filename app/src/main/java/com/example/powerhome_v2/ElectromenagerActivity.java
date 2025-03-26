package com.example.powerhome_v2;
import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;

public class ElectromenagerActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_electromenager);



        ImageButton goToHamburger = findViewById(R.id.menuHamburger);

        goToHamburger.setOnClickListener(v -> {
            Intent intent = new Intent(ElectromenagerActivity.this, NavMenuActivity.class);
            startActivity(intent);
        });

//        ImageView addEquip = findViewById(R.id.addEquipement);
//
//        addEquip.setOnClickListener(v -> {
//            Intent intent = new Intent(ElectromenagerActivity.this, SigninActivity.class);
//            startActivity(intent);
//        });

    }
}