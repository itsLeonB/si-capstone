package com.example.posyandu.features.main.konsultasi

import com.google.gson.annotations.SerializedName

data class CreateKonsultasiRoomResponse(

	@field:SerializedName("code")
	val code: Int,

	@field:SerializedName("data")
	val data: Data,

	@field:SerializedName("status")
	val status: String
)

data class Data(

	@field:SerializedName("konsultan")
	val konsultan: Konsultan,

	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("pasien")
	val pasien: Pasien
)

data class Konsultan(

	@field:SerializedName("nik")
	val nik: Long,

	@field:SerializedName("role")
	val role: String,

	@field:SerializedName("nama")
	val nama: String,

	@field:SerializedName("foto")
	val foto: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("tanggal_lahir")
	val tanggalLahir: String
)

data class Pasien(

	@field:SerializedName("nik")
	val nik: Long,

	@field:SerializedName("role")
	val role: String,

	@field:SerializedName("nama")
	val nama: String,

	@field:SerializedName("foto")
	val foto: String,

	@field:SerializedName("id")
	val id: Int,

	@field:SerializedName("tanggal_lahir")
	val tanggalLahir: String
)
