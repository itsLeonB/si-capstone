package com.example.posyandu.utils.file

import com.google.gson.annotations.SerializedName

data class FileUploadResponse(

    @field:SerializedName("code")
    val code: Int,

    @field:SerializedName("data")
    val data: FileData,

    @field:SerializedName("status")
    val status: String
)

data class FileData(

    @field:SerializedName("path")
    val path: String,

    @field:SerializedName("url")
    val url: String
)
