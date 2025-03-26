package com.example.powerhome_v2;

import android.os.Bundle;
import android.widget.ListView;

import androidx.appcompat.app.AppCompatActivity;

import java.util.ArrayList;
import java.util.List;

public class ResidentActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_residents);

        ListView lv = findViewById(R.id.listView);

        List<Appliance> applianceRayen = new ArrayList<>();
        applianceRayen.add(new Appliance(1, "machine à laver", "1FRDDA1", 1500));
        applianceRayen.add(new Appliance(1, "aspirateur", "1FRDDA1", 1500));
        applianceRayen.add(new Appliance(1, "climatiseur", "1FRDDA1", 1500));

        List<Appliance> applianceCedric = new ArrayList<>();
        applianceCedric.add(new Appliance(1, "machine à laver", "1FRDDA1", 1500));
        applianceCedric.add(new Appliance(1, "aspirateur", "1FRDDA1", 1500));

        List<Appliance> applianceIris = new ArrayList<>();
        applianceIris.add(new Appliance(1, "climatiseur", "1FRDDA1", 1500));
        applianceIris.add(new Appliance(1, "fer à repasser", "1FRDDA1", 1500));

        List<Appliance> applianceDanya = new ArrayList<>();
        applianceDanya.add(new Appliance(1, "aspirateur", "1FRDDA1", 1500));
        applianceDanya.add(new Appliance(1, "machine à laver", "1FRDDA1", 1500));



        List<Habitat> items = new ArrayList<>();
        items.add(new Habitat(1, "Rayen Bouzidi", 4, 1200.0, applianceRayen));
        items.add(new Habitat(2, "Cédric Boudet", 2, 60.0, applianceCedric));
        items.add(new Habitat(3, "Danya B", 1, 5.0, applianceDanya));
        items.add(new Habitat(4, "Iris N", 1, 25.0, applianceIris));
        items.add(new Habitat(4, "Iris N", 1, 25.0, applianceIris));
        items.add(new Habitat(4, "Iris N", 1, 25.0, applianceIris));
        items.add(new Habitat(4, "Iris N", 1, 25.0, applianceIris));
        items.add(new Habitat(4, "Iris N", 1, 25.0, applianceIris));

        HabitatAdapter ha = new HabitatAdapter(this, R.layout.item_habitat, items);
        lv.setAdapter(ha);
    }

}
