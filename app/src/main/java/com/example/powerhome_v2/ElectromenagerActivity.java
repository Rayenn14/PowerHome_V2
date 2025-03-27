package com.example.powerhome_v2;

import android.os.Bundle;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

public class ElectromenagerActivity extends BaseActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActivityContent(R.layout.activity_electromenager);

        ListView lv = findViewById(R.id.listView);
        TextView totalConsumptionTV = findViewById(R.id.total_consumption);

        // Création d'une liste d'équipements
        List<Appliance> applianceRayen = new ArrayList<>();
        applianceRayen.add(new Appliance(1, "machine à laver", "1FRDDA1", 1500));
        applianceRayen.add(new Appliance(1, "aspirateur", "1FRDDA2", 1500));
        applianceRayen.add(new Appliance(1, "climatiseur", "1FRDDA3", 2000));

        // Adapter
        ApplianceAdapter applianceAdapter = new ApplianceAdapter(this, R.layout.item_equipement, applianceRayen);
        lv.setAdapter(applianceAdapter);

        // Calculer la consommation totale
        int totalConsumption = 0;
        for (Appliance appliance : applianceRayen) {
            totalConsumption += appliance.getWattage();
        }

        // Mettre à jour le TextView avec la consommation totale
        totalConsumptionTV.setText("Consommation Totale: " + totalConsumption + "W");
    }

}
