package com.mercadona.mercastock.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import com.mercadona.mercastock.dia2.R

/**
 * Componente para el campo de nombre del producto
 * 
 * @param name Valor actual del nombre
 * @param onNameChange Callback para cambiar el nombre
 * @param nameError Mensaje de error del nombre (opcional)
 * @param focusRequester FocusRequester para gestión de focus
 * @param onNext Callback para navegar al siguiente campo
 */
@Composable
fun NameField(
    name: String,
    onNameChange: (String) -> Unit,
    nameError: String = "",
    focusRequester: FocusRequester? = null,
    onNext: (() -> Unit)? = null
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
        keyboardOptions = KeyboardOptions(
            imeAction = if (onNext != null) ImeAction.Next else ImeAction.Default
        ),
        keyboardActions = KeyboardActions(
            onNext = { onNext?.invoke() }
        ),
        modifier = Modifier
            .fillMaxWidth()
            .let { modifier ->
                if (focusRequester != null) {
                    modifier.focusRequester(focusRequester)
                } else {
                    modifier
                }
            }
    )
}
