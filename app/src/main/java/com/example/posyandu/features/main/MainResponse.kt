package com.example.posyandu.features.main

import android.os.Build
import androidx.annotation.RequiresApi
import com.google.gson.annotations.SerializedName
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.Locale

data class MainResponse(

    @field:SerializedName("code")
    val code: Int,

    @field:SerializedName("data")
    val data: MainData,

    @field:SerializedName("status")
    val status: String
)

data class RemajaData(

    @field:SerializedName("is_kader")
    val isKader: Boolean,

    @field:SerializedName("nama_ibu")
    val namaIbu: String,

    @field:SerializedName("posyandu")
    val posyandu: Posyandu,

    @field:SerializedName("nama_ayah")
    val namaAyah: String,

    @field:SerializedName("id")
    val id: Int,

    @field:SerializedName("user")
    val user: User
)

data class MainData(

    @field:SerializedName("jadwal_penyuluhan")
    val jadwalPenyuluhan: List<JadwalPenyuluhanItem>,

    @field:SerializedName("remaja")
    val remaja: RemajaData,

    @field:SerializedName("jadwal_posyandu")
    val jadwalPosyandu: List<JadwalPosyanduItem>
) {
    @RequiresApi(Build.VERSION_CODES.O)
    fun sortedPosyandu(): List<JadwalPosyanduItem> {
        val dateTimeFormatter =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

        val currentDateTime = LocalDateTime.now()

        val sorted: List<JadwalPosyanduItem> = jadwalPosyandu
            .filter { LocalDateTime.parse(it.waktuMulai, dateTimeFormatter) >= currentDateTime }
            .sortedBy { LocalDateTime.parse(it.waktuMulai, dateTimeFormatter) }

        return sorted
    }

    @RequiresApi(Build.VERSION_CODES.O)
    fun sortedPenyuluhan(): List<JadwalPenyuluhanItem> {
        val dateTimeFormatter =
            DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

        val currentDateTime = LocalDateTime.now()

        val sorted: List<JadwalPenyuluhanItem> = jadwalPenyuluhan
            .filter { LocalDateTime.parse(it.waktuMulai, dateTimeFormatter) >= currentDateTime }
            .sortedBy { LocalDateTime.parse(it.waktuMulai, dateTimeFormatter) }

        return sorted
    }
}
