package com.mercadona.mercastock.presentation.features.dashboard

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mercadona.mercastock.domain.model.Product
import com.mercadona.mercastock.domain.usecase.DeleteProductUseCase
import com.mercadona.mercastock.domain.usecase.GetAllProductsUseCase
import com.mercadona.mercastock.presentation.ui.components.SnackbarManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class DashboardViewModel @Inject constructor(
    private val getAllProductsUseCase: GetAllProductsUseCase,
    private val deleteProductUseCase: DeleteProductUseCase,
    private val snackbarManager: SnackbarManager,
) : ViewModel() {
    
    private val _searchQuery = MutableStateFlow("")
    val searchQuery: StateFlow<String> = _searchQuery.asStateFlow()
    
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()
    
    val products: StateFlow<List<Product>> = combine(
        getAllProductsUseCase(),
        _searchQuery
    ) { allProducts, query ->
        if (query.isBlank()) {
            allProducts
        } else {
            allProducts.filter { product ->
                product.name.contains(query, ignoreCase = true) ||
                product.category.contains(query, ignoreCase = true)
            }
        }
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5000),
        initialValue = emptyList()
    )

    init {
            viewModelScope.launch {
                loadProducts()
            }

    }
    
    fun loadProducts() {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                kotlinx.coroutines.delay(1000)
            } catch (e: Exception) {
                snackbarManager.showSnackbar(message = e.message?: "Error")
            } finally {
                _isLoading.value = false
            }
        }
    }
    
    fun updateSearchQuery(query: String) {
        _searchQuery.value = query
    }
    
    fun deleteProduct(productId: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                val result = deleteProductUseCase(productId)
                if (result.isFailure) {
                    snackbarManager.showSnackbar(message = result.exceptionOrNull()?.message?: "Error")
                }
            } catch (e: Exception) {
                snackbarManager.showSnackbar(message = e.message?: "Error")
            } finally {
                _isLoading.value = false
            }
        }
    }
}
