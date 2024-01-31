package com.example.posyandu.features.jadwalPosyandu

import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat
import java.util.Locale

data class JadwalPosyanduIndexResponse(

    @field:SerializedName("code")
    val code: Int,

    @field:SerializedName("data")
    val data: List<JadwalPosyandu>,

    @field:SerializedName("status")
    val status: String
) {
    fun sortedData(): List<JadwalPosyandu> {
        val dateFormat: SimpleDateFormat =
            SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

        val sortedPosyandu: List<JadwalPosyandu> = data.sortedByDescending {
            dateFormat.parse(it.waktuMulai)
        }

        return sortedPosyandu
    }
}

data class Posyandu(

    @field:SerializedName("nama")
    val nama: String,

    @field:SerializedName("foto")
    val foto: String,

    @field:SerializedName("id")
    val id: Int,

    @field:SerializedName("alamat")
    val alamat: String
)

data class JadwalPosyandu(

    @field:SerializedName("id")
    val id: Int,

    @field:SerializedName("waktu_mulai")
    val waktuMulai: String,

    @field:SerializedName("waktu_selesai")
    val waktuSelesai: String,

    @field:SerializedName("posyandu")
    val posyandu: Posyandu,
)
