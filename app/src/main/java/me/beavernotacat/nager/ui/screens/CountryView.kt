package me.beavernotacat.nager.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.em
import me.beavernotacat.nager.models.HolidayInfo
import me.beavernotacat.nager.ui.components.HolidayItem
import me.beavernotacat.nager.utils.CountryToEmoji


@Composable
fun CountryView(
    countryCode: String,
    holidaysInfo: List<HolidayInfo>,
    favorite: Boolean,
    addToFavorites: () -> Unit,
    removeFromFavorites: () -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
        ) {
        item {
            Text(
                text = CountryToEmoji(countryCode),
                fontSize = 50.em,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .fillMaxSize()
            )
        }

        items(holidaysInfo.size) { i ->
            HolidayItem(holidaysInfo[i])
        }


        item {
            if (favorite) Button(addToFavorites) { Text("I love this country!") } else Button(
                removeFromFavorites
            ) { Text("I hate this country!") }
        }

    }
}

