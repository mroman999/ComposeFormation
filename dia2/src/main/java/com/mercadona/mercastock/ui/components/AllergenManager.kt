package com.mercadona.mercastock.ui.components

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import com.mercadona.mercastock.dia2.R
import com.mercadona.mercastock.model.Allergen
import com.mercadona.mercastock.utils.Constants

/**
 * Componente para gestionar la lista de alérgenos
 * Incluye la lista de alérgenos y el input para añadir nuevos
 *
 * @param allergens Lista actual de alérgenos
 * @param onAllergensChange Callback para cambiar la lista de alérgenos
 */
@Composable
fun AllergenManager(
    allergens: List<Allergen>,
    onAllergensChange: (List<Allergen>) -> Unit,
) {
    var newAllergenText by remember { mutableStateOf("") }

    Column(
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_extra_large))
    ) {
        // Título de la sección
        Text(
            text = stringResource(R.string.allergens_section_title),
            style = MaterialTheme.typography.titleMedium
        )

        // Input para nuevo alérgeno
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_medium))
        ) {
            OutlinedTextField(
                value = newAllergenText,
                onValueChange = { newAllergenText = it },
                label = { Text(stringResource(R.string.new_allergen_field_label)) },
                placeholder = { Text(stringResource(R.string.new_allergen_field_placeholder)) },
                modifier = Modifier.weight(1f)
            )
            Button(
                onClick = {
                    if (newAllergenText.isNotBlank()) {
                        val newAllergen = Allergen(
                            id = allergens.size + 1,
                            name = newAllergenText,
                            isPresent = false
                        )
                        onAllergensChange(allergens + newAllergen)
                        newAllergenText = ""
                    }
                }
            ) {
                Text(stringResource(R.string.add_allergen_button))
            }
        }

        allergens.forEach { allergen ->
            AllergenItem(
                allergen = allergen,
                onTogglePresence = { allergenId ->
                    val updatedAllergens = allergens.map {
                        if (it.id == allergenId) it.copy(isPresent = !it.isPresent)
                        else it
                    }
                    onAllergensChange(updatedAllergens)
                },
                onRemove = { allergenId ->
                    if (allergenId > Constants.CUSTOM_ALLERGEN_MIN_ID) {
                        val updatedAllergens = allergens.filter { it.id != allergenId }
                        onAllergensChange(updatedAllergens)
                    }
                }
            )
        }
    }
}
