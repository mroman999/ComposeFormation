package com.mercadona.mercastock.presentation.features.product

import android.content.Context
import androidx.compose.material3.SnackbarDuration
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mercadona.mercastock.dia3.R
import com.mercadona.mercastock.domain.model.Allergen
import com.mercadona.mercastock.domain.model.Product
import com.mercadona.mercastock.domain.usecase.GetProductByIdUseCase
import com.mercadona.mercastock.domain.usecase.SaveProductUseCase
import com.mercadona.mercastock.presentation.navigation.NavigationDestination
import com.mercadona.mercastock.presentation.ui.components.SnackbarManager
import com.mercadona.mercastock.utils.Constants
import com.mercadona.mercastock.utils.ValidationUtils
import dagger.hilt.android.lifecycle.HiltViewModel
import dagger.hilt.android.qualifiers.ApplicationContext
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ProductFormViewModel @Inject constructor(
    private val getProductByIdUseCase: GetProductByIdUseCase,
    private val saveProductUseCase: SaveProductUseCase,
    private val savedStateHandle: SavedStateHandle,
    private val snackbarManager: SnackbarManager,
    @ApplicationContext private val context: Context,
) : ViewModel() {

    // Estados de UI
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    private val _navigateBack = MutableStateFlow(false)

    //
    private val _currentProductId = MutableStateFlow<String?>(null)

    val _uiState = MutableStateFlow<ProductFormUiState>(ProductFormUiState())
    val uiState: StateFlow<ProductFormUiState> = _uiState.asStateFlow()

    private val productId: String? =
        savedStateHandle.get<String>(NavigationDestination.ProductForm.PRODUCT_ID)

    init {
        productId?.let { id ->
            viewModelScope.launch {
                loadProduct(id)
            }
        }
    }

    fun loadProduct(productId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val result = getProductByIdUseCase(productId)
                result.onSuccess { product ->
                    _currentProductId.value = product.id
                    _uiState.value = _uiState.value.copy(
                        name = product.name,
                        price = product.price.toString(),
                        stock = product.stock,
                        category = product.category,
                        allergens = product.allergens
                    )
                }.onFailure { exception ->
                    snackbarManager.showSnackbar(
                        message = context.getString(
                            R.string.error_loading_product,
                            exception.message
                        ),
                        duration = SnackbarDuration.Long
                    )
                }
            } catch (e: Exception) {
                snackbarManager.showSnackbar(
                    message = context.getString(R.string.message_unexpected_error, e.message),
                    duration = SnackbarDuration.Long
                )
            } finally {
                _isLoading.value = false
            }
        }
    }

    fun updateName(name: String) {
        _uiState.update {
            it.copy(
                name = name
            )
        }
        validateName(name)
    }

    fun updatePrice(price: String) {
        _uiState.update {
            it.copy(
                price = price
            )
        }
        validatePrice(price)
    }

    fun updateStock(stock: Int) {
        _uiState.update {
            it.copy(
                stock = stock
            )
        }
        validateStock(stock)
    }

    fun updateCategory(category: String) {
        _uiState.update {
            it.copy(
                category = category
            )
        }
        validateCategory(category)
    }

    fun updateAllergens(allergens: List<Allergen>) {
        _uiState.update {
            it.copy(
                allergens = allergens
            )
        }
    }

    suspend fun saveProduct() {
        if (!isFormValid(
                _uiState.value.name,
                _uiState.value.price,
                _uiState.value.stock,
                _uiState.value.category
            )
        ) {
            snackbarManager.showSnackbar(
                message = context.getString(R.string.validation_form_errors),
                duration = SnackbarDuration.Long
            )
            return
        }

        _isLoading.value = true
        try {
            val product = Product(
                id = _currentProductId.value ?: "",
                name = _uiState.value.name,
                price = _uiState.value.price.toDouble(),
                stock = _uiState.value.stock,
                category = _uiState.value.category,
                allergens = _uiState.value.allergens
            )

            val result = saveProductUseCase(product)
            result.onSuccess {
                snackbarManager.showSnackbar(
                    message = context.getString(R.string.message_product_saved),
                    duration = SnackbarDuration.Long
                )
                _navigateBack.value = true
            }.onFailure { exception ->
                snackbarManager.showSnackbar(
                    message = context.getString(R.string.error_saving_product, exception.message),
                    duration = SnackbarDuration.Long
                )
                _isLoading.value = false
            }
        } catch (e: Exception) {
            snackbarManager.showSnackbar(
                message = context.getString(R.string.message_unexpected_error, e.message),
                duration = SnackbarDuration.Long
            )
            _isLoading.value = false
        } finally {
        }
    }

    private fun validateName(name: String) {
        val errorMessageId = ValidationUtils.validateName(name)
        _uiState.update {
            it.copy(
                nameError = if (errorMessageId == 0) "" else context.getString(errorMessageId)
            )
        }
    }

    private fun validatePrice(price: String) {
        val errorMessageId = ValidationUtils.validatePrice(price)
        _uiState.update {
            it.copy(
                priceError = if (errorMessageId == 0) "" else context.getString(errorMessageId)
            )
        }
    }

    private fun validateStock(stock: Int) {
        val errorMessageId = ValidationUtils.validateStock(stock)
        _uiState.update {
            it.copy(
                stockError = if (errorMessageId == 0) "" else context.getString(errorMessageId)
            )
        }
    }

    private fun validateCategory(category: String) {
        val errorMessageId = ValidationUtils.validateCategory(category)
        _uiState.update {
            it.copy(
                categoryError = if (errorMessageId == 0) "" else context.getString(errorMessageId)
            )
        }
    }

    private fun isFormValid(
        name: String,
        price: String,
        stock: Int,
        category: String,
    ): Boolean {
        val errors = ValidationUtils.validateForm(name, price, stock, category)
        return ValidationUtils.isFormValid(errors)
    }
}

data class ProductFormUiState(
    val name: String = "",
    val price: String = "",
    val stock: Int = 0,
    val category: String = "",
    val allergens: List<Allergen> = Constants.DEFAULT_ALLERGENS,
    val nameError: String = "",
    val priceError: String = "",
    val stockError: String = "",
    val categoryError: String = "",
    val isFormValid: Boolean = false,
    val navigateBack: Boolean = false,
)
