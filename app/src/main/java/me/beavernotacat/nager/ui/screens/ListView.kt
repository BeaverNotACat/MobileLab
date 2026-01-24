package me.beavernotacat.nager.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import me.beavernotacat.nager.ui.components.EmptyComponent
import me.beavernotacat.nager.ui.components.ErrorComponent
import me.beavernotacat.nager.ui.components.ListItem
import me.beavernotacat.nager.ui.components.LoadingComponent
import me.beavernotacat.nager.models.CountryInfo
import me.beavernotacat.nager.models.LoadingStates


@Composable
fun ListView(
    countries: List<CountryInfo>,
    favorites: Set<String>,
    loadingState: LoadingStates,
    onRetry: () -> Unit,
    openCountry: (String) -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.Top,
    ) {
        when (loadingState) {
            LoadingStates.LOADING -> LoadingComponent()
            LoadingStates.ERROR -> ErrorComponent(onRetry)
            LoadingStates.EMPTY -> EmptyComponent()
            LoadingStates.OK -> {
                LazyColumn(
                    modifier = Modifier.padding(16.dp)
                ) {
                    items(countries.size) { i ->
                        ListItem(countries[i], {
                            openCountry(countries[i].countryCode)
                        }, favorites.contains(countries[i].countryCode))
                    }
                }
            }
        }
    }
}

