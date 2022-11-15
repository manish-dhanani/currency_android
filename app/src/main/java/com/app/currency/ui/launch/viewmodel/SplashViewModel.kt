package com.app.currency.ui.launch.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SplashViewModel : ViewModel() {

    companion object {
        const val SPLASH_PLAY_DURATION = 3000L // 3000 millis
    }

    var isSplashPlayFinished = MutableLiveData(false)

    fun playSplash() {
        viewModelScope.launch {
            // Play splash animation.
            delay(SPLASH_PLAY_DURATION)

            isSplashPlayFinished.value = true
        }
    }

}