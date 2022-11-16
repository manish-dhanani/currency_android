package com.app.currency.ui.main.viewmodel

import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.app.currency.data.local.ConversionData
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

    private val _historicalDataResult = MutableLiveData<Result<List<ConversionData>>>()
    val historicalDataResult get() = _historicalDataResult

    private val _popularConversionsResult = MutableLiveData<Result<List<ConversionData>>>()
    val popularConversionsResult get() = _popularConversionsResult

    fun convert(from: String, to: String, amount: Double) {
        viewModelScope.launch {
            fixerRepository.convert(from, to, amount).collectLatest {
                _convertResult.postValue(it)
            }
        }
    }

    fun getHistoricalData(base: String, symbols: String, startDate: String, endDate: String) {
        viewModelScope.launch {
            fixerRepository.getHistoricalData(base, symbols, startDate, endDate).collectLatest {
                _historicalDataResult.postValue(it)
            }
        }
    }

    fun getPopularConversions(base: String, symbols: String) {
        viewModelScope.launch {
            fixerRepository.getPopularConversions(base, symbols).collectLatest {
                _popularConversionsResult.postValue(it)
            }
        }
    }

}