package com.example.spacex.ui.components

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.spacex.R

@Composable
fun AgencyLogo(
    name: String,
    modifier: Modifier
) {
    val logo = when (name) {
        "NASA" -> R.drawable.nasa
        "JAXA" -> R.drawable.jaxa
        "Roscosmos" -> R.drawable.roskosmos
        "SpaceX" -> R.drawable.spacex
        "ESA" -> R.drawable.esa
        else -> null
    }
    if (logo != null) {
        Image(painter = painterResource(logo), modifier = modifier, contentDescription = name)
    } else {
        Text(text = name, modifier = modifier, fontStyle = FontStyle.Italic)
    }
}


@Preview
@Composable
fun NasaAgencyLogoPreview() {
    AgencyLogo(name = "NASA", modifier = Modifier
        .width(400.dp)
        .height(400.dp))
}

@Preview
@Composable
fun JaxaAgencyLogoPreview() {
    AgencyLogo(name = "JAXA", modifier = Modifier
        .width(400.dp)
        .height(400.dp))
}

@Preview
@Composable
fun UnknownAgencyLogoPreview() {
    AgencyLogo(name = "Unknown", modifier = Modifier
        .width(400.dp)
        .height(400.dp))
}