package com.example.powerhome_v2;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;
import androidx.fragment.app.Fragment;
import com.koushikdutta.ion.Ion;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

public class MonHabitatFragment extends Fragment {
    private ProgressDialog progressDialog;
    private ListView lv;
    private TextView totalConsumptionTV;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        View view = inflater.inflate(R.layout.fragment_mon_habitat, container, false);

        lv = view.findViewById(R.id.listView);
        totalConsumptionTV = view.findViewById(R.id.total_consumption);

        ImageButton btnAddAppliance = view.findViewById(R.id.btn_add_appliance);
        btnAddAppliance.setOnClickListener(v -> showAddApplianceDialog());

        String token = getUserToken();
        if (token != null && !token.isEmpty()) {
            fetchAppliances(token);
        } else {
            Toast.makeText(getActivity(), "Token manquant", Toast.LENGTH_SHORT).show();
        }
        return view;
    }

    private String getUserToken() {
        SharedPreferences prefs = getActivity().getSharedPreferences("UserPrefs", getActivity().MODE_PRIVATE);
        return prefs.getString("user_token", null);
    }

    private void fetchAppliances(String token) {
        progressDialog = new ProgressDialog(getActivity());
        progressDialog.setMessage("Chargement des équipements...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        String url = "http://192.168.1.80/PowerHome/getAppliancesByUser.php?token=" + token;

        Ion.with(getActivity())
                .load(url)
                .asString()
                .setCallback((e, result) -> {
                    progressDialog.dismiss();
                    if (e != null) {
                        Toast.makeText(getActivity(), "Erreur de connexion", Toast.LENGTH_SHORT).show();
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
                        lv.setAdapter(new ApplianceAdapter(getActivity(), R.layout.item_equipement, appliances));
                        totalConsumptionTV.setText("Consommation Totale: " + totalConsumption + "W");
                    } catch (JSONException jsonException) {
                        Toast.makeText(getActivity(), "Aucun équipement trouvé", Toast.LENGTH_SHORT).show();
                    }
                });
    }

    private void showAddApplianceDialog() {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        builder.setTitle("Ajouter un équipement");
        LinearLayout layout = new LinearLayout(getActivity());
        layout.setOrientation(LinearLayout.VERTICAL);
        layout.setPadding(20, 20, 20, 20);
        final Spinner nameSpinner = new Spinner(getActivity());
        String[] appliances = {"Aspirateur", "Climatiseur", "Fer à repasser", "Machine à laver", "Micro-ondes"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(getActivity(), android.R.layout.simple_spinner_dropdown_item, appliances);
        nameSpinner.setAdapter(adapter);
        layout.addView(nameSpinner);
        final EditText referenceInput = new EditText(getActivity());
        referenceInput.setHint("Référence");
        layout.addView(referenceInput);
        final EditText wattageInput = new EditText(getActivity());
        wattageInput.setHint("Consommation (W)");
        wattageInput.setInputType(android.text.InputType.TYPE_CLASS_NUMBER);
        layout.addView(wattageInput);
        Button submitButton = new Button(getActivity());
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
                Toast.makeText(getActivity(), "Tous les champs sont obligatoires", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void addAppliance(String name, String reference, String wattage, String token) {
        String url = "http://192.168.1.80/PowerHome/addAppliance.php";
        Ion.with(getActivity())
                .load("POST", url)
                .setBodyParameter("name", name)
                .setBodyParameter("reference", reference)
                .setBodyParameter("wattage", wattage)
                .setBodyParameter("token", token)
                .asString()
                .setCallback((e, result) -> {
                    if (e != null) {
                        Toast.makeText(getActivity(), "Erreur d'ajout", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    Toast.makeText(getActivity(), "Équipement ajouté avec succès !", Toast.LENGTH_SHORT).show();
                    fetchAppliances(token);
                });
    }
}
