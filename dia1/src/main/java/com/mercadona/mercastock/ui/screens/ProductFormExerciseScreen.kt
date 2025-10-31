package com.mercadona.mercastock.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
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
 * 🎯 EJERCICIO PRÁCTICO - FORMULARIO DE PRODUCTOS (DÍA 1)
 * 
 * Este es el mismo formulario completo del día 1, pero con TODOs para completar
 * los 4 conceptos básicos de la formación:
 * 
 * TODO 1: Estado Local con remember y mutableStateOf
 * TODO 2: StateHoisting y Separación de Componentes
 * TODO 3: LazyColumn vs Column con Scroll
 * TODO 4: Validaciones y Gestión de Errores
 * 
 * INSTRUCCIONES:
 * - Busca cada TODO en el código
 * - Completa el código siguiendo las pistas
 * - Elimina las "SOLUCIONES TEMPORALES" cuando completes cada TODO
 * - Prueba la funcionalidad después de cada cambio
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductFormExerciseScreen() {
    val context = LocalContext.current
    
    // TODO 1: ESTADO LOCAL CON REMEMBER Y MUTABLESTATEOF
    // Completa la declaración de estados locales para el formulario
    // Pista: Usa remember { mutableStateOf() } para valores que cambian
    
    // Estado del nombre del producto
    // var productName by ??? // ¿Cómo declarar estado mutable para String?
    
    // Estados del formulario
    // var price by ??? // ¿Mismo patrón para el precio?
    // var stock by ??? // ¿Qué usar para números enteros? (mutableIntStateOf)
    // var category by ??? // ¿Cómo para la categoría?
    
    // Estados de alérgenos
    // var allergens by ??? // ¿Cómo manejar una lista de objetos?
    
    // Estados de error
    // var nameError by ??? // ¿Cómo para mensajes de error?
    // var priceError by ???
    // var stockError by ???
    // var categoryError by ???
    
    // SOLUCIÓN TEMPORAL (eliminar cuando completes TODO 1):
    var productName by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var stock by remember { mutableIntStateOf(0) }
    var category by remember { mutableStateOf("") }
    var allergens by remember { mutableStateOf(Constants.DEFAULT_ALLERGENS) }
    var nameError by remember { mutableStateOf("") }
    var priceError by remember { mutableStateOf("") }
    var stockError by remember { mutableStateOf("") }
    var categoryError by remember { mutableStateOf("") }

    // TODO 2: SCAFFOLD
    // Completa la estructura Scaffold con TopBar

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

        // TODO 1: StateHoisting - Separar contenido en componente independiente
        // Completa la llamada al componente de contenido pasando todos los estados
        // Pista: Pasa estados y callbacks para mantener la separación de responsabilidades

        ProductFormExerciseContent(
            paddingValues = paddingValues,
            productName = productName,
            // onNameChange = { ??? -> // ¿Cómo actualizar productName y validar?
            //     productName = ???
            //     nameError = ??? // ¿Cómo validar el nombre?
            // },
            onNameChange = { newName ->
                productName = newName
                nameError = ValidationUtils.validateName(newName, context)
            },
            nameError = nameError,
            price = price,
            // onPriceChange = { ??? -> // ¿Mismo patrón para precio?
            //     price = ???
            //     priceError = ???
            // },
            onPriceChange = { newPrice ->
                price = newPrice
                priceError = ValidationUtils.validatePrice(newPrice, context)
            },
            priceError = priceError,
            stock = stock,
            // onStockChange = { ??? -> // ¿Cómo para stock (Int)?
            //     stock = ???
            //     stockError = ???
            // },
            onStockChange = { newStock ->
                stock = newStock
                stockError = ValidationUtils.validateStock(newStock, context)
            },
            stockError = stockError,
            category = category,
            // onCategoryChange = { ??? -> // ¿Patrón para categoría?
            //     category = ???
            //     categoryError = ???
            // },
            onCategoryChange = { newCategory ->
                category = newCategory
                categoryError = ValidationUtils.validateCategory(newCategory, context)
            },
            categoryError = categoryError,
            allergens = allergens,
            // onAllergensChange = { ??? -> // ¿Cómo actualizar lista de alérgenos?
            //     allergens = ???
            // },
            onAllergensChange = { newAllergens ->
                allergens = newAllergens
            },
            onSaveButtonClick = {
                // TODO 4: VALIDACIONES Y GESTIÓN DE ERRORES
                // Completa la lógica de validación y guardado
                // Pista: Valida todos los campos y limpia el formulario si es válido

                // val errors = ??? // ¿Cómo validar todo el formulario?
                // nameError = errors["name"] ?: ""
                // priceError = errors["price"] ?: ""
                // stockError = errors["stock"] ?: ""
                // categoryError = errors["category"] ?: ""

                // val isValid = ??? // ¿Cómo verificar si el formulario es válido?
                // if (isValid) {
                //     // Limpiar formulario
                //     productName = ???
                //     price = ???
                //     stock = ???
                //     category = ???
                //     allergens = ??? // ¿Cómo resetear alérgenos?
                //
                //     // Limpiar errores
                //     nameError = ???
                //     priceError = ???
                //     stockError = ???
                //     categoryError = ???
                // }

                // SOLUCIÓN TEMPORAL (eliminar cuando completes TODO 4):
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
private fun ProductFormExerciseContent(
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
    // TODO 3: LAZYCOLUMN VS COLUMN CON SCROLL
    // Completa la estructura del formulario usando LazyColumn
    // Pista: LazyColumn es mejor para listas dinámicas, cada campo va en un item {}

    // LazyColumn(
    //     modifier = Modifier
    //         .fillMaxSize()
    //         .padding(paddingValues)
    //         .padding(dimensionResource(R.dimen.padding_small)),
    //     verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.content_spacing))
    // ) {
    //     // Campo Nombre
    //     item {
    //         NameField(
    //             name = productName,
    //             onNameChange = { ??? -> // ¿Cómo pasar el callback?
    //                 onNameChange(???)
    //             },
    //             nameError = nameError
    //         )
    //     }
    //
    //     // TODO 3a: Añade los demás campos siguiendo el mismo patrón
    //     // Campo Precio
    //     // Campo Stock
    //     // Selector Categoría
    //     // Gestión Alérgenos
    //     // Botón Guardar
    // }

    // SOLUCIÓN TEMPORAL (eliminar cuando completes TODO 3):
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
fun ProductFormExerciseScreenPreview() {
    MercaStockTheme {
        Scaffold { paddingValues ->
            ProductFormExerciseContent(
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
