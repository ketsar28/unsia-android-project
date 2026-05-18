package com.example.pertemuan_2;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Register extends AppCompatActivity {

    private EditText etRegName, etRegUsername, etRegPassword;

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

        // Inisialisasi View
        etRegName = findViewById(R.id.etRegName);
        etRegUsername = findViewById(R.id.etRegUsername);
        etRegPassword = findViewById(R.id.etRegPassword);
        Button btnDoRegister = findViewById(R.id.btnDoRegister);
        Button btnBackToLogin = findViewById(R.id.btnBackToLogin);

        // Logika Pendaftaran dengan SharedPreferences (Simulasi Database)
        btnDoRegister.setOnClickListener(v -> {
            String name = etRegName.getText().toString().trim();
            String username = etRegUsername.getText().toString().trim();
            String password = etRegPassword.getText().toString().trim();

            if (name.isEmpty() || username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, getString(R.string.msg_error_empty), Toast.LENGTH_SHORT).show();
                return;
            }

            if (password.length() < 6) {
                Toast.makeText(this, getString(R.string.error_password_short), Toast.LENGTH_SHORT).show();
                return;
            }

            // Simpan data ke SharedPreferences
            SharedPreferences sharedPref = getSharedPreferences("UserPrefs", MODE_PRIVATE);
            SharedPreferences.Editor editor = sharedPref.edit();
            editor.putString("registered_name", name);
            editor.putString("registered_username", username);
            editor.putString("registered_password", password);
            editor.apply();

            // Pesan Sukses yang Interaktif
            Toast.makeText(this, getString(R.string.msg_register_success), Toast.LENGTH_LONG).show();
            
            finish(); // Kembali ke halaman Login
        });

        btnBackToLogin.setOnClickListener(v -> finish());
    }
}
