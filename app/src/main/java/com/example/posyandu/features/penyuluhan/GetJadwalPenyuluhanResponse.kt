package com.example.posyandu.features.penyuluhan

import com.google.gson.annotations.SerializedName

data class GetJadwalPenyuluhanResponse(

	@field:SerializedName("code")
	val code: Int,

	@field:SerializedName("data")
	val data: Data,

	@field:SerializedName("status")
	val status: String
)

data class Data(

	@field:SerializedName("waktu_selesai")
	val waktuSelesai: String,

	@field:SerializedName("feedback")
	val feedback: String,

	@field:SerializedName("materi")
	val materi: String,

	@field:SerializedName("posyandu")
	val posyandu: Posyandu,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("title")
	val title: String,

	@field:SerializedName("waktu_mulai")
	val waktuMulai: String
)
