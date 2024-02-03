package com.example.posyandu.features.main.konsultasi

import com.google.gson.annotations.SerializedName

data class CreateKonsultasiResponse(

	@field:SerializedName("code")
	val code: Int,

	@field:SerializedName("data")
	val data: ChatData,

	@field:SerializedName("status")
	val status: String
)

data class Sender(

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

data class Room(

	@field:SerializedName("konsultan")
	val konsultan: Konsultan,

	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("pasien")
	val pasien: Pasien
)

data class Receiver(

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

data class ChatData(

	@field:SerializedName("is_read")
	val isRead: Boolean,

	@field:SerializedName("receiver")
	val receiver: Receiver,

	@field:SerializedName("sender")
	val sender: Sender,

	@field:SerializedName("id")
	val id: String,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("room")
	val room: Room,

	@field:SerializedName("timestamp")
	val timestamp: String
)
