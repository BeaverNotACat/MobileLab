package me.beavernotacat.splitmate.models

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

class ViewModel : ViewModel() {

    private val _splitState = MutableStateFlow<Split>(Split())
    val splitState: StateFlow<Split> = _splitState.asStateFlow()

    fun updateTotalAmount(amount: String) {
        _splitState.update {
            splitState.value.copy(totalAmount = amount)
        }
    }


    fun updatePeopleCount(count: String) {
        _splitState.update {
            splitState.value.copy(peopleCount = count)

        }
    }

    fun updateTipPercentage(tip: String) {
        _splitState.update {
            splitState.value.copy(tipPercentage = tip)
        }

    }

    fun startNewCalculation() {
        _splitState.update { Split() }
    }
}