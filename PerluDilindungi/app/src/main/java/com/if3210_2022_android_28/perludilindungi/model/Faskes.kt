package com.if3210_2022_android_28.perludilindungi.model

import android.os.Parcel
import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
data class Faskes(
    @PrimaryKey
    val id: Int,
    val kode: String?,
    val nama: String?,
    val kota: String?,
    val provinsi: String?,
    val alamat: String?,
    val latitude: Double,
    val longitude: Double,
    val telp: String?,
    val jenis_faskes: String?,
    val kelas_rs: String?,
    val status: String?,
    val bookmarked: Int

) : Parcelable {
    constructor(parcel: Parcel) : this(
        parcel.readInt(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readDouble(),
        parcel.readDouble(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readString(),
        parcel.readInt()
    ) {
    }

    override fun writeToParcel(parcel: Parcel, flags: Int) {
        parcel.writeInt(id)
        parcel.writeString(kode)
        parcel.writeString(nama)
        parcel.writeString(kota)
        parcel.writeString(provinsi)
        parcel.writeString(alamat)
        parcel.writeDouble(latitude)
        parcel.writeDouble(longitude)
        parcel.writeString(telp)
        parcel.writeString(jenis_faskes)
        parcel.writeString(kelas_rs)
        parcel.writeString(status)
        parcel.writeInt(bookmarked)
    }

    override fun describeContents(): Int {
        return 0
    }

    companion object CREATOR : Parcelable.Creator<Faskes> {
        override fun createFromParcel(parcel: Parcel): Faskes {
            return Faskes(parcel)
        }

        override fun newArray(size: Int): Array<Faskes?> {
            return arrayOfNulls(size)
        }
    }
}