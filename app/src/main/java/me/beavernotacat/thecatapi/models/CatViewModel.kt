package me.beavernotacat.thecatapi.models

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.FlowPreview
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.debounce
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.flatMapLatest
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch
import me.beavernotacat.thecatapi.models.local.CatImage
import me.beavernotacat.thecatapi.models.local.CatPatState
import me.beavernotacat.thecatapi.models.local.LoadingDetailsStates
import me.beavernotacat.thecatapi.models.local.LoadingSearchStates
import me.beavernotacat.thecatapi.repositories.CatApiRepository
import me.beavernotacat.thecatapi.repositories.FavoriteRepository
import me.beavernotacat.thecatapi.repositories.PatRepository
import me.beavernotacat.thecatapi.repositories.RecentRepository
import me.beavernotacat.thecatapi.repositories.SettingsRepository
import javax.inject.Inject

@HiltViewModel
@OptIn(ExperimentalCoroutinesApi::class)
class CatViewModel
@Inject constructor(
    private var catApiRepository: CatApiRepository,
    private var favoriteRepository: FavoriteRepository,
    private var recentRepository: RecentRepository,
    private var patRepository: PatRepository,
    private var settingsRepository: SettingsRepository,
) : ViewModel() {
    private var queryFlow = MutableStateFlow("")
    private var selectedFlow = MutableStateFlow("")

    @OptIn(FlowPreview::class)
    private var queryFilteredFlow = queryFlow
        .map { it.trim() }
        .distinctUntilChanged()
        .debounce(500)
    private var catImagesQueue = MutableStateFlow(emptyList<String>())
    var catImages = mutableStateOf(emptyMap<String, CatImage>())
    private val retryRequest = MutableSharedFlow<Unit>(extraBufferCapacity = 1)

    var favoriteFlow = favoriteRepository.getFavoritesFlow().map { favorites ->
        favorites.map { it.id }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(500),
        initialValue = emptyList()
    )

    private val favoriteCatsFlow = favoriteFlow.map { ids ->
        catApiRepository.getBreeds(ids)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(500),
        initialValue = emptyList()
    )

    private val recentFlow = recentRepository.getRecentFlow().map { recent ->
        recent.map { it.id }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(500),
        initialValue = emptyList()
    )

    private val recentCatsFlow = recentFlow.map { ids ->
        catApiRepository.getBreeds(ids)
    }.map { cats ->
        val ids = recentFlow.value
        cats.sortedBy { ids.indexOf(it.id) }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(500),
        initialValue = emptyList()
    )

    private fun observeCatsByState(state: CatPatState) = patRepository.observeByState(state).map { list ->
        list.map { it.id }
    }.map { ids ->
        catApiRepository.getBreeds(ids)
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(500),
        initialValue = emptyList()
    )

    private val pattedCatsFlow = observeCatsByState(CatPatState.Patted)
    private val toPatCatsFlow = observeCatsByState(CatPatState.ToPat)
    private val toFindCatsFlow = observeCatsByState(CatPatState.ToFind)
    private val notFoundCatsFlow = observeCatsByState(CatPatState.NotFound)
    val cacheTtlMinutesFlow = settingsRepository.cacheTtlFlow.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(500),
        initialValue = 60 * 60 * 1000
    )

    var searchFlow = combine(
        queryFilteredFlow, retryRequest.onStart { emit(Unit) }
    ) { query, _ ->
        query
    }.flatMapLatest { query ->
        flow {
            emit(LoadingSearchStates.Loading)
            val listResult = catApiRepository.searchBreeds(query)
            if (listResult.isEmpty()) {
                emit(LoadingSearchStates.Empty)
            } else {
                emit(LoadingSearchStates.Ok(listResult))
                var catImages = listResult.mapNotNull { it.imageId }
                catImagesQueue.emit(catImages)
            }
        }.catch {
            emit(LoadingSearchStates.Error(it.message))
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(500),
        initialValue = LoadingSearchStates.Loading
    )

    var selectedCatFlow = combine(
        selectedFlow, retryRequest.onStart { emit(Unit) }
    ) { id, _ ->
        id
    }.flatMapLatest { id ->
        flow {
            if (id.isEmpty()) {
                return@flow
            }
            emit(LoadingDetailsStates.Loading)
            val result = catApiRepository.fetchBreed(id)
            emit(LoadingDetailsStates.Ok(result))
            val imageId = result.imageId
            if (imageId != null) {
                catImagesQueue.emit(listOf(imageId))
            }
            
            recentRepository.addRecent(id)
        }.catch {
            emit(LoadingDetailsStates.Error(it.message))
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(500),
        initialValue = LoadingDetailsStates.Loading
    )


    val uiState: StateFlow<UiState> = combine(
        combine(queryFlow, searchFlow) { query, search -> query to search },
        combine(favoriteFlow, favoriteCatsFlow) { favorites, favoriteCats -> favorites to favoriteCats },
        combine(pattedCatsFlow, toPatCatsFlow, toFindCatsFlow) { patted, toPat, toFind ->
            Triple(patted, toPat, toFind)
        },
        recentCatsFlow,
        selectedCatFlow
    ) { searchInfo, favoriteInfo, patInfo, recentCats, selectedCat ->
        val (query, search) = searchInfo
        val (favorites, favoriteCats) = favoriteInfo
        val (patted, toPat, toFind) = patInfo
        UiState(
            searchScreen = search,
            detailsScreen = selectedCat,
            search = query,
            favorites = favorites,
            favoriteCats = favoriteCats,
            recentCats = recentCats,
            pattedCats = patted,
            toPatCats = toPat,
            toFindCats = toFind
        )
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(500),
        initialValue = UiState()
    )

    init {
        viewModelScope.launch {
            catImagesQueue.collect { queue ->
                fetchImages(queue)
            }
        }
    }


    fun setSearch(query: String) {
        queryFlow.value = query
    }

    fun retryQuery() {
        retryRequest.tryEmit(Unit)
    }

    fun setCacheTtlMinutes(minutes: Long) {
        viewModelScope.launch {
            settingsRepository.setCacheTtl(minutes)
        }
    }

    fun toggleFavorites(id: String) {
        viewModelScope.launch {
            favoriteRepository.toggleFavorite(id)
        }
    }

    fun fetchBreed(id: String) {
        selectedFlow.value = id
    }

    fun updatePatState(id: String, state: CatPatState) {
        viewModelScope.launch {
            patRepository.addPatState(id, state)
        }
    }


    fun fetchImages(ids: List<String>) {
        val filteredIds = ids.filter {
            !(catImages.value[it] != null && catImages.value[it] !is CatImage.FailedCatImage)
        }
        filteredIds.forEach {
            catImages.value = catImages.value.plus(Pair(it, CatImage.LoadingCatImage))
        }
        viewModelScope.launch {
            filteredIds.forEach { id ->
                catImages.value = catImages.value.plus(Pair(id, fetchImageUrl(id)))
            }
        }
    }

    suspend fun fetchImageUrl(id: String): CatImage {
        try {
            val image = catApiRepository.fetchImageUrl(id)
            return image
        } catch (e: Exception) {
            e.printStackTrace()
            return CatImage.FailedCatImage(e.message.orEmpty())
        }
    }
}
