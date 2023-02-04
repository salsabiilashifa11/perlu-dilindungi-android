package com.if3210_2022_android_28.perludilindungi.faskes

import android.annotation.SuppressLint
import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders
import com.if3210_2022_android_28.perludilindungi.R
import com.if3210_2022_android_28.perludilindungi.databinding.ActivityDetailBinding
import com.if3210_2022_android_28.perludilindungi.model.Faskes
import com.if3210_2022_android_28.perludilindungi.room.FaskesDB
import kotlinx.coroutines.*

class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private lateinit var viewModel: DaftarFaskesViewModel
    val db by lazy { FaskesDB(this) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_detail)
        viewModel = ViewModelProviders.of(this).get(DaftarFaskesViewModel::class.java)

        val faskes = intent.getParcelableExtra<Faskes>("FASKES")
        val id = faskes!!.id
        val nama = faskes.nama
        val kode = faskes.kode
        val jenis = faskes.jenis_faskes
        val alamat = faskes.alamat
        val telp = faskes.telp
        val status = faskes.status
        val lat = faskes.latitude
        val long = faskes.longitude
        var bookmarked = faskes.bookmarked

        binding.googlemapsButton.setOnClickListener {
            openMap(lat, long)
        }

        GlobalScope.launch(Dispatchers.Main) {
            bookmarked = db.faskesDao().getFaskes(faskes.id).count()
            setupBookmarkButton(bookmarked)
        }

        binding.addbookmarkButton.setOnClickListener {
            addBookmark(faskes)
            if (bookmarked != 1) {
                bookmarked = 1
            } else {
                bookmarked = 0
            }
            setupBookmarkButton(bookmarked)
        }

        binding.apply {
            namaFaskesDetail.text = nama
            kodeFaskesDetail.text = kode
            jenisFaskesDetail.text = jenis
            alamatFaskesDetail.text = alamat
            telpDetail.text = telp
        }
        if (status != null) {
            setStatusView(status)
        }
    }

    fun setStatusView(status: String) {
        if (status == "Siap Vaksinasi") {
            binding.kesiapanImageView.setImageResource(R.drawable.siap)
            binding.kesiapanDetail.text = resources.getString(R.string.siapvaksinasi)
        } else {
            binding.kesiapanImageView.setImageResource(R.drawable.tidaksiap)
            binding.kesiapanDetail.text = resources.getString(R.string.tidaksiapvaksinasi)
        }
    }

    @SuppressLint("QueryPermissionsNeeded")
    fun openMap(lat: Double, long: Double) {
        val uriString = "geo:$lat,$long"
        val gmmIntentUri = Uri.parse(uriString)
        val mapIntent = Intent(Intent.ACTION_VIEW, gmmIntentUri)
        mapIntent.setPackage("com.google.android.apps.maps");
        mapIntent.resolveActivity(packageManager)?.let {
            startActivity(mapIntent)
        }
    }

    fun setupBookmarkButton(bookmarked: Int) {
        if (bookmarked != 0) {
            binding.addbookmarkButton.text = resources.getString(R.string.unbookmark)
        } else {
            binding.addbookmarkButton.text = resources.getString(R.string.bookmark)
        }
    }

    fun addBookmark(faskes: Faskes) {
        //Periksa: sudah dibookmark (sudah ada di database)
        println(faskes.id)

        CoroutineScope(Dispatchers.IO).launch {
            if (db.faskesDao().getFaskes(faskes.id).count()!=0) {
                db.faskesDao().deleteFaskes(faskes)
                db.faskesDao().setBookmark(faskes.id, 0)
                println("Ketemu")
            } else { //Belom ada di bookmark
                db.faskesDao().addFaskes(faskes)
                db.faskesDao().setBookmark(faskes.id, 1)
                println("Nggak ketemu")
            }
            println(db.faskesDao().getFaskes())
        }
    }
}