package com.example.posyandu.features.pemeriksaan

import com.google.gson.annotations.SerializedName

data class IndexPemeriksaanByRemajaResponse(

    @field:SerializedName("code")
    val code: Int,

    @field:SerializedName("data")
    val data: List<Pemeriksaan>,

    @field:SerializedName("status")
    val status: String
)