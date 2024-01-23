package com.example.posyandu.features.daftarRemaja

import com.google.gson.annotations.SerializedName

data class CreateRemajaResponse(

    @field:SerializedName("code")
    val code: Int,

    @field:SerializedName("data")
    val data: Data,

    @field:SerializedName("status")
    val status: String
)

data class Data(

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