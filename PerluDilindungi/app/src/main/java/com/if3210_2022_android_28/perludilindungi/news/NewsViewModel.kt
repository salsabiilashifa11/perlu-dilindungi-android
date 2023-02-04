package com.if3210_2022_android_28.perludilindungi.news

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.if3210_2022_android_28.perludilindungi.model.NewsResponse
import com.if3210_2022_android_28.perludilindungi.network.RetrofitClient
import kotlinx.coroutines.launch

enum class NewsApiStatus { LOADING, ERROR, DONE }

class NewsViewModel : ViewModel() {

    // Contains detailed return status for debug
    private val _response = MutableLiveData<String>()
    val response: LiveData<String>
        get() = this._response

    // Contains return status (loading, done, error)
    private val _status = MutableLiveData<NewsApiStatus>()
    val status: LiveData<NewsApiStatus>
        get() = this._status

    // Contains every single news object
    private val _newsList = MutableLiveData<List<NewsResponse.News>>()
    val newsList: LiveData<List<NewsResponse.News>>
        get() = this._newsList

    // Contains a news object that has been selected by the user
    private val _selectedNews = MutableLiveData<NewsResponse.News>()
    val selectedNews: LiveData<NewsResponse.News>
        get() = this._selectedNews

    init {
        this.getNewsInstance()
    }

    private fun getNewsInstance() {
        viewModelScope.launch {
            _status.value = NewsApiStatus.LOADING
            try {
                _newsList.value = RetrofitClient.newsInstance.getNews().results
                _status.value = NewsApiStatus.DONE
                _response.value = "Success"
            } catch (e: Exception) {
                _status.value = NewsApiStatus.ERROR
                _response.value = "Error: ${e.message}"
                _newsList.value = ArrayList()
            }
        }
    }

    fun navigateToSelectedNews(news: NewsResponse.News) {
        this._selectedNews.value = news
    }

    fun navigateToSelectedNewsComplete() {
        this._selectedNews.value = null
    }
}