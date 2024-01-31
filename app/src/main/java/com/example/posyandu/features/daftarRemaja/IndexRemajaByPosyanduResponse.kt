package com.example.posyandu.features.daftarRemaja

import android.os.Build
import android.os.Parcel
import android.os.Parcelable
import androidx.annotation.RequiresApi
import com.google.gson.annotations.SerializedName
import java.time.LocalDate
import java.time.Period
import java.time.format.DateTimeFormatter

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
) {
    @RequiresApi(Build.VERSION_CODES.O)
    fun usia(): Int {
        val birthDate = LocalDate.parse(tanggalLahir, DateTimeFormatter.ofPattern("yyyy-MM-dd"))
        val today = LocalDate.now()
        val period = Period.between(birthDate, today)
        return period.years
    }
}

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

data class RemajaIdNama(
    val id: Int,
    val nama: String
) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString() ?: ""
    )

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(nama)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<RemajaIdNama> {
        override fun createFromParcel(parcel: Parcel): RemajaIdNama {
            return RemajaIdNama(parcel)
        }

        override fun newArray(size: Int): Array<RemajaIdNama?> {
            return arrayOfNulls(size)
        }
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
    val user: User
) {
    fun dataAvailable(): Boolean {
        return pemeriksaan.id != 0
    }
}
