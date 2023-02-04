package com.if3210_2022_android_28.perludilindungi.room

data class FaskesRoom(
    val id: Int,
    val kode: String,
    val nama: String,
    val kota: String,
    val provinsi: String,
    val alamat: String,
    val latitude: Double,
    val longitude: Double,
    val telp: String,
    val jenis_faskes: String,
    val kelas_rs: String,
    val status: String
)

