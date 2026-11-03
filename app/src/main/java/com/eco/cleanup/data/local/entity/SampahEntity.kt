package com.eco.cleanup.data.local.entity

import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.ForeignKey
import androidx.room.Index
import androidx.room.PrimaryKey

@Entity(
    tableName = "tb_sampah",
    foreignKeys = [
        ForeignKey(
            entity = KategoriEntity::class,
            parentColumns = ["id_kategori"],
            childColumns  = ["id_kategori"],
            onDelete      = ForeignKey.CASCADE
        )
    ],
    indices = [Index(value = ["id_kategori"])]
)
data class SampahEntity(
    @PrimaryKey(autoGenerate = true)
    @ColumnInfo(name = "id_sampah")
    val idSampah: Int = 0,

    @ColumnInfo(name = "id_kategori")
    val idKategori: Int,

    @ColumnInfo(name = "nama_sampah")
    val namaSampah: String,

    @ColumnInfo(name = "aset_gambar")
    val asetGambar: String,       // path: "item/apel.svg"

    @ColumnInfo(name = "teks_edukasi")
    val teksEdukasi: String,

    @ColumnInfo(name = "teks_tips")
    val teksTips: String
)

