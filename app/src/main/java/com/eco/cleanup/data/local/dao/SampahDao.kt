package com.eco.cleanup.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.eco.cleanup.data.local.entity.SampahEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface SampahDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(list: List<SampahEntity>)

    @Query("SELECT * FROM tb_sampah")
    fun getAllSampah(): Flow<List<SampahEntity>>

    @Query("SELECT * FROM tb_sampah WHERE id_kategori = :idKategori")
    fun getSampahByKategori(idKategori: Int): Flow<List<SampahEntity>>

    @Query("SELECT * FROM tb_sampah ORDER BY RANDOM() LIMIT :limit")
    suspend fun getRandomSampah(limit: Int): List<SampahEntity>

    @Query("""
        SELECT * FROM tb_sampah 
        WHERE id_kategori IN (:kategoriIds) 
        ORDER BY RANDOM() 
        LIMIT :limit
    """)
    suspend fun getRandomSampahByKategori(kategoriIds: List<Int>, limit: Int): List<SampahEntity>

    @Query("SELECT COUNT(*) FROM tb_sampah")
    suspend fun count(): Int
}

