package com.mercadona.mercastock.presentation.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.*
import androidx.compose.ui.tooling.preview.Preview
import com.mercadona.mercastock.dia4.R
import com.mercadona.mercastock.utils.Constants

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CategorySelector(
    category: String,
    onCategoryChange: (String) -> Unit,
    categoryError: String = "",
    focusRequester: FocusRequester? = null,
) {
    var expanded by remember { mutableStateOf(false) }
    val categories = Constants.PRODUCT_CATEGORIES
    
    // ✅ Strings de accesibilidad contextuales
    val categorySelectorDescription = stringResource(R.string.category_selector_description)
    val categorySelectedDescription = if (category.isNotEmpty()) {
        stringResource(R.string.category_selected_description, category)
    } else {
        stringResource(R.string.category_not_selected_description)
    }
    val categoryStateDescription = if (expanded) {
        stringResource(R.string.category_state_expanded)
    } else {
        stringResource(R.string.category_state_collapsed)
    }
    
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded },
        modifier = Modifier.semantics {
            // ✅ Semántica del contenedor dropdown
            contentDescription = categorySelectorDescription
            stateDescription = categoryStateDescription
            role = Role.DropdownList
        }
    ) {
        OutlinedTextField(
            value = category,
            onValueChange = {},
            readOnly = true,
            label = { Text(stringResource(R.string.form_category_label)) },
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
                .testTag("category_selector")
                .semantics {
                    // ✅ Semántica específica del campo
                    contentDescription = categorySelectedDescription
                    if (categoryError.isNotEmpty()) {
                        error(categoryError)
                    }
                }
                .let { modifier ->
                    if (focusRequester != null) {
                        modifier.focusRequester(focusRequester)
                    } else {
                        modifier
                    }
                }
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
            modifier = Modifier
                .testTag("category_dropdown_menu")
                .semantics {
                    // ✅ Semántica del menú desplegable
                    contentDescription = "Lista de categorías disponibles"
                }
        ) {
            categories.forEach { categoryRes ->
                val categoryText = stringResource(categoryRes)
                DropdownMenuItem(
                    text = { Text(categoryText) },
                    onClick = {
                        onCategoryChange(categoryText)
                        expanded = false
                    },
                    modifier = Modifier
                        .testTag("category_option_$categoryText")
                        .semantics {
                            // ✅ Semántica de cada opción
                            contentDescription = "Seleccionar categoría $categoryText"
                            role = Role.Button
                        }
                )
            }
        }
    }
}

@Composable
@Preview
fun CategorySelectorPreview() {
    MaterialTheme {
        Surface {
            CategorySelector(
                category = "Categoría 1",
                onCategoryChange = {},
                categoryError = "",
                focusRequester = null
            )
        }
    }
}