package com.example.posyandu.features.daftarRemaja

import com.example.posyandu.features.jadwalPosyandu.Posyandu
import com.google.gson.annotations.SerializedName

data class IndexRemajaByPosyanduResponse(

    @field:SerializedName("code")
    val code: Int,

    @field:SerializedName("data")
    val data: List<RemajaDataItem>,

    @field:SerializedName("status")
    val status: String
)

data class User(

    @field:SerializedName("nik")
    val nik: Long,

    @field:SerializedName("role")
    val role: String,

    @field:SerializedName("nama")
    val nama: String,

    @field:SerializedName("foto")
    val foto: String,

    @field:SerializedName("id")
    val id: Int,

    @field:SerializedName("tanggal_lahir")
    val tanggalLahir: String
)

data class Remaja(

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
        return beratBadan < 35 && tinggiBadan < 145 && sistole < 100 && diastole < 60 && lingkarLengan < 10 && tingkatGlukosa < 10 && kadarHemoglobin < 30
    }
}

data class RemajaDataItem(

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

    @field:SerializedName("pemeriksaan")
    val pemeriksaan: Pemeriksaan,

    @field:SerializedName("user")
    val user: User,

    val berisiko: Boolean = pemeriksaan.berisiko()
)
