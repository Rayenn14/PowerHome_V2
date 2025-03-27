package com.example.powerhome_v2;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.List;

public class ApplianceAdapter extends ArrayAdapter<Appliance> {
    Activity activity;
    int itemResourceId;
    List<Appliance> items;

    public ApplianceAdapter(Activity activity, int itemResourceId, List<Appliance> items) {
        super(activity, itemResourceId, items);
        this.activity = activity;
        this.itemResourceId = itemResourceId;
        this.items = items;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        View layout = convertView;
        if (convertView == null) {
            LayoutInflater inflater = activity.getLayoutInflater();
            layout = inflater.inflate(itemResourceId, parent, false);
        }

        Appliance appliance = items.get(position);

        TextView nameTV = layout.findViewById(R.id.equipment_name);
        TextView referenceTV = layout.findViewById(R.id.equipment_reference);
        TextView consumptionTV = layout.findViewById(R.id.equipment_consumption);
        ImageView iconImageView = layout.findViewById(R.id.iconEquipement);

        // Set the text fields for appliance name, reference, and consumption
        nameTV.setText(appliance.getName());
        referenceTV.setText("Référence: " + appliance.getReferences());
        consumptionTV.setText(appliance.getWattage() + "W");

        // Set the icon for the appliance based on its name
        String applianceName = appliance.getName().toLowerCase();
        if (applianceName.contains("aspirateur")) {
            iconImageView.setImageResource(R.drawable.aspi);
        } else if (applianceName.contains("climatiseur")) {
            iconImageView.setImageResource(R.drawable.ic_climatiseur);
        } else if (applianceName.contains("fer à repasser")) {
            iconImageView.setImageResource(R.drawable.fer);
        } else if (applianceName.contains("machine à laver")) {
            iconImageView.setImageResource(R.drawable.lessive);
        }
        else if (applianceName.contains("micro-onde")) {
            iconImageView.setImageResource(R.drawable.microonde);
        }else {
            // Default icon if no match is found
            iconImageView.setImageResource(R.drawable.profil);
        }

        return layout;
    }
}
