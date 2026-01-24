package me.beavernotacat.nager.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import me.beavernotacat.nager.models.HolidayInfo

@Composable
fun HolidayItem(
    holiday: HolidayInfo,
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
    ) {
        Text(text = holiday.name, style = MaterialTheme.typography.headlineMedium)
        Text(text = holiday.localName, style = MaterialTheme.typography.bodySmall)
        Text(text = holiday.date)
    }
}