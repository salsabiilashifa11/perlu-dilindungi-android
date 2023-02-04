package com.if3210_2022_android_28.perludilindungi.model

data class FaskesResponse(
    val success: Boolean,
    val message: String,
    val count_total: Int,
    val data: List<Faskes>
)
