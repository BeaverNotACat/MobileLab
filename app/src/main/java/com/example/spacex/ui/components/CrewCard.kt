package com.example.spacex.ui.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.spacex.data.CREW_DTO
import com.example.spacex.data.remote.CrewDto

@Composable
fun ListItem(
    crew: CrewDto,
    onClick: (String) -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp)
            .clickable {
                onClick(crew.id)
            },
        horizontalArrangement = Arrangement.spacedBy(8.dp),
    ) {
        AgencyLogo(crew.agency, Modifier
            .height(20.dp)
            .width(20.dp))
        Text(text = crew.name)
    }
}

@Preview(showBackground = true, backgroundColor = 0xFFFFFF)
@Composable
fun ListItemPreview() {
    ListItem(
        crew = CREW_DTO,
        onClick = { }
    )
}