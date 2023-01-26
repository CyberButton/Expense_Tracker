

package com.example.expensetracker.ui.home

import android.graphics.drawable.shapes.Shape
import androidx.annotation.StringRes
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.expensetracker.InventoryTopAppBar
import com.example.expensetracker.R
import com.example.expensetracker.data.Purchase
import com.example.expensetracker.ui.AppViewModelProvider
import com.example.expensetracker.ui.navigation.NavigationDestination
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.*

object HomeDestination : NavigationDestination {
    override val route = "home"
    override val titleRes = R.string.app_name
}

/**
 * Entry route for Home screen
 */
@Composable
fun HomeScreen(
    navigateToItemEntry: () -> Unit,
    navigateToItemUpdate: (Int) -> Unit,
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = viewModel(factory = AppViewModelProvider.Factory)
) {
    val homeUiState by viewModel.homeUiState.collectAsState()
    Scaffold(
        topBar = {
            InventoryTopAppBar(
                title = stringResource(HomeDestination.titleRes),
                canNavigateBack = false
            )
        },
        floatingActionButton = {
            ExtendedFloatingActionButton(
                onClick = navigateToItemEntry,
                text = { Text(text = "Add Purchase") },
                icon = {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = stringResource(id = R.string.item_entry_title),
                        tint = MaterialTheme.colors.onPrimary
                    )
                },
                modifier = Modifier.navigationBarsPadding()
            )
        },
    ) { innerPadding ->
        HomeBody(
            itemList = homeUiState.itemList,
            onItemClick = navigateToItemUpdate,
            modifier = modifier.padding(innerPadding)
        )
    }
}

@Composable
private fun HomeBody(
    itemList: List<Purchase>,
    onItemClick: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        InventoryListHeader()
        Divider()
        if (itemList.isEmpty()) {
            Text(
                text = stringResource(id = R.string.no_item_description),
                style = MaterialTheme.typography.subtitle2
            )
        } else {
            InventoryList(itemList = itemList, onItemClick = { onItemClick(it.id) })
        }
    }
}

@Composable
private fun InventoryList(
    itemList: List<Purchase>,
    onItemClick: (Purchase) -> Unit,
    modifier: Modifier = Modifier
) {
    LazyColumn(modifier = modifier, verticalArrangement = Arrangement.spacedBy(8.dp)) {
        items(items = itemList, key = { it.id }) { item ->
            InventoryItem(item = item, onItemClick = onItemClick)
            Divider()
        }
    }
}

@Composable
private fun InventoryListHeader(modifier: Modifier = Modifier) {
    Row(modifier = modifier.fillMaxWidth(), verticalAlignment = Alignment.CenterVertically) {
        headerList.forEach {
            Text(
                text = stringResource(it.headerStringId),
                modifier = Modifier.weight(it.weight),
                style = MaterialTheme.typography.h6
            )
        }
    }
}

@Composable
private fun InventoryItem(
    item: Purchase,
    onItemClick: (Purchase) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier
        .fillMaxWidth()
        .clickable { onItemClick(item) }
        .padding(vertical = 16.dp)
    ) {
        Text(
            text = item.name,
            modifier = Modifier.weight(1.5f),
            fontWeight = FontWeight.Bold
        )
        Text(
            text = NumberFormat.getCurrencyInstance(Locale.getDefault()).format(item.price),
            modifier = Modifier.weight(1.0f)
        )
        Text(text = SimpleDateFormat("HH:mm", Locale.ENGLISH).format(item.time), modifier = Modifier.weight(1.0f))

    }
}

private data class InventoryHeader(@StringRes val headerStringId: Int, val weight: Float)

private val headerList = listOf(
    InventoryHeader(headerStringId = R.string.item, weight = 1.5f),
    InventoryHeader(headerStringId = R.string.price, weight = 1.0f),
    InventoryHeader(headerStringId = R.string.quantity_in_stock, weight = 1.0f)

)

//@Preview(showBackground = true)
//@Composable
//fun HomeScreenRoutePreview() {
//    InventoryTheme {
//        HomeBody(
//            listOf(
//                Item(1, "Game", 100.0, 20),
//                Item(2, "Pen", 200.0, 30),
//                Item(3, "TV", 300.0, 50)
//            ),
//            onItemClick = {}
//        )
//    }
//}
