package me.beavernotacat.nager.models

enum class LoadingStates {
    LOADING,
    OK,
    ERROR,
    EMPTY
}

data class UiState (
    val search: String = "",
    val state: LoadingStates = LoadingStates.LOADING,
    val countries: List<CountryInfo> = emptyList(),
    val countryHolidays: Map<String, List<HolidayInfo>> = emptyMap(),
    val favoriteList: Set<String> = emptySet()
)