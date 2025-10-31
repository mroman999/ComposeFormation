package com.mercadona.mercastock.presentation.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mercadona.mercastock.domain.model.Allergen
import com.mercadona.mercastock.utils.Constants

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
                .padding(12.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
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
            
            if (allergen.id > Constants.CUSTOM_ALLERGEN_MIN_ID) {
                IconButton(
                    onClick = { onRemove(allergen.id) }
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = null,
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}

@Composable
@Preview
fun AllergenItemPreview() {
    MaterialTheme {
        AllergenItem(
            allergen = Allergen(
                id = 1,
                name = "Alérgeno 1",
                isPresent = true
            ),
            onTogglePresence = {},
            onRemove = {}
        )
    }
}

