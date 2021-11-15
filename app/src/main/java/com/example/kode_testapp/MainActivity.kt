package com.example.kode_testapp

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.kode_testapp.screens.main_page.MainPageFragment

class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .add(R.id.fragment_container, MainPageFragment())
                .addToBackStack(null)
                .commit()
        }
    }
}