package com.if3210_2022_android_28.perludilindungi.faskes

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.lifecycle.ViewModelProviders
import com.if3210_2022_android_28.perludilindungi.room.FaskesDB
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.if3210_2022_android_28.perludilindungi.NewsActivity
import com.if3210_2022_android_28.perludilindungi.R
import com.if3210_2022_android_28.perludilindungi.qrcode.QrCodeScannerActivity

class BookmarkActivity : AppCompatActivity(), DaftarFaskesInterface {

    //private lateinit var binding: ActivityBookmarkBinding
    private lateinit var viewModel: DaftarFaskesViewModel
    val db by lazy { FaskesDB(this) }
    private lateinit var qrCodeBtnView: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView( R.layout.activity_bookmark)
        viewModel = ViewModelProviders.of(this).get(DaftarFaskesViewModel::class.java)
        findViewById<BottomNavigationView>(R.id.bottom_navigation)!!.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener)

        qrCodeBtnView = findViewById(R.id.btn_qrcode)

        qrCodeBtnView.setOnClickListener {
            startActivity(Intent(this@BookmarkActivity, QrCodeScannerActivity::class.java))
        }

        viewModel.makeDBCall(db)

    }

    override fun onItemClick(position: Int) {
        println(viewModel.faskesArray[position])
        val faskes = viewModel.faskesArray[position]

        val intent = Intent(this, DetailActivity::class.java).apply {
            putExtra("FASKES", faskes)
        }
        startActivity(intent)
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
                val intent = Intent(this, MainActivity::class.java).apply {}
                startActivity(intent)
                overridePendingTransition(0,0)
                finish()
                return@OnNavigationItemSelectedListener true
            }
            R.id.bookmark -> {
                return@OnNavigationItemSelectedListener true
            }
        }
        false
    }
}