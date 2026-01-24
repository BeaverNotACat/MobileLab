package me.beavernotacat.nager.models

import android.icu.util.Calendar
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class ViewModel : ViewModel() {
    private val _uiState = MutableStateFlow(UiState())
    val uiState: StateFlow<UiState> = _uiState.asStateFlow()

    init {
        fetchCountries()
    }

    fun addToFavorites(code: String) {
        _uiState.update {
            _uiState.value.copy(favoriteList = _uiState.value.favoriteList + setOf(code))
        }

    }

    fun removeFromFavorites(code: String) {
        _uiState.update {
            _uiState.value.copy(favoriteList = _uiState.value.favoriteList - setOf(code))
        }
    }

    fun fetchCountries() {
        viewModelScope.launch {
            _uiState.update {
                _uiState.value.copy(state = LoadingStates.LOADING)
            }
            try {
                val listResult = apiService.availableCountries()
                _uiState.update {
                    _uiState.value.copy(
                        state = if (listResult.isNotEmpty()) LoadingStates.OK else LoadingStates.EMPTY,
                        countries = listResult
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _uiState.update {
                    _uiState.value.copy(
                        state = LoadingStates.ERROR,
                    )
                }

            }

        }
    }

    fun fetchCountry(code: String) {
        viewModelScope.launch {
            if (uiState.value.countryHolidays.contains(code)) return@launch
            _uiState.update {
                _uiState.value.copy(state = LoadingStates.LOADING)
            }
            try {
                val result = apiService.listHolidays(Calendar.getInstance().get(Calendar.YEAR), code)
                _uiState.update {
                    _uiState.value.copy(
                        state = if (result.isNotEmpty()) LoadingStates.OK else LoadingStates.EMPTY,
                        countryHolidays = _uiState.value.countryHolidays.plus(Pair(code, result))
                    )
                }
            } catch (e: Exception) {
                e.printStackTrace()
                _uiState.update {
                    _uiState.value.copy(
                        state = LoadingStates.ERROR,
                    )
                }

            }

        }

    }

}