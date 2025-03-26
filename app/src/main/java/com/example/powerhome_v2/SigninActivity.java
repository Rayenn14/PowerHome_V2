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

public class SigninActivity extends AppCompatActivity {
    private EditText emailField, passwordField;
    private ImageButton loginButton;
    private ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);


        emailField = findViewById(R.id.email);
        passwordField = findViewById(R.id.mdp);
        loginButton = findViewById(R.id.BoutonNext);

        TextView creerCompte = findViewById(R.id.Creeruncompteclick);
        TextView mdpOublie = findViewById(R.id.Mdpoublieeclick);
        ImageButton goBackTo = findViewById(R.id.RetourSplash);


        creerCompte.setOnClickListener(v -> startActivity(new Intent(SigninActivity.this, RegisterActivity.class)));
        mdpOublie.setOnClickListener(v -> startActivity(new Intent(SigninActivity.this, ForgetpwdActivity.class)));
        goBackTo.setOnClickListener(v -> startActivity(new Intent(SigninActivity.this, SplashActivity.class)));

        // Gestion du bouton de connexion
        loginButton.setOnClickListener(v -> loginUser());
    }

    private void loginUser() {
        String email = emailField.getText().toString().trim();
        String password = passwordField.getText().toString().trim();

        if (email.isEmpty() || password.isEmpty()) {
            Toast.makeText(this, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show();
            return;
        }

        progressDialog = new ProgressDialog(this);
        progressDialog.setMessage("Connexion en cours...");
        progressDialog.setCancelable(false);
        progressDialog.show();

        // URL du serveur (Service WEB), NE PAS OUBLIER DE METTRE SON IP ! Important
        String url = "http://192.168.1.18/PowerHome/login.php?email=" + email + "&password=" + password;

        Ion.with(this)
                .load(url)
                .asString()
                .setCallback(new FutureCallback<String>() {
                    @Override
                    public void onCompleted(Exception e, String result) {
                        progressDialog.dismiss();

                        if (e != null) {
                            Toast.makeText(SigninActivity.this, "Erreur de connexion", Toast.LENGTH_SHORT).show();
                            Log.e("LoginError", e.getMessage());
                            return;
                        }

                        try {
                            JSONObject response = new JSONObject(result);

                            if (response.has("token")) {
                                String token = response.getString("token");

                                Toast.makeText(SigninActivity.this, "Connexion réussie", Toast.LENGTH_SHORT).show();


                                getSharedPreferences("UserPrefs", MODE_PRIVATE)
                                        .edit()
                                        .putString("user_token", token)
                                        .apply();

                                // Redirige vers la 2e activité, la Resident pour les tests
                                Intent intent = new Intent(SigninActivity.this, ResidentActivity.class);
                                startActivity(intent);
                                finish();
                            }
                        } catch (JSONException jsonException) {
                            Toast.makeText(SigninActivity.this, "email ou mot de passe incorrect", Toast.LENGTH_SHORT).show();
                            Log.e("LoginError", "JSON Exception: " + jsonException.getMessage());
                        }
                    }
                });
    }
}
