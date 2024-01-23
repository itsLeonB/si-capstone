package com.example.posyandu.features.daftarRemaja

import com.google.gson.annotations.SerializedName

data class UpdateRemajaRequest(

    @field:SerializedName("is_kader")
    var isKader: Boolean? = null,

    @field:SerializedName("nama_ibu")
    val namaIbu: String,

    @field:SerializedName("nama_ayah")
    val namaAyah: String,

    @field:SerializedName("posyandu_id")
    val posyanduId: Int
)
