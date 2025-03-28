package com.example.powerhome_v2;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;
import androidx.appcompat.app.AppCompatActivity;
import com.koushikdutta.async.future.FutureCallback;
import com.koushikdutta.ion.Ion;
import org.json.JSONException;
import org.json.JSONObject;

public class RegisterActivity extends AppCompatActivity {
    private EditText nomField, prenomField, emailField, passwordField, etageField, superfaceField;
    private ImageButton registerButton;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);

        nomField = findViewById(R.id.nomregister);
        prenomField = findViewById(R.id.prenomegister);
        emailField = findViewById(R.id.emailegister);
        passwordField = findViewById(R.id.mdpregister);
        etageField = findViewById(R.id.etageegister);
        superfaceField = findViewById(R.id.superfaceegister);
        registerButton = findViewById(R.id.BoutonNext);

        TextView goBackToSignIn = findViewById(R.id.Seconnecterclick);
        goBackToSignIn.setOnClickListener(v -> {
            Intent intent = new Intent(RegisterActivity.this, ValidationActivity.class);
            startActivity(intent);
        });

        registerButton.setOnClickListener(v -> registerUser());
    }

    private void registerUser() {
        String firstname = nomField.getText().toString().trim();
        String lastname = prenomField.getText().toString().trim();
        String email = emailField.getText().toString().trim();
        String password = passwordField.getText().toString().trim();
        String floor = etageField.getText().toString().trim();
        String area = superfaceField.getText().toString().trim();

        if (firstname.isEmpty() || lastname.isEmpty() || email.isEmpty() || password.isEmpty() || floor.isEmpty() || area.isEmpty()) {
            Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Inscription en cours...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        String url = "http://192.168.1.18/PowerHome/signup.php?"
                + "firstname=" + firstname + "&lastname=" + lastname
                + "&email=" + email + "&password=" + password
                + "&floor=" + floor + "&area=" + area;

        Ion.with(this)
                .load(url)
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        progressDialog.dismiss();
                        if (e != null) {
                            Toast.makeText(RegisterActivity.this, "Erreur de connexion", Toast.LENGTH_SHORT).show();
                            Log.e("SignupError", e.getMessage());
                            return;
                        }
                        try {
                            JSONObject response = new JSONObject(result);
                            if (response.has("token")) {
                                String token = response.getString("token");

                                Toast.makeText(RegisterActivity.this, "Inscription réussie", Toast.LENGTH_SHORT).show();

                                getSharedPreferences("UserPrefs", MODE_PRIVATE)
                                        .edit()
                                        .putString("user_token", token)
                                        .apply();
                                Intent intent = new Intent(RegisterActivity.this, ValidationActivity.class);
                                startActivity(intent);
                                finish();
                            } else {
                                Toast.makeText(RegisterActivity.this, "Email déjà utilisé ou erreur d'inscription", Toast.LENGTH_SHORT).show();
                            }
                        } catch (JSONException jsonException) {
                            Toast.makeText(RegisterActivity.this, "Erreur de réponse JSON", Toast.LENGTH_SHORT).show();
                            Log.e("SignupError", "JSON Exception: " + jsonException.getMessage());
                        }
                    }
                });
    }
}
