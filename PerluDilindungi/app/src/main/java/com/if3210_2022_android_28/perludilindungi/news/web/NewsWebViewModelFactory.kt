package com.if3210_2022_android_28.perludilindungi.news.web

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.if3210_2022_android_28.perludilindungi.model.NewsResponse

class NewsWebViewModelFactory(
    private val news: NewsResponse.News,
    private val application: Application
) : ViewModelProvider.Factory {
    @Suppress("unchecked_cast")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(NewsWebViewModel::class.java)) {
            return NewsWebViewModel(this.news, this.application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}