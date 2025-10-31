package com.mercadona.mercastock.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.mercadona.mercastock.dia1.R

/**
 * Componente para el campo de nombre del producto
 * 
 * @param name Valor actual del nombre
 * @param onNameChange Callback para cambiar el nombre
 * @param nameError Mensaje de error del nombre (opcional)
 */
@Composable
fun NameField(
    name: String,
    onNameChange: (String) -> Unit,
    nameError: String = ""
) {
    OutlinedTextField(
        value = name,
        onValueChange = { newName ->
            onNameChange(newName)
        },
        label = { Text(stringResource(R.string.product_name_field_label)) },
        isError = nameError.isNotEmpty(),
        supportingText = {
            if (nameError.isNotEmpty()) {
                Text(
                    text = nameError,
                    color = MaterialTheme.colorScheme.error
                )
            } else {
                Text(stringResource(R.string.product_name_field_helper))
            }
        },
        modifier = Modifier.fillMaxWidth()
    )
}
