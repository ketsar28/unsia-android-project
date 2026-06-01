package com.example.pertemuan_2;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

public class ProfileFragment extends Fragment {

    private EditText etProfName, etProfPhone, etProfAlamat, etProfKel, etProfKec, etProfKota;
    private TextView tvProfileUsername, tvProfileRole, tvStatCount, tvStatStatus;
    private Button btnSaveProfile, btnLogout;

    private String initialPhone = "", initialAlamat = "", initialKel = "", initialKec = "", initialKota = "";
    private String userRole = "";

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_profile, container, false);

        tvProfileUsername = view.findViewById(R.id.tvProfileUsername);
        tvProfileRole = view.findViewById(R.id.tvProfileRole);
        tvStatCount = view.findViewById(R.id.tvStatCount);
        tvStatStatus = view.findViewById(R.id.tvStatStatus);
        etProfName = view.findViewById(R.id.etProfName);
        etProfPhone = view.findViewById(R.id.etProfPhone);
        etProfAlamat = view.findViewById(R.id.etProfAlamat);
        etProfKel = view.findViewById(R.id.etProfKel);
        etProfKec = view.findViewById(R.id.etProfKec);
        etProfKota = view.findViewById(R.id.etProfKota);
        btnSaveProfile = view.findViewById(R.id.btnSaveProfile);
        btnLogout = view.findViewById(R.id.btnLogout);

        if (getActivity() != null && getActivity().getIntent() != null) {
            String username = getActivity().getIntent().getStringExtra("EXTRA_USERNAME");
            userRole = getActivity().getIntent().getStringExtra("EXTRA_ROLE");
            if (username == null) username = "User";
            if (userRole == null) userRole = getString(R.string.label_role_user);

            tvProfileUsername.setText(username);
            tvProfileRole.setText(userRole);
            etProfName.setText(username);

            // Set stats
            tvStatStatus.setText(userRole.equalsIgnoreCase(getString(R.string.label_role_admin)) ? "Admin" : "User");
        }

        // Load saved profile stat count
        if (getActivity() != null) {
            SharedPreferences statsPrefs = getActivity().getSharedPreferences("BmiStats", Context.MODE_PRIVATE);
            int count = statsPrefs.getInt("bmi_count", 0);
            tvStatCount.setText(String.valueOf(count));
        }

        loadProfileData();
        setupProfileChangeWatcher();

        btnSaveProfile.setOnClickListener(v -> saveProfileData());

        btnLogout.setOnClickListener(v -> {
            Intent intent = new Intent(getActivity(), MainActivity.class);
            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            startActivity(intent);
            if (getActivity() != null) getActivity().finish();
            Toast.makeText(getContext(), getString(R.string.msg_logout_success), Toast.LENGTH_SHORT).show();
        });

        return view;
    }

    private void saveProfileData() {
        if (getActivity() == null) return;
        SharedPreferences prefs = getActivity().getSharedPreferences("UserProfile", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = prefs.edit();
        initialPhone = etProfPhone.getText().toString();
        initialAlamat = etProfAlamat.getText().toString();
        initialKel = etProfKel.getText().toString();
        initialKec = etProfKec.getText().toString();
        initialKota = etProfKota.getText().toString();
        editor.putString("phone", initialPhone);
        editor.putString("alamat", initialAlamat);
        editor.putString("kel", initialKel);
        editor.putString("kec", initialKec);
        editor.putString("kota", initialKota);
        editor.apply();
        btnSaveProfile.setEnabled(false);
        btnSaveProfile.setAlpha(0.5f);
        Toast.makeText(getContext(), getString(R.string.msg_profil_updated), Toast.LENGTH_SHORT).show();
    }

    private void loadProfileData() {
        if (getActivity() == null) return;
        SharedPreferences prefs = getActivity().getSharedPreferences("UserProfile", Context.MODE_PRIVATE);
        initialPhone = prefs.getString("phone", "");
        initialAlamat = prefs.getString("alamat", "");
        initialKel = prefs.getString("kel", "");
        initialKec = prefs.getString("kec", "");
        initialKota = prefs.getString("kota", "");
        etProfPhone.setText(initialPhone);
        etProfAlamat.setText(initialAlamat);
        etProfKel.setText(initialKel);
        etProfKec.setText(initialKec);
        etProfKota.setText(initialKota);
        btnSaveProfile.setEnabled(false);
        btnSaveProfile.setAlpha(0.5f);
    }

    private void setupProfileChangeWatcher() {
        TextWatcher watcher = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkProfileChanges();
            }

            @Override
            public void afterTextChanged(Editable s) {
            }
        };
        etProfPhone.addTextChangedListener(watcher);
        etProfAlamat.addTextChangedListener(watcher);
        etProfKel.addTextChangedListener(watcher);
        etProfKec.addTextChangedListener(watcher);
        etProfKota.addTextChangedListener(watcher);
    }

    private void checkProfileChanges() {
        boolean hasChanged = !etProfPhone.getText().toString().equals(initialPhone) ||
                !etProfAlamat.getText().toString().equals(initialAlamat) ||
                !etProfKel.getText().toString().equals(initialKel) ||
                !etProfKec.getText().toString().equals(initialKec) ||
                !etProfKota.getText().toString().equals(initialKota);
        btnSaveProfile.setEnabled(hasChanged);
        btnSaveProfile.setAlpha(hasChanged ? 1.0f : 0.5f);
    }
}
