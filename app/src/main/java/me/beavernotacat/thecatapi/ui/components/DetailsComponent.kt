package me.beavernotacat.thecatapi.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import me.beavernotacat.thecatapi.models.local.CatImage
import me.beavernotacat.thecatapi.models.local.CatInfo
import me.beavernotacat.thecatapi.models.local.CatPatState

fun String.toFlagEmoji(): String {
    if (this.length != 2) {
        return this
    }
    val countryCodeCaps = this.uppercase()
    val firstLetter = Character.codePointAt(countryCodeCaps, 0) - 0x41 + 0x1F1E6
    val secondLetter = Character.codePointAt(countryCodeCaps, 1) - 0x41 + 0x1F1E6

    return String(Character.toChars(firstLetter)) + String(Character.toChars(secondLetter))
}

fun Int.toMark(): String {
    return "⭐".repeat(this)
}

@Composable
fun DetailsComponent(
    catInfo: CatInfo,
    image: CatImage?,
    favorite: Boolean,
    patState: CatPatState,
    addToFavorites: () -> Unit,
    onUpdatePatState: (CatPatState) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center,
    ) {
        item {
            RemoteCatImage(image, Modifier)
        }
        item {
            Text(
                text = catInfo.name,
                modifier = Modifier.padding(bottom = 16.dp),
                style = MaterialTheme.typography.headlineLarge
            )
        }
        item { Text(text = catInfo.description) }
        item { DetailsRow(label = "Weight", value = "${catInfo.weight} kg") }
        item { DetailsRow(label = "Life span", value = "${catInfo.lifeSpan} years") }
        item { DetailsRow(label = "Indoor", value = if (catInfo.indoor == 1) "✅" else "⛔️") }
        item { DetailsRow(label = "Adaptability", value = catInfo.adaptability.toMark()) }
        item { DetailsRow(label = "Cat friendly", value = (catInfo.catFriendly ?: 0).toMark()) }
        item { DetailsRow(label = "Dog friendly", value = catInfo.dogFriendly.toMark()) }
        item { DetailsRow(label = "Child friendly", value = catInfo.childFriendly.toMark()) }
        item { DetailsRow(label = "Stranger friendly", value = catInfo.strangerFriendly.toMark()) }
        item { DetailsRow(label = "Social needs", value = catInfo.socialNeeds.toMark()) }
        item { DetailsRow(label = "Origin", value = catInfo.countryCode.toFlagEmoji()) }
        item { 
            Button(onClick = addToFavorites) { 
                Text(if (favorite) "Now i hate this cat." else "I love this cat!") 
            } 
        }
        if (patState == CatPatState.NotFound) {
            item {
                Button(
                    modifier = Modifier.padding(top = 8.dp),
                    onClick = { onUpdatePatState(CatPatState.ToFind) }
                ) {
                    Text("Add to \"Going to find\" list")
                }
            }
        }
        if (patState == CatPatState.ToFind) {
            item {
                Button(
                    modifier = Modifier.padding(top = 8.dp),
                    onClick = {
                        onUpdatePatState(
                            CatPatState.ToPat
                        )
                    }
                ) {
                    Text(
                        "Cat found and ready to be patted!"
                    )
                }
            }
        }
        if (patState == CatPatState.ToPat) {
            item {
                Button(
                    modifier = Modifier.padding(top = 8.dp),
                    onClick = {
                        onUpdatePatState(
                            CatPatState.Patted
                        )
                    }
                ) {
                    Text(
                        "Patted"
                    )
                }
            }
        }
    }
}
