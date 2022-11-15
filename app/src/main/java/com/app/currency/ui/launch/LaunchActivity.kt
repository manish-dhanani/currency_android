package com.app.currency.ui.launch

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.app.currency.R
import com.app.currency.databinding.ActivityLaunchBinding
import com.app.currency.ui.launch.view.SplashFragment
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LaunchActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLaunchBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityLaunchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportFragmentManager.beginTransaction()
            .replace(R.id.container, SplashFragment.newInstance())
            .commitNow()
    }

}