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

        // Inisialisasi View
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

        // Setup Spinner Aktivitas Olahraga
        String[] daftarAktivitas = {"Jarang (Sedenter)", "Ringan (1-3 hari/minggu)", "Sedang (3-5 hari/minggu)", "Berat (6-7 hari/minggu)", "Atlet (Sangat Berat)"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, daftarAktivitas);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spAktivitas.setAdapter(adapter);

        // 1. Tampilkan Nama User dari Intent
        String username = getIntent().getStringExtra("EXTRA_USERNAME");
        String role = getIntent().getStringExtra("EXTRA_ROLE");
        if (username == null) username = "User";
        tvWelcome.setText(getString(R.string.welcome_message, username));
        tvUserRole.setText(role);

        // 2. Logika SeekBar (Umur Dinamis)
        sbUmur.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            @Override
            public void onProgressChanged(SeekBar seekBar, int progress, boolean fromUser) {
                tvUmurDisplay.setText(progress + " Tahun");
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
                Toast.makeText(this, getString(R.string.error_empty_fields), Toast.LENGTH_SHORT).show();
                return;
            }

            if (!cbAgree.isChecked()) {
                Toast.makeText(this, "Mohon centang pernyataan kebenaran data!", Toast.LENGTH_SHORT).show();
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
            tvUmurDisplay.setText("20 Tahun");
        });

        // 5. Implicit Intent: Search info kesehatan
        findViewById(R.id.btnSearch).setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com/search?q=tips+hidup+sehat+bmi"));
            startActivity(intent);
        });

        // Logout
        findViewById(R.id.btnProfile).setOnClickListener(v -> finish());

        // 6. Logika Hapus Semua Data
        btnClearData.setOnClickListener(v -> {
            tableData.removeAllViews();
            dataCount = 0;
            addTableHeader();
            Toast.makeText(this, getString(R.string.data_cleared), Toast.LENGTH_SHORT).show();
        });
    }

    private String getBMICategory(double bmi) {
        if (bmi < 18.5) return "Kurus";
        else if (bmi < 25) return "Normal";
        else if (bmi < 30) return "Gemuk";
        else return "Obesitas";
    }

    private void addTableHeader() {
        TableRow headerRow = new TableRow(this);
        headerRow.setBackgroundColor(android.graphics.Color.parseColor("#F0F2F5"));
        headerRow.setPadding(12, 12, 12, 12);

        headerRow.addView(createTableHeaderCell("No"));
        headerRow.addView(createTableHeaderCell("Nama"));
        headerRow.addView(createTableHeaderCell("Gender"));
        headerRow.addView(createTableHeaderCell("BMI"));
        headerRow.addView(createTableHeaderCell("Kategori"));
        headerRow.addView(createTableHeaderCell("Umur"));

        tableData.addView(headerRow);
    }

    private TextView createTableHeaderCell(String text) {
        TextView tv = new TextView(this);
        tv.setText(text);
        tv.setTypeface(null, android.graphics.Typeface.BOLD);
        tv.setTextColor(android.graphics.Color.parseColor("#6200EE"));
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
        row.addView(createTableCell(umur + " th"));

        tableData.addView(row);
    }

    private TextView createTableCell(String text) {
        TextView tv = new TextView(this);
        tv.setText(text);
        int paddingPx = (int) (8 * getResources().getDisplayMetrics().density);
        tv.setPadding(paddingPx, paddingPx, paddingPx, paddingPx);
        tv.setTextColor(android.graphics.Color.parseColor("#333333"));
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
