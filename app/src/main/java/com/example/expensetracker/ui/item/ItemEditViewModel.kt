package com.example.expensetracker.ui.item

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.expensetracker.data.PurchaseRepo
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch


class ItemEditViewModel(
    savedStateHandle: SavedStateHandle,
    private val itemsRepository: PurchaseRepo
) : ViewModel() {

    private val itemIdForEdit: Int = checkNotNull(savedStateHandle[ItemEditDestination.itemIdArg])

    /**
     * Holds the item details ui state. The data is retrieved from [PurchaseRepo] and mapped to
     * the UI state.
     */
    val uiState: StateFlow<ItemEditUiState> =
        itemsRepository.getPurchaseStream(itemIdForEdit)
            .filterNotNull()
            .map {
                ItemEditUiState(outOfStock = it.price <= 0, itemDetails = it.toItemDetails())
            }.stateIn(
                scope = viewModelScope,
                started = SharingStarted.WhileSubscribed(TIMEOUT_MILLIS_FOR_EDIT),
                initialValue = ItemEditUiState()
            )

    /**
     * Holds current item ui state
     */
    var itemUiState by mutableStateOf(ItemUiState())
        private set

    private val itemId: Int = checkNotNull(savedStateHandle[ItemEditDestination.itemIdArg])

    init {
        viewModelScope.launch {
            itemUiState = itemsRepository.getPurchaseStream(itemId)
                .filterNotNull()
                .first()
                .toItemUiState(true)
        }
    }


    suspend fun updateItem() {
        if (validateInput(itemUiState.itemDetails)) {
            itemsRepository.updatePurchase(itemUiState.itemDetails.toItem())
        }
    }

    /**
     * Updates the [itemUiState] with the value provided in the argument. This method also triggers
     * a validation for input values.
     */
    fun updateUiState(itemDetails: ItemDetails) {
        itemUiState =
            ItemUiState(itemDetails = itemDetails, isEntryValid = validateInput(itemDetails))
    }

    suspend fun deleteItem() {
        itemsRepository.deletePurchase(uiState.value.itemDetails.toItem())
    }

    private fun validateInput(uiState: ItemDetails = itemUiState.itemDetails): Boolean {
        return with(uiState) {
            name.isNotBlank() && price.isNotBlank() //&& quantity.isNotBlank()
        }
    }

    companion object {
        private const val TIMEOUT_MILLIS_FOR_EDIT = 5_000L
    }
}

/**
 * UI state for ItemDetailsScreen
 */
data class ItemEditUiState(
    val outOfStock: Boolean = true,
    val itemDetails: ItemDetails = ItemDetails()
)