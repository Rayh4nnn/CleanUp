package com.eco.cleanup.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.eco.cleanup.data.local.entity.LevelEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface LevelDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(list: List<LevelEntity>)

    @Update
    suspend fun update(level: LevelEntity)

    @Query("SELECT * FROM tb_level ORDER BY id_level ASC")
    fun getAllLevel(): Flow<List<LevelEntity>>

    @Query("SELECT * FROM tb_level WHERE id_level = :id")
    suspend fun getLevelById(id: Int): LevelEntity?

    @Query("UPDATE tb_level SET status_terbuka = 1 WHERE id_level = :id")
    suspend fun unlockLevel(id: Int)

    @Query("""
        UPDATE tb_level 
        SET bintang = :bintang, skor_tertinggi = :skor 
        WHERE id_level = :id AND (skor_tertinggi < :skor OR bintang < :bintang)
    """)
    suspend fun updateHasil(id: Int, bintang: Int, skor: Int)

    @Query("SELECT COUNT(*) FROM tb_level")
    suspend fun count(): Int
}

