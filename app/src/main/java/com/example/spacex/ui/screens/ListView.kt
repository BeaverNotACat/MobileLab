package com.example.spacex.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SegmentedButton
import androidx.compose.material3.SegmentedButtonDefaults
import androidx.compose.material3.SingleChoiceSegmentedButtonRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.spacex.data.CREW_DTO_LIST
import com.example.spacex.data.Filter
import com.example.spacex.data.remote.CrewDto
import com.example.spacex.ui.components.ErrorComponent
import com.example.spacex.ui.components.ListItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ListScreen(
    crews: List<CrewDto>,
    retry: () -> Unit,
    isLoading: Boolean,
    errorMessage: String?,
    onSelect: (String) -> Unit,
    selectedFilter: Filter,
    onSelectFilter: (Filter) -> Unit
) {
    val filterItems = Filter.entries.toTypedArray()

    Scaffold(
        topBar = {
            SingleChoiceSegmentedButtonRow(modifier = Modifier.fillMaxWidth()) {
                filterItems.forEachIndexed { index, filter ->
                    SegmentedButton(
                        shape = SegmentedButtonDefaults.itemShape(
                            index = index,
                            count = filterItems.size
                        ),
                        onClick = { onSelectFilter(filter) },
                        selected = filter == selectedFilter,
                        label = { Text(filter.name) },
                        modifier = Modifier.fillMaxWidth()
                    )
                }
            }
        }

    ) { innerPadding ->
        Column(
            modifier = Modifier
                .padding(innerPadding)
                .padding(16.dp)
        ) {


            errorMessage?.let {
                ErrorComponent("Stars said $it", retry)
            }

            if (isLoading) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    CircularProgressIndicator()
                }
            } else if (crews.isEmpty()) {
                ErrorComponent("Seems like no one been to space yet", retry)
            } else {
                LazyColumn {
                    items(items = crews, key = { it.id }) { item ->
                        ListItem(
                            crew = item,
                            onClick = onSelect
                        )
                    }
                }
            }
        }
    }
}

@Preview
@Composable
fun EmptyListScreenPreview() {
    ListScreen(
        crews = emptyList(),
        retry = { },
        isLoading = false,
        errorMessage = null,
        onSelect = { },
        selectedFilter = Filter.ALL,
        onSelectFilter = { }
    )
}

@Preview
@Composable
fun LoadingListScreenPreview() {
    ListScreen(
        crews = emptyList(),
        retry = { },
        isLoading = true,
        errorMessage = null,
        onSelect = { },
        selectedFilter = Filter.ALL,
        onSelectFilter = { }
    )
}

@Preview
@Composable
fun ErrorListScreenPreview() {
    ListScreen(
        crews = emptyList(),
        retry = { },
        isLoading = true,
        errorMessage = "No crews for you",
        onSelect = { },
        selectedFilter = Filter.ALL,
        onSelectFilter = { }
    )
}

@Preview
@Composable
fun ListScreenPreview() {
    ListScreen(
        crews = CREW_DTO_LIST,
        retry = { },
        isLoading = false,
        errorMessage = null,
        onSelect = { },
        selectedFilter = Filter.ALL,
        onSelectFilter = { }
    )
}