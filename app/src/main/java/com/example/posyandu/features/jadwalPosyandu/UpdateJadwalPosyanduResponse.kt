package com.example.posyandu.features.jadwalPosyandu

import com.google.gson.annotations.SerializedName

data class UpdateJadwalPosyanduResponse(

	@field:SerializedName("code")
	val code: Int,

	@field:SerializedName("data")
	val data: JadwalPosyandu,

	@field:SerializedName("status")
	val status: String
)
