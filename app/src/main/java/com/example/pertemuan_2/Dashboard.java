package com.example.pertemuan_2;

import android.content.Intent;
import android.graphics.Typeface;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;

import com.google.android.material.button.MaterialButton;

public class Dashboard extends AppCompatActivity {

    private LinearLayout btnNavHome, btnNavSearch, btnNavProfile;
    private ImageView ivNavHome, ivNavSearch, ivNavProfile;
    private TextView tvNavHome, tvNavSearch, tvNavProfile;
    private MaterialButton btnToolbarLogout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_dashboard);

        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, 0);
            return insets;
        });

        // Initialize views
        btnNavHome = findViewById(R.id.btnNavHome);
        btnNavSearch = findViewById(R.id.btnNavSearch);
        btnNavProfile = findViewById(R.id.btnNavProfile);
        ivNavHome = findViewById(R.id.ivNavHome);
        ivNavSearch = findViewById(R.id.ivNavSearch);
        ivNavProfile = findViewById(R.id.ivNavProfile);
        tvNavHome = findViewById(R.id.tvNavHome);
        tvNavSearch = findViewById(R.id.tvNavSearch);
        tvNavProfile = findViewById(R.id.tvNavProfile);
        btnToolbarLogout = findViewById(R.id.btnToolbarLogout);

        // Load default fragment
        if (savedInstanceState == null) {
            loadFragment(new HomeFragment(), "home");
            updateNavStyles("home");
        }

        // Navigation click listeners
        btnNavHome.setOnClickListener(v -> {
            loadFragment(new HomeFragment(), "home");
            updateNavStyles("home");
        });

        btnNavSearch.setOnClickListener(v -> {
            loadFragment(new BmiFragment(), "search");
            updateNavStyles("search");
        });

        btnNavProfile.setOnClickListener(v -> {
            loadFragment(new ProfileFragment(), "profile");
            updateNavStyles("profile");
        });

        // Logout button
        btnToolbarLogout.setOnClickListener(v -> {
            Intent intent = new Intent(Dashboard.this, MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            finish();
            Toast.makeText(Dashboard.this, getString(R.string.msg_logout_success), Toast.LENGTH_SHORT).show();
        });
    }

    public void selectTab(String activeTab) {
        switch (activeTab) {
            case "home":
                loadFragment(new HomeFragment(), "home");
                updateNavStyles("home");
                break;
            case "search":
                loadFragment(new BmiFragment(), "search");
                updateNavStyles("search");
                break;
            case "profile":
                loadFragment(new ProfileFragment(), "profile");
                updateNavStyles("profile");
                break;
        }
    }

    private void loadFragment(Fragment fragment, String tag) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.fragment_container, fragment, tag)
                .commit();
    }

    private void updateNavStyles(String activeTab) {
        int colorInactive = ContextCompat.getColor(this, R.color.text_secondary);
        int colorActive = ContextCompat.getColor(this, R.color.primary_green);

        // Reset all to inactive
        ivNavHome.setColorFilter(colorInactive);
        ivNavSearch.setColorFilter(colorInactive);
        ivNavProfile.setColorFilter(colorInactive);

        tvNavHome.setTextColor(colorInactive);
        tvNavSearch.setTextColor(colorInactive);
        tvNavProfile.setTextColor(colorInactive);

        tvNavHome.setTypeface(null, Typeface.NORMAL);
        tvNavSearch.setTypeface(null, Typeface.NORMAL);
        tvNavProfile.setTypeface(null, Typeface.NORMAL);

        // Set active tab
        switch (activeTab) {
            case "home":
                ivNavHome.setColorFilter(colorActive);
                tvNavHome.setTextColor(colorActive);
                tvNavHome.setTypeface(null, Typeface.BOLD);
                break;
            case "search":
                ivNavSearch.setColorFilter(colorActive);
                tvNavSearch.setTextColor(colorActive);
                tvNavSearch.setTypeface(null, Typeface.BOLD);
                break;
            case "profile":
                ivNavProfile.setColorFilter(colorActive);
                tvNavProfile.setTextColor(colorActive);
                tvNavProfile.setTypeface(null, Typeface.BOLD);
                break;
        }
    }
}
