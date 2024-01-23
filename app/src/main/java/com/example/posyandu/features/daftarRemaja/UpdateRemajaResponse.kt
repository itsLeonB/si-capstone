package com.example.posyandu.features.daftarRemaja

import com.google.gson.annotations.SerializedName

data class UpdateRemajaResponse(

    @field:SerializedName("code")
    val code: Int,

    @field:SerializedName("data")
    val data: UpdateRemajaData,

    @field:SerializedName("status")
    val status: String
)

data class UpdateRemajaData(

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

