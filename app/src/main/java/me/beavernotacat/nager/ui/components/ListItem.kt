package me.beavernotacat.nager.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3ExpressiveApi
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import me.beavernotacat.nager.models.CountryInfo
import me.beavernotacat.nager.utils.CountryToEmoji

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ListItem(
    countryInfo: CountryInfo,
    onClick: () -> Unit,
    favorite: Boolean
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable {
                onClick()
            },
        horizontalArrangement = Arrangement.Absolute.Left,
    ) {
        Text(text = CountryToEmoji(countryInfo.countryCode))
        Text(text = countryInfo.name)
        if (favorite) Text(text = "⭐️")
    }
}