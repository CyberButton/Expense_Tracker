package com.example.expensetracker.ui.home


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expensetracker.data.Purchase
import com.example.expensetracker.data.PurchaseRepo
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.stateIn


/**
 * View Model to retrieve all items in the Room database.
 */
class HomeViewModel(itemsRepository: PurchaseRepo) : ViewModel() {
    //var currentDate by mutableStateOf(LocalDate.now())

    val homeUiState: StateFlow<HomeUiState> =
        itemsRepository.getAllPurchasesStream().map { HomeUiState(it) }
            .stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS),
                initialValue = HomeUiState()
            )

    companion object {
        private const val TIMEOUT_MILLIS = 5_000L
    }
}

/**
 * Ui State for HomeScreen
 */
data class HomeUiState(val itemList: List<Purchase> = listOf())
