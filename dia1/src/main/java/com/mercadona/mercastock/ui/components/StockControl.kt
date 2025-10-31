package com.mercadona.mercastock.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.style.TextAlign
import com.mercadona.mercastock.dia1.R

/**
 * Componente para controlar el stock con botones +/-
 * 
 * @param stock Valor actual del stock
 * @param onStockChange Callback para cambiar el stock
 * @param stockError Mensaje de error del stock (opcional)
 */
@Composable
fun StockControl(
    stock: Int,
    onStockChange: (Int) -> Unit,
    stockError: String = ""
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_medium))
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
                onClick = { if (stock > 0) onStockChange(stock - 1) },
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
                onClick = { onStockChange(stock + 1) }
            ) {
                Text(stringResource(R.string.increment_button))
            }
        }
        
        if (stockError.isNotEmpty()) {
            Text(
                text = stockError,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}
