package com.example.pertemuan_2;

import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Typeface;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.CheckBox;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

public class MainActivity extends AppCompatActivity {

    private TextInputEditText etUsername, etPassword;
    private TextInputLayout tilUsername, tilPassword;
    private Spinner spRole;
    private CheckBox cbRemember;
    private MaterialButton btnLogin, btnRegister;

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

        // Initialize views
        tilUsername = findViewById(R.id.tilUsername);
        tilPassword = findViewById(R.id.tilPassword);
        etUsername = findViewById(R.id.etUsername);
        etPassword = findViewById(R.id.etPassword);
        spRole = findViewById(R.id.spRole);
        cbRemember = findViewById(R.id.cbRemember);
        btnLogin = findViewById(R.id.btnLogin);
        btnRegister = findViewById(R.id.btnRegister);

        // Setup spinner with custom adapter
        setupRoleSpinner();

        // Login button click
        btnLogin.setOnClickListener(v -> performLogin());

        // Register button click
        btnRegister.setOnClickListener(v -> {
            Intent intent = new Intent(MainActivity.this, Register.class);
            startActivity(intent);
        });
    }

    private void setupRoleSpinner() {
        String[] roles = {
                getString(R.string.label_admin),
                getString(R.string.label_user)
        };

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,
                android.R.layout.simple_spinner_item, roles) {

            @NonNull
            @Override
            public View getView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getView(position, convertView, parent);
                TextView textView = (TextView) view;
                textView.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.text_main));
                textView.setTypeface(textView.getTypeface(), Typeface.BOLD);
                textView.setTextSize(15);
                return view;
            }

            @Override
            public View getDropDownView(int position, @Nullable View convertView, @NonNull ViewGroup parent) {
                View view = super.getDropDownView(position, convertView, parent);
                TextView textView = (TextView) view;
                textView.setTextColor(ContextCompat.getColor(MainActivity.this, R.color.text_main));
                textView.setTypeface(textView.getTypeface(), Typeface.BOLD);
                textView.setBackgroundColor(ContextCompat.getColor(MainActivity.this, R.color.surface_green));
                textView.setPadding(32, 24, 32, 24);
                textView.setTextSize(15);
                return view;
            }
        };

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spRole.setAdapter(adapter);
    }

    private void performLogin() {
        String username = etUsername.getText() != null ? etUsername.getText().toString().trim() : "";
        String password = etPassword.getText() != null ? etPassword.getText().toString().trim() : "";
        String role = spRole.getSelectedItem().toString();

        // Validation: Username empty
        if (username.isEmpty()) {
            tilUsername.setError(getString(R.string.error_username_empty));
            etUsername.requestFocus();
            return;
        } else {
            tilUsername.setError(null);
        }

        // Validation: Password empty
        if (password.isEmpty()) {
            tilPassword.setError(getString(R.string.error_password_empty));
            etPassword.requestFocus();
            return;
        } else {
            tilPassword.setError(null);
        }

        // Clear all errors
        tilUsername.setError(null);
        tilPassword.setError(null);

        // Login logic based on role
        if (role.equals(getString(R.string.label_admin))) {
            // Admin login
            if (username.equals("admin") && password.equals("admin123")) {
                Toast.makeText(this, getString(R.string.msg_login_success_admin), Toast.LENGTH_SHORT).show();
                navigateToDashboard("Administrator", getString(R.string.label_role_admin));
            } else {
                Toast.makeText(this, getString(R.string.msg_error_wrong_creds), Toast.LENGTH_SHORT).show();
            }
        } else {
            // User login
            if (username.equals("user") && password.equals("user123")) {
                // Static user credentials
                Toast.makeText(this, getString(R.string.msg_login_success_user), Toast.LENGTH_SHORT).show();
                navigateToDashboard("Andi Pratama", getString(R.string.label_role_user));
            } else {
                // Check SharedPreferences for registered user
                SharedPreferences prefs = getSharedPreferences("UserPrefs", MODE_PRIVATE);
                String regUsername = prefs.getString("registered_username", "");
                String regPassword = prefs.getString("registered_password", "");
                String regName = prefs.getString("registered_name", "");

                if (!regUsername.isEmpty() && username.equals(regUsername) && password.equals(regPassword)) {
                    Toast.makeText(this, getString(R.string.msg_login_success_user), Toast.LENGTH_SHORT).show();
                    navigateToDashboard(regName, getString(R.string.label_role_user));
                } else {
                    Toast.makeText(this, getString(R.string.msg_error_wrong_creds), Toast.LENGTH_SHORT).show();
                }
            }
        }
    }

    private void navigateToDashboard(String username, String role) {
        Intent intent = new Intent(MainActivity.this, Dashboard.class);
        intent.putExtra("EXTRA_USERNAME", username);
        intent.putExtra("EXTRA_ROLE", role);
        startActivity(intent);
        finish();
    }
}
