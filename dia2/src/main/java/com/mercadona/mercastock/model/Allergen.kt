package com.mercadona.mercastock.model

/**
 * Modelo de datos para un alérgeno
 * 
 * @param id Identificador único del alérgeno
 * @param name Nombre del alérgeno (ej: "Gluten", "Lactosa")
 * @param isPresent Indica si el alérgeno está presente en el producto
 */
data class Allergen(
    val id: Int,
    val name: String,
    val isPresent: Boolean
)
