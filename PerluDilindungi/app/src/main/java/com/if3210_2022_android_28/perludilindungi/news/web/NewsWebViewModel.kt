package com.if3210_2022_android_28.perludilindungi.news.web

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.if3210_2022_android_28.perludilindungi.model.NewsResponse

class NewsWebViewModel(news: NewsResponse.News, app: Application) : AndroidViewModel(app) {
    private val _news = MutableLiveData<NewsResponse.News>()
    val news: LiveData<NewsResponse.News>
        get() = this._news

    init {
        this._news.value = news
    }
}