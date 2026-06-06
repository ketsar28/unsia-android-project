# 💚 CarePulse — Premium Health & BMI Tracker App

[![Platform](https://img.shields.io/badge/Platform-Android-3DDC84?style=for-the-badge&logo=android&logoColor=white)](https://developer.android.com/)
[![Language](https://img.shields.io/badge/Language-Java-ED8B00?style=for-the-badge&logo=openjdk&logoColor=white)](https://www.oracle.com/java/)
[![Framework](https://img.shields.io/badge/Theme-Material%203-0F5132?style=for-the-badge&logo=google-materials&logoColor=white)](https://m3.material.io/)
[![Academic Project](https://img.shields.io/badge/Course-Pemrograman%20Bergerak%20--%20IF701-blue?style=for-the-badge)](https://unsia.ac.id/)

**CarePulse** adalah aplikasi pemantau kesehatan dan kalkulator BMI (*Body Mass Index*) premium, modern, dan aman yang dikembangkan untuk memenuhi tugas **Ujian Tengah Semester (UTS) Mata Kuliah Pemrograman Bergerak (IF701)** pada Program Studi Informatika, **Universitas Siber Asia (UNSIA)**. 

Aplikasi ini telah dirombak total menjadi aplikasi berstandar industri dengan estetika visual *Emerald Green* yang dinamis (dilengkapi dengan *gradient backgrounds* dan *micro-interactions*), sistem keamanan autentikasi multi-peran (*Role-Based Access Control*), registrasi akun baru yang fungsional, pengolahan data persisten melalui **SharedPreferences**, serta protokol validasi input medis yang sangat kokoh.

---

## 🗺️ Fitur Utama & Keunggulan CarePulse

### 1. 🔑 Splash Screen & Autentikasi Cerdas
* **Premium Splash Screen**: Menampilkan logo CarePulse dengan latar belakang gradasi vertikal emerald mewah selama **3 detik** secara otomatis menggunakan `Handler` & `Looper`. Dilengkapi dengan animasi masuk yang halus (*fade-in & slide-up*).
* **Fungsionalitas Registrasi Baru**: Halaman pendaftaran akun kini aktif sepenuhnya! Menginput nama lengkap, username, dan password (minimal 6 karakter) akan menyimpan kredensial ke `SharedPreferences` lokal.
* **Auto-Fill & Auto-Route**: Setelah pendaftaran berhasil, aplikasi akan menampilkan dialog Material kustom, otomatis menutup layar registrasi, mengarahkan pengguna kembali ke layar Login, mem-prefill field username, dan menyeleksi status peran sebagai **User**.
* **Animasi Masuk Modern**: Layar Login dan Register menyajikan efek animasi *slide-up* berdurasi 1 detik pada card input untuk memberikan kesan visual yang premium.
* **Multi-Role Login**: Membedakan masuk sebagai **Admin** atau **User** melalui pilihan *Material Dropdown (Spinner)*.
* **Inline Validation**: Deteksi dini kolom kosong menggunakan `TextInputLayout.setError()` secara interaktif.

### 2. 👥 Matriks Hak Akses Ketat (Role-Based Access Control / RBAC)
Proyek ini membagi wewenang secara tegas antara peran **Admin** dan **User**:

| Wewenang / Fitur | 🛠️ ADMIN (Superuser) | 💚 USER (Regular Member) |
| :--- | :--- | :--- |
| **Menu Grid (Home)** | **Akses Penuh (6 Fitur)**:<br>1. Kelola Pengguna (Simulasi)<br>2. Data Master (Statistik)<br>3. Laporan (Ringkasan BMI)<br>4. Pengaturan Aplikasi<br>5. Kirim Notifikasi<br>6. Pusat Bantuan Admin | **Akses Terbatas (3 Fitur)**:<br>1. Beranda (Tips Kesehatan)<br>2. Pengumuman Terkini dari Admin<br>3. Profil Saya<br>*(Menu kelola disembunyikan)* |
| **Input Form (BMI)** | **Bebas Input Nama**: Kolom nama aktif penuh untuk mencatat data kesehatan siapa pun. | **Terkunci Otomatis**: Kolom nama otomatis terisi dengan nama login Anda dan berstatus **Read-Only (Locked)**. |
| **Kontrol Riwayat** | **Akses Penuh**: Tombol **Hapus Semua** aktif berwarna merah untuk membersihkan data dari `SharedPreferences`. | **Akses Ditolak**: Tombol Hapus dinonaktifkan (Greyed-out). Menampilkan Toast penolakan jika dicoba. |
| **Statistik Profil** | **Multi-User Stats**: Menghitung data agregat dari seluruh riwayat data di sistem. | **Personal Stats**: Menampilkan jumlah kalkulasi yang dilakukan oleh user tersebut. |

### 3. 📊 Kalkulator BMI & Penyimpanan Persisten (BmiFragment)
* **Penyimpanan Lokal Persisten**: Berbeda dengan data statis *in-memory* biasa, seluruh riwayat kalkulasi BMI kini disimpan ke dalam **SharedPreferences** (Key: `BmiPrefs`). Data tidak akan hilang meskipun aplikasi ditutup, dikeluarkan dari background, atau HP dimatikan!
* **Validasi Fisik Medis Berlapis**: 
  * **Berat Badan**: Dibatasi ketat di rentang **20 kg – 250 kg**.
  * **Tinggi Badan**: Dibatasi ketat di rentang **100 cm – 250 cm**.
  * Jika di luar rentang, proses langsung diblokir dan memicu `TextInputLayout.setError()`.
* **Interactive SeekBar**: Pemilihan umur (10 – 100 tahun) yang dinamis menggunakan seekbar horizontal dengan tampilan angka real-time.
* **Pernyataan Persetujuan (Agreement CheckBox)**: Tombol hitung terkunci sebelum pengguna menyetujui validitas data.
* **Tabel Riwayat Dinamis (Zebra-Striped)**: Riwayat perhitungan disajikan dalam `TableLayout` di dalam `HorizontalScrollView` dengan baris selang-seling warna abu/putih demi keterbacaan data maksimal.

### 4. ⚙️ Profil Diri Interaktif (ProfileFragment)
* **Real-time Change Detection**: Menggunakan `TextWatcher` pada kolom nomor telepon dan alamat untuk mendeteksi perubahan input secara instan dan mengaktifkan tombol simpan secara dinamis.
* **Penyimpanan Profil**: Informasi profil seperti Telepon, Alamat, Kelurahan, Kecamatan, dan Kota disimpan dengan aman di penyimpanan lokal `SharedPreferences` (Key: `UserProfile`).
* **Statistik Dinamis**: Halaman Profil memuat counter total perhitungan BMI dari `SharedPreferences` secara real-time.

---

## 🛠️ Arsitektur & Teknologi

* **Bahasa Pemrograman**: Java (JDK 11 / 17)
* **Minimum SDK**: API 24 (Android 7.0 Nougat)
* **Target & Compile SDK**: API 34 (Android 14)
* **Desain UI**: Material Design 3 (M3) dengan komponen modern (`TextInputLayout`, `MaterialCardView`, `MaterialButton`).
* **Latar Belakang**: Custom Light Gradient Drawable (`gradient_light_bg.xml`) untuk transisi visual yang halus di semua layar utama.
* **Penyimpanan Data**: `SharedPreferences` (Aman, cepat, dan sesuai dengan batasan UTS tanpa memerlukan database SQLite/Room/MySQL eksternal yang kompleks).

---

## 📂 Struktur Direktori Proyek Utama

```
android_studio/app/src/main/
├── java/com/example/pertemuan_2/
│   ├── SplashActivity.java    # Layar pembuka (Splash screen 3 detik dengan animasi)
│   ├── MainActivity.java      # Login Activity (Multi-Role, validasi, onResume autofill)
│   ├── Register.java          # Register Activity (Fungsional, menyimpan ke SharedPreferences)
│   ├── Dashboard.java         # Shell utama penampung Navigasi, Fragment, & Toolbar
│   ├── HomeFragment.java      # Beranda dinamis (Matriks menu Admin/User)
│   ├── BmiFragment.java       # Form BMI, Validasi medis, Persistensi data, & Tabel Zebra
│   ├── ProfileFragment.java   # Profil dengan TextWatcher & SharedPreferences
│   ├── Biodata.java           # Legacy Activity (Backward Compatibility)
│   └── TampilBiodata.java     # Legacy Activity (Backward Compatibility)
│
└── res/
    ├── drawable/              # Ikon kustom XML Vector & Light Gradient Background
    ├── layout/                # Seluruh desain visual XML (Activity & Fragment)
    └── values/
        ├── colors.xml         # Skema warna Emerald Green premium
        ├── strings.xml        # Lokalisasi bahasa Indonesia lengkap
        └── themes.xml         # Konfigurasi style Material 3 NoActionBar
```

---

## 🚀 Cara Menjalankan Proyek

1. **Buka di Android Studio**:
   * Pilih **File** -> **Open**, arahkan ke folder `android_studio` di dalam direktori proyek.
2. **Sinkronisasi Gradle**:
   * Tunggu Gradle menyelesaikan proses sinkronisasi (*Gradle Sync*).
3. **Jalankan Aplikasi**:
   * Hubungkan perangkat Android fisik (aktifkan USB Debugging) atau gunakan Emulator.
   * Klik tombol **Run** (Ikon Segitiga Hijau) di toolbar atas Android Studio.

---

## 🧪 Panduan Pengujian & Testing Aplikasi (Demo UTS)

Silakan gunakan panduan pengujian berikut untuk demonstrasi saat presentasi:

### 1. Kredensial Akun Pengujian
* **🔐 Akun Demo USER (Statis)**:
  * **Username**: `user`
  * **Password**: `user123`
* **🔐 Akun Demo ADMIN (Statis)**:
  * **Username**: `admin`
  * **Password**: `admin123`
* **🔐 Akun Custom USER**:
  * Didapatkan melalui pendaftaran di halaman **Register** (disimpan secara dinamis dan diisolasi di SharedPreferences).

### 2. Langkah-Langkah Skenario Pengujian (Step-by-Step Testing)

#### Skenario A: Pendaftaran Multi-Akun & Keandalan Autofill
1. Jalankan aplikasi, amati animasi logo pada **Splash Screen** selama **3 detik**.
2. Di halaman **Login**, klik tombol **"Belum punya akun? Daftar di sini"**.
3. Daftarkan **User Pertama**:
   * **Nama Lengkap**: `Budi Santoso`
   * **Username**: `budi`
   * **Kata Sandi**: `budi123` (minimal 6 karakter)
   * Klik **"DAFTAR SEKARANG"**.
   * Setelah muncul dialog sukses, klik **"Masuk Sekarang"**. Anda diarahkan ke layar login dengan field Username otomatis terisi `budi` dan Kata Sandi terisi `budi123`.
4. Hapus input tersebut, klik **"Daftar di sini"** lagi untuk mendaftarkan **User Kedua**:
   * **Nama Lengkap**: `Cici Amelia`
   * **Username**: `cici`
   * **Kata Sandi**: `cici123`
   * Klik **"DAFTAR SEKARANG"** -> klik **"Masuk Sekarang"**.
   * Pastikan Username `cici` dan Kata Sandi `cici123` otomatis terinput dengan benar di halaman login.

#### Skenario B: Pengujian Isolasi Data Pengguna (Data Masing-masing)
1. Login menggunakan akun `budi` / `budi123`.
2. Masuk ke menu **Kalkulator** (tab tengah). Lakukan perhitungan BMI (misal: Berat `70`, Tinggi `172`, Umur `24`).
3. Tekan **"HITUNG"**. Data kalkulasi BMI `Budi Santoso` akan tersimpan ke dalam tabel riwayat.
4. Masuk ke tab **Profil** (paling kanan). Amati bahwa **Total Cek BMI** bertambah menjadi **1**.
5. Isi data profil tambahan (Telepon: `08123456789`, Alamat: `Jl. Melati No. 5`). Klik **"SIMPAN PERUBAHAN"**.
6. **Logout** dari akun Budi melalui tombol Logout di kanan atas toolbar.
7. Login menggunakan akun `cici` / `cici123`.
8. Masuk ke menu **Kalkulator** (tab tengah). Verifikasi bahwa tabel riwayat BMI milik Cici **kosong** (data Budi tidak bocor ke Cici).
9. Lakukan perhitungan BMI untuk Cici (misal: Berat `50`, Tinggi `160`, Umur `21`). Tekan **"HITUNG"**. Riwayat BMI Cici sekarang memiliki 1 data.
10. Masuk ke tab **Profil**, pastikan total cek Cici adalah **1** dan form profil (Telepon & Alamat) masih kosong. Isi telepon: `08987654321`. Klik simpan.
11. **Logout** dan masuk kembali ke `budi`. Pastikan data profil Budi (`Jl. Melati No. 5`) dan riwayat BMI Budi yang lama masih tetap utuh (tidak tertimpa data Cici).

#### Skenario C: Pengujian Panel Kontrol Admin & Manajemen Akun (Hapus Pengguna)
1. Logout dari akun User, lalu login sebagai **Admin**:
   * **Username**: `admin`
   * **Password**: `admin123`
   * **Masuk Sebagai**: Pilih **Admin** pada Spinner.
2. Di halaman **Beranda**, status Anda tertulis **Admin** dengan badge hijau **"Akses Penuh"**.
3. Klik menu **Kelola Pengguna**. Amati bahwa dialog kini menampilkan seluruh daftar pengguna aktif secara dinamis:
   - *Andi Pratama (user) - Akun Bawaan*
   - *Budi Santoso (budi) - Klik untuk Detail*
   - *Cici Amelia (cici) - Klik untuk Detail*
4. Klik pada nama **Budi Santoso (budi)**.
5. Sistem akan menampilkan **Material Detail Dialog** berisi biodata profil lengkap Budi (Nama Lengkap, Username, Telepon, Alamat, Kelurahan, Kecamatan, Kota, dan Total Cek BMI).
6. Di bagian bawah detail profil tersebut, klik tombol merah **"Hapus Pengguna"**.
7. Sistem akan memicu **Material Dialog Konfirmasi**: *"Apakah Anda yakin ingin menghapus akun Budi Santoso (budi) beserta semua datanya?"*.
8. Klik **"Hapus"**. Pastikan muncul Toast sukses bahwa akun `budi` berhasil dihapus.
9. Klik kembali menu **Kelola Pengguna**, verifikasi bahwa akun `budi` telah hilang dari daftar.
10. Masuk ke tab **Kalkulator** (tab tengah) di Admin. Perhatikan bahwa tabel riwayat BMI di bagian bawah sekarang terisi dengan seluruh data milik `user` dan `cici` secara otomatis (agregat), tidak kosong lagi!
11. Buka kembali tab Beranda (tab kiri), klik menu **Data Master** dan **Laporan**. Sistem akan mengumpulkan data kalkulasi dari seluruh user aktif yang tersisa (`user` dan `cici`) secara dinamis untuk menyajikan data agregat (Rata-rata umur, kategori terbanyak, rata-rata BMI, dll).
12. Logout dari Admin.
13. Di layar login, coba masuk kembali menggunakan akun yang telah dihapus: `budi` / `budi123`. Pastikan login **gagal** (menampilkan Toast *"Username atau kata sandi salah!"*).
14. Coba login menggunakan akun yang tidak dihapus: `cici` / `cici123`. Pastikan login **berhasil** dan data Cici tetap aman.

---
*CarePulse — "Your Health, Our Priority" | Dibuat dengan 💚 untuk Kualitas Terbaik Ujian Tengah Semester.*
