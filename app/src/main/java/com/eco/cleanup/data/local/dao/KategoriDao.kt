package com.eco.cleanup.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.eco.cleanup.data.local.entity.KategoriEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface KategoriDao {

    @Insert(onConflict = OnConflictStrategy.IGNORE)
    suspend fun insertAll(list: List<KategoriEntity>)

    @Query("SELECT * FROM tb_kategori")
    fun getAllKategori(): Flow<List<KategoriEntity>>

    @Query("SELECT * FROM tb_kategori WHERE id_kategori = :id")
    suspend fun getKategoriById(id: Int): KategoriEntity?

    @Query("SELECT COUNT(*) FROM tb_kategori")
    suspend fun count(): Int
}

