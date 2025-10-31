package com.mercadona.mercastock.model

/**
 * Modelo de datos para un producto
 * 
 * @param id Identificador único del producto
 * @param name Nombre del producto
 * @param price Precio del producto
 * @param stock Cantidad en stock
 * @param category Categoría del producto
 * @param allergens Lista de alérgenos del producto
 */
data class Product(
    val id: Int = 0,
    val name: String = "",
    val price: Double = 0.0,
    val stock: Int = 0,
    val category: String = "",
    val allergens: List<Allergen> = emptyList()
)
