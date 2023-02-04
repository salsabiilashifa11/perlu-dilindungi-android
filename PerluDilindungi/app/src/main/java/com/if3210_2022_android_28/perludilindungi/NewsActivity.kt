package com.if3210_2022_android_28.perludilindungi

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.android.material.floatingactionbutton.FloatingActionButton
import com.if3210_2022_android_28.perludilindungi.faskes.BookmarkActivity
import com.if3210_2022_android_28.perludilindungi.faskes.MainActivity
import com.if3210_2022_android_28.perludilindungi.qrcode.QrCodeScannerActivity

class NewsActivity : AppCompatActivity() {

    private lateinit var qrCodeBtnView: FloatingActionButton

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_news)

        qrCodeBtnView = findViewById(R.id.btn_qrcode)

        qrCodeBtnView.setOnClickListener {
            startActivity(Intent(this@NewsActivity, QrCodeScannerActivity::class.java))
        }

        findViewById<BottomNavigationView>(R.id.news_bottom_navigation).setOnItemSelectedListener { menuItem ->
            when (menuItem.itemId) {
                R.id.news -> {
                    true
                }
                R.id.faskes -> {
                    val intent = Intent(this, MainActivity::class.java).apply {}
                    startActivity(intent)
                    overridePendingTransition(0, 0)
                    finish()
                    true
                }
                R.id.bookmark -> {
                    val intent = Intent(this, BookmarkActivity::class.java).apply {}
                    startActivity(intent)
                    overridePendingTransition(0, 0)
                    finish()
                    true
                }
                else -> false
            }
        }
    }
}