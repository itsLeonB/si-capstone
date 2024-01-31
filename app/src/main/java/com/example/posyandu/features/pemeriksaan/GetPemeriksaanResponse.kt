package com.example.posyandu.features.pemeriksaan

import com.google.gson.annotations.SerializedName

data class GetPemeriksaanResponse(

    @field:SerializedName("code")
    val code: Int,

    @field:SerializedName("data")
    val data: Pemeriksaan,

    @field:SerializedName("status")
    val status: String
)

data class Pemeriksaan(

    @field:SerializedName("pemberian_fe")
    val pemberianFe: Boolean,

    @field:SerializedName("keterangan")
    val keterangan: String,

    @field:SerializedName("tingkat_glukosa")
    val tingkatGlukosa: Int,

    @field:SerializedName("berat_badan")
    val beratBadan: Int,

    @field:SerializedName("posyandu")
    val posyandu: Posyandu,

    @field:SerializedName("kondisi_umum")
    val kondisiUmum: String,

    @field:SerializedName("kadar_hemoglobin")
    val kadarHemoglobin: Int,

    @field:SerializedName("waktu_pengukuran")
    val waktuPengukuran: String,

    @field:SerializedName("remaja")
    val remaja: Remaja,

    @field:SerializedName("lingkar_lengan")
    val lingkarLengan: Int,

    @field:SerializedName("sistole")
    val sistole: Int,

    @field:SerializedName("tinggi_badan")
    val tinggiBadan: Int,

    @field:SerializedName("id")
    val id: Int,

    @field:SerializedName("diastole")
    val diastole: Int
) {
    fun berisiko(): Boolean {
        return beratBadan < 35 || tinggiBadan < 145 || sistole < 100 || diastole < 60 || lingkarLengan < 10 || tingkatGlukosa < 10 || kadarHemoglobin < 30
    }
}
