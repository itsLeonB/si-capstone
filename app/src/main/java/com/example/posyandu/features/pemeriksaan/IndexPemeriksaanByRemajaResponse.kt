package com.example.posyandu.features.pemeriksaan

import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat
import java.util.Locale

data class IndexPemeriksaanByRemajaResponse(

    @field:SerializedName("code")
    val code: Int,

    @field:SerializedName("data")
    val data: List<Pemeriksaan>,

    @field:SerializedName("status")
    val status: String
) {
    fun sortedPemeriksaan(): List<Pemeriksaan> {
        val dateFormat: SimpleDateFormat =
            SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

        val sortedPemeriksaan: List<Pemeriksaan> = data.sortedByDescending {
            dateFormat.parse(it.waktuPengukuran)
        }

        return sortedPemeriksaan
    }
}