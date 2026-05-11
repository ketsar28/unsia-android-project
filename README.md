# Unsia Android Project

Project ini adalah kumpulan tugas mata kuliah Pemrograman Bergerak (Android) di Universitas Siber Asia.

## 📌 Update Terbaru: Pertemuan 3 (Activity & Intent)
Pada pertemuan ini, fokus utama adalah perpindahan antar halaman (Activity) dan pengiriman data menggunakan **Intent**.

### Fitur Baru & Perubahan:
- **Navigasi Antar Halaman**: Menggunakan `Explicit Intent` untuk berpindah dari Login ke Register dan ke Input Biodata.
- **Passing Data**: Mengirim data Nama, Umur, dan Nilai dari `BiodataActivity` ke `TampilBiodataActivity`.
- **UI Modern**: Implementasi `MaterialCardView` untuk tampilan yang lebih profesional dan bersih.
- **Resource Management**: Seluruh teks dipindahkan ke `strings.xml` untuk mendukung *best practice* pengembangan Android.
- **Fix Edge-to-Edge**: Penyesuaian padding dan warna teks agar kompatibel dengan sistem Android terbaru (termasuk Dark Mode).

## 🚀 Fitur Umum
- **Layout Responsive**: Penggunaan `LinearLayout` dan `ScrollView`.
- **Custom Vector Asset**: Ikon profil menggunakan Vector Drawable.
- **Input Components**: `EditText`, `RadioGroup`, `RadioButton`, dan `CheckBox`.

## 🛠️ Teknologi
- **Language**: Java
- **UI Framework**: Material Components for Android
- **Satuan Ukuran**: `dp` (layout) dan `sp` (teks)
- **Gradle**: Wrapper enabled

## 📸 Cara Menjalankan
1. Clone repository:
   ```bash
   git clone https://github.com/ketsar28/unsia-android-project.git
   ```
2. Buka di Android Studio (Koala atau versi lebih baru).
3. Build & Run:
   ```bash
   ./gradlew assembleDebug
   ```

---
**Author**: [ketsar28](https://github.com/ketsar28)
