package com.mercadona.mercastock.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import com.mercadona.mercastock.dia1.R
import com.mercadona.mercastock.model.Allergen
import com.mercadona.mercastock.utils.Constants

/**
 * Componente para mostrar un alérgeno individual
 * 
 * @param allergen Alérgeno a mostrar
 * @param onTogglePresence Callback para cambiar el estado de presencia
 * @param onRemove Callback para eliminar el alérgeno (solo personalizados)
 */
@Composable
fun AllergenItem(
    allergen: Allergen,
    onTogglePresence: (Int) -> Unit,
    onRemove: (Int) -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (allergen.isPresent) 
                MaterialTheme.colorScheme.errorContainer 
            else 
                MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(dimensionResource(R.dimen.allergen_item_padding)),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_large))
        ) {
            Checkbox(
                checked = allergen.isPresent,
                onCheckedChange = { onTogglePresence(allergen.id) }
            )
            
            Text(
                text = allergen.name,
                modifier = Modifier.weight(1f),
                style = MaterialTheme.typography.bodyMedium,
                color = if (allergen.isPresent) 
                    MaterialTheme.colorScheme.onErrorContainer 
                else 
                    MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            // Botón para eliminar alérgenos personalizados (id > CUSTOM_ALLERGEN_MIN_ID)
            if (allergen.id > Constants.CUSTOM_ALLERGEN_MIN_ID) {
                IconButton(
                    onClick = { onRemove(allergen.id) }
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = stringResource(R.string.cd_delete_allergen),
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}
