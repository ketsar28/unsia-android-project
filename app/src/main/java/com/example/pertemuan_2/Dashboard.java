package com.example.pertemuan_2;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Dashboard extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dashboard);
        
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        TextView tvWelcome = findViewById(R.id.tvWelcome);
        TextView tvUserRole = findViewById(R.id.tvUserRole);
        LinearLayout btnSearch = findViewById(R.id.btnSearch);
        LinearLayout btnProfile = findViewById(R.id.btnProfile);
        LinearLayout btnHome = findViewById(R.id.btnHome);

        // Ambil data dari Intent
        String username = getIntent().getStringExtra("EXTRA_USERNAME");
        String role = getIntent().getStringExtra("EXTRA_ROLE");

        // Tampilkan data
        tvWelcome.setText("Selamat Datang,\n" + username);
        tvUserRole.setText(role);

        // Implicit Intent: Membuka Google saat klik Search
        btnSearch.setOnClickListener(v -> {
            String url = "https://www.google.com";
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
            startActivity(intent);
        });

        // Contoh navigasi balik (Log out)
        btnProfile.setOnClickListener(v -> finish());
    }
}
