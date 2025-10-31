package com.mercadona.mercastock.presentation.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.*
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mercadona.mercastock.dia4.R

@Composable
fun StockControl(
    stock: Int,
    onStockChange: (Int) -> Unit,
    stockError: String = ""
) {
    val decreaseStockDescription = stringResource(R.string.accessibility_decrease_stock)
    val increaseStockDescription = stringResource(R.string.accessibility_increase_stock)
    val currentStockDescription = stringResource(R.string.accessibility_current_stock, stock)
    
    // ✅ Descripción contextual del control completo
    val stockControlDescription = stringResource(R.string.stock_control_description, stock)
    val stockStateDescription = when {
        stock == 0 -> stringResource(R.string.stock_state_empty)
        stock < 5 -> stringResource(R.string.stock_state_low)
        else -> stringResource(R.string.stock_state_normal)
    }
    
    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp),
        modifier = Modifier
            .testTag("stock_control")
            .semantics {
                // ✅ Semántica del control completo
                contentDescription = stockControlDescription
                stateDescription = stockStateDescription
                if (stockError.isNotEmpty()) {
                    error(stockError)
                }
            }
    ) {
        Text(
            text = stringResource(R.string.form_stock_label),
            style = MaterialTheme.typography.labelMedium
        )
        
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Button(
                onClick = { if (stock > 0) onStockChange(stock - 1) },
                enabled = stock > 0,
                modifier = Modifier
                    .testTag("stock_decrement_button")
                    .semantics {
                        contentDescription = decreaseStockDescription
                        role = Role.Button
                    }
            ) {
                Text(stringResource(R.string.form_stock_decrease))
            }
            
            Text(
                text = stock.toString(),
                style = MaterialTheme.typography.headlineSmall,
                modifier = Modifier
                    .weight(1f)
                    .testTag("stock_display")
                    .semantics {
                        contentDescription = currentStockDescription
                    },
                textAlign = TextAlign.Center
            )
            
            Button(
                onClick = { onStockChange(stock + 1) },
                modifier = Modifier
                    .testTag("stock_increment_button")
                    .semantics {
                        contentDescription = increaseStockDescription
                        role = Role.Button
                    }
            ) {
                Text(stringResource(R.string.form_stock_increase))
            }
        }
        
        if (stockError.isNotEmpty()) {
            Text(
                text = stockError,
                color = MaterialTheme.colorScheme.error,
                style = MaterialTheme.typography.bodySmall,
                modifier = Modifier
                    .testTag("stock_error")
                    .semantics {
                        // ✅ Semántica del mensaje de error
                        contentDescription = "Error en stock: $stockError"
                        liveRegion = LiveRegionMode.Assertive
                    }
            )
        }
    }
}

@Composable
@Preview
fun StockControlPreview() {
    MaterialTheme {
        Surface {
            StockControl(
                stock = 10,
                onStockChange = {},
                stockError = ""
            )
        }
    }
}
