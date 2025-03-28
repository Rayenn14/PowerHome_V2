package com.example.powerhome_v2;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.appcompat.app.ActionBarDrawerToggle;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentTransaction;
import com.google.android.material.navigation.NavigationView;
import androidx.drawerlayout.widget.DrawerLayout;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;


public class MainActivity extends AppCompatActivity {

    private DrawerLayout drawerLayout;
    private NavigationView navigationView;
    private ActionBarDrawerToggle toggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_navmenu);

        drawerLayout = findViewById(R.id.drawer_layout);
        navigationView = findViewById(R.id.nav_view);


        Toolbar toolbar = findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        View headerView = navigationView.getHeaderView(0);
        // Récupérer les éléments du header
        TextView nomResident = headerView.findViewById(R.id.nomResident);

        // Charger les données depuis SharedPreferences
        SharedPreferences sharedPreferences = getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        String nom = sharedPreferences.getString("user_nom", "Nom inconnu");
        String prenom = sharedPreferences.getString("user_prenom", "Prénom inconnu");

        // Afficher le nom et prénom dynamiquement
        nomResident.setText(prenom + " " + nom);

        // Configuration du bouton hamburger
        toggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                toolbar,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
        );

        // Configuration du bouton hamburger
        toggle = new ActionBarDrawerToggle(
                this,
                drawerLayout,
                R.string.navigation_drawer_open,
                R.string.navigation_drawer_close
        );

        // Ajouter ces lignes pour la synchronisation
        drawerLayout.addDrawerListener(toggle);
        toggle.syncState();

        // Activation du bouton "home" dans l'ActionBar
        if (getSupportActionBar() != null) {
            getSupportActionBar().setDisplayHomeAsUpEnabled(true);
            getSupportActionBar().setHomeButtonEnabled(true);
        }

        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(MenuItem item) {
                int itemId = item.getItemId();

                if (itemId == R.id.nav_home) {
                    loadFragment(new HabitatListFragment());
                }
                else if (itemId == R.id.nav_user) {
                    loadFragment(new MonHabitatFragment());
                }
                else if (itemId == R.id.nav_editprofile) {
                    loadFragment(new ProfilFragment());
                }
                else if (itemId == R.id.nav_notif) {
                    loadFragment(new CalendarFragment());
                }
                drawerLayout.closeDrawers();
                return true;
            }
        });


        // Charge le fragment par défaut au démarrage
        if (savedInstanceState == null) {
            loadFragment(new HabitatListFragment());
        }
    }

    // Méthode pour charger un fragment
    private void loadFragment(Fragment fragment) {
        FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
        transaction.replace(R.id.content_frame, fragment);
        transaction.addToBackStack(null); // Ajoute le fragment à la pile pour pouvoir revenir en arrière
        transaction.commit();
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (toggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

}
