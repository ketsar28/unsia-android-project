package com.example.pertemuan_2;

import android.app.AlertDialog;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;

import com.google.android.material.card.MaterialCardView;

public class HomeFragment extends Fragment {

    private TextView tvHomeName, tvHomeRole, tvAccessTag, tvAccessDesc, tvNoteTitle, tvNoteDesc;
    private View gridAdmin, gridUser;
    private MaterialCardView cvAccessTag;
    // Admin menu cards
    private MaterialCardView cvMenuKelola, cvMenuData, cvMenuLaporan, cvMenuPengaturan, cvMenuNotifikasi, cvMenuBantuan;
    // User menu cards
    private MaterialCardView cvMenuBeranda, cvMenuPengumuman, cvMenuProfil;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_home, container, false);

        // Bind Views
        tvHomeName = view.findViewById(R.id.tvHomeName);
        tvHomeRole = view.findViewById(R.id.tvHomeRole);
        tvAccessTag = view.findViewById(R.id.tvAccessTag);
        tvAccessDesc = view.findViewById(R.id.tvAccessDesc);
        cvAccessTag = view.findViewById(R.id.cvAccessTag);
        gridAdmin = view.findViewById(R.id.gridAdmin);
        gridUser = view.findViewById(R.id.gridUser);
        tvNoteTitle = view.findViewById(R.id.tvNoteTitle);
        tvNoteDesc = view.findViewById(R.id.tvNoteDesc);

        // Admin menu cards
        cvMenuKelola = view.findViewById(R.id.cvMenuKelola);
        cvMenuData = view.findViewById(R.id.cvMenuData);
        cvMenuLaporan = view.findViewById(R.id.cvMenuLaporan);
        cvMenuPengaturan = view.findViewById(R.id.cvMenuPengaturan);
        cvMenuNotifikasi = view.findViewById(R.id.cvMenuNotifikasi);
        cvMenuBantuan = view.findViewById(R.id.cvMenuBantuan);

        // User menu cards
        cvMenuBeranda = view.findViewById(R.id.cvMenuBeranda);
        cvMenuPengumuman = view.findViewById(R.id.cvMenuPengumuman);
        cvMenuProfil = view.findViewById(R.id.cvMenuProfil);

        // Get Role Data from Activity Intent
        if (getActivity() != null && getActivity().getIntent() != null) {
            String username = getActivity().getIntent().getStringExtra("EXTRA_USERNAME");
            String role = getActivity().getIntent().getStringExtra("EXTRA_ROLE");
            if (username == null) username = "User";
            if (role == null) role = getString(R.string.label_role_user);

            tvHomeName.setText(username);
            tvHomeRole.setText(role);

            if (role.equalsIgnoreCase(getString(R.string.label_role_admin))) {
                // ADMIN VIEW
                gridAdmin.setVisibility(View.VISIBLE);
                gridUser.setVisibility(View.GONE);
                tvAccessTag.setText(getString(R.string.home_access_full));
                tvAccessTag.setTextColor(ContextCompat.getColor(requireContext(), R.color.primary_dark_green));
                cvAccessTag.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.surface_green));
                tvAccessDesc.setText(getString(R.string.home_admin_info));
                tvNoteTitle.setText("Panel Kontrol Admin");
                tvNoteDesc.setText("Anda memiliki akses penuh ke semua fitur manajemen dan laporan.");
                setupAdminMenuListeners();
            } else {
                // USER VIEW
                gridAdmin.setVisibility(View.GONE);
                gridUser.setVisibility(View.VISIBLE);
                tvAccessTag.setText(getString(R.string.home_access_limited));
                tvAccessTag.setTextColor(ContextCompat.getColor(requireContext(), R.color.white));
                cvAccessTag.setCardBackgroundColor(ContextCompat.getColor(requireContext(), R.color.amber_warning));
                tvAccessDesc.setText(getString(R.string.home_user_info));
                tvNoteTitle.setText(getString(R.string.home_restricted_title));
                tvNoteDesc.setText(getString(R.string.home_restricted_desc));
                setupUserMenuListeners();
            }
        }
        return view;
    }

    private void setupAdminMenuListeners() {
        cvMenuKelola.setOnClickListener(v -> showAdminDialog("Kelola Pengguna",
                "Daftar Pengguna Aktif:\n\n1. Andi Pratama \u2014 Status: Aktif\n2. Budi Santoso \u2014 Status: Aktif\n3. Citra Dewi \u2014 Status: Ditangguhkan\n\nTotal: 3 pengguna terdaftar."));
        cvMenuData.setOnClickListener(v -> showAdminDialog("Data Master",
                "Statistik Data Master:\n\n\u2022 Jumlah Data Kesehatan: 12 entri\n\u2022 Kategori Terbanyak: Normal (45%)\n\u2022 Rata-rata Umur Pengguna: 28 Tahun\n\nData terakhir diperbarui hari ini."));
        cvMenuLaporan.setOnClickListener(v -> showAdminDialog("Laporan Sistem",
                "Ringkasan Laporan:\n\n\uD83D\uDCCA Total Kalkulasi BMI: 15 kali\n\uD83D\uDCC8 Rata-rata BMI Seluruh Pengguna: 22.4 (Normal)\n\uD83C\uDFCB\uFE0F Aktivitas Terpopuler: Sedang (3-5 hari/minggu)\n\nSemua data berjalan dengan baik."));
        cvMenuPengaturan.setOnClickListener(v -> showAdminDialog("Pengaturan Sistem",
                "Konfigurasi Sistem:\n\n\uD83D\uDD10 Autentikasi: Aktif (Static Credentials)\n\uD83C\uDFA8 Tema Aplikasi: CarePulse Emerald\n\uD83D\uDCF1 Versi Aplikasi: 1.0 (Build UTS)\n\uD83D\uDDC4\uFE0F Penyimpanan: SharedPreferences (Lokal)"));
        cvMenuNotifikasi.setOnClickListener(v -> showAdminDialog("Kirim Notifikasi",
                "Pesan Notifikasi Terbaru:\n\n\uD83D\uDCE2 \"Jaga kesehatan, rutin berolahraga!\"\n\uD83D\uDCE2 \"Pemeriksaan BMI rutin sangat penting.\"\n\nNotifikasi berhasil dikirim ke semua pengguna."));
        cvMenuBantuan.setOnClickListener(v -> showAdminDialog("Pusat Bantuan Admin",
                "Panduan Admin:\n\n1. Kelola Pengguna: Lihat & atur status akun.\n2. Data Master: Pantau statistik kesehatan.\n3. Laporan: Analisis data BMI seluruh pengguna.\n4. Pengaturan: Konfigurasi sistem aplikasi.\n\nHubungi developer untuk bantuan teknis."));
    }

    private void setupUserMenuListeners() {
        String[] healthTips = {
                "\uD83D\uDCA7 Minum minimal 8 gelas air putih per hari untuk menjaga hidrasi tubuh.",
                "\uD83C\uDFC3 Berjalan kaki 30 menit setiap hari dapat mengurangi risiko penyakit jantung.",
                "\uD83E\uDD57 Konsumsi 5 porsi buah dan sayuran setiap hari untuk nutrisi optimal.",
                "\uD83D\uDE34 Tidur 7-9 jam per malam penting untuk pemulihan tubuh dan pikiran.",
                "\uD83E\uDDD8 Luangkan 10 menit per hari untuk meditasi guna mengurangi stres."
        };
        cvMenuBeranda.setOnClickListener(v -> {
            String tip = healthTips[(int) (Math.random() * healthTips.length)];
            showUserDialog("Tips Kesehatan Harian", tip);
        });
        cvMenuPengumuman.setOnClickListener(v -> showUserDialog("Pengumuman",
                "\uD83D\uDCE2 Pengumuman dari Admin:\n\n\"Jaga kesehatan Anda! Rutin cek BMI dan konsultasi dengan ahli gizi untuk pola makan yang lebih baik.\"\n\nDiposting oleh: Administrator"));
        cvMenuProfil.setOnClickListener(v -> {
            if (getActivity() instanceof Dashboard) {
                Toast.makeText(getContext(), "Navigasi ke halaman Profil...", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void showAdminDialog(String title, String message) {
        if (getContext() == null) return;
        new AlertDialog.Builder(getContext())
                .setTitle("\uD83D\uDEE0\uFE0F " + title)
                .setMessage(message)
                .setPositiveButton("Tutup", null)
                .show();
    }

    private void showUserDialog(String title, String message) {
        if (getContext() == null) return;
        new AlertDialog.Builder(getContext())
                .setTitle("\uD83D\uDC9A " + title)
                .setMessage(message)
                .setPositiveButton("Mengerti", null)
                .show();
    }
}
