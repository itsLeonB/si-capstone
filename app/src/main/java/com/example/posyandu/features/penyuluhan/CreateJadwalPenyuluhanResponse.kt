package com.example.posyandu.features.penyuluhan

import com.google.gson.annotations.SerializedName

data class CreateJadwalPenyuluhanResponse(

    @field:SerializedName("code")
    val code: Int,

    @field:SerializedName("data")
    val data: String,

    @field:SerializedName("status")
    val status: String
)

