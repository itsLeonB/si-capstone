package com.example.posyandu.features.penyuluhan

import com.google.gson.annotations.SerializedName

data class UpdateJadwalPenyuluhanResponse(

	@field:SerializedName("code")
	val code: Int,

	@field:SerializedName("data")
	val data: JadwalPenyuluhan,

	@field:SerializedName("status")
	val status: String
)

