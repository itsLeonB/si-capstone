package com.example.posyandu.features.main.konsultasi

import com.google.gson.annotations.SerializedName

data class GetbyRoomRequest(

	@field:SerializedName("room_id")
	val roomId: String,

	@field:SerializedName("receiver_id")
	val receiverId: Int,

	@field:SerializedName("message")
	val message: String,

	@field:SerializedName("sender_id")
	val senderId: Int
)
