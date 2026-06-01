# 💚 CarePulse — Premium Health & BMI Tracker App

[![Platform](https://img.shields.io/badge/Platform-Android-3DDC84?style=for-the-badge&logo=android&logoColor=white)](https://developer.android.com/)
[![Language](https://img.shields.io/badge/Language-Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)](https://www.oracle.com/java/)
[![Framework](https://img.shields.io/badge/Theme-Material%203-0F5132?style=for-the-badge&logo=google-materials&logoColor=white)](https://m3.material.io/)
[![Academic Project](https://img.shields.io/badge/Course-Pemrograman%20Bergerak%20--%20IF701-blue?style=for-the-badge)](https://unsia.ac.id/)

**CarePulse** adalah aplikasi pemantau kesehatan dan kalkulator BMI (*Body Mass Index*) premium, modern, dan aman yang dikembangkan untuk memenuhi tugas **Ujian Tengah Semester (UTS) Mata Kuliah Pemrograman Bergerak (IF701)** pada Program Studi Informatika, **Universitas Siber Asia (UNSIA)**. 

Aplikasi ini telah dirombak total dari proyek sebelumnya menjadi sebuah aplikasi berstandar industri dengan estetika visual *Emerald Green* yang sangat memukau, dilengkapi sistem keamanan autentikasi multi-peran (*Role-Based Access Control*), pengolahan data dinamis, serta protokol validasi input medis yang sangat kokoh.

---

## 📸 Tampilan Utama Aplikasi
Aplikasi mengusung bahasa desain **Material 3** modern dengan sentuhan tema hijau emerald yang mewah (*Premium Dark Emerald & Soft Mint*), memberikan kenyamanan maksimal bagi mata pengguna (*User-Friendly & Accessible*).

---

## 🗺️ Fitur Utama & Keunggulan CarePulse

### 1. 🔑 Splash Screen & Autentikasi Cerdas
* **Premium Splash Screen**: Menampilkan logo kustom CarePulse dengan latar belakang gradasi vertikal emerald mewah selama **3 detik** secara otomatis menggunakan `Handler` & `Looper`. Dilengkapi indikator progress bar horizontal yang elegan dan informasi footer Universitas Siber Asia.
* **Multi-Role Login**: Autentikasi modern yang membedakan hak masuk sebagai **Admin** atau **User** melalui pilihan *Material Dropdown (Spinner)* dengan kustomisasi visual dropdown.
* **Inline Validation**: Deteksi dini kolom kosong menggunakan `TextInputLayout.setError()` yang interaktif (garis tepi otomatis berubah menjadi merah menyala dengan deskripsi error di bawah kolom).
* **Remember Me**: Opsi mengingat sesi login menggunakan checkbox yang langsung terintegrasi secara dinamis.

### 2. 👥 Matriks Hak Akses Ketat (Role-Based Access Control / RBAC)
Untuk memberikan perbedaan fungsional yang nyata, aplikasi membagi wewenang secara tegas di tingkat kode Java:

| Wewenang / Fitur | 🛠️ ADMIN (Superuser) | 💚 USER (Regular Member) |
| :--- | :--- | :--- |
| **Menu Grid (Home)** | **Akses Penuh (6 Fitur)**:<br>1. Kelola Pengguna (Simulasi Akun)<br>2. Data Master (Statistik Sistem)<br>3. Laporan (Analisis BMI Kolektif)<br>4. Pengaturan Aplikasi<br>5. Kirim Notifikasi Masal<br>6. Pusat Bantuan Admin | **Akses Terbatas (3 Fitur)**:<br>1. Beranda (Tips Hidup Sehat)<br>2. Pengumuman Terkini dari Admin<br>3. Profil Saya<br>*(Menu kelola disembunyikan)* |
| **Input Form (BMI)** | **Bebas Input Nama**: Kolom nama aktif penuh untuk mencatat data kesehatan pasien mana pun. | **Terkunci Otomatis**: Kolom nama otomatis terisi dengan nama login (*"Andi Pratama"*) dan berstatus **Read-Only (Locked)** untuk mencegah manipulasi data orang lain. |
| **Kontrol Riwayat** | **Akses Penuh**: Tombol **Hapus Semua** aktif berwarna merah menyala untuk membersihkan tabel riwayat. | **Akses Ditolak**: Tombol Hapus dinonaktifkan (Greyed-out). Menampilkan peringatan Toast penolakan akses jika dicoba. |
| **Statistik Home** | **Multi-User Stats**: Mampu menghitung data agregat dari seluruh riwayat data di sistem. | **Personal Stats**: Hanya menampilkan performa dan catatan riwayat data milik pribadinya saja. |

### 3. 📊 Kalkulator BMI & Protokol Validasi Medis (BmiFragment)
* **Validasi Fisik Medis Berlapis**: Mencegah data tidak logis (*garbage data*):
  * **Berat Badan**: Dibatasi ketat di rentang **20 kg – 250 kg**.
  * **Tinggi Badan**: Dibatasi ketat di rentang **100 cm – 250 cm**.
  * Jika di luar rentang, proses langsung diblokir dan memicu `TextInputLayout.setError()`.
* **Interactive SeekBar**: Pemilihan umur (10 – 100 tahun) yang dinamis menggunakan seekbar horizontal dengan tampilan angka real-time.
* **Pernyataan Persetujuan (Agreement CheckBox)**: Tombol hitung terkunci sebelum pengguna mencentang persetujuan data. Jika dilewati, teks persetujuan otomatis berubah menjadi merah untuk menarik perhatian visual.
* **Simulasi Penyimpanan Realistis**: Menggunakan horizontal indeterminate `ProgressBar` selama 800ms untuk mensimulasikan proses kompilasi dan penyimpanan data yang aman.
* **Tabel Riwayat Dinamis (Zebra-Striped)**: Riwayat perhitungan disajikan dalam `TableLayout` di dalam `HorizontalScrollView` dengan baris selang-seling warna abu/putih demi keterbacaan data maksimal.

### 4. ⚙️ Profil Diri Interaktif (ProfileFragment)
* **Real-time Change Detection**: Menggunakan `TextWatcher` pada kolom nomor telepon dan alamat untuk mendeteksi perubahan input secara instan dan mengaktifkan tombol simpan secara dinamis.
* **Penyimpanan Lokal Persisten**: Informasi profil seperti Nama, Telepon, Alamat, Kelurahan, Kecamatan, dan Kota disimpan dengan aman di penyimpanan lokal `SharedPreferences` sehingga data tidak hilang saat aplikasi ditutup.

---

## 🛠️ Arsitektur & Teknologi

* **Bahasa Pemrograman**: Java (JDK 17)
* **Minimum SDK**: API 24 (Android 7.0 Nougat)
* **Target & Compile SDK**: API 34 / 35 (Android 14 / 15)
* **Desain UI**: Material Design 3 (M3) dengan komponen modern:
  * `TextInputLayout` & `TextInputEditText` (Input modern dengan label mengambang)
  * `MaterialCardView` (Desain kartu berbayang dengan sudut melengkung premium)
  * `MaterialButton` & `ShapeableImageView` (Elemen modern)
* **Penyimpanan Data**: `SharedPreferences` (Lokal & Cepat)
* **Arsitektur UI**: Multi-Fragment Single-Activity (Dashboard mengendalikan `HomeFragment`, `BmiFragment`, dan `ProfileFragment`).

---

## 📂 Struktur Direktori Proyek Utama

```
android_studio/app/src/main/
├── java/com/example/pertemuan_2/
│   ├── SplashActivity.java    # Layar pembuka (Splash screen 3 detik)
│   ├── MainActivity.java      # Login Activity (Multi-Role, validasi)
│   ├── Register.java          # Register Activity (No-Op sesuai instruksi UTS)
│   ├── Dashboard.java         # Shell utama penampung Navigasi & Toolbar
│   ├── HomeFragment.java      # Beranda dinamis (Matriks menu Admin/User)
│   ├── BmiFragment.java       # Form BMI, Validasi medis, & Tabel Zebra
│   ├── ProfileFragment.java   # Profil dengan TextWatcher & SharedPreferences
│   ├── Biodata.java           # Legacy Activity (Backward Compatibility)
│   └── TampilBiodata.java     # Legacy Activity (Backward Compatibility)
│
└── res/
    ├── drawable/              # Ikon kustom XML Vector (ic_carepulse_logo, gradient, dll)
    ├── layout/                # Seluruh desain visual XML (Activity & Fragment)
    └── values/
        ├── colors.xml         # Skema warna Emerald Green premium
        ├── strings.xml        # Lokalisasi bahasa Indonesia lengkap
        └── themes.xml         # Konfigurasi style Material 3 NoActionBar
```

---

## 🚀 Cara Menjalankan Proyek

1. **Clone Repositori**:
   ```bash
   git clone https://github.com/ketsar28/pertemuan-8-uts.git
   ```
2. **Buka di Android Studio**:
   * Jalankan Android Studio (versi Hedgehog atau yang lebih baru direkomendasikan).
   * Pilih **File** -> **Open**, arahkan ke folder `android_studio` di dalam direktori klon.
3. **Sinkronisasi Gradle**:
   * Tunggu Gradle menyelesaikan proses sinkronisasi (*Gradle Sync*).
4. **Jalankan Aplikasi**:
   * Hubungkan perangkat Android fisik (aktifkan USB Debugging) atau gunakan Emulator.
   * Klik tombol **Run** (Ikon Segitiga Hijau) di toolbar atas Android Studio.

---

## 📝 Akun Pengujian UTS

Gunakan kredensial statis berikut untuk menguji pembagian hak akses aplikasi:

* **🔐 Peran ADMIN**:
  * **Username**: `admin`
  * **Password**: `admin123`
  * **Akses**: Panel kontrol 6 menu aktif, input nama bebas, dan hapus semua data aktif.

* **🔐 Peran USER**:
  * **Username**: `user`
  * **Password**: `user123`
  * **Akses**: Panel kontrol 3 menu (lifestyle), input nama terkunci (*"Andi Pratama"*), dan penghapusan data diblokir.

---

## 🎓 Informasi Akademik

* **Mata Kuliah**: Pemrograman Bergerak — IF701
* **Dosen Pengampu**: Andi Asvin M. Suradi, S.Kom., M.T.
* **Institusi**: Program Studi Informatika, Universitas Siber Asia (UNSIA)
* **Mahasiswa (Author)**: [Ketsar](https://github.com/ketsar28)

---
*CarePulse — "Your Health, Our Priority" | Dibuat dengan 💚 untuk Kualitas Terbaik Ujian Tengah Semester.*
