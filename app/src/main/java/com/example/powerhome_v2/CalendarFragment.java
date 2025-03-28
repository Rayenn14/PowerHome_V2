package com.example.powerhome_v2;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.koushikdutta.ion.Ion;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class CalendarFragment extends Fragment {
    private LinearLayout minuitToSix, sixToMidi, midiToDixHuit, dixHuitToVingt;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_calendar, container, false);

        // Initialisation des vues
        minuitToSix = view.findViewById(R.id.minuitToSix);
        sixToMidi = view.findViewById(R.id.sixToMidi);
        midiToDixHuit = view.findViewById(R.id.midiToDixHuit);
        dixHuitToVingt = view.findViewById(R.id.dixHuitToVingtTrois);

        loadConsumptionData();

        return view;
    }

    private void loadConsumptionData() {
        SharedPreferences prefs = getActivity().getSharedPreferences("UserPrefs", getActivity().MODE_PRIVATE);
        String token = prefs.getString("user_token", null);
        String URL = "http://192.168.1.18/PowerHome/getConsumption.php?token=" + token;

        Ion.with(getActivity())
                .load(URL)
                .asString()
                .setCallback((e, result) -> {
                    if (e != null) {
                        Toast.makeText(getActivity(), "Erreur de connexion", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    Log.d("API Response", result);

                    try {
                        // Parse the response as a JSONArray
                        JSONArray jsonArray = new JSONArray(result);
                        updateUI(jsonArray);
                    } catch (JSONException jsonException) {
                        jsonException.printStackTrace();
                        Toast.makeText(getActivity(), "Erreur de parsing JSON", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void updateUI(JSONArray jsonArray) {
        try {
            // Assurer que le JSON contient les informations nécessaires pour chaque créneau horaire
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject timeSlot = jsonArray.getJSONObject(i);

                // Récupérer les informations sur les consommations et les max_wattage
                String timeSlotLabel = timeSlot.getString("begin") + "-" + timeSlot.getString("end");

                // Convertir max_wattage et total_consumption en entiers
                int consumption = Integer.parseInt(timeSlot.getString("total_consumption"));
                int maxWattage = Integer.parseInt(timeSlot.getString("max_wattage"));

                // Déterminer quel créneau horaire mettre à jour
                switch (timeSlotLabel) {
                    case "2025-06-25 00:00:00-2025-06-25 05:59:00":
                        setColor(minuitToSix, consumption, maxWattage);
                        break;
                    case "2025-06-25 06:00:00-2025-06-25 11:59:00":
                        setColor(sixToMidi, consumption, maxWattage);
                        break;
                    case "2025-06-25 12:00:00-2025-06-25 17:59:00":
                        setColor(midiToDixHuit, consumption, maxWattage);
                        break;
                    case "2025-06-25 18:00:00-2025-06-25 23:59:00":
                        setColor(dixHuitToVingt, consumption, maxWattage);
                        break;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }

    private void setColor(LinearLayout layout, int consumption, int maxWattage) {
        // S'assurer que maxWattage est supérieur à 0 pour éviter une division par 0
        if (maxWattage == 0) {
            layout.setBackgroundColor(Color.GRAY); // Gris si max_wattage est 0
            return;
        }

        // Calculer le pourcentage de consommation par rapport au max_wattage
        double percentage = (double) consumption / maxWattage * 100;

        // Déterminer la couleur en fonction de ce pourcentage
        if (percentage <= 30) {
            layout.setBackgroundColor(Color.GREEN); // Vert
        } else if (percentage <= 70) {
            layout.setBackgroundColor(Color.parseColor("#FFA500")); // Orange
        } else if (percentage <= 100) {
            layout.setBackgroundColor(Color.RED); // Rouge
        } else {
            layout.setBackgroundColor(Color.GRAY); // Gris pour les valeurs au-delà de 100%
        }
    }

}
