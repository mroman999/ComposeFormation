package com.mercadona.mercastock.presentation.ui.components

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.*
import androidx.compose.ui.tooling.preview.Preview
import com.mercadona.mercastock.dia4.R

@Composable
fun SearchBar(
    query: String,
    hint: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    // ✅ Descripciones de accesibilidad contextuales
    val searchBarDescription = stringResource(R.string.search_bar_description)
    val searchStateDescription = if (query.isNotEmpty()) {
        stringResource(R.string.search_state_active, query)
    } else {
        stringResource(R.string.search_state_empty)
    }
    
    OutlinedTextField(
        value = query,
        onValueChange = onQueryChange,
        label = { Text(hint) },
        leadingIcon = {
            Icon(
                imageVector = Icons.Default.Search,
                contentDescription = null // ✅ null porque el TextField ya tiene descripción
            )
        },
        modifier = modifier
            .testTag("search_bar")
            .semantics {
                // ✅ Semántica del campo de búsqueda
                contentDescription = searchBarDescription
                stateDescription = searchStateDescription
                role = Role.Button
                
                // ✅ Información adicional para lectores de pantalla
                if (query.isNotEmpty()) {
                    liveRegion = LiveRegionMode.Polite
                }
            }
    )
}

@Composable
@Preview
fun SearchBarPreview() {
    MaterialTheme {
        Surface {
            SearchBar(
                query = "",
                hint = "Buscar",
                onQueryChange = {}
            )
        }
    }
}