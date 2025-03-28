package com.example.powerhome_v2;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import androidx.appcompat.app.AlertDialog;
import androidx.fragment.app.Fragment;

import com.koushikdutta.ion.Ion;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class CalendarFragment extends Fragment {
    private LinearLayout minuitToSix, sixToMidi, midiToDixHuit, dixHuitToVingt;
    private Button btnAddReservation;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_calendar, container, false);

        // Initialisation des vues
        minuitToSix = view.findViewById(R.id.minuitToSix);
        sixToMidi = view.findViewById(R.id.sixToMidi);
        midiToDixHuit = view.findViewById(R.id.midiToDixHuit);
        dixHuitToVingt = view.findViewById(R.id.dixHuitToVingtTrois);

        // Initialisation du bouton pour ajouter une réservation
        btnAddReservation = view.findViewById(R.id.btn_add_reservation);
        btnAddReservation.setOnClickListener(v -> showAddReservationDialog());

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

    private void showAddReservationDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Ajouter une réservation");

        // Création d'un layout pour le dialogue
        LinearLayout layout = new LinearLayout(getActivity());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(20, 20, 20, 20);

        // Spinner pour choisir l'équipement
        final Spinner applianceSpinner = new Spinner(getActivity());
        List<String> applianceNames = new ArrayList<>();
        applianceNames.add("Chargement des équipements...");

        ArrayAdapter<String> applianceAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, applianceNames);
        applianceSpinner.setAdapter(applianceAdapter);
        layout.addView(applianceSpinner);

        // Spinner pour choisir le créneau horaire
        final Spinner timeSlotSpinner = new Spinner(getActivity());
        String[] timeSlots = {
                "00:00 - 06:00", "06:00 - 12:00", "12:00 - 18:00", "18:00 - 00:00"
        };
        ArrayAdapter<String> timeSlotAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, timeSlots);
        timeSlotSpinner.setAdapter(timeSlotAdapter);
        layout.addView(timeSlotSpinner);

        // Bouton de soumission
        Button submitButton = new Button(getActivity());
        submitButton.setText("Soumettre");
        layout.addView(submitButton);

        // Configuration du builder avec notre layout personnalisé
        builder.setView(layout);
        AlertDialog dialog = builder.create();
        dialog.show();

        // Récupérer les équipements de l'utilisateur connecté
        SharedPreferences prefs = getActivity().getSharedPreferences("UserPrefs", getActivity().MODE_PRIVATE);
        String token = prefs.getString("user_token", null);
        if (token != null && !token.isEmpty()) {
            fetchUserAppliances(token, applianceSpinner, applianceAdapter);
        }

        // Gestion de la soumission du formulaire
        submitButton.setOnClickListener(v -> {
            String appliance = applianceSpinner.getSelectedItem().toString();
            String timeSlot = timeSlotSpinner.getSelectedItem().toString();

            if (appliance.equals("Sélectionner un équipement") || timeSlot == null) {
                Toast.makeText(getActivity(), "Veuillez sélectionner un équipement et un créneau horaire.", Toast.LENGTH_SHORT).show();
                return;
            }

            // Récupérer l'ID de l'équipement en utilisant le tag du spinner
            List<Integer> applianceIds = (List<Integer>) applianceSpinner.getTag();
            int applianceId = applianceIds.get(applianceSpinner.getSelectedItemPosition() - 1); // -1 pour ignorer "Sélectionner un équipement"

            // Envoie l'ID de l'équipement et la valeur numérique pour le créneau horaire
            int timeSlotValue = getTimeSlotValue(timeSlot);
            addReservationToDatabase(token, applianceId, timeSlotValue);
        });
    }


    private int getTimeSlotValue(String timeSlot) {
        switch (timeSlot) {
            case "00:00 - 06:00":
                return 1;
            case "06:00 - 12:00":
                return 2;
            case "12:00 - 18:00":
                return 3;
            case "18:00 - 00:00":
                return 4;
            default:
                return 0; // Valeur par défaut, au cas où quelque chose échoue
        }
    }

    // Fonction pour récupérer les équipements de l'utilisateur connecté et mettre à jour le Spinner
    private void fetchUserAppliances(String token, final Spinner applianceSpinner, final ArrayAdapter<String> applianceAdapter) {
        String url = "http://192.168.1.18/PowerHome/getAppliancesByUser.php?token=" + token;

        Ion.with(getActivity())
                .load(url)
                .asString()
                .setCallback((e, result) -> {
                    if (e != null) {
                        Toast.makeText(getActivity(), "Erreur de connexion", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    try {
                        JSONArray appliancesArray = new JSONArray(result);
                        List<String> applianceNames = new ArrayList<>();
                        final List<Integer> applianceIds = new ArrayList<>(); // Liste pour stocker les IDs des équipements
                        applianceNames.add("Sélectionner un équipement");

                        // Remplir la liste avec les noms des équipements et les IDs
                        for (int i = 0; i < appliancesArray.length(); i++) {
                            JSONObject applianceJson = appliancesArray.getJSONObject(i);
                            applianceNames.add(applianceJson.getString("name"));
                            applianceIds.add(applianceJson.getInt("id")); // Ajouter l'ID de l'équipement
                        }

                        // Met à jour le Spinner avec les noms des équipements
                        applianceAdapter.clear();
                        applianceAdapter.addAll(applianceNames);
                        applianceAdapter.notifyDataSetChanged();

                        // Enregistrez les IDs des équipements dans une variable globale ou dans une méthode interne
                        applianceSpinner.setTag(applianceIds);  // Stocker les IDs dans un tag

                    } catch (JSONException jsonException) {
                        Toast.makeText(getActivity(), "Aucun équipement trouvé", Toast.LENGTH_SHORT).show();
                    }
                });
    }


    private void addReservationToDatabase(String token, int applianceId, int timeSlotValue) {
        String url = "http://192.168.1.18/PowerHome/add_appliance_to_timeslot.php"
                + "?token=" + token
                + "&appliance_id=" + applianceId
                + "&time_slot=" + timeSlotValue;

        Ion.with(getActivity())
                .load(url)
                .asString()
                .setCallback((e, result) -> {
                    try {
                        // Nettoyer la réponse des éventuels caractères parasites
                        String cleanResult = result.replaceAll("<[^>]*>", "").trim();
                        JSONObject jsonResponse = new JSONObject(cleanResult);

                        if (jsonResponse.getString("status").equals("success")) {
                            // Mettre à jour l'UI
                            loadConsumptionData();
                        } else {
                            // Gérer l'erreur
                        }
                    } catch (Exception ex) {
                        Log.e("JSON Parse", "Raw response: " + result);
                        ex.printStackTrace();
                    }
                });
    }

}
