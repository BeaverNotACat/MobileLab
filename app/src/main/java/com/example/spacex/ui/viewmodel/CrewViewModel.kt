package com.example.spacex.ui.viewmodel

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.spacex.data.Filter
import com.example.spacex.data.SpaceXRepository
import com.example.spacex.data.remote.CrewDto
import kotlinx.coroutines.launch

data class CrewUiState(
    val isLoading: Boolean = false,
    val errorMessage: String? = null,
    val selectedFilter: Filter = Filter.ALL
)

class CrewViewModel(private val repository: SpaceXRepository = SpaceXRepository()) : ViewModel() {

    var uiState by mutableStateOf(CrewUiState())

    private var _recent = mutableListOf<String>()

    private var _items = mutableMapOf<String, CrewDto>()
    val items: List<CrewDto>
        get() {
            if (uiState.selectedFilter == Filter.RECENT) {
                return _items.values.filter { _recent.contains(it.id) }
            }
            return _items.values.toList()
        }


    fun getItem(id: String): CrewDto? {
        return _items[id]
    }

    fun addRecent(id: String) {
        if (_recent.contains(id)) {
            _recent.remove(id)
        }
        _recent.add(id)
        if (_recent.size > 5) {
            _recent.removeAt(0)
        }
    }

    fun setFilter(filter: Filter) {
        uiState = uiState.copy(selectedFilter = filter)
    }

    init {
        list()
    }

    fun list() {
        uiState = uiState.copy(isLoading = true, errorMessage = null)

        viewModelScope.launch {
            try {
                val result = repository.listCrews()
                result.forEach {
                    _items[it.id] = it
                }
                uiState = uiState.copy(isLoading = false)
            } catch (ex: Exception) {
                uiState = uiState.copy(
                    isLoading = false,
                    errorMessage = ex.message
                )
                ex.printStackTrace()
            }
        }
    }
}