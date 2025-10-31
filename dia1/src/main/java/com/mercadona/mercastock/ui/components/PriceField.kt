package com.mercadona.mercastock.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.mercadona.mercastock.dia1.R

/**
 * Componente para el campo de precio del producto
 * 
 * @param price Valor actual del precio
 * @param onPriceChange Callback para cambiar el precio
 * @param priceError Mensaje de error del precio (opcional)
 */
@Composable
fun PriceField(
    price: String,
    onPriceChange: (String) -> Unit,
    priceError: String = ""
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
        modifier = Modifier.fillMaxWidth()
    )
}
