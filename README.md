# Unsia Android Project

Project ini adalah kumpulan tugas mata kuliah Pemrograman Bergerak (Android) di Universitas Siber Asia.

## 📌 Update Terbaru: Pertemuan 5 (Advanced Input, Formatting & Dynamic Table)
Pada pertemuan ini, dilakukan pengembangan fitur Dashboard dengan penekanan pada pengolahan input pengguna yang lebih kompleks dan penyajian data yang lebih rapi.

### Fitur Baru & Perubahan:
- **Advanced Form Input**: 
  - Penambahan `Spinner` (Dropdown) untuk pemilihan Program Studi (Prodi).
  - Implementasi `CheckBox` opsional; status persetujuan ("Setuju" / "Tidak Setuju") kini direkam dalam tabel.
- **Auto Data Formatting**: Implementasi logika *Title Case* (contoh: "puTRi" -> "Putri") secara otomatis menggunakan fungsi kustom sebelum data disimpan ke tabel.
- **Dynamic Table Enhancements**:
  - Tabel kini mencakup kolom: No, Nama Lengkap, Gender, Prodi, Umur, dan Status.
  - Fitur **Hapus Semua**: Memungkinkan pengguna membersihkan seluruh data di tabel secara dinamis.
  - *Zebra-striping*: Baris tabel memiliki warna selang-seling untuk meningkatkan keterbacaan data.
- **UI/UX Refinement**:
  - Penggunaan `HorizontalScrollView` pada tabel agar data tetap dapat diakses pada layar yang sempit.
  - Perbaikan layout menggunakan `clipToPadding="false"` pada `ScrollView` utama agar konten tidak terpotong oleh bottom navigation.
  - Custom styling pada `Spinner` menggunakan drawable resource.

## 📌 Pertemuan 4 (Validation, Dashboard & Implicit Intent)
- **Dynamic Display Name**: User dapat memasukkan nama lengkap saat login yang kemudian ditampilkan secara dinamis di Dashboard.
- **Login Validation**: Validasi input kosong (Nama, Username, Password).
- **Login Credentials**: Admin (`admin/admin123`) dan User (`user/user123`).
- **Implicit Intent**: Tombol "Search" di navigasi bawah mengarahkan user ke browser eksternal (Google).

## 📌 Pertemuan 3 (Activity & Intent)
- **Navigasi Antar Halaman**: Menggunakan `Explicit Intent`.
- **Passing Data**: Mengirim data Nama, Umur, dan Nilai antar activity.

## 🛠️ Teknologi
- **Language**: Java
- **UI Framework**: Material Components for Android
- **Satuan Ukuran**: `dp` (layout) dan `sp` (teks)

---
**Author**: [ketsar28](https://github.com/ketsar28)
