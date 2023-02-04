package com.if3210_2022_android_28.perludilindungi.qrcode.repository

import com.if3210_2022_android_28.perludilindungi.network.RetrofitClient
import com.if3210_2022_android_28.perludilindungi.model.QrCode
import com.if3210_2022_android_28.perludilindungi.model.QrCodePost
import retrofit2.Response

class Repository {
    suspend fun getUserQrCode(qrCodePost: QrCodePost): Response<QrCode> {
        return RetrofitClient.qrCodeInstance.getUserStatus(qrCodePost)
    }
}