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

public class HabitatAdapter extends ArrayAdapter<Habitat> {
    Activity activity;
    int itemResourceId;

    List<Habitat> items;

    public HabitatAdapter(Activity activity, int itemResourceId, List<Habitat> items) {
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

        Habitat habitat = items.get(position);

        TextView nameTV = layout.findViewById(R.id.nom);
        TextView numTV = layout.findViewById(R.id.num);
        TextView etageTV = layout.findViewById(R.id.etage);


        LinearLayout iconContainer = layout.findViewById(R.id.icon_container);
        iconContainer.removeAllViews();

        nameTV.setText(habitat.getResidentName());
        numTV.setText(habitat.nbAppliance() + " appareils");
        etageTV.setText(String.valueOf(habitat.getFloor()));



        for (Appliance appliance : habitat.getAppliances()) {
            String NomAppliance = appliance.name.toLowerCase();  // Assure-toi que le nom est en minuscules

            if (NomAppliance.contains("aspirateur")) {
                addIcon(iconContainer, R.drawable.ic_aspirateur);
            } else if (NomAppliance.contains("climatiseur")) {
                addIcon(iconContainer, R.drawable.ic_climatiseur);
            } else if (NomAppliance.contains("fer à repasser")) {
                addIcon(iconContainer, R.drawable.ic_fer_a_repasser);
            } else if (NomAppliance.contains("machine à laver")) {
                addIcon(iconContainer, R.drawable.ic_machine_a_laver);
            }
        }

        return layout;
    }

    private void addIcon(LinearLayout iconContainer, int iconResId) {
        ImageView icon = new ImageView(activity);
        icon.setImageResource(iconResId);

        float density = activity.getResources().getDisplayMetrics().density;
        int sizeInDp = 24;
        int sizeInPx = (int) (sizeInDp * density);
        LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(sizeInPx, sizeInPx);
        icon.setLayoutParams(layoutParams);

        iconContainer.addView(icon);
    }


}
