package com.example.pertemuan_2;

import android.content.Context;
import android.content.SharedPreferences;
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
    private String loginUsername = "";

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
            loginUsername = getActivity().getIntent().getStringExtra("EXTRA_LOGIN_USER");
            if (username == null) username = "User";
            if (role == null) role = getString(R.string.label_role_user);
            if (loginUsername == null) loginUsername = "user";

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

    private java.util.List<String> getAllUsernames() {
        java.util.List<String> usernames = new java.util.ArrayList<>();
        usernames.add("user");
        usernames.add("admin");
        
        if (getContext() == null) return usernames;
        SharedPreferences userPrefs = requireContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        String userList = userPrefs.getString("user_list", "");
        if (!userList.isEmpty()) {
            String[] entries = userList.split("##");
            for (String entry : entries) {
                String[] parts = entry.split("\\|");
                if (parts.length >= 1) {
                    String uname = parts[0].trim().toLowerCase();
                    if (!usernames.contains(uname)) {
                        usernames.add(uname);
                    }
                }
            }
        }
        return usernames;
    }

    private String getAggregatedBmiHistory() {
        if (getContext() == null) return "";
        StringBuilder allHistory = new StringBuilder();
        SharedPreferences bmiPrefs = requireContext().getSharedPreferences("BmiPrefs", Context.MODE_PRIVATE);
        
        java.util.List<String> usernames = getAllUsernames();
        for (String uname : usernames) {
            String userHistory = bmiPrefs.getString("bmi_history_" + uname, "");
            if (!userHistory.isEmpty()) {
                if (allHistory.length() > 0) {
                    allHistory.append("##");
                }
                allHistory.append(userHistory);
            }
        }
        return allHistory.toString();
    }

    private void setupAdminMenuListeners() {
        cvMenuKelola.setOnClickListener(v -> showKelolaPenggunaDialog());
        
        cvMenuData.setOnClickListener(v -> {
            String history = getAggregatedBmiHistory();
            int totalEntries = 0;
            String topCategory = "-";
            double avgAge = 0;
            
            if (!history.isEmpty()) {
                String[] entries = history.split("##");
                totalEntries = entries.length;
                int countKurus = 0, countNormal = 0, countOverweight = 0, countObesitas = 0;
                int sumAge = 0;
                for (String entry : entries) {
                    String[] parts = entry.split("\\|");
                    if (parts.length == 5) {
                        String category = parts[3];
                        String ageStr = parts[4];
                        try {
                            sumAge += Integer.parseInt(ageStr);
                        } catch (NumberFormatException ignored) {}
                        
                        if (category.equalsIgnoreCase("Kurus") || category.equalsIgnoreCase("Underweight") || category.equalsIgnoreCase("Sangat Kurus")) countKurus++;
                        else if (category.equalsIgnoreCase("Normal")) countNormal++;
                        else if (category.equalsIgnoreCase("Overweight") || category.equalsIgnoreCase("Gemuk")) countOverweight++;
                        else if (category.equalsIgnoreCase("Obesitas") || category.equalsIgnoreCase("Obese")) countObesitas++;
                    }
                }
                avgAge = (double) sumAge / totalEntries;
                
                int max = countNormal;
                topCategory = "Normal";
                if (countKurus > max) { max = countKurus; topCategory = "Kurus"; }
                if (countOverweight > max) { max = countOverweight; topCategory = "Overweight"; }
                if (countObesitas > max) { max = countObesitas; topCategory = "Obesitas"; }
            }
            
            String message = "Statistik Data Master:\n\n" +
                    "• Total Data Kesehatan: " + totalEntries + " Entri\n" +
                    "• Kategori Terbanyak: " + topCategory + "\n" +
                    "• Rata-rata Umur: " + String.format(java.util.Locale.US, "%.1f", avgAge) + " Tahun";
            
            showAdminDialog("Data Master", message);
        });

        cvMenuLaporan.setOnClickListener(v -> {
            String history = getAggregatedBmiHistory();
            int totalEntries = 0;
            double avgBmi = 0;
            double maxBmi = 0;
            String maxBmiUser = "-";
            
            if (!history.isEmpty()) {
                String[] entries = history.split("##");
                totalEntries = entries.length;
                double sumBmi = 0;
                for (String entry : entries) {
                    String[] parts = entry.split("\\|");
                    if (parts.length == 5) {
                        try {
                            double bmiVal = Double.parseDouble(parts[2]);
                            sumBmi += bmiVal;
                            if (bmiVal > maxBmi) {
                                maxBmi = bmiVal;
                                maxBmiUser = parts[0];
                            }
                        } catch (NumberFormatException ignored) {}
                    }
                }
                avgBmi = sumBmi / totalEntries;
            }
            
            String message = "Ringkasan Laporan:\n\n" +
                    "📊 Total Kalkulasi: " + totalEntries + " kali\n" +
                    "📈 Rata-rata BMI: " + String.format(java.util.Locale.US, "%.1f", avgBmi) + "\n" +
                    "🔥 BMI Tertinggi: " + String.format(java.util.Locale.US, "%.1f", maxBmi) + " (" + maxBmiUser + ")";
            
            showAdminDialog("Laporan Sistem", message);
        });

        cvMenuPengaturan.setOnClickListener(v -> {
            SharedPreferences userPrefs = requireContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
            String userList = userPrefs.getString("user_list", "");
            int customUserCount = 0;
            if (!userList.isEmpty()) {
                customUserCount = userList.split("##").length;
            }
            
            String message = "Konfigurasi Aplikasi:\n\n" +
                    "🔑 Autentikasi: Aktif\n" +
                    "👤 Jumlah Akun Kustom: " + customUserCount + "\n" +
                    "🎨 Tema: CarePulse Emerald\n" +
                    "📱 Versi: 1.0.0 (Production)";
            
            showAdminDialog("Pengaturan Sistem", message);
        });

        cvMenuNotifikasi.setOnClickListener(v -> {
            showAdminDialog("Kirim Notifikasi",
                    "Notifikasi Sistem:\n\n" +
                    "📢 \"Jaga kesehatan dan rutin berolahraga!\"\n" +
                    "📢 \"Pemeriksaan BMI berkala mendeteksi risiko dini.\"");
        });

        cvMenuBantuan.setOnClickListener(v -> {
            showAdminDialog("Pusat Bantuan",
                    "Panduan Fitur Admin:\n\n" +
                    "1. Kelola Pengguna: Lihat & hapus user terdaftar.\n" +
                    "2. Data Master: Statistik entri data.\n" +
                    "3. Laporan: Analisis BMI agregat.\n" +
                    "4. Pengaturan: Info akun & aplikasi.");
        });
    }

    private void showKelolaPenggunaDialog() {
        if (getContext() == null) return;
        
        SharedPreferences userPrefs = requireContext().getSharedPreferences("UserPrefs", Context.MODE_PRIVATE);
        String userList = userPrefs.getString("user_list", "");
        
        java.util.List<String> displayList = new java.util.ArrayList<>();
        displayList.add("Andi Pratama (user) - Akun Bawaan");
        
        final java.util.List<String[]> regUsers = new java.util.ArrayList<>();
        
        if (!userList.isEmpty()) {
            String[] entries = userList.split("##");
            for (String entry : entries) {
                String[] parts = entry.split("\\|");
                if (parts.length == 3) {
                    displayList.add(parts[2] + " (" + parts[0] + ") - Klik untuk Detail");
                    regUsers.add(parts);
                }
            }
        }
        
        String[] items = displayList.toArray(new String[0]);
        
        new com.google.android.material.dialog.MaterialAlertDialogBuilder(getContext())
                .setTitle("⚙️ Kelola Pengguna")
                .setItems(items, (dialog, which) -> {
                    String targetUsername;
                    String targetName;
                    
                    if (which == 0) {
                        targetUsername = "user";
                        targetName = "Andi Pratama";
                    } else {
                        String[] selectedUser = regUsers.get(which - 1);
                        targetUsername = selectedUser[0];
                        targetName = selectedUser[2];
                    }
                    
                    // Load Profile Data
                    SharedPreferences targetProfPrefs = requireContext().getSharedPreferences("UserProfile_" + targetUsername.toLowerCase(), Context.MODE_PRIVATE);
                    String phone = targetProfPrefs.getString("phone", "Belum diisi");
                    String alamat = targetProfPrefs.getString("alamat", "Belum diisi");
                    String kel = targetProfPrefs.getString("kel", "Belum diisi");
                    String kec = targetProfPrefs.getString("kec", "Belum diisi");
                    String kota = targetProfPrefs.getString("kota", "Belum diisi");
                    
                    // Load BMI Stats Count
                    SharedPreferences targetStatsPrefs = requireContext().getSharedPreferences("BmiStats", Context.MODE_PRIVATE);
                    int bmiCount = targetStatsPrefs.getInt("bmi_count_" + targetUsername.toLowerCase(), 0);
                    
                    String infoMessage = "Detail Profil Pengguna:\n\n" +
                            "👤 Nama Lengkap: " + targetName + "\n" +
                            "🔑 Username: " + targetUsername + "\n" +
                            "📞 No. Telepon: " + phone + "\n" +
                            "🏠 Alamat: " + alamat + "\n" +
                            "📍 Kelurahan: " + kel + "\n" +
                            "📍 Kecamatan: " + kec + "\n" +
                            "🏙️ Kota/Kab: " + kota + "\n\n" +
                            "📊 Total Cek BMI: " + bmiCount + " kali";
                    
                    com.google.android.material.dialog.MaterialAlertDialogBuilder detailBuilder = 
                        new com.google.android.material.dialog.MaterialAlertDialogBuilder(getContext())
                            .setTitle("👤 Profil: " + targetName)
                            .setMessage(infoMessage)
                            .setPositiveButton("Tutup", null);
                            
                    if (!targetUsername.equalsIgnoreCase("user")) {
                        detailBuilder.setNegativeButton("Hapus Pengguna", (dialogConf, whichConf) -> {
                            new com.google.android.material.dialog.MaterialAlertDialogBuilder(getContext())
                                    .setTitle("Konfirmasi Hapus")
                                    .setMessage("Apakah Anda yakin ingin menghapus akun " + targetName + " (" + targetUsername + ") beserta semua datanya?")
                                    .setPositiveButton("Hapus", (dialogConf2, whichConf2) -> {
                                        java.util.List<String> newEntries = new java.util.ArrayList<>();
                                        if (!userList.isEmpty()) {
                                            String[] entries = userList.split("##");
                                            for (String entry : entries) {
                                                String[] parts = entry.split("\\|");
                                                if (parts.length == 3 && !parts[0].equalsIgnoreCase(targetUsername)) {
                                                    newEntries.add(entry);
                                                }
                                            }
                                        }
                                        
                                        StringBuilder sb = new StringBuilder();
                                        for (int i = 0; i < newEntries.size(); i++) {
                                            if (i > 0) sb.append("##");
                                            sb.append(newEntries.get(i));
                                        }
                                        
                                        SharedPreferences.Editor editor = userPrefs.edit();
                                        editor.putString("user_list", sb.toString());
                                        
                                        String regUsername = userPrefs.getString("registered_username", "");
                                        if (regUsername.equalsIgnoreCase(targetUsername)) {
                                            editor.remove("registered_username");
                                            editor.remove("registered_password");
                                            editor.remove("registered_name");
                                        }
                                        editor.apply();
                                        
                                        SharedPreferences bmiPrefs = requireContext().getSharedPreferences("BmiPrefs", Context.MODE_PRIVATE);
                                        bmiPrefs.edit().remove("bmi_history_" + targetUsername.toLowerCase()).apply();
                                        
                                        SharedPreferences statsPrefs = requireContext().getSharedPreferences("BmiStats", Context.MODE_PRIVATE);
                                        statsPrefs.edit().remove("bmi_count_" + targetUsername.toLowerCase()).apply();
                                        
                                        SharedPreferences profPrefs = requireContext().getSharedPreferences("UserProfile_" + targetUsername.toLowerCase(), Context.MODE_PRIVATE);
                                        profPrefs.edit().clear().apply();
                                        
                                        Toast.makeText(getContext(), "Akun '" + targetUsername + "' berhasil dihapus!", Toast.LENGTH_SHORT).show();
                                    })
                                    .setNegativeButton("Batal", null)
                                    .show();
                        });
                    }
                    detailBuilder.show();
                })
                .setPositiveButton("Tutup", null)
                .show();
    }

    private void setupUserMenuListeners() {
        if (getContext() == null) return;
        
        // Get username for personalization
        String currentName = "User";
        if (getActivity() != null && getActivity().getIntent() != null) {
            String intentName = getActivity().getIntent().getStringExtra("EXTRA_USERNAME");
            if (intentName != null && !intentName.isEmpty()) {
                currentName = intentName;
            }
        }
        final String finalName = currentName;

        cvMenuBeranda.setOnClickListener(v -> {
            // Get latest BMI from SharedPreferences
            SharedPreferences prefs = requireContext().getSharedPreferences("BmiPrefs", Context.MODE_PRIVATE);
            String history = prefs.getString("bmi_history_" + loginUsername.toLowerCase(), "");
            
            String title = "Tips Kesehatan Personal";
            String message;
            
            if (!history.isEmpty()) {
                String[] entries = history.split("##");
                String lastEntry = entries[entries.length - 1];
                String[] parts = lastEntry.split("\\|");
                if (parts.length == 5) {
                    String bmiVal = parts[2];
                    double bmi = Double.parseDouble(bmiVal);
                    
                    if (bmi < 18.5) {
                        message = "Halo " + finalName + "!\n\nBerdasarkan hasil cek BMI terakhir Anda (skor " + bmiVal + " - Kurus):\n\n" +
                                "• Tingkatkan asupan kalori sehat (kacang-kacangan, alpukat, protein).\n" +
                                "• Lakukan latihan kekuatan untuk meningkatkan massa otot.\n" +
                                "• Makan lebih sering dengan porsi kecil namun padat gizi.\n\n" +
                                "Semangat menaikkan berat badan ideal secara sehat! 💪";
                    } else if (bmi < 25) {
                        message = "Halo " + finalName + "!\n\nBerdasarkan hasil cek BMI terakhir Anda (skor " + bmiVal + " - Normal):\n\n" +
                                "• Pertahankan pola makan bergizi seimbang Anda!\n" +
                                "• Lakukan latihan fisik 150 menit per minggu (kardio & kekuatan).\n" +
                                "• Tidur cukup 7-9 jam per malam untuk pemulihan tubuh.\n\n" +
                                "Kerja bagus, kondisi fisik Anda sudah ideal! Tetap konsisten! 💚";
                    } else if (bmi < 30) {
                        message = "Halo " + finalName + "!\n\nBerdasarkan hasil cek BMI terakhir Anda (skor " + bmiVal + " - Overweight):\n\n" +
                                "• Kurangi porsi karbohidrat sederhana dan makanan manis.\n" +
                                "• Tingkatkan jalan kaki minimal 10.000 langkah sehari.\n" +
                                "• Konsumsi lebih banyak makanan tinggi serat agar kenyang lebih lama.\n\n" +
                                "Yuk, lakukan sedikit penyesuaian porsi untuk kembali ke berat ideal! 🏃‍♂️";
                    } else {
                        message = "Halo " + finalName + "!\n\nBerdasarkan hasil cek BMI terakhir Anda (skor " + bmiVal + " - Obesitas):\n\n" +
                                "• Kurangi asupan kalori harian dan hindari gorengan/fast food.\n" +
                                "• Prioritaskan olahraga low-impact seperti renang atau jalan santai.\n" +
                                "• Sangat disarankan berkonsultasi dengan ahli gizi profesional.\n\n" +
                                "Langkah kecil yang konsisten setiap hari akan membuahkan hasil besar! 🌟";
                    }
                } else {
                    message = "Lakukan cek BMI rutin di menu Kalkulator untuk mendapatkan saran kesehatan yang disesuaikan dengan profil Anda.";
                }
            } else {
                message = "Halo " + finalName + "!\n\nAnda belum melakukan pengukuran BMI pertama Anda.\n\nSilakan buka tab Kalkulator di bawah untuk menghitung BMI Anda. Halaman Beranda ini akan memberikan tips kesehatan yang disesuaikan khusus dengan kondisi fisik Anda setelahnya!";
            }
            
            showUserDialog(title, message);
        });

        cvMenuPengumuman.setOnClickListener(v -> {
            // Get latest BMI from SharedPreferences
            SharedPreferences prefs = requireContext().getSharedPreferences("BmiPrefs", Context.MODE_PRIVATE);
            String history = prefs.getString("bmi_history_" + loginUsername.toLowerCase(), "");
            
            String message;
            if (!history.isEmpty()) {
                String[] entries = history.split("##");
                String lastEntry = entries[entries.length - 1];
                String[] parts = lastEntry.split("\\|");
                String category = parts.length == 5 ? parts[3] : "Normal";
                String bmiVal = parts.length == 5 ? parts[2] : "0.0";
                
                message = "📢 Pengumuman untuk " + finalName + ":\n\n" +
                        "Pemeriksaan kesehatan rutin mendeteksi bahwa indeks massa tubuh (BMI) Anda saat ini berada di kategori " + category.toUpperCase() + " dengan skor " + bmiVal + ".\n\n" +
                        "Silakan cek menu Kalkulator secara berkala untuk terus memantau perkembangan kesehatan Anda!\n\n" +
                        "Salam Sehat,\nAdministrator";
            } else {
                message = "📢 Pengumuman Penting:\n\nHalo " + finalName + ", selamat bergabung di CarePulse! Kami mencatat Anda belum melakukan kalkulasi BMI pertama Anda.\n\nMari mulai memantau berat badan ideal Anda dengan menggunakan kalkulator BMI pada tab Kalkulator.\n\nSalam Sehat,\nAdministrator";
            }
            showUserDialog("Pengumuman Terkini", message);
        });

        cvMenuProfil.setOnClickListener(v -> {
            if (getActivity() instanceof Dashboard) {
                ((Dashboard) getActivity()).selectTab("profile");
            }
        });
    }

    private void showAdminDialog(String title, String message) {
        if (getContext() == null) return;
        new com.google.android.material.dialog.MaterialAlertDialogBuilder(getContext())
                .setTitle("⚙️ " + title)
                .setMessage(message)
                .setPositiveButton("Tutup", null)
                .show();
    }

    private void showUserDialog(String title, String message) {
        if (getContext() == null) return;
        new com.google.android.material.dialog.MaterialAlertDialogBuilder(getContext())
                .setTitle("💚 " + title)
                .setMessage(message)
                .setPositiveButton("Mengerti", null)
                .show();
    }
}
