package com.example.posyandu.features.register

import com.google.gson.annotations.SerializedName

data class RegisterUserRequest(

	@field:SerializedName("provinsi")
	val provinsi: String,

	@field:SerializedName("rt")
	val rt: Int,

	@field:SerializedName("kota")
	val kota: String,

	@field:SerializedName("role")
	val role: String,

	@field:SerializedName("rw")
	val rw: Int,

	@field:SerializedName("kode_pos")
	val kodePos: Int,

	@field:SerializedName("telepon")
	val telepon: String,

	@field:SerializedName("kelurahan")
	val kelurahan: String,

	@field:SerializedName("alamat")
	val alamat: String,

	@field:SerializedName("nik")
	val nik: Long,

	@field:SerializedName("password")
	val password: String,

	@field:SerializedName("nama")
	val nama: String,

	@field:SerializedName("tempat_lahir")
	val tempatLahir: String,

	@field:SerializedName("foto")
	val foto: String,

	@field:SerializedName("kecamatan")
	val kecamatan: String,

	@field:SerializedName("email")
	val email: String,

	@field:SerializedName("tanggal_lahir")
	val tanggalLahir: String,

	@field:SerializedName("username")
	val username: String
)