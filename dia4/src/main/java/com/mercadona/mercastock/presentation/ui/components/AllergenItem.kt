package com.mercadona.mercastock.presentation.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.mercadona.mercastock.dia4.R
import com.mercadona.mercastock.domain.model.Allergen
import com.mercadona.mercastock.utils.Constants

@Composable
fun AllergenItem(
    allergen: Allergen,
    onTogglePresence: (Int) -> Unit,
    onRemove: (Int) -> Unit
) {
    // ✅ Strings de accesibilidad contextuales
    val allergenPresentDescription = stringResource(R.string.allergen_present_description, allergen.name)
    val allergenAbsentDescription = stringResource(R.string.allergen_absent_description, allergen.name)
    val removeAllergenDescription = stringResource(R.string.allergen_remove_description, allergen.name)
    val allergenStateDescription = if (allergen.isPresent) {
        stringResource(R.string.allergen_state_present)
    } else {
        stringResource(R.string.allergen_state_absent)
    }

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("allergen_item_${allergen.id}")
            .semantics(mergeDescendants = true) {
                // ✅ Semántica del contenedor: agrupa toda la información del alérgeno
                contentDescription = if (allergen.isPresent) allergenPresentDescription else allergenAbsentDescription
                stateDescription = allergenStateDescription
                role = Role.Button // El card completo actúa como un elemento interactivo
            },
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
                onCheckedChange = { onTogglePresence(allergen.id) },
                modifier = Modifier
                    .testTag("allergen_checkbox_${allergen.id}")
                    .semantics {
                        // ✅ Semántica específica del checkbox
                        contentDescription = if (allergen.isPresent) {
                            "Marcar ${allergen.name} como presente"
                        } else {
                            "Marcar ${allergen.name} como no presente"
                        }
                    }
            )
            
            Text(
                text = allergen.name,
                modifier = Modifier
                    .weight(1f),
                style = MaterialTheme.typography.bodyMedium,
                color = if (allergen.isPresent) 
                    MaterialTheme.colorScheme.onErrorContainer 
                else 
                    MaterialTheme.colorScheme.onSurfaceVariant
            )
            
            if (allergen.id > Constants.CUSTOM_ALLERGEN_MIN_ID) {
                IconButton(
                    onClick = { onRemove(allergen.id) },
                    modifier = Modifier
                        .testTag("allergen_remove_${allergen.id}")
                        .semantics {
                            // ✅ Semántica específica del botón de eliminar
                            contentDescription = removeAllergenDescription
                            role = Role.Button
                        }
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = null, // ✅ null porque el IconButton ya tiene descripción
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

