package com.example.posyandu.features.main.konsultasi

import com.google.gson.annotations.SerializedName

data class GetbyRoomResponse(

	@field:SerializedName("code")
	val code: Int,

	@field:SerializedName("data")
	val data: List<RoomDataItem>,

	@field:SerializedName("status")
	val status: String
)

data class RoomDataItem(

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
