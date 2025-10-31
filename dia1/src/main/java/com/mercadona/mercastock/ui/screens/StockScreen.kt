package com.mercadona.mercastock.ui.screens

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.mercadona.mercastock.dia1.R

@Composable
fun StockScreen() {
    var stock by remember { mutableIntStateOf(0) }
    var stockInt = 0

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = stringResource(R.string.stock_field_label),
            style = MaterialTheme.typography.labelMedium
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_extra_large)),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = {
                    if (stock > 0) {
                        stockInt--
                        stock--
                        Log.d("Stock", "Stock disminuido: $stockInt")
                    }
                },
                enabled = stock > 0
            ) {
                Text(stringResource(R.string.decrement_button))
            }

            Text(
                text = stock.toString(),
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier.weight(1f),
                textAlign = TextAlign.Center
            )

            Button(
                onClick = {
                    stock++
                    stockInt++
                    Log.d("Stock", "Stock aumentado: $stockInt")
                }
            ) {
                Text(stringResource(R.string.increment_button))
            }
        }
    }
}