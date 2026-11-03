package com.eco.cleanup.data.repository

import com.eco.cleanup.data.local.CleanUpDatabase
import com.eco.cleanup.data.local.entity.LevelEntity
import com.eco.cleanup.data.local.entity.SampahEntity
import kotlinx.coroutines.flow.Flow

class GameRepository(private val db: CleanUpDatabase) {

    // ── Sampah ────────────────────────────────────────────────────
    fun getAllSampah(): Flow<List<SampahEntity>> =
        db.sampahDao().getAllSampah()

    suspend fun getRandomSampah(limit: Int = 5): List<SampahEntity> =
        db.sampahDao().getRandomSampah(limit)

    /** Ambil item random campuran semua kategori untuk Level 1 */
    suspend fun getSampahForLevel(levelId: Int): List<SampahEntity> {
        return when (levelId) {
            1 -> db.sampahDao().getRandomSampahByKategori(listOf(1, 2, 3), limit = 5)
            2 -> db.sampahDao().getRandomSampahByKategori(listOf(1, 2, 3), limit = 7)
            3 -> db.sampahDao().getRandomSampahByKategori(listOf(1, 2, 3), limit = 10)
            else -> db.sampahDao().getRandomSampah(5)
        }
    }

    // ── Level ─────────────────────────────────────────────────────
    fun getAllLevel(): Flow<List<LevelEntity>> =
        db.levelDao().getAllLevel()

    suspend fun getLevelById(id: Int): LevelEntity? =
        db.levelDao().getLevelById(id)

    suspend fun saveHasil(levelId: Int, bintang: Int, skor: Int) {
        db.levelDao().updateHasil(levelId, bintang, skor)
        // Buka level berikutnya jika menang (bintang > 0)
        if (bintang > 0) {
            db.levelDao().unlockLevel(levelId + 1)
        }
    }
}

