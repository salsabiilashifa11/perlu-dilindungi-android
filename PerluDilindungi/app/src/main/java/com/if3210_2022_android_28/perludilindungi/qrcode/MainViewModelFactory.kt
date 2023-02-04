package com.if3210_2022_android_28.perludilindungi.qrcode

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.if3210_2022_android_28.perludilindungi.qrcode.repository.Repository

class MainViewModelFactory(private val repository: Repository): ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return MainViewModel(repository) as T
    }
}