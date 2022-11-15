package com.app.currency.ui.main

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.app.currency.R
import com.app.currency.databinding.ActivityMainBinding
import com.app.currency.ui.main.view.MainFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportFragmentManager.beginTransaction()
            .replace(R.id.container, MainFragment.newInstance())
            .commitNow()
    }

}