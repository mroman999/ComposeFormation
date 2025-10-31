package com.mercadona.mercastock.presentation.ui.components

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.ui.res.stringResource
import com.mercadona.mercastock.dia4.R
import com.mercadona.mercastock.domain.model.Allergen
import androidx.compose.foundation.lazy.items
import com.mercadona.mercastock.utils.Constants

@Composable
fun AllergenManager(
    allergens: List<Allergen>,
    onAllergensChange: (List<Allergen>) -> Unit,
) {
    var newAllergenText by remember { mutableStateOf("") }

    Column(
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        Text(
            text = stringResource(R.string.allergens_title),
            style = MaterialTheme.typography.titleMedium
        )

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            OutlinedTextField(
                value = newAllergenText,
                onValueChange = { newAllergenText = it },
                label = { Text(stringResource(R.string.allergen_new_label)) },
                placeholder = { Text(stringResource(R.string.allergen_new_placeholder)) },
                modifier = Modifier.weight(1f)
            )
            Button(
                onClick = {
                    if (newAllergenText.isNotBlank()) {
                        val newAllergen = Allergen(
                            id = (allergens.maxOfOrNull { it.id } ?: 0) + 1,
                            name = newAllergenText,
                            isPresent = false
                        )
                        onAllergensChange(allergens + newAllergen)
                        newAllergenText = ""
                    }
                }
            ) {
                Text(stringResource(R.string.allergen_add_button))
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
