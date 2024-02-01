package com.example.posyandu.features.main

import android.os.Build
import androidx.annotation.RequiresApi
import com.google.gson.annotations.SerializedName
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter
import java.util.Locale

data class MainBidanResponse(

    @field:SerializedName("code")
    val code: Int,

    @field:SerializedName("data")
    val data: MainBidanData,

    @field:SerializedName("status")
    val status: String
)

data class PemeriksaanItem(

    @field:SerializedName("kondisi_umum")
    val kondisiUmum: String,

    @field:SerializedName("pemberian_fe")
    val pemberianFe: Boolean,

    @field:SerializedName("keterangan")
    val keterangan: String,

    @field:SerializedName("tekanan_darah")
    val tekananDarah: Int,

    @field:SerializedName("kadar_hemoglobin")
    val kadarHemoglobin: Int,

    @field:SerializedName("waktu_pengukuran")
    val waktuPengukuran: String,

    @field:SerializedName("remaja")
    val remaja: Remaja,

    @field:SerializedName("lingkar_lengan")
    val lingkarLengan: Int,

    @field:SerializedName("tingkat_glukosa")
    val tingkatGlukosa: Int,

    @field:SerializedName("berat_badan")
    val beratBadan: Int,

    @field:SerializedName("tinggi_badan")
    val tinggiBadan: Int,

    @field:SerializedName("id")
    val id: Int
)

data class MainBidanData(

    @field:SerializedName("jadwal_penyuluhan")
    val jadwalPenyuluhan: List<JadwalPenyuluhanItem>,

    @field:SerializedName("jadwal_posyandu")
    val jadwalPosyandu: List<JadwalPosyanduItem>,

    @field:SerializedName("posyandu")
    val posyandu: Posyandu,

    @field:SerializedName("bidan")
    val bidan: Bidan,

    @field:SerializedName("pemeriksaan")
    val pemeriksaan: List<PemeriksaanItem>
) {
    fun sortedPosyandu(): List<JadwalPosyanduItem> {
        val dateFormat: SimpleDateFormat =
            SimpleDateFormat("yyyy-MM-dd HH:mm:ss", Locale.getDefault())

        val sortedPosyandu: List<JadwalPosyanduItem> = jadwalPosyandu.sortedByDescending {
            dateFormat.parse(it.waktuMulai)
        }

        return sortedPosyandu
    }
}

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
) {
    @RequiresApi(Build.VERSION_CODES.O)
    fun usia(): Int {
        val birthDate = LocalDate.parse(tanggalLahir, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        val today = LocalDate.now()
        val period = Period.between(birthDate, today)
        return period.years
    }
}

data class JadwalPosyanduItem(

    @field:SerializedName("waktu_selesai")
    val waktuSelesai: String,

    @field:SerializedName("posyandu")
    val posyandu: Posyandu,

    @field:SerializedName("id")
    val id: Int,

    @field:SerializedName("waktu_mulai")
    val waktuMulai: String
)

data class Bidan(

    @field:SerializedName("jabatan")
    val jabatan: String,

    @field:SerializedName("id")
    val id: Int,

    @field:SerializedName("user")
    val user: User
)

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

data class JadwalPenyuluhanItem(

    @field:SerializedName("waktu_selesai")
    val waktuSelesai: String,

    @field:SerializedName("feedback")
    val feedback: String,

    @field:SerializedName("materi")
    val materi: String,

    @field:SerializedName("posyandu")
    val posyandu: Posyandu,

    @field:SerializedName("id")
    val id: Int,

    @field:SerializedName("title")
    val title: String,

    @field:SerializedName("waktu_mulai")
    val waktuMulai: String
)