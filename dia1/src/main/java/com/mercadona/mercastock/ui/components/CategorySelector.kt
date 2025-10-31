package com.mercadona.mercastock.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.mercadona.mercastock.dia1.R
import com.mercadona.mercastock.utils.Constants

/**
 * Componente para seleccionar la categoría del producto
 * 
 * @param category Categoría seleccionada actualmente
 * @param onCategoryChange Callback para cambiar la categoría
 * @param categoryError Mensaje de error de la categoría (opcional)
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategorySelector(
    category: String,
    onCategoryChange: (String) -> Unit,
    categoryError: String = ""
) {
    var expanded by remember { mutableStateOf(false) }
    val categories = Constants.PRODUCT_CATEGORIES
    
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
    ) {
        OutlinedTextField(
            value = category,
            onValueChange = {},
            readOnly = true,
            label = { Text(stringResource(R.string.product_category_field_label)) },
            isError = categoryError.isNotEmpty(),
            supportingText = {
                if (categoryError.isNotEmpty()) {
                    Text(
                        text = categoryError,
                        color = MaterialTheme.colorScheme.error
                    )
                }
            },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier
                .menuAnchor(MenuAnchorType.PrimaryNotEditable)
                .fillMaxWidth()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false }
        ) {
            categories.forEach { categoryOption ->
                DropdownMenuItem(
                    text = { Text(categoryOption) },
                    onClick = {
                        onCategoryChange(categoryOption)
                        expanded = false
                    }
                )
            }
        }
    }
}
