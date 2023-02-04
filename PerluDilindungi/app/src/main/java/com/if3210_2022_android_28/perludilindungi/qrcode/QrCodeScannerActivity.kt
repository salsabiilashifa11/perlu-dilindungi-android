package com.if3210_2022_android_28.perludilindungi.qrcode

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.lifecycle.ViewModelProvider
import com.budiyev.android.codescanner.*
import com.if3210_2022_android_28.perludilindungi.faskes.MainActivity

import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices

import com.if3210_2022_android_28.perludilindungi.model.QrCodePost
import com.if3210_2022_android_28.perludilindungi.qrcode.repository.Repository
import com.if3210_2022_android_28.perludilindungi.R

class QrCodeScannerActivity : AppCompatActivity(), SensorEventListener {
    private lateinit var codeScanner: CodeScanner
    private lateinit var viewModel: MainViewModel
    private lateinit var fusedLocationClient: FusedLocationProviderClient
    private lateinit var sensorManager: SensorManager
    private lateinit var statusTextView: TextView
    private lateinit var temperatureTextView: TextView
    private lateinit var vaksinTextView: TextView
    private lateinit var statusImgView: ImageView
    private lateinit var backBtnView: Button

    private var temperature: Sensor? = null
    private var latitude: Double = 0.0
    private var longitude: Double = 0.0
    private var qrCodeText = ""

