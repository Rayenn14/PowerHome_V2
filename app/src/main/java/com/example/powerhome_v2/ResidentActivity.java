package com.example.powerhome_v2;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.ListView;
import android.widget.Toast;
import com.koushikdutta.ion.Ion;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import java.util.ArrayList;
import java.util.List;

public class ResidentActivity extends BaseActivity {
    private ListView listView;
    private HabitatAdapter adapter;
    private List<Habitat> habitatList;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setActivityContent(R.layout.activity_residents);
        listView = findViewById(R.id.listView);
        habitatList = new ArrayList<>();
        adapter = new HabitatAdapter(this, R.layout.item_habitat, habitatList);
        listView.setAdapter(adapter);
        loadHabitatData();
    }

    private void loadHabitatData() {
        SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
        String token = prefs.getString("user_token", null);
        String URL = "http://192.168.1.18/PowerHome/getHabitats_v2.php?token=" + token;

        Ion.with(this)
                .load(URL)
                .asString()
                .setCallback((e, result) -> {
                    if (e != null) {
                        Toast.makeText(this, "Erreur de connexion", Toast.LENGTH_SHORT).show();
                        return;
                    }
                    System.out.println("JSON reçu: " + result);

                    try {
                        JSONArray jsonArray = new JSONArray(result);
                        habitatList.clear();
                        for (int i = 0; i < jsonArray.length(); i++) {
                            JSONObject jsonHabitat = jsonArray.getJSONObject(i);
                            int id = jsonHabitat.getInt("id");
                            int floor = jsonHabitat.getInt("floor");
                            double area = jsonHabitat.getDouble("area");

                            // Récupère le prénom et le nom du résident
                            String firstname = jsonHabitat.getString("firstname");
                            String lastname = jsonHabitat.getString("lastname");

                            // Combine prénom et nom pour le nom du résident
                            String residentName = firstname + " " + lastname;

                            List<Appliance> appliances = new ArrayList<>();
                            JSONArray jsonAppliances = jsonHabitat.getJSONArray("appliances");
                            for (int j = 0; j < jsonAppliances.length(); j++) {
                                JSONObject jsonAppliance = jsonAppliances.getJSONObject(j);
                                appliances.add(new Appliance(
                                        jsonAppliance.getInt("id"),
                                        jsonAppliance.getString("name"),
                                        jsonAppliance.getString("reference"),
                                        jsonAppliance.getInt("wattage")
                                ));
                            }

                            // Ajoute l'habitat à la liste avec le nouveau nom de résident
                            habitatList.add(new Habitat(id, residentName, floor, area, appliances));
                        }
                        adapter.notifyDataSetChanged();
                    } catch (JSONException jsonException) {
                        jsonException.printStackTrace();
                        Toast.makeText(this, "Erreur de parsing JSON", Toast.LENGTH_SHORT).show();
                    }
                });
    }

}
