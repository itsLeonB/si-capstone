package com.example.posyandu.features.penyuluhan

import com.google.gson.annotations.SerializedName

data class CreateJadwalPenyuluhanRequest(

	@field:SerializedName("waktu_selesai")
	val waktuSelesai: String,

	@field:SerializedName("feedback")
	val feedback: String,

	@field:SerializedName("materi")
	val materi: String,

	@field:SerializedName("posyandu_id")
	val posyanduId: Int,

	@field:SerializedName("title")
	val title: String,

	@field:SerializedName("waktu_mulai")
	val waktuMulai: String
)
