# ♻️ CleanUp - Permainan Edukasi Memilah Sampah

**CleanUp** adalah aplikasi permainan edukasi berbasis Android yang dirancang untuk melatih kepekaan dan pemahaman anak-anak dalam memilah dan mengklasifikasikan jenis sampah sehari-hari secara interaktif dan menyenangkan.

---

## 🎮 Fitur & Mekanik Permainan

1. **Pemilahan 3 Kategori Sampah**:
   - 🟢 **Organik** (Hijau): Sisa makanan, daun, kulit buah, dll.
   - 🔵 **Anorganik** (Biru): Botol plastik, kaca, kaleng, kardus, dll.
   - 🔴 **B3 / Berbahaya & Beracun** (Merah): Baterai, lampu, obat, limbah elektronik, dll.
2. **Interaksi Drag-and-Drop**: Pemain membaca edukasi mengenai benda sampah lalu menyeretnya langsung ke tong sampah yang sesuai.
3. **Sistem Gamifikasi**:
   - Poin (+10 setiap jawaban benar).
   - Nyawa (3 ikon hati, berkurang jika salah memilih tong).
   - Rating Bintang (1–3 bintang berdasarkan performa per level).
4. **Peta Level (Level Progression)**: Akses level bertingkat dengan sistem gembok (*lock/unlock*).
5. **Fitur Pendukung**:
   - Halaman Panduan Edukasi lengkap.
   - Halaman Pengaturan (kontrol Musik Latar & Efek Suara).
   - Dialog Jeda (*Pause*) dan Layar Hasil (*Win/Game Over*).

---

## 🛠️ Tech Stack & Arsitektur

- **Bahasa Pemrograman**: Kotlin
- **UI Toolkit**: Jetpack Compose (Declarative UI) + Material 3
- **Basis Data Lokal**: Room Database (SQLite) dengan Auto-Seeder 30 item sampah
- **Navigasi**: Jetpack Navigation Compose
- **State Management**: Android ViewModel + Kotlin StateFlow / Flow
- **Penyimpanan Pengaturan**: Jetpack DataStore Preferences
- **Aset & Grafis**: Coil SVG Decoder
- **Audio**: Android MediaPlayer (Backsound loop & SFX interaktif)
- **Tipografi**: Fredoka (Judul) & Plus Jakarta Sans (Body Text)

---

## 🚀 Cara Menjalankan Proyek

1. **Clone repository ini**:
   ```bash
   git clone <URL_REPOSITORY_ANDA>
   cd CleanUp
   ```
2. **Buka di Android Studio**:
   - Pilih **Open** dan arahkan ke folder proyek ini.
   - Tunggu proses Gradle Sync selesai secara otomatis.
3. **Jalankan Aplikasi**:
   - Hubungkan perangkat fisik Android (USB Debugging) atau gunakan Android Emulator.
   - Tekan tombol **Run (Shift + F10)**.
