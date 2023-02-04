package com.if3210_2022_android_28.perludilindungi.qrcode

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.if3210_2022_android_28.perludilindungi.model.QrCode
import com.if3210_2022_android_28.perludilindungi.model.QrCodePost
import com.if3210_2022_android_28.perludilindungi.qrcode.repository.Repository
import kotlinx.coroutines.launch
import retrofit2.Response

class MainViewModel(private  val repository: Repository): ViewModel() {
    val myResponse: MutableLiveData<Response<QrCode>> = MutableLiveData()

    fun getQrCodeStatus(qrCodePost: QrCodePost) {
        viewModelScope.launch {
            val response: Response<QrCode> = repository.getUserQrCode(qrCodePost)
            myResponse.value = response
        }
    }
}