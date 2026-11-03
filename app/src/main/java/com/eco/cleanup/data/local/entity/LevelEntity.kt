package com.eco.cleanup.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tb_level")
data class LevelEntity(
    @PrimaryKey
    @ColumnInfo(name = "id_level")
    val idLevel: Int,

    @ColumnInfo(name = "nama_level")
    val namaLevel: String,

    @ColumnInfo(name = "status_terbuka")
    val statusTerbuka: Boolean,

    @ColumnInfo(name = "bintang")
    val bintang: Int = 0,          // 0–3

    @ColumnInfo(name = "skor_tertinggi")
    val skorTertinggi: Int = 0
)

