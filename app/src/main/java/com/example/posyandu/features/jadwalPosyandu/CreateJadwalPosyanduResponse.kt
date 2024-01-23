package com.example.posyandu.features.jadwalPosyandu

import com.google.gson.annotations.SerializedName

data class CreateJadwalPosyanduResponse(

	@field:SerializedName("posyandu_id")
	val posyanduId: Int,

	@field:SerializedName("waktu_mulai")
	val waktuMulai: String,

	@field:SerializedName("waktu_selesai")
	val waktuSelesai: String,
)
