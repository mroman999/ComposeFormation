package com.mercadona.mercastock.presentation.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.mercadona.mercastock.dia3.R
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
    
    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = !expanded }
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
            onDismissRequest = { expanded = false }
        ) {
            categories.forEach { categoryRes ->
                val categoryText = stringResource(categoryRes)
                DropdownMenuItem(
                    text = { Text(categoryText) },
                    onClick = {
                        onCategoryChange(categoryText)
                        expanded = false
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