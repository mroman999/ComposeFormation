package com.mercadona.mercastock.presentation.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mercadona.mercastock.dia4.R
import com.mercadona.mercastock.presentation.ui.model.ProductStatisticsItem

@Composable
fun StatisticsCard(statistics: ProductStatisticsItem) {
    // ✅ Descripción contextual de las estadísticas
    val statisticsDescription = stringResource(
        R.string.statistics_card_description,
        statistics.totalProducts,
        statistics.totalValue,
        statistics.lowStockCount
    )
    
    val statisticsStateDescription = if (statistics.lowStockCount > 0) {
        stringResource(R.string.statistics_state_warning)
    } else {
        stringResource(R.string.statistics_state_normal)
    }
    
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("statistics_card")
            .semantics(mergeDescendants = true) {
                // ✅ Semántica del resumen estadístico
                contentDescription = statisticsDescription
                stateDescription = statisticsStateDescription
                role = Role.Image // Actúa como información visual
                
                if (statistics.lowStockCount > 0) {
                    // ✅ Marcar como información importante
                    liveRegion = LiveRegionMode.Polite
                }
            }
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = stringResource(R.string.dashboard_title),
                style = MaterialTheme.typography.titleMedium,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                StatisticItem(
                    label = stringResource(R.string.dashboard_total_products),
                    value = statistics.totalProducts.toString(),
                    testTag = "total_products_stat"
                )
                StatisticItem(
                    label = stringResource(R.string.dashboard_total_value),
                    value = "€${String.format("%.2f", statistics.totalValue)}",
                    testTag = "total_value_stat"
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                StatisticItem(
                    label = stringResource(R.string.dashboard_low_stock),
                    value = statistics.lowStockCount.toString(),
                    isWarning = statistics.lowStockCount > 0,
                    testTag = "low_stock_stat"
                )
                StatisticItem(
                    label = "Precio Promedio",
                    value = "€${String.format("%.2f", statistics.averagePrice)}",
                    testTag = "average_price_stat"
                )
            }
        }
    }
}


@Composable
fun StatisticItem(
    label: String,
    value: String,
    isWarning: Boolean = false,
    testTag: String = ""
) {
    // ✅ Descripción contextual de cada estadística
    val itemDescription = if (isWarning) {
        stringResource(R.string.statistic_item_warning_description, label, value)
    } else {
        stringResource(R.string.statistic_item_description, label, value)
    }
    
    Column(
        modifier = Modifier
            .testTag(testTag)
            .semantics(mergeDescendants = true) {
                // ✅ Semántica de cada elemento estadístico
                contentDescription = itemDescription
                if (isWarning) {
                    stateDescription = "Requiere atención"
                    liveRegion = LiveRegionMode.Polite
                }
            }
    ) {
        Text(
            text = label,
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
        Row(
            verticalAlignment = Alignment.CenterVertically
        ) {
            if (isWarning) {
                Icon(
                    imageVector = Icons.Default.Warning,
                    contentDescription = null, // ✅ null porque está incluido en mergeDescendants
                    tint = MaterialTheme.colorScheme.error,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
            }
            Text(
                text = value,
                style = MaterialTheme.typography.titleMedium,
                color = if (isWarning) MaterialTheme.colorScheme.error else MaterialTheme.colorScheme.onSurface
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun StatisticsCardPreview() {
    MaterialTheme {
        StatisticsCard(
            statistics = ProductStatisticsItem(
                totalProducts = 15,
                totalValue = 1250.75,
                lowStockCount = 3,
                averagePrice = 83.38
            )
        )
    }
}