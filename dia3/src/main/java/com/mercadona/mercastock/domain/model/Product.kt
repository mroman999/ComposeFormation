package com.mercadona.mercastock.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Product(
    val id: String = "",
    val name: String = "",
    val price: Double = 0.0,
    val stock: Int = 0,
    val category: String = "",
    val allergens: List<Allergen> = emptyList()
) : Parcelable {
    fun getTotalStockValue(): Double = price * stock
    
    fun isLowStock(threshold: Int = 5): Boolean = stock <= threshold
    
    fun hasAllergens(): Boolean = allergens.any { it.isPresent }
    
    fun getPresentAllergens(): List<Allergen> = allergens.filter { it.isPresent }
    
    fun isValid(): Boolean {
        return name.isNotBlank() && 
               price > 0 && 
               stock >= 0 && 
               category.isNotBlank()
    }
}
