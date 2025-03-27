package com.example.powerhome_v2;

import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ElectromenagerActivity extends BaseActivity {

    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActivityContent(R.layout.activity_electromenager);

        ListView lv = findViewById(R.id.listView);
        TextView totalConsumptionTV = findViewById(R.id.total_consumption);

        // Récupérer le token depuis les SharedPreferences
        String token = getUserToken();
        if (token != null && !token.isEmpty()) {
            // Lancer la récupération des équipements
            fetchAppliances(token, lv, totalConsumptionTV);
        } else {
            Toast.makeText(this, "Token manquant", Toast.LENGTH_SHORT).show();
        }
    }

    private String getUserToken() {
        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        return prefs.getString("user_token", null);
    }

    private void fetchAppliances(String token, ListView lv, TextView totalConsumptionTV) {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Chargement des équipements...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        // URL de l'API pour récupérer les équipements
        String url = "http://192.168.1.18/PowerHome/getAppliancesByUser.php?token=" + token;

        Ion.with(this)
                .load(url)
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        progressDialog.dismiss();

                        if (e != null) {
                            Toast.makeText(ElectromenagerActivity.this, "Erreur de connexion", Toast.LENGTH_SHORT).show();
                            return;
                        }

                        Log.d("API Response", result);

                        try {
                            JSONArray appliancesArray = new JSONArray(result);
                            List<Appliance> appliances = new ArrayList<>();

                            for (int i = 0; i < appliancesArray.length(); i++) {
                                JSONObject applianceJson = appliancesArray.getJSONObject(i);
                                appliances.add(new Appliance(
                                        applianceJson.getInt("id"),
                                        applianceJson.getString("name"),
                                        applianceJson.getString("reference"),
                                        applianceJson.getInt("wattage")
                                ));
                            }

                            if (!appliances.isEmpty()) {
                                // Créer l'adaptateur et l'afficher dans la ListView
                                ApplianceAdapter applianceAdapter = new ApplianceAdapter(ElectromenagerActivity.this, R.layout.item_equipement, appliances);
                                lv.setAdapter(applianceAdapter);

                                // Calculer la consommation totale
                                int totalConsumption = 0;
                                for (Appliance appliance : appliances) {
                                    totalConsumption += appliance.getWattage();
                                }

                                // Mettre à jour le TextView de consommation totale
                                totalConsumptionTV.setText("Consommation Totale: " + totalConsumption + "W");
                            } else {
                                Toast.makeText(ElectromenagerActivity.this, "Aucun équipement trouvé", Toast.LENGTH_SHORT).show();
                            }

                        } catch (JSONException jsonException) {
                            Toast.makeText(ElectromenagerActivity.this, "Erreur de données", Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }
}
