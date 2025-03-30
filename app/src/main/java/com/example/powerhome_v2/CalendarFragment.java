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
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
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
    private ImageButton jour1, jour2, jour3, jour4, jour5, jour6, jour7;
    private TextView maxWattageMinuitToSix, pourcentageMinuitToSix,
            maxWattageSixToMidi, pourcentageSixToMidi,
            maxWattageMidiToDixHuit, pourcentageMidiToDixHuit,
            maxWattageDixHuitToVingt, pourcentageDixHuitToVingt,
            currentDateTextView;
    private int currentConsumptionMinuitToSix, maxWattageMinuitToSixValue, currentConsumptionSixToMidi, maxWattageSixToMidiValue,
            currentConsumptionMidiToDixHuit, maxWattageMidiToDixHuitValue,
            currentConsumptionDixHuitToVingt, maxWattageDixHuitToVingtValue;


    private final String[] dates = {
            "Mercredi 20 Juin 2025",
            "Jeudi 21 Juin 2025",
            "Vendredi 22 Juin 2025",
            "Samedi 23 Juin 2025",
            "Dimanche 24 Juin 2025",
            "Lundi 25 Juin 2025",
            "Mardi 26 Juin 2025"
    };

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.activity_calendar, container, false);

        minuitToSix = view.findViewById(R.id.minuitToSix);
        sixToMidi = view.findViewById(R.id.sixToMidi);
        midiToDixHuit = view.findViewById(R.id.midiToDixHuit);
        dixHuitToVingt = view.findViewById(R.id.dixHuitToVingtTrois);

        maxWattageMinuitToSix = view.findViewById(R.id.maxWattageMinuitToSix);
        pourcentageMinuitToSix = view.findViewById(R.id.pourcentageMinuitToSix);

        maxWattageSixToMidi = view.findViewById(R.id.maxWattageSixToMidi);
        pourcentageSixToMidi = view.findViewById(R.id.pourcentageSixToMidi);

        maxWattageMidiToDixHuit = view.findViewById(R.id.maxWattageMidiToDixHuit);
        pourcentageMidiToDixHuit = view.findViewById(R.id.pourcentageMidiToDixHuit);

        maxWattageDixHuitToVingt = view.findViewById(R.id.maxWattageDixHuitToVingt);
        pourcentageDixHuitToVingt = view.findViewById(R.id.pourcentageDixHuitToVingt);

        btnAddReservation = view.findViewById(R.id.btn_add_reservation);
        btnAddReservation.setOnClickListener(v -> showAddReservationDialog());

        currentDateTextView = view.findViewById(R.id.currentDateTextView);

        jour1 = view.findViewById(R.id.jour1);
        jour2 = view.findViewById(R.id.jour2);
        jour3 = view.findViewById(R.id.jour3);
        jour4 = view.findViewById(R.id.jour4);
        jour5 = view.findViewById(R.id.jour5);
        jour6 = view.findViewById(R.id.jour6);
        jour7 = view.findViewById(R.id.jour7);

        View.OnClickListener dayClickListener = v -> {
            int dayIndex = getDayIndex(v.getId());
            updateDateDisplay(dayIndex);
            updateDaySelection((ImageButton) v);

            if (v.getId() == R.id.jour1) {
                loadConsumptionData();
            } else {
                reinitialiserCases();
            }
        };

        jour1.setOnClickListener(dayClickListener);
        jour2.setOnClickListener(dayClickListener);
        jour3.setOnClickListener(dayClickListener);
        jour4.setOnClickListener(dayClickListener);
        jour5.setOnClickListener(dayClickListener);
        jour6.setOnClickListener(dayClickListener);
        jour7.setOnClickListener(dayClickListener);

        loadConsumptionData();

        return view;
    }

    private void updateDateDisplay(int dayIndex) {
        if (currentDateTextView != null && dayIndex >= 0 && dayIndex < dates.length) {
            currentDateTextView.setText(dates[dayIndex]);
        }
    }

    private int getDayIndex(int viewId) {
        if (viewId == R.id.jour1) return 0;
        else if (viewId == R.id.jour2) return 1;
        else if (viewId == R.id.jour3) return 2;
        else if (viewId == R.id.jour4) return 3;
        else if (viewId == R.id.jour5) return 4;
        else if (viewId == R.id.jour6) return 5;
        else if (viewId == R.id.jour7) return 6;
        return 0;
    }

    private void loadConsumptionData() {
        SharedPreferences prefs = getActivity().getSharedPreferences("UserPrefs", getActivity().MODE_PRIVATE);
        String token = prefs.getString("user_token", null);
        String URL = "http://192.168.1.80/PowerHome/getConsumption.php?token=" + token;

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
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject timeSlot = jsonArray.getJSONObject(i);

                int consumption = Integer.parseInt(timeSlot.getString("total_consumption"));
                int maxWattage = Integer.parseInt(timeSlot.getString("max_wattage"));
                double percentage = (maxWattage == 0) ? 0 : (double) consumption / maxWattage * 100;

                String timeSlotLabel = timeSlot.getString("begin") + "-" + timeSlot.getString("end");

                switch (timeSlotLabel) {
                    case "2025-06-25 00:00:00-2025-06-25 05:59:00":
                        setColor(minuitToSix, consumption, maxWattage);
                        maxWattageMinuitToSix.setText("Max: " + maxWattage + "W");
                        pourcentageMinuitToSix.setText("Usage: " + String.format("%.1f", percentage) + "%");
                        currentConsumptionMinuitToSix = consumption;
                        maxWattageMinuitToSixValue = maxWattage;
                        break;
                    case "2025-06-25 06:00:00-2025-06-25 11:59:00":
                        setColor(sixToMidi, consumption, maxWattage);
                        maxWattageSixToMidi.setText("Max: " + maxWattage + "W");
                        pourcentageSixToMidi.setText("Usage: " + String.format("%.1f", percentage) + "%");
                        currentConsumptionSixToMidi = consumption;
                        maxWattageSixToMidiValue = maxWattage;
                        break;
                    case "2025-06-25 12:00:00-2025-06-25 17:59:00":
                        setColor(midiToDixHuit, consumption, maxWattage);
                        maxWattageMidiToDixHuit.setText("Max: " + maxWattage + "W");
                        pourcentageMidiToDixHuit.setText("Usage: " + String.format("%.1f", percentage) + "%");
                        currentConsumptionMidiToDixHuit = consumption;
                        maxWattageMidiToDixHuitValue = maxWattage;
                        break;
                    case "2025-06-25 18:00:00-2025-06-25 23:59:00":
                        setColor(dixHuitToVingt, consumption, maxWattage);
                        maxWattageDixHuitToVingt.setText("Max: " + maxWattage + "W");
                        pourcentageDixHuitToVingt.setText("Usage: " + String.format("%.1f", percentage) + "%");
                        currentConsumptionDixHuitToVingt = consumption;
                        maxWattageDixHuitToVingtValue = maxWattage;
                        break;
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void setColor(LinearLayout layout, int consumption, int maxWattage) {
        if (maxWattage == 0) {
            layout.setBackgroundColor(Color.GRAY); // Gris si max_wattage est 0
            return;
        }
        double percentage = (double) consumption / maxWattage * 100;
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

        LinearLayout layout = new LinearLayout(getActivity());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(20, 20, 20, 20);

        final Spinner applianceSpinner = new Spinner(getActivity());
        List<String> applianceNames = new ArrayList<>();
        applianceNames.add("Chargement des équipements...");

        ArrayAdapter<String> applianceAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, applianceNames);
        applianceSpinner.setAdapter(applianceAdapter);
        layout.addView(applianceSpinner);

        final Spinner timeSlotSpinner = new Spinner(getActivity());
        String[] timeSlots = {
                "00:00 - 06:00", "06:00 - 12:00", "12:00 - 18:00", "18:00 - 00:00"
        };
        ArrayAdapter<String> timeSlotAdapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, timeSlots);
        timeSlotSpinner.setAdapter(timeSlotAdapter);
        layout.addView(timeSlotSpinner);

        Button submitButton = new Button(getActivity());
        submitButton.setText("Soumettre");
        layout.addView(submitButton);

        builder.setView(layout);
        AlertDialog dialog = builder.create();
        dialog.show();

        SharedPreferences prefs = getActivity().getSharedPreferences("UserPrefs", getActivity().MODE_PRIVATE);
        String token = prefs.getString("user_token", null);
        if (token != null && !token.isEmpty()) {
            fetchUserAppliances(token, applianceSpinner, applianceAdapter);
        }

        submitButton.setOnClickListener(v -> {
            String appliance = applianceSpinner.getSelectedItem().toString();
            String timeSlot = timeSlotSpinner.getSelectedItem().toString();

            if (appliance.equals("Sélectionner un équipement") || timeSlot == null) {
                Toast.makeText(getActivity(), "Veuillez sélectionner un équipement et un créneau horaire.", Toast.LENGTH_SHORT).show();
                return;
            }

            List<Appliance> appliances = (List<Appliance>) applianceSpinner.getTag();
            int selectedPosition = applianceSpinner.getSelectedItemPosition();
            Appliance selectedAppliance = appliances.get(selectedPosition - 1);
            int applianceWattage = selectedAppliance.getWattage();

            // Récupération des données du créneau
            int timeSlotValue = getTimeSlotValue(timeSlot);
            int currentConsumption;
            int maxWattage;

            switch (timeSlotValue) {
                case 1:
                    currentConsumption = currentConsumptionMinuitToSix;
                    maxWattage = maxWattageMinuitToSixValue;
                    break;
                case 2:
                    currentConsumption = currentConsumptionSixToMidi;
                    maxWattage = maxWattageSixToMidiValue;
                    break;
                case 3:
                    currentConsumption = currentConsumptionMidiToDixHuit;
                    maxWattage = maxWattageMidiToDixHuitValue;
                    break;
                case 4:
                    currentConsumption = currentConsumptionDixHuitToVingt;
                    maxWattage = maxWattageDixHuitToVingtValue;
                    break;
                default:
                    Toast.makeText(getActivity(), "Créneau invalide", Toast.LENGTH_SHORT).show();
                    return;
            }

            // Vérification de la consommation
            if (currentConsumption + applianceWattage > maxWattage) {
                Toast.makeText(getActivity(), "La consommation maximale de " + maxWattage + "W serait dépassée !", Toast.LENGTH_LONG).show();
                return;
            }

            // Ajout de la réservation
            addReservationToDatabase(token, selectedAppliance.getId(), timeSlotValue);
            dialog.dismiss();
            loadConsumptionData();
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
                return 0;
        }
    }

    private void fetchUserAppliances(String token, final Spinner applianceSpinner, final ArrayAdapter<String> applianceAdapter) {
        String url = "http://192.168.1.80/PowerHome/getAppliancesByUser.php?token=" + token;

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
                        List<Appliance> appliancesList = new ArrayList<>();
                        List<String> applianceNames = new ArrayList<>();
                        applianceNames.add("Sélectionner un équipement");

                        for (int i = 0; i < appliancesArray.length(); i++) {
                            JSONObject applianceJson = appliancesArray.getJSONObject(i);
                            Appliance appliance = new Appliance(
                                    applianceJson.getInt("id"),
                                    applianceJson.getString("name"),
                                    applianceJson.getString("reference"),
                                    applianceJson.getInt("wattage")
                            );
                            appliancesList.add(appliance);
                            applianceNames.add(appliance.getName());
                        }
                        applianceAdapter.clear();
                        applianceAdapter.addAll(applianceNames);
                        applianceAdapter.notifyDataSetChanged();
                        applianceSpinner.setTag(appliancesList);
                    } catch (JSONException jsonException) {
                        Toast.makeText(getActivity(), "Aucun équipement trouvé", Toast.LENGTH_SHORT).show();
                    }
                });
    }
    private void addReservationToDatabase(String token, int applianceId, int timeSlotValue) {
        String url = "http://192.168.1.80/PowerHome/add_appliance_to_timeslot.php" + "?token=" + token + "&appliance_id=" + applianceId + "&time_slot=" + timeSlotValue;
        Ion.with(getActivity())
                .load(url)
                .asString()
                .setCallback((e, result) -> {
                    try {
                        String cleanResult = result.replaceAll("<[^>]*>", "").trim();
                        JSONObject jsonResponse = new JSONObject(cleanResult);

                        if (jsonResponse.getString("status").equals("success")) {
                            loadConsumptionData();
                        } else {
                            // Ajoutez ici la gestion spécifique des erreurs
                            String errorMessage = jsonResponse.optString("message", "Erreur inconnue");

                            if (errorMessage.toLowerCase().contains("déjà réservé") ||
                                    errorMessage.toLowerCase().contains("duplicate entry")) {
                                Toast.makeText(getActivity(),
                                        "Cet appareil est déjà réservé pour ce créneau",
                                        Toast.LENGTH_LONG).show();
                            } else {
                                Toast.makeText(getActivity(),
                                        "Erreur: " + errorMessage,
                                        Toast.LENGTH_LONG).show();
                            }
                        }
                    } catch (Exception ex) {
                        Log.e("JSON Parse", "Raw response: " + result);
                        ex.printStackTrace();
                        Toast.makeText(getActivity(),
                                "Erreur de traitement de la réponse",
                                Toast.LENGTH_SHORT).show();
                    }
                });
    }
    public void reinitialiserCases(){
        maxWattageMinuitToSix.setText(" ");
        pourcentageMinuitToSix.setText(" ");
        maxWattageSixToMidi.setText(" ");
        pourcentageSixToMidi.setText(" ");
        maxWattageMidiToDixHuit.setText(" ");
        pourcentageMidiToDixHuit.setText(" ");
        maxWattageDixHuitToVingt.setText(" ");
        pourcentageDixHuitToVingt.setText(" ");
        setColor(minuitToSix, 0, 100);
        setColor(sixToMidi, 0, 100);
        setColor(midiToDixHuit, 0, 100);
        setColor(dixHuitToVingt, 0, 100);
    }
    private void updateDaySelection(ImageButton selectedDay) {
        int[] dayButtons = {R.id.jour1, R.id.jour2, R.id.jour3, R.id.jour4, R.id.jour5, R.id.jour6, R.id.jour7};

        for (int id : dayButtons) {
            ImageButton btn = getView().findViewById(id);
            if (btn == selectedDay) {
                btn.setImageResource(R.drawable.btn1);
            } else {
                btn.setImageResource(R.drawable.btn2);
            }
        }
    }
}