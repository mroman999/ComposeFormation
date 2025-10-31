package com.mercadona.mercastock.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import com.mercadona.mercastock.dia1.R
import com.mercadona.mercastock.model.Product

/**
 * Componente para mostrar un producto individual en la lista
 * 
 * @param product Producto a mostrar
 */
@Composable
fun ProductItem(product: Product) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(dimensionResource(R.dimen.card_padding))
        ) {
            Text(
                text = product.name,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = "Precio: ${product.price}€ | Stock: ${product.stock} | Categoría: ${product.category}",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
            if (product.allergens.isNotEmpty()) {
                Text(
                    text = "Alérgenos: ${product.allergens.joinToString(", ") { it.name }}",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.padding(top = dimensionResource(R.dimen.spacing_small))
                )
            }
        }
    }
}
