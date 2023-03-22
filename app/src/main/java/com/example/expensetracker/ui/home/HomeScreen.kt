package com.example.expensetracker.ui.home

import androidx.annotation.StringRes
import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.expensetracker.InventoryTopAppBar
import com.example.expensetracker.R
import com.example.expensetracker.data.Purchase
import com.example.expensetracker.ui.AppViewModelProvider
import com.example.expensetracker.ui.navigation.NavigationDestination
import com.vanpra.composematerialdialogs.MaterialDialog
import com.vanpra.composematerialdialogs.datetime.date.datepicker
import com.vanpra.composematerialdialogs.rememberMaterialDialogState
import java.text.NumberFormat
import java.time.LocalDate
import java.time.LocalTime
import java.time.format.DateTimeFormatter
import java.util.*

object HomeDestination : NavigationDestination {
    override val route = "home"
    override val titleRes = R.string.app_name
}

private var currentDate by mutableStateOf(LocalDate.now())

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
    val dateDialogState = rememberMaterialDialogState()
    val totalSpent by remember {
        mutableStateOf({
            var sum = 0
            for (items in homeUiState.itemList) {
                if (LocalDate.parse(items.date) == currentDate) {
                    sum += items.price
                }
            }
            sum
        })
    }
    Scaffold(
        topBar = {
            InventoryTopAppBar(
                title = "Total spent today: " + NumberFormat.getCurrencyInstance(Locale.getDefault())
                    .format(totalSpent.invoke()),
                canNavigateBack = false
            )
        },
        bottomBar = {
            BottomAppBar(backgroundColor = MaterialTheme.colors.surface) {
                Row(modifier.fillMaxWidth()) {
                    Button(
                        onClick = { dateDialogState.show() },
                        colors = ButtonDefaults.buttonColors(backgroundColor = MaterialTheme.colors.secondary),
                        shape = RoundedCornerShape(15.dp),
                        modifier = Modifier
                            .padding(horizontal = 20.dp)
                            .padding(bottom = 5.dp),
                        elevation = ButtonDefaults.elevation(7.dp)
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.calendar),
                            contentDescription = "calendar"
                        )
                    }
                    Spacer(modifier = modifier.padding(horizontal = 20.dp))
                    if (currentDate == LocalDate.now()) {
                        ExtendedFloatingActionButton(
                            onClick = navigateToItemEntry,
                            text = { Text(text = "Add Purchase") },
                            icon = {
                                Icon(
                                    imageVector = Icons.Default.Add,
                                    //painter = painterResource(id = R.drawable.calendar),
                                    contentDescription = stringResource(id = R.string.item_entry_title),
                                    tint = MaterialTheme.colors.onSecondary
                                )
                            },
                            shape = RoundedCornerShape(15.dp),
                            modifier = Modifier
                                .navigationBarsPadding()
                                .padding(horizontal = 20.dp)

                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        HomeBody(
            itemList = homeUiState.itemList,
            onItemClick = navigateToItemUpdate,
            modifier = modifier.padding(innerPadding)
        )
    }
    MaterialDialog(
        dialogState = dateDialogState,
        buttons = {
            positiveButton(text = "Ok") {
            }
            negativeButton(text = "Cancel")
        }
    ) {
        datepicker(
            initialDate = currentDate,
            title = "Pick a date",
            allowedDateValidator = {
                it <= LocalDate.now()
            }
        ) {
            currentDate = it
        }
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
    LazyColumn(modifier = modifier ) {
        items(items = itemList, key = { it.id }) { item ->
            if (currentDate == LocalDate.parse(item.date)) {
                InventoryItem(item = item, onItemClick = onItemClick)
                Divider()
            }
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
        Text(
            text = LocalTime.parse(item.time, DateTimeFormatter.ISO_TIME).format(
                DateTimeFormatter.ofPattern("HH:mm")
            ), modifier = Modifier.weight(1.0f)
        )

    }
}

private data class InventoryHeader(@StringRes val headerStringId: Int, val weight: Float)

private val headerList = listOf(
    InventoryHeader(headerStringId = R.string.item, weight = 1.5f),
    InventoryHeader(headerStringId = R.string.price, weight = 1.0f),
    InventoryHeader(headerStringId = R.string.quantity_in_stock, weight = 1.0f)

)

