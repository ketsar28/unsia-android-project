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

        // Register button action with professional dialog
        btnDoRegister.setOnClickListener(v -> {
            new com.google.android.material.dialog.MaterialAlertDialogBuilder(Register.this)
                    .setTitle("Pendaftaran Akun")
                    .setMessage(getString(R.string.msg_register_disabled) + "\n\nSilakan masuk dengan akun demo yang tersedia:\n• Username: user\n• Password: user123\n\nAtau gunakan akun admin:\n• Username: admin\n• Password: admin123")
                    .setPositiveButton("Mengerti", null)
                    .show();
        });

        // Back to login
        btnBackToLogin.setOnClickListener(v -> finish());
    }
}
