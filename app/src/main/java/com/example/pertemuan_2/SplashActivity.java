package com.example.pertemuan_2;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class SplashActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_splash);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inisialisasi views untuk animasi premium
        LinearLayout centerContent = findViewById(R.id.centerContent);
        ProgressBar progressBar = findViewById(R.id.progressBar);
        LinearLayout footerContainer = findViewById(R.id.footerContainer);

        // Set state awal sebelum animasi dimulai
        centerContent.setAlpha(0f);
        centerContent.setTranslationY(60f); // Geser sedikit ke bawah
        progressBar.setAlpha(0f);
        footerContainer.setAlpha(0f);

        // Jalankan animasi masuk yang halus (Fade In & Slide Up)
        centerContent.animate()
                .alpha(1f)
                .translationY(0f)
                .setDuration(1200)
                .setInterpolator(new android.view.animation.DecelerateInterpolator())
                .start();

        progressBar.animate()
                .alpha(1f)
                .setStartDelay(500)
                .setDuration(800)
                .start();

        footerContainer.animate()
                .alpha(0.7f)
                .setStartDelay(800)
                .setDuration(800)
                .start();

        // Transisi ke MainActivity dengan efek fade yang profesional
        new Handler(Looper.getMainLooper()).postDelayed(() -> {
            Intent intent = new Intent(SplashActivity.this, MainActivity.class);
            startActivity(intent);
            overridePendingTransition(android.R.anim.fade_in, android.R.anim.fade_out);
            finish();
        }, 3200);
    }
}
