package me.beavernotacat.thecatapi.ui.views

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.PrimaryTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import me.beavernotacat.thecatapi.models.local.CatImage
import me.beavernotacat.thecatapi.models.local.CatInfo
import me.beavernotacat.thecatapi.models.local.LoadingSearchStates
import me.beavernotacat.thecatapi.ui.components.EmptyComponent
import me.beavernotacat.thecatapi.ui.components.ErrorComponent
import me.beavernotacat.thecatapi.ui.components.ListItem
import me.beavernotacat.thecatapi.ui.components.LoadingComponent


@Composable
@OptIn(ExperimentalMaterial3Api::class)
fun SearchView(
    searchState: LoadingSearchStates,
    images: Map<String, CatImage>,
    favoriteIds: List<String>,
    favoriteCats: List<CatInfo>,
    recentCats: List<CatInfo>,
    pattedCats: List<CatInfo>,
    toPatCats: List<CatInfo>,
    toFindCats: List<CatInfo>,
    searchString: String,
    onSearch: (String) -> Unit,
    onRetry: () -> Unit,
    setSelectedCat: (String) -> Unit,
    onOpenSettings: () -> Unit,
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    var selectedFilterTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Search", "Recent", "Favorites", "Pat lists")
    val filterTabs = listOf("To find", "To pat", "Patted")

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp, 0.dp),
        verticalArrangement = Arrangement.Top,
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Text("Cats")
            TextButton(
                onClick = onOpenSettings, modifier = Modifier.testTag("open settings")
            ) {
                Text("Settings")
            }
        }

        PrimaryTabRow(selectedTabIndex = selectedTab) {
            tabs.forEachIndexed { index, title ->
                Tab(
                    selected = selectedTab == index,
                    onClick = { selectedTab = index },
                    text = { Text(title) })
            }
        }
        when (selectedTab) {
            0 -> {
                TextField(
                    value = searchString,
                    onValueChange = onSearch,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("search field")
                )
                when (searchState) {
                    LoadingSearchStates.Loading -> LoadingComponent()
                    LoadingSearchStates.Empty -> EmptyComponent()
                    is LoadingSearchStates.Error -> ErrorComponent(searchState.message, onRetry)
                    is LoadingSearchStates.Ok -> {
                        val cats = searchState.cats
                        CatList(cats, images, favoriteIds, setSelectedCat)
                    }
                }
            }

            1 -> {
                if (recentCats.isEmpty()) {
                    EmptyComponent()
                } else {
                    CatList(recentCats, images, favoriteIds, setSelectedCat)
                }
            }

            2 -> {
                if (favoriteCats.isEmpty()) {
                    EmptyComponent()
                } else {
                    CatList(favoriteCats, images, favoriteIds, setSelectedCat)
                }
            }

            3 -> {
                PrimaryTabRow(selectedTabIndex = selectedFilterTab) {
                    filterTabs.forEachIndexed { index, title ->
                        Tab(
                            selected = selectedFilterTab == index,
                            onClick = { selectedFilterTab = index },
                            text = { Text(title) })
                    }
                }

                when (selectedFilterTab) {
                    0 -> {
                        if (toFindCats.isEmpty()) {
                            EmptyComponent()
                        } else {
                            CatList(toFindCats, images, favoriteIds, setSelectedCat)
                        }
                    }

                    1 -> {
                        if (toPatCats.isEmpty()) {
                            EmptyComponent()
                        } else {
                            CatList(toPatCats, images, favoriteIds, setSelectedCat)
                        }
                    }

                    2 -> {
                        if (pattedCats.isEmpty()) {
                            EmptyComponent()
                        } else {
                            CatList(pattedCats, images, favoriteIds, setSelectedCat)
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CatList(
    cats: List<CatInfo>,
    images: Map<String, CatImage>,
    favoriteIds: List<String>,
    setSelectedCat: (String) -> Unit
) {
    LazyColumn(
        modifier = Modifier.padding(vertical = 16.dp)
    ) {
        items(cats.size) { key ->
            ListItem(
                cats[key],
                images[cats[key].imageId],
                { setSelectedCat(cats[key].id) },
                favoriteIds.contains(cats[key].id)
            )
        }
    }
}
