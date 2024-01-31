package com.example.posyandu.features.pemeriksaan

import com.google.gson.annotations.SerializedName

data class PemeriksaanUpdateResponse(

	@field:SerializedName("code")
	val code: Int,

	@field:SerializedName("data")
	val data: Data,

	@field:SerializedName("status")
	val status: String
)