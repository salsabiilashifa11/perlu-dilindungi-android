package com.if3210_2022_android_28.perludilindungi.faskes

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.if3210_2022_android_28.perludilindungi.model.Faskes
import com.if3210_2022_android_28.perludilindungi.model.FaskesResponse
import com.if3210_2022_android_28.perludilindungi.model.KotaResponse
import com.if3210_2022_android_28.perludilindungi.model.ProvinsiResponse
import com.if3210_2022_android_28.perludilindungi.network.RetrofitClient
import com.if3210_2022_android_28.perludilindungi.room.FaskesDB
import kotlinx.coroutines.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DaftarFaskesViewModel : ViewModel() {

    lateinit var kotaData: MutableLiveData<List<String>>
    lateinit var provinsiData: MutableLiveData<List<String>>
    lateinit var faskesData: MutableLiveData<List<Faskes>>
    lateinit var faskesArray: List<Faskes>


    init {
        kotaData = MutableLiveData()
        provinsiData = MutableLiveData()
        faskesData = MutableLiveData()
    }

    fun getKotaDataObserver(): MutableLiveData<List<String>> {
        return kotaData
    }

    fun getProvinsiDataObserver(): MutableLiveData<List<String>> {
        return provinsiData
    }

    fun getFaskesDataObserver(): MutableLiveData<List<Faskes>> {
        return faskesData
    }

    fun makeProvinsiApiCall() {
        viewModelScope.launch {
            try {
                val provinsiList: List<String> = RetrofitClient.provinsiInstance.getProvinsi().results.map {
                    it.value
                }
                showProv(provinsiList)
            } catch (e: Exception) {
                println(e.message)
            }
        }
    }

    fun makeKotaApiCall(provinsi: String) {
        viewModelScope.launch {
            try {
                val kotaList: List<String> = RetrofitClient.kotaInstance.getKota(provinsi).results.map {
                    it.value
                }
                showKota(kotaList)
            } catch (e: Exception) {
                println(e.message)
            }
        }
    }

    fun makeFaskesApiCall(provinsi: String, kota: String, userLat: Double, userLong: Double) {
        viewModelScope.launch {
            try {
                val faskesList: List<Faskes> = RetrofitClient.faskesInstance.getFaskes(provinsi, kota).data
                showFaskes(sortFaskes(faskesList, userLat, userLong).take(5))
            } catch (e: Exception) {
                println(e.message)
            }
        }
    }

    fun makeDBCall(db: FaskesDB) {
        GlobalScope.launch(Dispatchers.Main) {
            val bookmarkList = db.faskesDao().getFaskes()
                showFaskes(bookmarkList)
        }
    }

    fun showProv(provinsiList: List<String>) {
        provinsiData.postValue(provinsiList)
    }

    fun showKota(kotaList: List<String>) {
        kotaData.postValue(kotaList)
    }

    fun showFaskes(faskesList: List<Faskes>) {
        faskesData.postValue(faskesList)
        faskesArray = faskesList
    }

    fun sortFaskes(faskesList:List<Faskes>, userLat: Double, userLong: Double):List<Faskes>{
        if (faskesList.count() < 2){
            return faskesList
        }
        val pivot = faskesList[faskesList.count()/2]
        val equal = faskesList.filter {
            distanceInKm(it.latitude, it.longitude, userLat, userLong) == distanceInKm(pivot.latitude, pivot.longitude, userLat, userLong)
        }
        val less = faskesList.filter {
            distanceInKm(it.latitude, it.longitude, userLat, userLong) < distanceInKm(pivot.latitude, pivot.longitude, userLat, userLong)
        }
        val greater = faskesList.filter {
            distanceInKm(it.latitude, it.longitude, userLat, userLong) > distanceInKm(pivot.latitude, pivot.longitude, userLat, userLong)
        }

        return sortFaskes(less, userLat, userLong) + equal + sortFaskes(greater, userLat, userLong)
    }

    fun distanceInKm(lat1: Double, lon1: Double, lat2: Double, lon2: Double): Double {
        val theta = lon1 - lon2
        var dist = Math.sin(deg2rad(lat1)) * Math.sin(deg2rad(lat2)) + Math.cos(deg2rad(lat1)) * Math.cos(deg2rad(lat2)) * Math.cos(deg2rad(theta))
        dist = Math.acos(dist)
        dist = rad2deg(dist)
        dist = dist * 60 * 1.1515
        dist = dist * 1.609344
        return dist
    }

    private fun deg2rad(deg: Double): Double {
        return deg * Math.PI / 180.0
    }

    private fun rad2deg(rad: Double): Double {
        return rad * 180.0 / Math.PI
    }

}