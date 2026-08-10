package me.beavernotacat.thecatapi.ui.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp

@Composable
fun SettingsView(
    currentCacheTtlHours: Long,
    onSaveCacheTtlHours: (Long) -> Unit,
) {
    var ttlInput by remember(currentCacheTtlHours) { mutableStateOf(currentCacheTtlHours.toString()) }
    val ttlValue = ttlInput.toLongOrNull()
    val isValid = ttlValue != null && ttlValue >= 1L

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp),
    ) {
        Text("Cache TTL (hours)")
        TextField(
            value = ttlInput,
            onValueChange = { ttlInput = it.filter(Char::isDigit) },
            modifier = Modifier
                .fillMaxWidth()
                .testTag("cache ttl input"),
            placeholder = { Text("Enter hours") },
            isError = ttlInput.isNotEmpty() && !isValid,
        )
        if (ttlInput.isNotEmpty() && !isValid) {
            Text("TTL must be at least 1 hour.")
        }
        Button(
            onClick = { ttlValue?.let(onSaveCacheTtlHours) },
            enabled = isValid,
            modifier = Modifier.testTag("save cache ttl"),
        ) {
            Text("Save")
        }
    }
}
