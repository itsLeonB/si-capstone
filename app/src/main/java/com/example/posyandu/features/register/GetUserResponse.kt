package com.example.posyandu.features.register

import com.google.gson.annotations.SerializedName

data class GetUserResponse(

    @field:SerializedName("code")
    val code: Int,

    @field:SerializedName("data")
    val data: User,

    @field:SerializedName("status")
    val status: String
)