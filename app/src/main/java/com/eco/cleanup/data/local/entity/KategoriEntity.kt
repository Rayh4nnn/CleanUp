package com.eco.cleanup.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tb_kategori")
data class KategoriEntity(
    @PrimaryKey
    @ColumnInfo(name = "id_kategori")
    val idKategori: Int,

    @ColumnInfo(name = "nama_kategori")
    val namaKategori: String,

    @ColumnInfo(name = "warna_tong")
    val warnaTong: String
)

