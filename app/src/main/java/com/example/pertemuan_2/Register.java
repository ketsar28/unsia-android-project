package com.example.pertemuan_2;

import android.os.Bundle;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import com.google.android.material.button.MaterialButton;
import com.google.android.material.textfield.TextInputEditText;

public class Register extends AppCompatActivity {

    private TextInputEditText etRegName, etRegUsername, etRegPassword;
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
        etRegName = findViewById(R.id.etRegName);
        etRegUsername = findViewById(R.id.etRegUsername);
        etRegPassword = findViewById(R.id.etRegPassword);
        btnDoRegister = findViewById(R.id.btnDoRegister);
        btnBackToLogin = findViewById(R.id.btnBackToLogin);

        // Register button: NO actual registration per UTS instructions
        btnDoRegister.setOnClickListener(v -> {
            Toast.makeText(Register.this, getString(R.string.msg_register_disabled), Toast.LENGTH_SHORT).show();
        });

        // Back to login
        btnBackToLogin.setOnClickListener(v -> finish());
    }
}
