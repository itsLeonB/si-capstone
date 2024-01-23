package com.example.posyandu.features.main.posyandu

import com.google.gson.annotations.SerializedName

data class UpdatePosyanduResponse(

    @field:SerializedName("code")
    val code: Int,

    @field:SerializedName("data")
    val data: Posyandu,

    @field:SerializedName("status")
    val status: String
)
