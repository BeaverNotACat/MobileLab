package me.beavernotacat.thecatapi.ui.components

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
import me.beavernotacat.thecatapi.models.local.CatImage
import me.beavernotacat.thecatapi.models.local.CatInfo

@OptIn(ExperimentalMaterial3ExpressiveApi::class)
@Composable
fun ListItem(
    catInfo: CatInfo,
    image: CatImage?,
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
        RemoteCatImage(image, Modifier
            .height(32.dp)
            .width(64.dp))
        if (favorite) Text(text = "😻")
        Text(text = catInfo.name)

    }
}