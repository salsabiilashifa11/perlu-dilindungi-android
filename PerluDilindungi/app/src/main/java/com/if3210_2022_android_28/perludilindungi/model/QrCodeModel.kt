package com.if3210_2022_android_28.perludilindungi.model

data class QrCode (
    val success: Boolean,
    val code: Int,
    val message: String,
    val data: QrCodeData
)

data class QrCodeData (
    val userStatus: String,
    val reason: String,
)

data class QrCodePost (
    val qrCode: String,
    val latitude: Double,
    val longitude: Double
)
