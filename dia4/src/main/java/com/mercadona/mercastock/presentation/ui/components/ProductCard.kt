package com.mercadona.mercastock.presentation.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.*
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mercadona.mercastock.dia4.R
import com.mercadona.mercastock.domain.model.Allergen
import com.mercadona.mercastock.domain.model.Product


@Composable
fun ProductCard(
    product: Product,
    onEdit: () -> Unit,
    onDelete: () -> Unit
) {
    // ✅ Descripciones de accesibilidad contextuales y detalladas
    val contentDescription = when {
        product.hasAllergens() && product.isLowStock() -> {
            stringResource(
                R.string.accessibility_product_card_with_allergens_low_stock,
                product.name,
                product.category,
                product.price.toString(),
                product.stock,
                product.getPresentAllergens().joinToString { it.name }
            )
        }
        product.hasAllergens() -> {
            stringResource(
                R.string.accessibility_product_card_with_allergens,
                product.name,
                product.category,
                product.price.toString(),
                product.stock,
                product.getPresentAllergens().joinToString { it.name }
            )
        }
        product.isLowStock() -> {
            stringResource(
                R.string.accessibility_product_card_low_stock,
                product.name,
                product.category,
                product.price.toString(),
                product.stock
            )
        }
        else -> {
            stringResource(
                R.string.accessibility_product_card,
                product.name,
                product.category,
                product.price.toString(),
                product.stock
            )
        }
    }
    
    // ✅ Estado dinámico para TalkBack
    val productStateDescription = when {
        product.isLowStock() -> stringResource(R.string.product_state_low_stock)
        product.stock == 0 -> stringResource(R.string.product_state_out_of_stock)
        else -> stringResource(R.string.product_state_available)
    }
    
    val editButtonDescription = stringResource(R.string.accessibility_edit_product, product.name)
    val deleteButtonDescription = stringResource(R.string.accessibility_delete_product, product.name)
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("product_card_${product.id}")
            .semantics(mergeDescendants = true) {
                // ✅ Semántica completa del producto
                this.contentDescription = contentDescription
                this.stateDescription = productStateDescription
                role = Role.Button
                
                // ✅ Información adicional para lectores de pantalla
                if (product.hasAllergens()) {
                    // Marcar como contenido importante por alérgenos
                    liveRegion = LiveRegionMode.Polite
                }
            }
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = product.name,
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = product.category,
                        style = MaterialTheme.typography.bodyMedium,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Text(
                        text = "€${product.price} • Stock: ${product.stock}",
                        style = MaterialTheme.typography.bodySmall
                    )

                    if (product.hasAllergens()) {
                        Text(
                            text = "Alérgenos: ${product.getPresentAllergens().joinToString { it.name }}",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.error
                        )
                    }
                }

                Row {
                    IconButton(
                        onClick = onEdit,
                        modifier = Modifier
                            .testTag("edit_product_${product.id}")
                            .semantics {
                                // ✅ Semántica específica del botón editar
                                this.contentDescription = editButtonDescription
                                role = Role.Button
                            }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = null // ✅ null porque IconButton ya tiene descripción
                        )
                    }
                    IconButton(
                        onClick = onDelete,
                        modifier = Modifier
                            .testTag("delete_product_${product.id}")
                            .semantics {
                                // ✅ Semántica específica del botón eliminar
                                this.contentDescription = deleteButtonDescription
                                role = Role.Button
                                // ✅ Marcar como acción destructiva
                                liveRegion = LiveRegionMode.Assertive
                            }
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = null, // ✅ null porque IconButton ya tiene descripción
                            tint = MaterialTheme.colorScheme.error
                        )
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun ProductCardPreview() {
    MaterialTheme {
        ProductCard(
            product = Product(
                id = "1",
                name = "Leche Entera",
                price = 1.25,
                stock = 50,
                category = "Lácteos",
                allergens = listOf(Allergen(1,"Lactosa", true))
            ),
            onEdit = {},
            onDelete = {}
        )
    }
}