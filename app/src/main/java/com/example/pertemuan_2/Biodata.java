package com.example.pertemuan_2;

import android.content.Intent;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import androidx.activity.EdgeToEdge;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.graphics.Insets;
import androidx.core.view.ViewCompat;
import androidx.core.view.WindowInsetsCompat;

public class Biodata extends AppCompatActivity {

    private EditText etNama, etUmur, etNilai;
    private Button btnKirim;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EdgeToEdge.enable(this);
        setContentView(R.layout.activity_biodata);
        
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main), (v, insets) -> {
            Insets systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars());
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom);
            return insets;
        });

        // Inisialisasi View
        etNama = findViewById(R.id.etNama);
        etUmur = findViewById(R.id.etUmur);
        etNilai = findViewById(R.id.etNilai);
        btnKirim = findViewById(R.id.btnKirim);

        btnKirim.setOnClickListener(v -> {
            String nama = etNama.getText().toString();
            String umur = etUmur.getText().toString();
            String nilai = etNilai.getText().toString();

            // Validasi sederhana
            if (nama.isEmpty() || umur.isEmpty() || nilai.isEmpty()) {
                Toast.makeText(this, "Mohon lengkapi semua data!", Toast.LENGTH_SHORT).show();
            } else {
                // Berpindah ke TampilBiodata sambil membawa data (Explicit Intent)
                Intent intent = new Intent(Biodata.this, TampilBiodata.class);
                intent.putExtra("EXTRA_NAMA", nama);
                intent.putExtra("EXTRA_UMUR", umur);
                intent.putExtra("EXTRA_NILAI", nilai);
                startActivity(intent);
            }
        });
    }
}
