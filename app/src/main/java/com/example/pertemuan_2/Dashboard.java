package com.example.pertemuan_2;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.TypedValue;
import android.view.View;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.SeekBar;
import android.widget.Spinner;
import android.widget.ArrayAdapter;
import android.widget.TableLayout;
import android.widget.TableRow;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.ContextCompat;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

import java.util.Locale;

public class Dashboard extends AppCompatActivity {

    private SeekBar sbUmur;
    private TextView tvUmurDisplay, tvWelcome, tvUserRole;
    private EditText etInputName, etWeight, etHeight;
    private RadioGroup rgGender;
    private CheckBox cbAgree;
    private ProgressBar pbSaving;
    private Button btnSave, btnReset, btnClearData;
    private Spinner spAktivitas;
    private TableLayout tableData;
    private int dataCount = 0;

    // Navigation and Containers
    private LinearLayout btnNavHome, btnNavSearch, btnNavProfile;
    private LinearLayout containerHome, containerSearch, containerProfile;
    private android.widget.ImageView ivNavHome, ivNavSearch, ivNavProfile;
    private TextView tvNavHome, tvNavSearch, tvNavProfile;

    // Search Tab Views
    private EditText etSearchQuery;
    private Button btnDoSearch;

    // Profile Tab Views
    private EditText etProfName, etProfPhone, etProfAlamat, etProfKel, etProfKec, etProfKota;
    private Button btnSaveProfile, btnLogout;

    private String initialPhone, initialAlamat, initialKel, initialKec, initialKota;

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

        // Inisialisasi View Home
        tvWelcome = findViewById(R.id.tvWelcome);
        tvUserRole = findViewById(R.id.tvUserRole);
        sbUmur = findViewById(R.id.sbUmur);
        tvUmurDisplay = findViewById(R.id.tvUmurDisplay);
        etInputName = findViewById(R.id.etInputName);
        etWeight = findViewById(R.id.etWeight);
        etHeight = findViewById(R.id.etHeight);
        rgGender = findViewById(R.id.rgGender);
        cbAgree = findViewById(R.id.cbAgree);
        pbSaving = findViewById(R.id.pbSaving);
        btnSave = findViewById(R.id.btnSave);
        btnReset = findViewById(R.id.btnReset);
        btnClearData = findViewById(R.id.btnClearData);
        spAktivitas = findViewById(R.id.spAktivitas);
        tableData = findViewById(R.id.tableData);

        // Inisialisasi Navigation & Containers
        btnNavHome = findViewById(R.id.btnNavHome);
        btnNavSearch = findViewById(R.id.btnNavSearch);
        btnNavProfile = findViewById(R.id.btnNavProfile);
        containerHome = findViewById(R.id.containerHome);
        containerSearch = findViewById(R.id.containerSearch);
        containerProfile = findViewById(R.id.containerProfile);
        ivNavHome = findViewById(R.id.ivNavHome);
        ivNavSearch = findViewById(R.id.ivNavSearch);
        ivNavProfile = findViewById(R.id.ivNavProfile);
        tvNavHome = findViewById(R.id.tvNavHome);
        tvNavSearch = findViewById(R.id.tvNavSearch);
        tvNavProfile = findViewById(R.id.tvNavProfile);

        // Inisialisasi Search Views
        etSearchQuery = findViewById(R.id.etSearchQuery);
        btnDoSearch = findViewById(R.id.btnDoSearch);

        // Inisialisasi Profile Views
        etProfName = findViewById(R.id.etProfName);
        etProfPhone = findViewById(R.id.etProfPhone);
        etProfAlamat = findViewById(R.id.etProfAlamat);
        etProfKel = findViewById(R.id.etProfKel);
        etProfKec = findViewById(R.id.etProfKec);
        etProfKota = findViewById(R.id.etProfKota);
        btnSaveProfile = findViewById(R.id.btnSaveProfile);
        btnLogout = findViewById(R.id.btnLogout);

        // Setup Spinner Aktivitas Olahraga
        ArrayAdapter<CharSequence> adapter = new ArrayAdapter<CharSequence>(this,
                android.R.layout.simple_spinner_item, getResources().getStringArray(R.array.aktivitas_array)) {
            @Override
            public View getView(int position, View convertView, android.view.ViewGroup parent) {
                View v = super.getView(position, convertView, parent);
                ((TextView) v).setTextColor(ContextCompat.getColor(getContext(), R.color.black));
                ((TextView) v).setTypeface(null, android.graphics.Typeface.BOLD);
                return v;
            }

            @Override
            public View getDropDownView(int position, View convertView, android.view.ViewGroup parent) {
                View v = super.getDropDownView(position, convertView, parent);
                v.setBackgroundColor(ContextCompat.getColor(getContext(), R.color.surface_green));
                ((TextView) v).setTextColor(ContextCompat.getColor(getContext(), R.color.black));
                ((TextView) v).setTypeface(null, android.graphics.Typeface.BOLD);
                return v;
            }
        };
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spAktivitas.setAdapter(adapter);

