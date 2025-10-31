package com.mercadona.mercastock.presentation.features.product

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mercadona.mercastock.dia4.R
import com.mercadona.mercastock.domain.model.Allergen
import com.mercadona.mercastock.presentation.ui.components.AllergenManager
import com.mercadona.mercastock.presentation.ui.components.CategorySelector
import com.mercadona.mercastock.presentation.ui.components.NameField
import com.mercadona.mercastock.presentation.ui.components.PriceField
import com.mercadona.mercastock.presentation.ui.components.StockControl
import com.mercadona.mercastock.utils.Constants
import kotlinx.coroutines.launch

@Composable
fun ProductFormScreen(
    onNavigateBack: () -> Unit,
    viewModel: ProductFormViewModel = hiltViewModel(),
) {
    val scope = rememberCoroutineScope()

    val uiState by viewModel.uiState.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    
    LaunchedEffect(uiState.navigateBack) {
        if (uiState.navigateBack) {
            onNavigateBack()
        }
    }

    ProductFormScreenContent(
        uiState = uiState,
        isLoading = isLoading,
        onNameChange = viewModel::updateName,
        onPriceChange = viewModel::updatePrice,
        onStockChange = viewModel::updateStock,
        onCategoryChange = viewModel::updateCategory,
        onAllergensChange = viewModel::updateAllergens,
        onSave = {
            scope.launch {
                viewModel.saveProduct()
            }
        }
    )
}

@Composable
fun ProductFormScreenContent(
    uiState: ProductFormUiState,
    onNameChange: (String) -> Unit,
    onPriceChange: (String) -> Unit,
    onStockChange: (Int) -> Unit,
    onCategoryChange: (String) -> Unit,
    onAllergensChange: (List<Allergen>) -> Unit,
    onSave: () -> Unit,
    isLoading: Boolean = false
) {
    val savingDescription = stringResource(R.string.accessibility_saving_product)
    val savingStateDescription = stringResource(R.string.accessibility_state_saving)
    val formDescription = stringResource(R.string.accessibility_product_form)
    val formValidStateDescription = stringResource(R.string.accessibility_state_form_valid)
    val formErrorsStateDescription = stringResource(R.string.accessibility_state_form_errors)
    val saveButtonDescription = stringResource(R.string.accessibility_save_product)
    val buttonEnabledStateDescription = stringResource(R.string.accessibility_state_button_enabled)
    val buttonDisabledStateDescription = stringResource(R.string.accessibility_state_button_disabled_form_errors)
    
    if (isLoading) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(dimensionResource(R.dimen.padding_small))
                .testTag("product_form_loading")
                .semantics {
                    contentDescription = savingDescription
                    stateDescription = savingStateDescription
                },
            contentAlignment = Alignment.Center
        ) {
            CircularProgressIndicator()
        }
    } else {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(dimensionResource(R.dimen.padding_small))
                .testTag("product_form")
                .semantics {
                    contentDescription = formDescription
                    stateDescription = if (uiState.isFormValid) 
                        formValidStateDescription 
                    else 
                        formErrorsStateDescription
                },
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            item {
                NameField(
                    name = uiState.name,
                    onNameChange = onNameChange,
                    nameError = uiState.nameError
                )
            }

            item {
                PriceField(
                    price = uiState.price,
                    onPriceChange = onPriceChange,
                    priceError = uiState.priceError
                )
            }

            item {
                StockControl(
                    stock = uiState.stock,
                    onStockChange = onStockChange
                )
            }

            item {
                CategorySelector(
                    category = uiState.category,
                    onCategoryChange = onCategoryChange,
                    categoryError = uiState.categoryError
                )
            }

            item {
                AllergenManager(
                    allergens = uiState.allergens,
                    onAllergensChange = onAllergensChange
                )
            }

            item {
                Button(
                    onClick = onSave,
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("save_product_button")
                        .semantics {
                            contentDescription = saveButtonDescription
                            stateDescription = if (uiState.isFormValid) 
                                buttonEnabledStateDescription 
                            else 
                                buttonDisabledStateDescription
                            role = Role.Button
                            onClick("Guardar producto") {
                                // Se implementa con true si hay que añadir comportamiento
//                                someMethod()
//                                onSave()
//                                true
                                // Si no, devolver false y se ejecutará el méthod asociado al boton
                                false
                            }
                        },
                    enabled = uiState.isFormValid
                ) {
                    Text(stringResource(R.string.form_save_button))
                }
            }
        }
    }
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun ProductFormScreenPreview() {
    MaterialTheme {
        ProductFormScreenContent(
            uiState = ProductFormUiState(
                name = stringResource(R.string.preview_product_example),
                price = "9.99",
                stock = 25,
                category = stringResource(R.string.preview_category_general),
                allergens = Constants.DEFAULT_ALLERGENS,
                isFormValid = true,
                nameError = "",
                priceError = ""
            ),
            isLoading = false,
            onNameChange = {},
            onPriceChange = {},
            onStockChange = {},
            onCategoryChange = {},
            onAllergensChange = {},
            onSave = {}
        )
    }
}
