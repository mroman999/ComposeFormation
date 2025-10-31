package com.mercadona.mercastock.ui.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mercadona.mercastock.model.Allergen
import com.mercadona.mercastock.model.Product
import com.mercadona.mercastock.utils.Constants
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

/**
 * ViewModel para el formulario de productos
 * Gestiona el estado del formulario y los alérgenos
 */
@HiltViewModel
class ProductFormViewModel @Inject constructor() : ViewModel() {

    // Estado de los alérgenos gestionado por ViewModel
    private val _allergens = MutableStateFlow(Constants.DEFAULT_ALLERGENS)
    val allergens: StateFlow<List<Allergen>> = _allergens.asStateFlow()

    // Estado de productos guardados
    private val _savedProducts = MutableStateFlow<List<Product>>(emptyList())
    val savedProducts: StateFlow<List<Product>> = _savedProducts.asStateFlow()

    // Estado de carga para simular operaciones asíncronas
    private val _isLoading = MutableStateFlow(false)
    val isLoading: StateFlow<Boolean> = _isLoading.asStateFlow()

    // Estado para mostrar mensajes
    private val _message = MutableStateFlow("")
    val message: StateFlow<String> = _message.asStateFlow()

    // Contador para generar IDs únicos
    private var nextProductId = 1

    init {
        // carga de Datos
    }

    /**
     * Actualiza la lista de alérgenos
     */
    fun updateAllergens(newAllergens: List<Allergen>) {
        _allergens.value = newAllergens
    }

    /**
     * Añade un nuevo alérgeno personalizado
     */
    fun addCustomAllergen(name: String) {
        val currentAllergens = _allergens.value
        val newId = (currentAllergens.maxOfOrNull { it.id } ?: 0) + 1
        val newAllergen = Allergen(newId, name, false)
        _allergens.value = currentAllergens + newAllergen
    }

    /**
     * Elimina un alérgeno (solo los personalizados)
     */
    fun removeAllergen(allergenId: Int) {
        if (allergenId > Constants.CUSTOM_ALLERGEN_MIN_ID) {
            _allergens.value = _allergens.value.filter { it.id != allergenId }
        }
    }

    /**
     * Guarda un producto (simula operación asíncrona)
     */
    fun saveProduct(
        name: String,
        price: String,
        stock: Int,
        category: String,
        allergens: List<Allergen>
    ) {
        viewModelScope.launch {
            _isLoading.value = true
            _message.value = "Guardando producto..."

            // Simula operación de red
            delay(2000)

            val product = Product(
                id = nextProductId++,
                name = name,
                price = price.toDoubleOrNull() ?: 0.0,
                stock = stock,
                category = category,
                allergens = allergens.filter { it.isPresent }
            )

            _savedProducts.value = _savedProducts.value + product
            _isLoading.value = false
            _message.value = "Producto guardado correctamente"

            // Limpiar mensaje después de un tiempo
            delay(3000)
            _message.value = ""
        }
    }

    /**
     * Resetea los alérgenos a su estado inicial
     */
    fun resetAllergens() {
        _allergens.value = Constants.DEFAULT_ALLERGENS
    }

    /**
     * Simula carga de alérgenos desde una fuente externa
     */
    fun loadAllergensFromServer() {
        viewModelScope.launch {
            _isLoading.value = true
            _message.value = "Cargando alérgenos..."

            // Simula carga desde servidor
            delay(1500)

            val serverAllergens = listOf(
                Allergen(7, "Mostaza", false),
                Allergen(8, "Apio", false),
                Allergen(9, "Sulfitos", false)
            )

            _allergens.value = Constants.DEFAULT_ALLERGENS + serverAllergens
            _isLoading.value = false
            _message.value = "Alérgenos actualizados"

            // Limpiar mensaje
            delay(2000)
            _message.value = ""
        }
    }

    /**
     * Limpia el mensaje actual
     */
    fun clearMessage() {
        _message.value = ""
    }
}