        // 1. Tampilkan Nama User dari Intent
        String username = getIntent().getStringExtra("EXTRA_USERNAME");
        String role = getIntent().getStringExtra("EXTRA_ROLE");
        if (username == null) username = "User";
        tvWelcome.setText(getString(R.string.welcome_message, username));
        tvUserRole.setText(role);
        etProfName.setText(username);

        // 2. Logika SeekBar (Umur Dinamis)
        sbUmur.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvUmurDisplay.setText(getString(R.string.format_umur_display, progress));
            }
            @Override public void onStartTrackingTouch(SeekBar seekBar) {}
            @Override public void onStopTrackingTouch(SeekBar seekBar) {}
        });

        // 3. Logika Simpan Data & Hitung BMI
        btnSave.setOnClickListener(v -> {
            String namaRaw = etInputName.getText().toString().trim();
            String weightStr = etWeight.getText().toString().trim();
            String heightStr = etHeight.getText().toString().trim();

            if (namaRaw.isEmpty() || weightStr.isEmpty() || heightStr.isEmpty()) {
                Toast.makeText(this, getString(R.string.msg_error_empty), Toast.LENGTH_SHORT).show();
                return;
            }

            if (!cbAgree.isChecked()) {
                Toast.makeText(this, getString(R.string.error_agreement), Toast.LENGTH_SHORT).show();
                return;
            }

            pbSaving.setVisibility(View.VISIBLE);
            btnSave.setEnabled(false);

            new Handler().postDelayed(() -> {
                pbSaving.setVisibility(View.GONE);
                btnSave.setEnabled(true);
                
                double weight = Double.parseDouble(weightStr);
                double heightCm = Double.parseDouble(heightStr);
                double heightM = heightCm / 100;
                double bmi = weight / (heightM * heightM);
                
                String kategori = getBMICategory(bmi);
                String namaFormatted = formatTitleCase(namaRaw);
                int selectedGenderId = rgGender.getCheckedRadioButtonId();
                RadioButton rbSelected = findViewById(selectedGenderId);
                String gender = (rbSelected != null) ? rbSelected.getText().toString() : "-";
                
                // Tambahkan ke Tabel
                addDataToTable(namaFormatted, gender, String.format(Locale.US, "%.1f", bmi), kategori, String.valueOf(sbUmur.getProgress()));

                Toast.makeText(this, getString(R.string.data_saved), Toast.LENGTH_SHORT).show();
            }, 1500);
        });

        // 4. Logika Reset
        btnReset.setOnClickListener(v -> {
            etInputName.setText("");
            etWeight.setText("");
            etHeight.setText("");
            rgGender.check(R.id.rbPria);
            cbAgree.setChecked(false);
            sbUmur.setProgress(20);
            spAktivitas.setSelection(0);
            tvUmurDisplay.setText(getString(R.string.format_umur_display, 20));
        });

        // 5. Logika Hapus Semua Data
        btnClearData.setOnClickListener(v -> {
            tableData.removeAllViews();
            dataCount = 0;
            addTableHeader();
            Toast.makeText(this, getString(R.string.data_cleared), Toast.LENGTH_SHORT).show();
        });

        // 6. Logika Navigasi Tab
        btnNavHome.setOnClickListener(v -> setActiveTab("home"));
        btnNavSearch.setOnClickListener(v -> setActiveTab("search"));
        btnNavProfile.setOnClickListener(v -> setActiveTab("profile"));

        // 7. Logika Search Google
        btnDoSearch.setOnClickListener(v -> {
            String query = etSearchQuery.getText().toString().trim();
            if (query.isEmpty()) {
                Toast.makeText(this, getString(R.string.msg_error_empty), Toast.LENGTH_SHORT).show();
            } else {
                Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/search?q=" + query));
                startActivity(intent);
            }
        });

        // 8. Logika Profile
        loadProfileData();
        setupProfileChangeWatcher();
        btnSaveProfile.setOnClickListener(v -> saveProfileData());
        btnLogout.setOnClickListener(v -> {
            finish();
            Toast.makeText(this, "Logged Out", Toast.LENGTH_SHORT).show();
        });
    }

    private void setActiveTab(String tabName) {
        // Reset Visibilities
        containerHome.setVisibility(View.GONE);
        containerSearch.setVisibility(View.GONE);
        containerProfile.setVisibility(View.GONE);

        // Reset Nav Styles
        resetNavStyles();

        int activeColor = ContextCompat.getColor(this, R.color.primary_green);
        int inactiveColor = ContextCompat.getColor(this, R.color.text_secondary);

        if (tabName.equals("home")) {
            containerHome.setVisibility(View.VISIBLE);
            ivNavHome.setColorFilter(activeColor);
            tvNavHome.setTextColor(activeColor);
            tvNavHome.setTypeface(null, android.graphics.Typeface.BOLD);
        } else if (tabName.equals("search")) {
            containerSearch.setVisibility(View.VISIBLE);
            ivNavSearch.setColorFilter(activeColor);
            tvNavSearch.setTextColor(activeColor);
            tvNavSearch.setTypeface(null, android.graphics.Typeface.BOLD);
        } else if (tabName.equals("profile")) {
            containerProfile.setVisibility(View.VISIBLE);
            ivNavProfile.setColorFilter(activeColor);
            tvNavProfile.setTextColor(activeColor);
            tvNavProfile.setTypeface(null, android.graphics.Typeface.BOLD);
        }
    }

    private void resetNavStyles() {
        int inactiveColor = ContextCompat.getColor(this, R.color.text_secondary);
        
        ivNavHome.setColorFilter(inactiveColor);
        tvNavHome.setTextColor(inactiveColor);
        tvNavHome.setTypeface(null, android.graphics.Typeface.NORMAL);

        ivNavSearch.setColorFilter(inactiveColor);
        tvNavSearch.setTextColor(inactiveColor);
        tvNavSearch.setTypeface(null, android.graphics.Typeface.NORMAL);

        ivNavProfile.setColorFilter(inactiveColor);
        tvNavProfile.setTextColor(inactiveColor);
        tvNavProfile.setTypeface(null, android.graphics.Typeface.NORMAL);
    }

    private void saveProfileData() {
        android.content.SharedPreferences prefs = getSharedPreferences("UserProfile", MODE_PRIVATE);
        android.content.SharedPreferences.Editor editor = prefs.edit();
        
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
        Toast.makeText(this, getString(R.string.msg_profil_updated), Toast.LENGTH_SHORT).show();
    }

    private void loadProfileData() {
        android.content.SharedPreferences prefs = getSharedPreferences("UserProfile", MODE_PRIVATE);
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
        android.text.TextWatcher watcher = new android.text.TextWatcher() {
            @Override public void beforeTextChanged(CharSequence s, int start, int count, int after) {}
            @Override public void onTextChanged(CharSequence s, int start, int before, int count) {
                checkProfileChanges();
            }
            @Override public void afterTextChanged(android.text.Editable s) {}
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


    private String getBMICategory(double bmi) {
        if (bmi < 18.5) return getString(R.string.kurus);
        else if (bmi < 25) return getString(R.string.normal);
        else if (bmi < 30) return getString(R.string.gemuk);
        else return getString(R.string.obesitas);
    }

    private void addTableHeader() {
        TableRow headerRow = new TableRow(this);
        headerRow.setBackgroundColor(ContextCompat.getColor(this, R.color.surface_green));
        headerRow.setPadding(12, 12, 12, 12);

        headerRow.addView(createTableHeaderCell(getString(R.string.header_no)));
        headerRow.addView(createTableHeaderCell(getString(R.string.header_nama)));
        headerRow.addView(createTableHeaderCell(getString(R.string.header_gender)));
        headerRow.addView(createTableHeaderCell(getString(R.string.header_bmi)));
        headerRow.addView(createTableHeaderCell(getString(R.string.header_status)));
        headerRow.addView(createTableHeaderCell(getString(R.string.header_umur)));

        tableData.addView(headerRow);
    }

    private TextView createTableHeaderCell(String text) {
        TextView tv = new TextView(this);
        tv.setText(text);
        tv.setTypeface(null, android.graphics.Typeface.BOLD);
        tv.setTextColor(ContextCompat.getColor(this, R.color.primary_dark_green));
        int paddingPx = (int) (8 * getResources().getDisplayMetrics().density);
        tv.setPadding(paddingPx, paddingPx, paddingPx, paddingPx);
        return tv;
    }

    private void addDataToTable(String nama, String gender, String bmi, String kategori, String umur) {
        dataCount++;
        TableRow row = new TableRow(this);
        row.setPadding(10, 10, 10, 10);
        if (dataCount % 2 == 0) row.setBackgroundColor(android.graphics.Color.parseColor("#F9F9F9"));

        row.addView(createTableCell(String.valueOf(dataCount)));
        row.addView(createTableCell(nama));
        row.addView(createTableCell(gender));
        row.addView(createTableCell(bmi));
        row.addView(createTableCell(kategori));
        row.addView(createTableCell(getString(R.string.format_umur_display, Integer.parseInt(umur))));

        tableData.addView(row);
    }

    private TextView createTableCell(String text) {
        TextView tv = new TextView(this);
        tv.setText(text);
        int paddingPx = (int) (8 * getResources().getDisplayMetrics().density);
        tv.setPadding(paddingPx, paddingPx, paddingPx, paddingPx);
        tv.setTextColor(ContextCompat.getColor(this, R.color.text_main));
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        return tv;
    }

    private String formatTitleCase(String input) {
        if (input == null || input.isEmpty()) return "";
        StringBuilder titleCase = new StringBuilder();
        boolean nextTitleCase = true;
        for (char c : input.toLowerCase().toCharArray()) {
            if (Character.isSpaceChar(c)) nextTitleCase = true;
            else if (nextTitleCase) {
                c = Character.toTitleCase(c);
                nextTitleCase = false;
            }
            titleCase.append(c);
        }
        return titleCase.toString();
    }
}
