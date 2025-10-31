package com.mercadona.mercastock.utils

import com.mercadona.mercastock.model.Allergen

/**
 * Constantes de la aplicación
 */
object Constants {
    
    /**
     * Categorías disponibles para los productos
     */
    val PRODUCT_CATEGORIES = listOf(
        "Alimentación",
        "Bebidas", 
        "Limpieza",
        "Higiene Personal",
        "Hogar",
        "Mascotas"
    )
    
    /**
     * Alérgenos predefinidos más comunes
     */
    val DEFAULT_ALLERGENS = listOf(
        Allergen(1, "Gluten", false),
        Allergen(2, "Lactosa", false),
        Allergen(3, "Frutos secos", false),
        Allergen(4, "Huevos", false),
        Allergen(5, "Pescado", false),
        Allergen(6, "Soja", false)
    )
    
    /**
     * ID mínimo para alérgenos personalizados
     * Los alérgenos con ID mayor a este valor pueden ser eliminados
     */
    const val CUSTOM_ALLERGEN_MIN_ID = 6
}
