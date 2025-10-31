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
import androidx.compose.ui.text.input.KeyboardType
import com.mercadona.mercastock.dia2.R

/**
 * Componente para el campo de precio del producto
 * 
 * @param price Valor actual del precio
 * @param onPriceChange Callback para cambiar el precio
 * @param priceError Mensaje de error del precio (opcional)
 * @param focusRequester FocusRequester para gestión de focus
 * @param onNext Callback para navegar al siguiente campo
 */
@Composable
fun PriceField(
    price: String,
    onPriceChange: (String) -> Unit,
    priceError: String = "",
    focusRequester: FocusRequester? = null,
    onNext: (() -> Unit)? = null
) {
    OutlinedTextField(
        value = price,
        onValueChange = { newPrice ->
            onPriceChange(newPrice)
        },
        label = { Text(stringResource(R.string.product_price_field_label)) },
        isError = priceError.isNotEmpty(),
        supportingText = {
            if (priceError.isNotEmpty()) {
                Text(
                    text = priceError,
                    color = MaterialTheme.colorScheme.error
                )
            } else {
                Text(stringResource(R.string.product_price_field_helper))
            }
        },
        keyboardOptions = KeyboardOptions(
            keyboardType = KeyboardType.Decimal,
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
