package com.mercadona.mercastock.presentation.ui.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import com.mercadona.mercastock.dia3.R

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
        label = { Text(stringResource(R.string.form_price_label)) },
        isError = priceError.isNotEmpty(),
        supportingText = {
            if (priceError.isNotEmpty()) {
                Text(
                    text = priceError,
                    color = MaterialTheme.colorScheme.error
                )
            } else {
                Text(stringResource(R.string.form_price_hint))
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

@Composable
@Preview
fun PriceFieldPreview() {
    MaterialTheme {
        Surface {
            PriceField(
                price = "12.34",
                onPriceChange = {},
                priceError = "",
                focusRequester = null,
                onNext = null
            )
        }
    }
}
