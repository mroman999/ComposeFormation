package com.mercadona.mercastock.utils

import android.content.Context
import com.mercadona.mercastock.dia1.R

/**
 * Utilidades para validación de formularios
 */
object ValidationUtils {
    
    /**
     * Valida el nombre del producto
     * @param value Nombre a validar
     * @param context Contexto para obtener strings
     * @return Mensaje de error o cadena vacía si es válido
     */
    fun validateName(value: String, context: Context): String {
        return when {
            value.isBlank() -> context.getString(R.string.validation_name_empty)
            value.length < 2 -> context.getString(R.string.validation_name_too_short)
            !value.first().isUpperCase() -> context.getString(R.string.validation_name_must_start_uppercase)
            else -> ""
        }
    }
    
    /**
     * Valida el precio del producto
     * @param value Precio a validar como String
     * @param context Contexto para obtener strings
     * @return Mensaje de error o cadena vacía si es válido
     */
    fun validatePrice(value: String, context: Context): String {
        return when {
            value.isBlank() -> context.getString(R.string.validation_price_empty)
            value.toDoubleOrNull() == null -> context.getString(R.string.validation_price_invalid)
            value.toDoubleOrNull()!! <= 0 -> context.getString(R.string.validation_price_must_be_positive)
            else -> ""
        }
    }
    
    /**
     * Valida el stock del producto
     * @param value Stock a validar
     * @param context Contexto para obtener strings
     * @return Mensaje de error o cadena vacía si es válido
     */
    fun validateStock(value: Int, context: Context): String {
        return if (value < 0) context.getString(R.string.validation_stock_negative) else ""
    }
    
    /**
     * Valida la categoría del producto
     * @param value Categoría a validar
     * @param context Contexto para obtener strings
     * @return Mensaje de error o cadena vacía si es válido
     */
    fun validateCategory(value: String, context: Context): String {
        return if (value.isBlank()) context.getString(R.string.validation_category_empty) else ""
    }
    
    /**
     * Valida el formulario completo
     * @param name Nombre del producto
     * @param price Precio del producto
     * @param stock Stock del producto
     * @param category Categoría del producto
     * @param context Contexto para obtener strings
     * @return Map con los errores de cada campo
     */
    fun validateForm(
        name: String,
        price: String,
        stock: Int,
        category: String,
        context: Context
    ): Map<String, String> {
        return mapOf(
            "name" to validateName(name, context),
            "price" to validatePrice(price, context),
            "stock" to validateStock(stock, context),
            "category" to validateCategory(category, context)
        )
    }
    
    /**
     * Verifica si el formulario es válido
     * @param errors Map de errores del formulario
     * @return true si no hay errores
     */
    fun isFormValid(errors: Map<String, String>): Boolean {
        return errors.values.all { it.isEmpty() }
    }
}
