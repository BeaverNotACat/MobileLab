package com.example.spacex.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.spacex.R
import com.example.spacex.data.CREW_DTO
import com.example.spacex.data.remote.CrewDto
import com.example.spacex.ui.components.AgencyLogo
import com.example.spacex.ui.components.RemoteImage

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailView(
    person: CrewDto,
    openWikipedia: () -> Unit
) {
    Scaffold { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {

            Row {
                Text(person.name, style = MaterialTheme.typography.headlineMedium)
                IconButton(onClick = openWikipedia) {
                    Icon(
                        painter = painterResource(R.drawable.open_in),
                        contentDescription = "Wikipedia"
                    )
                }
            }
            RemoteImage(person.image, Modifier
                .fillMaxWidth()
                .height(400.dp))
            AgencyLogo(person.agency, Modifier
                .width(100.dp)
                .height(100.dp))
        }
    }
}

@Preview
@Composable
fun DetailScreenPreview() {
    DetailView(
        person = CREW_DTO,
        openWikipedia = { }
    )
}
