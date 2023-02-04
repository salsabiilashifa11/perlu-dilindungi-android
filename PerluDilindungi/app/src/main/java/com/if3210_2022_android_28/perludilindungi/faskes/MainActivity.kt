package com.if3210_2022_android_28.perludilindungi.faskes

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.if3210_2022_android_28.perludilindungi.NewsActivity
import com.if3210_2022_android_28.perludilindungi.R
import com.if3210_2022_android_28.perludilindungi.databinding.ActivityMainBinding
import com.if3210_2022_android_28.perludilindungi.qrcode.QrCodeScannerActivity

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private lateinit var viewModel: DaftarFaskesViewModel
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var qrCodeBtnView: FloatingActionButton
    var userLat: Double = 0.0
    var userLong: Double = 0.0

    @RequiresApi(Build.VERSION_CODES.N)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = DataBindingUtil.setContentView(this, R.layout.activity_main)
        viewModel = ViewModelProviders.of(this).get(DaftarFaskesViewModel::class.java)
        qrCodeBtnView = findViewById(R.id.btn_qrcode)

        qrCodeBtnView.setOnClickListener {
            startActivity(Intent(this@MainActivity, QrCodeScannerActivity::class.java))
        }

        //Location related services
        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        val locationPermissionRequest = registerForActivityResult(
            ActivityResultContracts.RequestMultiplePermissions()
        ) { permissions ->
            when {
                permissions.getOrDefault(Manifest.permission.ACCESS_FINE_LOCATION, false) -> {
                    // Precise location access granted.
                }
                permissions.getOrDefault(Manifest.permission.ACCESS_COARSE_LOCATION, false) -> {
                    // Only approximate location access granted.
                } else -> {
                // No location access granted.
            }
            }
        }
        locationPermissionRequest.launch(arrayOf(
            Manifest.permission.ACCESS_FINE_LOCATION,
            Manifest.permission.ACCESS_COARSE_LOCATION))

        binding.autocompleteTextView.setOnDismissListener {
            fetchKota(binding.autocompleteTextView.text.toString())
        }

        binding.autocompleteTextView2.setOnDismissListener {
            val provinsi = binding.autocompleteTextView.text.toString()
            val kota = binding.autocompleteTextView2.text.toString()
            viewModel.makeFaskesApiCall(provinsi, kota, userLat, userLong)
        }

        binding.bottomNavigation!!.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        fetchProv()
        getLocation()
    }


    fun fetchProv() {
        viewModel.getProvinsiDataObserver().observe(this, Observer<List<String>>{
            if (it != null) {
                val arrayAdapter = ArrayAdapter(this, R.layout.dropdown_item, it)
                binding.autocompleteTextView.setAdapter(arrayAdapter)
            } else {
                Toast.makeText(this, "Error", Toast.LENGTH_LONG).show()
            }
        })
        viewModel.makeProvinsiApiCall()

    }

    fun fetchKota(provinsi: String) {
        viewModel.getKotaDataObserver().observe(this, Observer<List<String>>{
            if (it != null) {
                val arrayAdapter = ArrayAdapter(this, R.layout.dropdown_item, it)
                binding.autocompleteTextView2.setAdapter(arrayAdapter)
            } else {
                Toast.makeText(this, "Error", Toast.LENGTH_LONG).show()
            }
        })
        viewModel.makeKotaApiCall(provinsi)
    }

    fun getLocation() {
        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.ACCESS_COARSE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            return
        }
        fusedLocationClient.lastLocation.addOnSuccessListener {
            if(it != null) {
                userLat = it.latitude
                userLong = it.longitude
            }
        }
    }

    //Navigation
    private val mOnNavigationItemSelectedListener = BottomNavigationView.OnNavigationItemSelectedListener { menuItem ->
        when (menuItem.itemId) {
            R.id.news -> {
                val intent = Intent(this, NewsActivity::class.java).apply {}
                startActivity(intent)
                overridePendingTransition(0,0)
                finish()
                return@OnNavigationItemSelectedListener true
            }
            R.id.faskes -> {
                return@OnNavigationItemSelectedListener true
            }
            R.id.bookmark -> {
                val intent = Intent(this, BookmarkActivity::class.java).apply {}
                startActivity(intent)
                overridePendingTransition(0,0)
                finish()
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }

}