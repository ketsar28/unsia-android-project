package com.example.pertemuan_2;

import android.content.Intent;
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

    private EditText etDisplayName, etUsername, etPassword;
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
        etDisplayName = findViewById(R.id.etDisplayName);
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        rgRole = findViewById(R.id.rgRole);
        Button btnLogin = findViewById(R.id.btnLogin);
        Button btnRegister = findViewById(R.id.btnRegister);

        // 2. Aksi Tombol Login dengan Validasi
        btnLogin.setOnClickListener(v -> {
            String displayName = etDisplayName.getText().toString().trim();
            String username = etUsername.getText().toString().trim();
            String password = etPassword.getText().toString().trim();
            
            int selectedId = rgRole.getCheckedRadioButtonId();
            RadioButton rbSelected = findViewById(selectedId);
            String role = rbSelected.getText().toString();

            // VALIDASI 1: Cek apakah ada kolom yang kosong
            if (displayName.isEmpty() || username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, getString(R.string.error_empty_fields), Toast.LENGTH_SHORT).show();
                return;
            }

            // VALIDASI 2: Cek Kredensial (Username & Password) sesuai Role
            boolean isValid = false;

            if (role.equalsIgnoreCase("Admin")) {
                // Default Admin: admin / admin123
                if (username.equals("admin") && password.equals("admin123")) {
                    isValid = true;
                }
            } else {
                // Default User: user / user123
                if (username.equals("user") && password.equals("user123")) {
                    isValid = true;
                }
            }

            if (isValid) {
                // Jika Valid: Masuk ke Dashboard dengan membawa Nama Display Dinamis
                Toast.makeText(this, String.format(getString(R.string.login_success), role), Toast.LENGTH_SHORT).show();
                
                Intent intent = new Intent(MainActivity.this, Dashboard.class);
                intent.putExtra("EXTRA_USERNAME", displayName); // Nama dari input user
                intent.putExtra("EXTRA_ROLE", role);
                startActivity(intent);
                finish(); 
            } else {
                // Jika Gagal: Tampilkan pesan error
                Toast.makeText(this, getString(R.string.error_invalid_login), Toast.LENGTH_SHORT).show();
            }
        });

        // Navigasi ke Halaman Register
        btnRegister.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, Register.class);
            startActivity(intent);
        });
    }
}
