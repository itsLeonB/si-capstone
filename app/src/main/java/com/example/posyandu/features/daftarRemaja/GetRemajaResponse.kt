package com.example.posyandu.features.daftarRemaja

import com.google.gson.annotations.SerializedName

data class GetRemajaResponse(

	@field:SerializedName("code")
	val code: Int,

	@field:SerializedName("data")
	val data: Remaja,

	@field:SerializedName("status")
	val status: String
)
