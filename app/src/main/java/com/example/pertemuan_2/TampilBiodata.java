package com.example.pertemuan_2;

import android.os.Bundle;
import android.widget.Button;
import android.widget.TextView;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class TampilBiodata extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_tampil_biodata);
        
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inisialisasi View
        TextView tvNama = findViewById(R.id.tvHasilNama);
        TextView tvUmur = findViewById(R.id.tvHasilUmur);
        TextView tvNilai = findViewById(R.id.tvHasilNilai);
        Button btnKembali = findViewById(R.id.btnKembali);

        // Menangkap data dari Intent
        String nama = getIntent().getStringExtra("EXTRA_NAMA");
        String umur = getIntent().getStringExtra("EXTRA_UMUR");
        String nilai = getIntent().getStringExtra("EXTRA_NILAI");

        // Menampilkan data
        tvNama.setText(nama);
        // Menggunakan format string dari strings.xml untuk menghindari peringatan lint
        tvUmur.setText(getString(R.string.format_umur, umur));
        tvNilai.setText(nilai);

        // Tombol kembali
        btnKembali.setOnClickListener(v -> finish());
    }
}
