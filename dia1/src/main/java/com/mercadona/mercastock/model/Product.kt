package com.mercadona.mercastock.model

/**
 * Modelo de datos para un producto
 * 
 * @param id Identificador único del producto
 * @param name Nombre del producto
 * @param price Precio del producto en euros
 * @param stock Cantidad disponible en stock
 * @param category Categoría del producto (Alimentación, Bebidas, etc.)
 * @param allergens Lista de alérgenos presentes en el producto
 */
data class Product(
    val id: Int,
    val name: String,
    val price: Double,
    val stock: Int,
    val category: String,
    val allergens: List<Allergen>
)
