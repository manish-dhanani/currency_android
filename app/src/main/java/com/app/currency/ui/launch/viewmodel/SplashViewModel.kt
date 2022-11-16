package com.app.currency.ui.launch.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.currency.data.repo.FixerRepository
import com.app.currency.data.util.Result
import com.app.currency.util.DataState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class SplashViewModel @Inject constructor(private val fixerRepository: FixerRepository) :
    ViewModel() {

    companion object {
        const val SPLASH_PLAY_DURATION = 3000L // 3 sec
        const val SPLASH_REPEAT_DURATION = 1000L // 1 sec
    }

    private val _symbolsResult = MutableLiveData<Result<String>>()
    val symbolsResult get() = _symbolsResult

    var symbolsDataState = DataState.NONE

    var isSplashPlayFinished = MutableLiveData(false)

    fun getSymbols() {
        viewModelScope.launch {
            fixerRepository.getSymbols().collectLatest {
                _symbolsResult.postValue(it)
            }
        }
    }

    fun playSplash(splashDuration: Long) {
        viewModelScope.launch {
            // Play splash animation.
            delay(splashDuration)

            when (symbolsDataState) {
                DataState.FETCHING -> {
                    // Continue splash animation for 1 sec.
                    playSplash(SPLASH_REPEAT_DURATION)
                }
                else -> {
                    isSplashPlayFinished.value = true
                }
            }
        }
    }

}