package com.if3210_2022_android_28.perludilindungi.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

data class NewsResponse(
    val success: Boolean,
    val message: String,
    //@Json(name = "count_total") val countTotal: Int,
    val count_total: Int,
    val results: List<News>
) {
    @Parcelize
    data class News(
        val title: String,
        val link: List<String>,
        val guid: String,
        val pubDate: String,
        val description: Description,
        val enclosure: Enclosure
    ) : Parcelable {

        @Parcelize
        data class Description(
            //@Json(name = "__cdata") val cData: String
            val __cdata: String
        ) : Parcelable

        @Parcelize
        data class Enclosure(
            // @Json(name = "_url") val url: String,
            // @Json(name = "_length") val length: String,
            // @Json(name = "_type") val type: String
            val _url: String,
            val _length: String,
            val _type: String
        ) : Parcelable
    }
}
