package com.example.powerhome_v2;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.appcompat.app.AppCompatActivity;
import androidx.drawerlayout.widget.DrawerLayout;

import com.google.android.material.navigation.NavigationView;

public abstract class BaseActivity extends AppCompatActivity {
    protected DrawerLayout drawerLayout;
    protected NavigationView navigationView;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        super.setContentView(R.layout.activity_navmenu);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);
        ImageButton menuHamburger = findViewById(R.id.menuHamburger);


        View headerView = navigationView.getHeaderView(0);
        TextView nomResident = headerView.findViewById(R.id.nomResident);
        TextView etageResident = headerView.findViewById(R.id.etageResident);
        ImageView imageProfile = headerView.findViewById(R.id.imageProfile);

        nomResident.setText("Sophia Rose");
        etageResident.setText("Étage : 2");
        imageProfile.setImageResource(R.drawable.profil);


        menuHamburger.setOnClickListener(v -> drawerLayout.openDrawer(navigationView));

        navigationView.setNavigationItemSelectedListener(item -> {
            handleNavigation(item.getItemId());
            drawerLayout.closeDrawers();
            return true;
        });
    }

    private void handleNavigation(int itemId) {
        Class<?> targetActivity = null;

        if (itemId == R.id.nav_home) {
            targetActivity = ResidentActivity.class;
        } else if (itemId == R.id.nav_bookmark) {
            targetActivity = ElectromenagerActivity.class;
        } else if (itemId == R.id.nav_user) {
            targetActivity = NvMdpActivity.class;
        } else if (itemId == R.id.nav_editprofile) {
            targetActivity = ProfilActivity.class;
        }

        if (targetActivity != null && !this.getClass().equals(targetActivity)) {
            startActivity(new Intent(this, targetActivity));
            if (targetActivity == ResidentActivity.class) {
                finish();
            }
        }
    }

    protected void setActivityContent(int layoutResId) {
        FrameLayout contentFrame = findViewById(R.id.content_frame);
        getLayoutInflater().inflate(layoutResId, contentFrame, true);
    }
}