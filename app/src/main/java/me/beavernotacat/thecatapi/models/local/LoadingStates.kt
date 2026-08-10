package me.beavernotacat.thecatapi.models.local


sealed class LoadingSearchStates {
    data object Loading: LoadingSearchStates()
    data class Ok(val cats: List<CatInfo>): LoadingSearchStates()
    data class Error(val message: String?): LoadingSearchStates()
    data object Empty: LoadingSearchStates()
}

sealed class LoadingDetailsStates {
    data object Loading: LoadingDetailsStates()
    data class Ok(val cat: CatInfo): LoadingDetailsStates()
    data class Error(val message: String?): LoadingDetailsStates()
}
