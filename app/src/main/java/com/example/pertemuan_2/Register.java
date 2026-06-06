package com.example.pertemuan_2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class Register extends AppCompatActivity {

    private TextInputEditText etRegName, etRegUsername, etRegPassword;
    private TextInputLayout tilRegName, tilRegUsername, tilRegPassword;
    private MaterialButton btnDoRegister, btnBackToLogin;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_register);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Initialize views
        tilRegName = findViewById(R.id.tilRegName);
        tilRegUsername = findViewById(R.id.tilRegUsername);
        tilRegPassword = findViewById(R.id.tilRegPassword);
        etRegName = findViewById(R.id.etRegName);
        etRegUsername = findViewById(R.id.etRegUsername);
        etRegPassword = findViewById(R.id.etRegPassword);
        btnDoRegister = findViewById(R.id.btnDoRegister);
        btnBackToLogin = findViewById(R.id.btnBackToLogin);

        // Animate register card on start
        android.view.View registerCard = findViewById(R.id.registerCard);
        if (registerCard != null) {
            registerCard.setAlpha(0f);
            registerCard.setTranslationY(60f);
            registerCard.animate()
                    .alpha(1f)
                    .translationY(0f)
                    .setDuration(1000)
                    .setInterpolator(new android.view.animation.DecelerateInterpolator())
                    .start();
        }

        // Register button action with user registration saving

        btnDoRegister.setOnClickListener(v -> {
            String name = etRegName.getText() != null ? etRegName.getText().toString().trim() : "";
            String username = etRegUsername.getText() != null ? etRegUsername.getText().toString().trim() : "";
            String password = etRegPassword.getText() != null ? etRegPassword.getText().toString().trim() : "";

            boolean hasError = false;

            if (name.isEmpty()) {
                tilRegName.setError("Nama Lengkap tidak boleh kosong!");
                hasError = true;
            } else {
                tilRegName.setError(null);
            }

            if (username.isEmpty()) {
                tilRegUsername.setError("Username tidak boleh kosong!");
                hasError = true;
            } else {
                tilRegUsername.setError(null);
            }

            if (password.isEmpty()) {
                tilRegPassword.setError("Kata Sandi tidak boleh kosong!");
                hasError = true;
            } else if (password.length() < 6) {
                tilRegPassword.setError("Kata Sandi minimal 6 karakter!");
                hasError = true;
            } else {
                tilRegPassword.setError(null);
            }

            if (hasError) return;

            SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
            
            // Check if username is already registered in the list
            String userList = prefs.getString("user_list", "");
            if (!userList.isEmpty()) {
                String[] entries = userList.split("##");
                for (String entry : entries) {
                    String[] parts = entry.split("\\|");
                    if (parts.length == 3 && parts[0].equalsIgnoreCase(username)) {
                        tilRegUsername.setError("Username sudah terdaftar!");
                        return;
                    }
                }
            }
            if (username.equalsIgnoreCase("admin") || username.equalsIgnoreCase("user")) {
                tilRegUsername.setError("Username sudah digunakan oleh akun bawaan!");
                return;
            }

            // Save credentials to SharedPreferences user list
            String newUserEntry = username + "|" + password + "|" + name;
            if (userList.isEmpty()) {
                userList = newUserEntry;
            } else {
                userList = userList + "##" + newUserEntry;
            }

            SharedPreferences.Editor editor = prefs.edit();
            editor.putString("user_list", userList);
            
            // Legacy fallback keys (updated to the latest registered user)
            editor.putString("registered_username", username);
            editor.putString("registered_password", password);
            editor.putString("registered_name", name);
            
            // Autofill helper keys
            editor.putString("last_reg_username", username);
            editor.putString("last_reg_password", password);
            editor.apply();

            // Show a premium material dialog
            new com.google.android.material.dialog.MaterialAlertDialogBuilder(Register.this)
                    .setTitle("Pendaftaran Berhasil!")
                    .setMessage("Akun Anda berhasil didaftarkan!\n\n• Nama: " + name + "\n• Username: " + username + "\n\nSilakan masuk menggunakan akun baru ini.")
                    .setPositiveButton("Masuk Sekarang", (dialog, which) -> {
                        Intent resultIntent = new Intent();
                        resultIntent.putExtra("registered_username", username);
                        setResult(RESULT_OK, resultIntent);
                        finish();
                    })
                    .setCancelable(false)
                    .show();
        });

        // Back to login
        btnBackToLogin.setOnClickListener(v -> finish());
    }
}

