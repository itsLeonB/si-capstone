package com.example.posyandu.features.pemeriksaan

import com.google.gson.annotations.SerializedName

data class PemeriksaanUpdateRequest(

	@field:SerializedName("pemberian_fe")
	val pemberianFe: Boolean,

	@field:SerializedName("keterangan")
	val keterangan: String,

	@field:SerializedName("tingkat_glukosa")
	val tingkatGlukosa: Int,

	@field:SerializedName("berat_badan")
	val beratBadan: Int,

	@field:SerializedName("posyandu_id")
	val posyanduId: Int,

	@field:SerializedName("kondisi_umum")
	val kondisiUmum: String,

	@field:SerializedName("kadar_hemoglobin")
	val kadarHemoglobin: Int,

	@field:SerializedName("waktu_pengukuran")
	val waktuPengukuran: String,

	@field:SerializedName("lingkar_lengan")
	val lingkarLengan: Int,

	@field:SerializedName("sistole")
	val sistole: Int,

	@field:SerializedName("tinggi_badan")
	val tinggiBadan: Int,

	@field:SerializedName("remaja_id")
	val remajaId: Int,

	@field:SerializedName("diastole")
	val diastole: Int
)
