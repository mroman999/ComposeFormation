package com.mercadona.mercastock.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import com.mercadona.mercastock.dia1.R
import com.mercadona.mercastock.model.Allergen
import com.mercadona.mercastock.ui.components.AllergenManager
import com.mercadona.mercastock.ui.components.CategorySelector
import com.mercadona.mercastock.ui.components.NameField
import com.mercadona.mercastock.ui.components.PriceField
import com.mercadona.mercastock.ui.components.StockControl
import com.mercadona.mercastock.ui.theme.MercaStockTheme
import com.mercadona.mercastock.utils.Constants
import com.mercadona.mercastock.utils.ValidationUtils

/**
 * - Formulario básico con validaciones
 * - StateHoisting con Scaffold y TopBar dinámico
 * - Lista de alérgenos con LazyColumn
 * - Gestión completa de productos
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductFormScreen() {
    val context = LocalContext.current
    
    // Estado del nombre del producto
    var productName by remember { mutableStateOf("") }

    // Estados del formulario
    var price by remember { mutableStateOf("") }
    var stock by remember { mutableIntStateOf(0) }
    var category by remember { mutableStateOf("") }

    // Estados de alérgenos
    var allergens by remember {
        mutableStateOf(Constants.DEFAULT_ALLERGENS)
    }

    // Estados de error
    var nameError by remember { mutableStateOf("") }
    var priceError by remember { mutableStateOf("") }
    var stockError by remember { mutableStateOf("") }
    var categoryError by remember { mutableStateOf("") }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = productName.ifBlank { stringResource(R.string.new_product_title) }
                    )
                }
            )
        }
    ) { paddingValues ->
        ProductFormScreenContent(
            paddingValues,
            productName,
            onNameChange = { newName ->
                productName = newName
                nameError = ValidationUtils.validateName(newName, context)
            },
            nameError,
            price,
            onPriceChange = { newPrice ->
                price = newPrice
                priceError = ValidationUtils.validatePrice(newPrice, context)
            },
            priceError,
            stock,
            onStockChange = { newStock ->
                stock = newStock
                stockError = ValidationUtils.validateStock(newStock, context)
            },
            stockError,
            category,
            onCategoryChange = { newCategory ->
                category = newCategory
                categoryError = ValidationUtils.validateCategory(newCategory, context)
            },
            categoryError,
            allergens,
            onAllergensChange = { newAllergens ->
                allergens = newAllergens
            },
            onSaveButtonClick = {
                val errors = ValidationUtils.validateForm(productName, price, stock, category, context)
                nameError = errors["name"] ?: ""
                priceError = errors["price"] ?: ""
                stockError = errors["stock"] ?: ""
                categoryError = errors["category"] ?: ""

                val isValid = ValidationUtils.isFormValid(errors)
                if (isValid) {
                    // Limpiar formulario
                    productName = ""
                    price = ""
                    stock = 0
                    category = ""
                    allergens = allergens.map { it.copy(isPresent = false) }

                    // Limpiar errores
                    nameError = ""
                    priceError = ""
                    stockError = ""
                    categoryError = ""
                }
            }
        )
    }
}

@Composable
private fun ProductFormScreenContent(
    paddingValues: PaddingValues,
    productName: String,
    onNameChange: (String) -> Unit,
    nameError: String,
    price: String,
    onPriceChange: (String) -> Unit,
    priceError: String,
    stock: Int,
    onStockChange: (Int) -> Unit,
    stockError: String,
    category: String,
    onCategoryChange: (String) -> Unit,
    categoryError: String,
    allergens: List<Allergen>,
    onAllergensChange: (List<Allergen>) -> Unit,
    onSaveButtonClick: () -> Unit,
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(paddingValues)
            .padding(dimensionResource(R.dimen.padding_small)),
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.content_spacing))
    ) {
        // Campo Nombre
        item {
            NameField(
                name = productName,
                onNameChange = { newName ->
                    onNameChange(newName)
                },
                nameError = nameError
            )
        }

        // Campo Precio
        item {
            PriceField(
                price = price,
                onPriceChange = { newPrice ->
                    onPriceChange(newPrice)
                },
                priceError = priceError
            )
        }

        // Control de Stock
        item {
            StockControl(
                stock = stock,
                onStockChange = { newStock ->
                    onStockChange(newStock)
                },
                stockError = stockError
            )
        }

        // Selector de Categoría
        item {
            CategorySelector(
                category = category,
                onCategoryChange = { newCategory ->
                    onCategoryChange(newCategory)
                },
                categoryError = categoryError
            )
        }

        // Gestión de Alérgenos
        item {
            AllergenManager(
                allergens = allergens,
                onAllergensChange = { newAllergens ->
                    onAllergensChange(newAllergens)
                }
            )
        }

        // Botón guardar
        item {
            Button(
                onClick = { onSaveButtonClick() },
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.save_product_action))
            }
        }
    }
}

@Composable
@Preview
fun ProductFormScreenPreview() {
    MercaStockTheme {
        Scaffold { paddingValues ->
            ProductFormScreenContent(
                paddingValues = paddingValues,
                productName = stringResource(R.string.new_product_title),
                onNameChange = {},
                nameError = "",
                price = "13.50",
                onPriceChange = {},
                priceError = "",
                stock = 12,
                onStockChange = {},
                stockError = "",
                category = "Alimentación",
                onCategoryChange = {},
                categoryError = "",
                allergens = Constants.DEFAULT_ALLERGENS,
                onAllergensChange = {},
                onSaveButtonClick = {}
            )
        }
    }
}