    private val LOCATION_PERMISSION_REQ_CODE = 1000
    private val CAMERA_PERMISSION_REQ_CODE = 101

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.qrcode_scanner)

        fusedLocationClient = LocationServices.getFusedLocationProviderClient(this)
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        temperature = sensorManager.getDefaultSensor(Sensor.TYPE_AMBIENT_TEMPERATURE)

        setupPermissions()
        codeScanner(this)
    }

    private fun codeScanner(context: Context) {
        val scannerView = findViewById<CodeScannerView>(R.id.scanner_view)
        statusTextView = findViewById(R.id.txt_status)
        temperatureTextView = findViewById(R.id.txt_temperature)
        statusImgView = findViewById(R.id.img_status)
        vaksinTextView = findViewById(R.id.txt_vaksin)
        backBtnView = findViewById(R.id.btn_back_qrcode)

        backBtnView.setOnClickListener {
            startActivity(Intent(this@QrCodeScannerActivity, MainActivity::class.java))
        }

        codeScanner = CodeScanner(this, scannerView)
        codeScanner.apply {
            // Parameters (default values)
            camera = CodeScanner.CAMERA_BACK // or CAMERA_FRONT or specific camera id
            formats = CodeScanner.ALL_FORMATS // list of type BarcodeFormat,

            // ex. listOf(BarcodeFormat.QR_CODE)
            autoFocusMode = AutoFocusMode.SAFE // or CONTINUOUS
            scanMode = ScanMode.CONTINUOUS // or CONTINUOUS or PREVIEW

            isAutoFocusEnabled = true // Whether to enable auto focus or not
            isFlashEnabled = false // Whether to enable flash or not

            // Callbacks
            decodeCallback = DecodeCallback {
                runOnUiThread {
                    qrCodeText = it.text
//                    Toast.makeText(context, "Scan result: ${it.text}", Toast.LENGTH_SHORT).show()
                    Log.d("Response", it.text)
                    getCurrentLocation()

                    val repository = Repository()
                    val viewModelFactory = MainViewModelFactory(repository)

                    viewModel = ViewModelProvider(this@QrCodeScannerActivity, viewModelFactory).get(
                        MainViewModel::class.java)
                    val qrCodePost = QrCodePost(it.text, latitude, longitude)

                    viewModel.getQrCodeStatus(qrCodePost)
                    viewModel.myResponse.observe(this@QrCodeScannerActivity) { response ->
                        if (response.isSuccessful) {
                            var userStatus: String = response.body()?.data?.userStatus.toString()

                            Log.d("Response", response.body()?.message.toString())
                            Log.d("Response", response.body()?.code.toString())
                            Log.d("Response", response.body()?.success.toString())
                            Log.d("Response", userStatus)

                            when (userStatus) {
                                "green" -> {
                                    statusTextView.setText("Berhasil")
                                    statusImgView.setBackgroundResource(R.drawable.green)
                                    statusImgView.setImageResource(R.drawable.green)
                                    vaksinTextView.setText("Sudah Vaksin")
                                }
                                "red" -> {
                                    statusTextView.setText("Gagal")
                                    statusImgView.setBackgroundResource(R.drawable.red)
                                    statusImgView.setImageResource(R.drawable.red)
                                    vaksinTextView.setText("Belum Vaksin")
                                }
                                "yellow" -> {
                                    statusTextView.setText("Warning")
                                    statusImgView.setBackgroundResource(R.drawable.red)
                                    statusImgView.setImageResource(R.drawable.red)
                                    vaksinTextView.setText("Belum Vaksin")
                                }
                                "black" -> {
                                    statusTextView.setText("Blacklist")
                                    statusImgView.setBackgroundResource(R.drawable.red)
                                    statusImgView.setImageResource(R.drawable.red)
                                    vaksinTextView.setText("Belum Vaksin")
                                }
                            }

                        } else {
                            Log.d("Response", "Failed")
                        }
                    }
                }
            }

            errorCallback = ErrorCallback { // or ErrorCallback.SUPPRESS
                runOnUiThread {
                    Toast.makeText(context, "Camera initialization error: ${it.message}",
                        Toast.LENGTH_LONG).show()
                }
            }

            scannerView.setOnClickListener {
                codeScanner.startPreview()
            }
        }
    }

    private fun getCurrentLocation() {
        // checking location permission
        if (ActivityCompat.checkSelfPermission(this,
                Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // request permission
            ActivityCompat.requestPermissions(this,
                arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQ_CODE)
            return
        }

        fusedLocationClient.lastLocation
            .addOnSuccessListener { location ->
                // getting the last known or current location
                latitude = location.latitude
                longitude = location.longitude

                Log.d("Response latitude: ", latitude.toString())
                Log.d("Response longitude: ", longitude.toString())
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed on getting current location",
                    Toast.LENGTH_SHORT).show()
            }
    }

    private fun setupPermissions() {
        val cameraPermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.CAMERA)
        val locationPermission = ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION)

        if (cameraPermission != PackageManager.PERMISSION_GRANTED) {
            makeRequestCamera()
        }

        if (locationPermission != PackageManager.PERMISSION_GRANTED) {
            makeRequestLocation()
        }
    }

    private fun makeRequestCamera() {
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.CAMERA), CAMERA_PERMISSION_REQ_CODE)
    }

    private fun makeRequestLocation() {
        ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQ_CODE)
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<out String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            LOCATION_PERMISSION_REQ_CODE -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "You need the location permission", Toast.LENGTH_SHORT).show()
                    makeRequestLocation()
                }
            }

            CAMERA_PERMISSION_REQ_CODE -> {
                if (grantResults.isEmpty() || grantResults[0] != PackageManager.PERMISSION_GRANTED) {
                    Toast.makeText(this, "You need the camera permission", Toast.LENGTH_SHORT).show()
                    makeRequestCamera()
                }
            }
        }
    }

    override fun onAccuracyChanged(sensor: Sensor, accuracy: Int) {
        // Do something here if sensor accuracy changes.
    }

    override fun onSensorChanged(event: SensorEvent) {
        val temperatureChanged = event.values[0]
        var temperature = temperatureChanged.toString() + "°C"

        temperatureTextView.setText(temperature)
        // Do something with this sensor data.
        Log.d("Temperature", temperature + "°C")
    }

    override fun onResume() {
        super.onResume()
        codeScanner.startPreview()
        sensorManager.registerListener(this, temperature, SensorManager.SENSOR_DELAY_NORMAL)
    }

    override fun onPause() {
        codeScanner.releaseResources()
        sensorManager.unregisterListener(this)
        super.onPause()
    }
}