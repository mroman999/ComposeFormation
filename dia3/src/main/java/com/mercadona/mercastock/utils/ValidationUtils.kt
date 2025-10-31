package com.mercadona.mercastock.utils

import com.mercadona.mercastock.dia3.R

object ValidationUtils {

    const val NAME = "name"
    const val PRICE = "price"
    const val STOCK = "stock"
    const val CATEGORY = "category"
    
    fun validateName(value: String): Int {
        return when {
            value.isBlank() -> R.string.validation_name_required
            value.length < 2 -> R.string.validation_name_min_length
            !value.first().isUpperCase() -> R.string.validation_name_capitalize
            else -> 0
        }
    }
    
    fun validatePrice(value: String): Int {
        return when {
            value.isBlank() -> R.string.validation_price_required
            value.toDoubleOrNull() == null -> R.string.validation_price_invalid
            value.toDoubleOrNull()!! <= 0 -> R.string.validation_price_positive
            else -> 0
        }
    }
    
    fun validateStock(value: Int): Int {
        return if (value < 0) R.string.validation_stock_negative else 0
    }
    
    fun validateCategory(value: String): Int {
        return if (value.isBlank()) R.string.validation_category_required else 0
    }
    
    fun validateForm(
        name: String,
        price: String,
        stock: Int,
        category: String,
    ): Map<String, Int> {
        return mapOf(
            NAME to validateName(name),
            PRICE to validatePrice(price),
            STOCK to validateStock(stock),
            CATEGORY to validateCategory(category)
        )
    }
    
    fun isFormValid(errors: Map<String, Int>): Boolean {
        return errors.values.all { it == 0 }
    }
}
