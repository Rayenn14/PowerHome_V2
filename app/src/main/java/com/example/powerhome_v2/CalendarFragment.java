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
                        // Parse the response as a JSONArray instead of JSONObject
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
            // Parcours du tableau JSON pour récupérer la consommation pour chaque créneau horaire
            for (int i = 0; i < jsonArray.length(); i++) {
                JSONObject jsonObject = jsonArray.getJSONObject(i);
                String begin = jsonObject.getString("begin");
                String end = jsonObject.getString("end");
                int totalConsumption = jsonObject.getInt("total_consumption");

                // Associer les créneaux horaires à leurs vues respectives
                if (begin.equals("2025-06-25 00:00:00") && end.equals("2025-06-25 05:59:00")) {
                    setColor(minuitToSix, totalConsumption);
                } else if (begin.equals("2025-06-25 06:00:00") && end.equals("2025-06-25 11:59:00")) {
                    setColor(sixToMidi, totalConsumption);
                } else if (begin.equals("2025-06-25 12:00:00") && end.equals("2025-06-25 17:59:00")) {
                    setColor(midiToDixHuit, totalConsumption);
                } else if (begin.equals("2025-06-25 18:00:00") && end.equals("2025-06-25 23:59:00")) {
                    setColor(dixHuitToVingt, totalConsumption);
                }
            }
        } catch (JSONException e) {
            e.printStackTrace();
        }
    }


    private void setColor(LinearLayout layout, int consumption) {
        if (consumption < 500) {
            layout.setBackgroundColor(Color.GREEN);
        } else if (consumption <= 1500) {
            layout.setBackgroundColor(Color.parseColor("#FFA500"));
        } else {
            layout.setBackgroundColor(Color.RED);
        }
    }
}
