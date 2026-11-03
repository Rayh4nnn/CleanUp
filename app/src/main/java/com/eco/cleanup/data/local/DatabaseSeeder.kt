package com.eco.cleanup.data.local

import com.eco.cleanup.data.local.entity.KategoriEntity
import com.eco.cleanup.data.local.entity.LevelEntity
import com.eco.cleanup.data.local.entity.SampahEntity

object DatabaseSeeder {

    suspend fun seedIfEmpty(db: CleanUpDatabase) {
        if (db.kategoriDao().count() == 0) {
            seed(db)
        } else {
            // Pastikan level terisi lengkap (Level 1, 2, 3) jika database sudah terbuat
            seedLevel(db)
        }
    }

    suspend fun seed(db: CleanUpDatabase) {
        seedKategori(db)
        seedSampah(db)
        seedLevel(db)
    }

    private suspend fun seedKategori(db: CleanUpDatabase) {
        db.kategoriDao().insertAll(
            listOf(
                KategoriEntity(1, "Organik",    "Hijau"),
                KategoriEntity(2, "Anorganik",  "Biru"),
                KategoriEntity(3, "B3",         "Merah")
            )
        )
    }

    private suspend fun seedSampah(db: CleanUpDatabase) {
        db.sampahDao().insertAll(
            listOf(
                // ── ORGANIK (id_kategori = 1) ──────────────────────────────────
                SampahEntity(
                    idKategori  = 1,
                    namaSampah  = "Apel",
                    asetGambar  = "item/apel.svg",
                    teksEdukasi = "Apel adalah buah organik yang dapat terurai secara alami di tanah.",
                    teksTips    = "Kulit dan sisa apel bisa dijadikan kompos untuk menyuburkan tanaman!"
                ),
                SampahEntity(
                    idKategori  = 1,
                    namaSampah  = "Pisang",
                    asetGambar  = "item/pisang.svg",
                    teksEdukasi = "Kulit pisang adalah sampah organik yang mudah terurai.",
                    teksTips    = "Kulit pisang bisa dipakai sebagai pupuk alami atau bahan kompos."
                ),
                SampahEntity(
                    idKategori  = 1,
                    namaSampah  = "Semangka",
                    asetGambar  = "item/semangka.svg",
                    teksEdukasi = "Kulit dan biji semangka termasuk sampah organik.",
                    teksTips    = "Sisa semangka bisa dikomposkan untuk menyuburkan kebun."
                ),
                SampahEntity(
                    idKategori  = 1,
                    namaSampah  = "Roti",
                    asetGambar  = "item/roti.svg",
                    teksEdukasi = "Roti basi adalah limbah organik dari sisa makanan sehari-hari.",
                    teksTips    = "Hindari membuang roti sembarangan agar tidak mengundang hama."
                ),
                SampahEntity(
                    idKategori  = 1,
                    namaSampah  = "Teh",
                    asetGambar  = "item/teh.svg",
                    teksEdukasi = "Ampas teh merupakan sampah organik yang mudah diurai.",
                    teksTips    = "Ampas teh bisa dijadikan pupuk untuk tanaman hias."
                ),
                SampahEntity(
                    idKategori  = 1,
                    namaSampah  = "Telur",
                    asetGambar  = "item/telur.svg",
                    teksEdukasi = "Cangkang telur adalah sampah organik kaya kalsium.",
                    teksTips    = "Haluskan cangkang telur lalu tabur di tanah sebagai pupuk alami."
                ),
                SampahEntity(
                    idKategori  = 1,
                    namaSampah  = "Daun",
                    asetGambar  = "item/daun.svg",
                    teksEdukasi = "Daun kering adalah sampah organik yang terurai dengan cepat.",
                    teksTips    = "Daun kering bisa dijadikan mulsa atau kompos untuk tanaman."
                ),
                SampahEntity(
                    idKategori  = 1,
                    namaSampah  = "Sawi",
                    asetGambar  = "item/sawi.svg",
                    teksEdukasi = "Sisa sawi dan sayuran lain termasuk sampah organik.",
                    teksTips    = "Kompos dari sisa sayur sangat baik untuk kesuburan tanah."
                ),
                SampahEntity(
                    idKategori  = 1,
                    namaSampah  = "Limbah Sayur",
                    asetGambar  = "item/limbah_sayur.svg",
                    teksEdukasi = "Sisa potongan sayuran adalah sampah organik dapur.",
                    teksTips    = "Kumpulkan sisa sayur dalam wadah khusus untuk dijadikan kompos."
                ),
                SampahEntity(
                    idKategori  = 1,
                    namaSampah  = "Kotoran Hewan",
                    asetGambar  = "item/kotoran_hewan.svg",
                    teksEdukasi = "Kotoran hewan peliharaan termasuk sampah organik.",
                    teksTips    = "Kotoran hewan bisa diolah menjadi pupuk kandang yang menyuburkan."
                ),

                // ── ANORGANIK (id_kategori = 2) ───────────────────────────────
                SampahEntity(
                    idKategori  = 2,
                    namaSampah  = "Botol Kaca",
                    asetGambar  = "item/botol_kaca.svg",
                    teksEdukasi = "Botol kaca adalah sampah anorganik yang bisa didaur ulang.",
                    teksTips    = "Cuci botol kaca dan serahkan ke bank sampah untuk didaur ulang."
                ),
                SampahEntity(
                    idKategori  = 2,
                    namaSampah  = "Botol Plastik",
                    asetGambar  = "item/botol_plastik.svg",
                    teksEdukasi = "Botol plastik butuh ratusan tahun untuk terurai di alam.",
                    teksTips    = "Pilih botol isi ulang untuk mengurangi sampah plastik."
                ),
                SampahEntity(
                    idKategori  = 2,
                    namaSampah  = "Kaleng",
                    asetGambar  = "item/kaleng.svg",
                    teksEdukasi = "Kaleng aluminium adalah sampah anorganik bernilai daur ulang tinggi.",
                    teksTips    = "Remukkan kaleng sebelum dibuang agar menghemat ruang tempat sampah."
                ),
                SampahEntity(
                    idKategori  = 2,
                    namaSampah  = "Kardus",
                    asetGambar  = "item/kardus.svg",
                    teksEdukasi = "Kardus bekas termasuk sampah anorganik yang mudah didaur ulang.",
                    teksTips    = "Lipat kardus dan jual ke pengepul kertas bekas."
                ),
                SampahEntity(
                    idKategori  = 2,
                    namaSampah  = "Kertas",
                    asetGambar  = "item/kertas.svg",
                    teksEdukasi = "Kertas bekas adalah sampah anorganik yang bisa didaur ulang.",
                    teksTips    = "Gunakan kedua sisi kertas sebelum membuangnya."
                ),
                SampahEntity(
                    idKategori  = 2,
                    namaSampah  = "Kresek",
                    asetGambar  = "item/kresek.svg",
                    teksEdukasi = "Kantong kresek adalah plastik tipis yang sulit terurai.",
                    teksTips    = "Ganti kresek dengan tas belanja kain untuk lingkungan lebih bersih."
                ),
                SampahEntity(
                    idKategori  = 2,
                    namaSampah  = "Styrofoam",
                    asetGambar  = "item/styrofoam.svg",
                    teksEdukasi = "Styrofoam sangat sulit terurai dan berbahaya bagi lingkungan.",
                    teksTips    = "Hindari penggunaan styrofoam; pilih kemasan ramah lingkungan."
                ),
                SampahEntity(
                    idKategori  = 2,
                    namaSampah  = "Soda",
                    asetGambar  = "item/soda.svg",
                    teksEdukasi = "Kaleng minuman soda termasuk sampah anorganik dari logam.",
                    teksTips    = "Kaleng minuman bisa dijual ke pengepul sebagai bahan daur ulang."
                ),
                SampahEntity(
                    idKategori  = 2,
                    namaSampah  = "Kaset",
                    asetGambar  = "item/kaset.svg",
                    teksEdukasi = "Kaset lama adalah sampah anorganik dari plastik dan logam.",
                    teksTips    = "Serahkan kaset lama ke tempat daur ulang elektronik."
                ),
                SampahEntity(
                    idKategori  = 2,
                    namaSampah  = "Odol",
                    asetGambar  = "item/odol.svg",
                    teksEdukasi = "Tube odol kosong termasuk sampah anorganik plastik.",
                    teksTips    = "Potong tube odol habis, bersihkan, lalu daur ulang."
                ),

                // ── B3 / Berbahaya (id_kategori = 3) ──────────────────────────
                SampahEntity(
                    idKategori  = 3,
                    namaSampah  = "Baterai",
                    asetGambar  = "item/batrai.svg",
                    teksEdukasi = "Baterai mengandung merkuri dan logam berat yang berbahaya.",
                    teksTips    = "Kumpulkan baterai bekas dan serahkan ke drop box khusus B3."
                ),
                SampahEntity(
                    idKategori  = 3,
                    namaSampah  = "Handphone",
                    asetGambar  = "item/hp.svg",
                    teksEdukasi = "HP lama mengandung logam berat dan bahan kimia berbahaya.",
                    teksTips    = "Serahkan HP bekas ke program daur ulang elektronik resmi."
                ),
                SampahEntity(
                    idKategori  = 3,
                    namaSampah  = "Lampu",
                    asetGambar  = "item/lampu.svg",
                    teksEdukasi = "Lampu neon/LED mengandung merkuri yang berbahaya jika pecah.",
                    teksTips    = "Jangan memecah lampu bekas; bungkus dan bawa ke tempat B3."
                ),
                SampahEntity(
                    idKategori  = 3,
                    namaSampah  = "Masker",
                    asetGambar  = "item/masker.svg",
                    teksEdukasi = "Masker medis bekas berpotensi mengandung kuman dan virus.",
                    teksTips    = "Masker bekas harus digunting dan dibungkus plastik sebelum dibuang."
                ),
                SampahEntity(
                    idKategori  = 3,
                    namaSampah  = "Obat",
                    asetGambar  = "item/obat.svg",
                    teksEdukasi = "Obat-obatan kedaluwarsa termasuk limbah B3 yang berbahaya.",
                    teksTips    = "Kembalikan obat kedaluwarsa ke apotek, jangan buang ke selokan."
                ),
                SampahEntity(
                    idKategori  = 3,
                    namaSampah  = "Pil",
                    asetGambar  = "item/pil.svg",
                    teksEdukasi = "Pil obat bekas termasuk limbah B3 karena kandungan kimianya.",
                    teksTips    = "Simpan pil bekas dalam wadah tertutup dan serahkan ke apotek."
                ),
                SampahEntity(
                    idKategori  = 3,
                    namaSampah  = "Racun",
                    asetGambar  = "item/racun.svg",
                    teksEdukasi = "Wadah bekas pestisida/racun termasuk limbah B3 sangat berbahaya.",
                    teksTips    = "Jangan isi ulang botol bekas racun; serahkan ke pengelola B3."
                ),
                SampahEntity(
                    idKategori  = 3,
                    namaSampah  = "Jarum Suntik",
                    asetGambar  = "item/suntik.svg",
                    teksEdukasi = "Jarum suntik bekas adalah limbah medis B3 yang sangat berbahaya.",
                    teksTips    = "Jarum suntik harus dibuang di wadah khusus (safety box) di klinik."
                ),
                SampahEntity(
                    idKategori  = 3,
                    namaSampah  = "Termometer",
                    asetGambar  = "item/termometer.svg",
                    teksEdukasi = "Termometer raksa mengandung merkuri yang sangat berbahaya.",
                    teksTips    = "Ganti termometer raksa dengan digital untuk keamanan keluarga."
                ),
                SampahEntity(
                    idKategori  = 3,
                    namaSampah  = "Limbah Berbahaya",
                    asetGambar  = "item/limbah_berbahaya.svg",
                    teksEdukasi = "Limbah B3 industri mengandung zat kimia berbahaya bagi lingkungan.",
                    teksTips    = "Limbah B3 wajib dikelola oleh pihak berwenang, jangan buang sembarangan."
                )
            )
        )
    }

    private suspend fun seedLevel(db: CleanUpDatabase) {
        db.levelDao().insertAll(
            listOf(
                LevelEntity(
                    idLevel       = 1,
                    namaLevel     = "Mudah",
                    statusTerbuka = true,
                    bintang       = 0,
                    skorTertinggi = 0
                ),
                LevelEntity(
                    idLevel       = 2,
                    namaLevel     = "Sedang",
                    statusTerbuka = false,
                    bintang       = 0,
                    skorTertinggi = 0
                ),
                LevelEntity(
                    idLevel       = 3,
                    namaLevel     = "Susah",
                    statusTerbuka = false,
                    bintang       = 0,
                    skorTertinggi = 0
                )
            )
        )
    }
}

