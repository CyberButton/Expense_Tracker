package com.example.expensetracker.ui.item

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.expensetracker.data.Purchase
import com.example.expensetracker.data.PurchaseRepo
import java.time.LocalDate
import java.time.LocalTime

/**
 * View Model to validate and insert items in the Room database.
 */
class ItemEntryViewModel(private val itemsRepository: PurchaseRepo) : ViewModel() {

    /**
     * Holds current item ui state
     */
    var itemUiState by mutableStateOf(ItemUiState())
        private set

    /**
     * Updates the [itemUiState] with the value provided in the argument. This method also triggers
     * a validation for input values.
     */
    fun updateUiState(itemDetails: ItemDetails) {
        //Log.d("started", "started updationg UiState")
        itemUiState =
            ItemUiState(itemDetails = itemDetails, isEntryValid = validateInput(itemDetails))
    }


    suspend fun saveItem() {
        if (validateInput()) {
            itemsRepository.insertPurchase(itemUiState.itemDetails.toItem())
        }
    }

    private fun validateInput(uiState: ItemDetails = itemUiState.itemDetails): Boolean {
        //Log.d("started", "started valiating input")
        //println(uiState.name.isNotBlank() && uiState.price.isNotBlank())
        return with(uiState) {
            name.isNotBlank() && price.isNotBlank() //&& quantity.isNotBlank()
        }
    }
}

/**
 * Represents Ui State for an Item.
 */
data class ItemUiState(
    val itemDetails: ItemDetails = ItemDetails(),
    val isEntryValid: Boolean = false
)

data class ItemDetails(
    val id: Int = 0,
    val name: String = "",
    val price: String = "",
    //val quantity: String = "",
    val date: LocalDate = LocalDate.now(),
    val time: LocalTime = LocalTime.now()
)


fun ItemDetails.toItem(): Purchase = Purchase(
    id = id,
    name = name,
    price = price.toIntOrNull() ?: 0,
    //quantity = quantity.toIntOrNull() ?: 0
    date = date.toString(),
    time = time.toString()
)


fun Purchase.toItemUiState(isEntryValid: Boolean = false): ItemUiState = ItemUiState(
    itemDetails = this.toItemDetails(),
    isEntryValid = isEntryValid
)


fun Purchase.toItemDetails(): ItemDetails = ItemDetails(
    id = id,
    name = name,
    price = price.toString(),
    date = LocalDate.parse(date),
    time = LocalTime.parse(time)
    //quantity = quantity.toString()
)

