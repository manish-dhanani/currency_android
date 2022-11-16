package com.app.currency.ui.main.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.currency.data.local.ConvertResult
import com.app.currency.data.repo.FixerRepository
import com.app.currency.data.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val fixerRepository: FixerRepository) :
    ViewModel() {

    private val _convertResult = MutableLiveData<Result<ConvertResult>>()
    val convertResult get() = _convertResult

    fun convert(from: String, to: String, amount: Double) {
        viewModelScope.launch {
            fixerRepository.convert(from, to, amount).collectLatest {
                _convertResult.postValue(it)
            }
        }
    }

}