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

public class Dashboard extends AppCompatActivity {

    private SeekBar sbUmur;
    private TextView tvUmurDisplay, tvWelcome, tvUserRole;
    private EditText etInputName;
    private RadioGroup rgGender;
    private CheckBox cbAgree;
    private ProgressBar pbSaving;
    private Button btnSave, btnReset, btnClearData;
    private Spinner spJurusan;
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
        rgGender = findViewById(R.id.rgGender);
        cbAgree = findViewById(R.id.cbAgree);
        pbSaving = findViewById(R.id.pbSaving);
        btnSave = findViewById(R.id.btnSave);
        btnReset = findViewById(R.id.btnReset);
        btnClearData = findViewById(R.id.btnClearData);
        spJurusan = findViewById(R.id.spJurusan);
        tableData = findViewById(R.id.tableData);

        // Setup Spinner Jurusan
        String[] daftarJurusan = {"Informatika", "Sistem Informasi", "Teknik Elektro", "Teknik Sipil", "Manajemen", "Akuntansi", "Ilmu Komunikasi"};
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, android.R.layout.simple_spinner_item, daftarJurusan);
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item);
        spJurusan.setAdapter(adapter);

        LinearLayout btnSearch = findViewById(R.id.btnSearch);
        LinearLayout btnProfile = findViewById(R.id.btnProfile);
        LinearLayout btnHome = findViewById(R.id.btnHome);

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

        // 3. Logika Simpan Data (dengan Loading Interaksi)
        btnSave.setOnClickListener(v -> {
            String namaRaw = etInputName.getText().toString().trim();
            if (namaRaw.isEmpty()) {
                Toast.makeText(this, "Nama tidak boleh kosong!", Toast.LENGTH_SHORT).show();
                return;
            }

            // Tampilkan Loading
            pbSaving.setVisibility(View.VISIBLE);
            btnSave.setEnabled(false);
            btnReset.setEnabled(false);

            // Simulasi proses penyimpanan (delay 2 detik)
            new Handler().postDelayed(() -> {
                pbSaving.setVisibility(View.GONE);
                btnSave.setEnabled(true);
                btnReset.setEnabled(true);
                
                // Format Nama ke Title Case
                String namaFormatted = formatTitleCase(namaRaw);
                int umur = sbUmur.getProgress();

                // Ambil Gender
                int selectedGenderId = rgGender.getCheckedRadioButtonId();
                RadioButton rbSelected = findViewById(selectedGenderId);
                String gender = (rbSelected != null) ? rbSelected.getText().toString() : "-";

                // Ambil Jurusan
                String prodi = spJurusan.getSelectedItem().toString();

                // Status Persetujuan
                String status = cbAgree.isChecked() ? "Setuju" : "Tidak Setuju";

                // Tambahkan ke Tabel
                addDataToTable(namaFormatted, gender, prodi, String.valueOf(umur), status);

                Toast.makeText(this, "Data " + namaFormatted + " Berhasil Disimpan!", Toast.LENGTH_LONG).show();
            }, 2000);
        });

        // 4. Logika Reset
        btnReset.setOnClickListener(v -> {
            etInputName.setText("");
            rgGender.check(R.id.rbPria);
            cbAgree.setChecked(false);
            sbUmur.setProgress(20);
            spJurusan.setSelection(0);
            tvUmurDisplay.setText("20 Tahun");
        });

        // 5. Implicit Intent: Search
        btnSearch.setOnClickListener(v -> {
            Intent intent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://www.google.com"));
            startActivity(intent);
        });

        // Log out
        btnProfile.setOnClickListener(v -> finish());

        // 6. Logika Hapus Semua Data
        btnClearData.setOnClickListener(v -> {
            // Refresh tabel
            tableData.removeAllViews();
            dataCount = 0;
            // Tambahkan header kembali
            addTableHeader();
            Toast.makeText(this, "Semua data telah dihapus!", Toast.LENGTH_SHORT).show();
        });
    }

    private void addTableHeader() {
        TableRow headerRow = new TableRow(this);
        headerRow.setBackgroundColor(android.graphics.Color.parseColor("#F0F2F5"));
        headerRow.setPadding(12, 12, 12, 12);

        headerRow.addView(createTableHeaderCell("No"));
        headerRow.addView(createTableHeaderCell("Nama Lengkap"));
        headerRow.addView(createTableHeaderCell("Gender"));
        headerRow.addView(createTableHeaderCell("Prodi"));
        headerRow.addView(createTableHeaderCell("Umur"));
        headerRow.addView(createTableHeaderCell("Status"));

        tableData.addView(headerRow);
    }

    private TextView createTableHeaderCell(String text) {
        TextView tv = new TextView(this);
        tv.setText(text);
        tv.setTypeface(null, android.graphics.Typeface.BOLD);
        tv.setTextColor(android.graphics.Color.parseColor("#6200EE")); // purple
        int paddingPx = (int) (8 * getResources().getDisplayMetrics().density);
        tv.setPadding(paddingPx, paddingPx, paddingPx, paddingPx);
        return tv;
    }

    private void addDataToTable(String nama, String gender, String prodi, String umur, String status) {
        dataCount++;

        TableRow row = new TableRow(this);
        row.setPadding(10, 10, 10, 10);

        // Atur background selang-seling agar mudah dibaca
        if (dataCount % 2 == 0) {
            row.setBackgroundColor(android.graphics.Color.parseColor("#F9F9F9"));
        }

        TextView tvNo = createTableCell(String.valueOf(dataCount));
        TextView tvNama = createTableCell(nama);
        TextView tvGender = createTableCell(gender);
        TextView tvProdi = createTableCell(prodi);
        TextView tvUmur = createTableCell(umur + " th");
        TextView tvStatus = createTableCell(status);

        row.addView(tvNo);
        row.addView(tvNama);
        row.addView(tvGender);
        row.addView(tvProdi);
        row.addView(tvUmur);
        row.addView(tvStatus);

        tableData.addView(row);
    }

    private String formatTitleCase(String input) {
        if (input == null || input.isEmpty()) return "";
        
        StringBuilder titleCase = new StringBuilder();
        boolean nextTitleCase = true;

        for (char c : input.toLowerCase().toCharArray()) {
            if (Character.isSpaceChar(c)) {
                nextTitleCase = true;
            } else if (nextTitleCase) {
                c = Character.toTitleCase(c);
                nextTitleCase = false;
            }
            titleCase.append(c);
        }

        return titleCase.toString();
    }

    private TextView createTableCell(String text) {
        TextView tv = new TextView(this);
        tv.setText(text);
        // Gunakan padding yang lebih luas (sekitar 8dp)
        int paddingPx = (int) (8 * getResources().getDisplayMetrics().density);
        tv.setPadding(paddingPx, paddingPx, paddingPx, paddingPx);
        tv.setTextColor(android.graphics.Color.parseColor("#333333"));
        tv.setTextSize(TypedValue.COMPLEX_UNIT_SP, 14);
        return tv;
    }
}
