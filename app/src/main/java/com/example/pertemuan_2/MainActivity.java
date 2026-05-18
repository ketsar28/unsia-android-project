package com.example.pertemuan_2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class MainActivity extends AppCompatActivity {

    private EditText etUsername, etPassword;
    private RadioGroup rgRole;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_main);
        
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // 1. Inisialisasi View
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        rgRole = findViewById(R.id.rgRole);
        Button btnLogin = findViewById(R.id.btnLogin);
        Button btnRegister = findViewById(R.id.btnRegister);

        // 2. Aksi Tombol Login
        btnLogin.setOnClickListener(v -> {
            String usernameInput = etUsername.getText().toString().trim();
            String passwordInput = etPassword.getText().toString().trim();
            
            int selectedId = rgRole.getCheckedRadioButtonId();
            RadioButton rbSelected = findViewById(selectedId);
            String role = (rbSelected != null) ? rbSelected.getText().toString() : "";

            // VALIDASI: Cek field kosong
            if (usernameInput.isEmpty() || passwordInput.isEmpty()) {
                Toast.makeText(this, getString(R.string.msg_error_empty), Toast.LENGTH_SHORT).show();
                return;
            }

            if (role.equalsIgnoreCase(getString(R.string.label_admin))) {
                // CASE 1: Login Admin (Static)
                if (usernameInput.equals("admin") && passwordInput.equals("admin123")) {
                    Toast.makeText(this, getString(R.string.msg_login_success_admin), Toast.LENGTH_SHORT).show();
                    
                    Intent intent = new Intent(MainActivity.this, Dashboard.class);
                    intent.putExtra("EXTRA_USERNAME", "Administrator");
                    intent.putExtra("EXTRA_ROLE", getString(R.string.label_role_admin));
                    startActivity(intent);
                    finish();
                } else {
                    Toast.makeText(this, getString(R.string.msg_error_wrong_creds), Toast.LENGTH_SHORT).show();
                }
            } else {
                // CASE 2: Login Pengguna Biasa (Cek SharedPreferences)
                SharedPreferences sharedPref = getSharedPreferences("UserPrefs", MODE_PRIVATE);
                String regUsername = sharedPref.getString("registered_username", null);
                String regPassword = sharedPref.getString("registered_password", null);
                String regName = sharedPref.getString("registered_name", "User");

                if (regUsername == null) {
                    // Belum ada data registrasi
                    Toast.makeText(this, getString(R.string.msg_error_no_user), Toast.LENGTH_LONG).show();
                } else if (usernameInput.equals(regUsername) && passwordInput.equals(regPassword)) {
                    // Login Berhasil
                    Toast.makeText(this, String.format(getString(R.string.msg_login_success_user), regName), Toast.LENGTH_SHORT).show();
                    
                    Intent intent = new Intent(MainActivity.this, Dashboard.class);
                    intent.putExtra("EXTRA_USERNAME", regName); // Nama dari hasil register
                    intent.putExtra("EXTRA_ROLE", getString(R.string.label_role_user));
                    startActivity(intent);
                    finish();
                } else {
                    // Username/Password salah
                    Toast.makeText(this, getString(R.string.msg_error_wrong_creds), Toast.LENGTH_SHORT).show();
                }
            }
        });

        // Navigasi ke Halaman Register
        btnRegister.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, Register.class);
            startActivity(intent);
        });
    }
}
