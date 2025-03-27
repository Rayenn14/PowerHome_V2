package com.example.powerhome_v2;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import com.koushikdutta.ion.Ion;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class ElectromenagerActivity extends BaseActivity {

    private ProgressDialog progressDialog;
    private ListView lv;
    private TextView totalConsumptionTV;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActivityContent(R.layout.activity_electromenager);

        lv = findViewById(R.id.listView);
        totalConsumptionTV = findViewById(R.id.total_consumption);

        ImageButton btnAddAppliance = findViewById(R.id.btn_add_appliance);
        btnAddAppliance.setOnClickListener(v -> showAddApplianceDialog());

        String token = getUserToken();
        if (token != null && !token.isEmpty()) {
            fetchAppliances(token);
        } else {
            Toast.makeText(this, "Token manquant", Toast.LENGTH_SHORT).show();
        }
    }

    private String getUserToken() {
        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        return prefs.getString("user_token", null);
    }

    private void fetchAppliances(String token) {
        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Chargement des équipements...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        String url = "http://192.168.1.18/PowerHome/getAppliancesByUser.php?token=" + token;

        Ion.with(this)
                .load(url)
                .asString()
                .setCallback((e, result) -> {
                    progressDialog.dismiss();

                    if (e != null) {
                        Toast.makeText(this, "Erreur de connexion", Toast.LENGTH_SHORT).show();
                        return;
                    }

                    try {
                        JSONArray appliancesArray = new JSONArray(result);
                        List<Appliance> appliances = new ArrayList<>();

                        int totalConsumption = 0;
                        for (int i = 0; i < appliancesArray.length(); i++) {
                            JSONObject applianceJson = appliancesArray.getJSONObject(i);
                            int wattage = applianceJson.getInt("wattage");
                            appliances.add(new Appliance(
                                    applianceJson.getInt("id"),
                                    applianceJson.getString("name"),
                                    applianceJson.getString("reference"),
                                    wattage
                            ));
                            totalConsumption += wattage;
                        }

                        lv.setAdapter(new ApplianceAdapter(this, R.layout.item_equipement, appliances));
                        totalConsumptionTV.setText("Consommation Totale: " + totalConsumption + "W");
                    } catch (JSONException jsonException) {
                        Toast.makeText(this, "Aucun équipement trouvé", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void showAddApplianceDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(this);
        builder.setTitle("Ajouter un équipement");

        LinearLayout layout = new LinearLayout(this);
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(20, 20, 20, 20);

        final Spinner nameSpinner = new Spinner(this);
        String[] appliances = {"Aspirateur", "Climatiseur", "Fer à repasser", "Machine à laver", "Micro-ondes"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_dropdown_item, appliances);
        nameSpinner.setAdapter(adapter);
        layout.addView(nameSpinner);

        final EditText referenceInput = new EditText(this);
        referenceInput.setHint("Référence");
        layout.addView(referenceInput);

        final EditText wattageInput = new EditText(this);
        wattageInput.setHint("Consommation (W)");
        wattageInput.setInputType(android.text.InputType.TYPE_CLASS_NUMBER);
        layout.addView(wattageInput);

        Button submitButton = new Button(this);
        submitButton.setText("Soumettre");
        layout.addView(submitButton);

        builder.setView(layout);
        AlertDialog dialog = builder.create();
        dialog.show();

        submitButton.setOnClickListener(v -> {
            String name = nameSpinner.getSelectedItem().toString();
            String reference = referenceInput.getText().toString().trim();
            String wattage = wattageInput.getText().toString().trim();
            String token = getUserToken();

            if (!reference.isEmpty() && !wattage.isEmpty()) {
                addAppliance(name, reference, wattage, token);
                dialog.dismiss();
            } else {
                Toast.makeText(this, "Tous les champs sont obligatoires", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addAppliance(String name, String reference, String wattage, String token) {
        String url = "http://192.168.1.18/PowerHome/addAppliance.php";

        Ion.with(this)
                .load("POST", url)
                .setBodyParameter("name", name)
                .setBodyParameter("reference", reference)
                .setBodyParameter("wattage", wattage)
                .setBodyParameter("token", token)
                .asString()
                .setCallback((e, result) -> {
                    if (e != null) {
                        Toast.makeText(this, "Erreur d'ajout", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Toast.makeText(this, "Équipement ajouté avec succès !", Toast.LENGTH_SHORT).show();
                    fetchAppliances(token);
                });
    }
}
