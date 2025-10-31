package com.mercadona.mercastock.utils

import com.mercadona.mercastock.dia4.R
import com.mercadona.mercastock.domain.model.Allergen

object Constants {
    
    val PRODUCT_CATEGORIES = listOf(
        R.string.category_food,
        R.string.category_drinks,
        R.string.category_cleaning,
        R.string.category_personal_hygiene,
        R.string.category_home,
        R.string.category_pets
    )
    
    val DEFAULT_ALLERGENS = listOf(
        Allergen(1, "Gluten", false),
        Allergen(2, "Lactosa", false),
        Allergen(3, "Frutos secos", false),
        Allergen(4, "Huevos", false),
        Allergen(5, "Pescado", false),
        Allergen(6, "Soja", false)
    )
    
    const val CUSTOM_ALLERGEN_MIN_ID = 6
}
