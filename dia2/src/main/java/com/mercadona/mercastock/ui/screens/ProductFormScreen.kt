package com.mercadona.mercastock.ui.screens

import android.util.Log
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.tween
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import com.mercadona.mercastock.dia2.R
import com.mercadona.mercastock.model.Allergen
import com.mercadona.mercastock.ui.components.AllergenManager
import com.mercadona.mercastock.ui.components.CategorySelector
import com.mercadona.mercastock.ui.components.NameField
import com.mercadona.mercastock.ui.components.PriceField
import com.mercadona.mercastock.ui.components.StockControl
import com.mercadona.mercastock.ui.theme.MercaStockTheme
import com.mercadona.mercastock.ui.viewmodel.ProductFormViewModel
import com.mercadona.mercastock.utils.Constants
import com.mercadona.mercastock.utils.ValidationUtils
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * Pantalla del formulario de productos con funcionalidades avanzadas del día 2:
 * - ViewModel para gestión de estado
 * - rememberSaveable para persistencia
 * - Optimización con remember()
 * - LaunchedEffect, SideEffect, DisposableEffect
 * - derivedStateOf
 * - rememberCoroutineScope
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductFormScreen(
    viewModel: ProductFormViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val lifecycleOwner = LocalLifecycleOwner.current

    // Estados locales con rememberSaveable para persistir cambios de configuración
    var productName by rememberSaveable { mutableStateOf("") }
    var price by rememberSaveable { mutableStateOf("") }
    var stock by rememberSaveable { mutableIntStateOf(0) }
    var category by rememberSaveable { mutableStateOf("") }

    // Estados de error (no necesitan persistir)
    var nameError by remember { mutableStateOf("") }
    var priceError by remember { mutableStateOf("") }
    var stockError by remember { mutableStateOf("") }
    var categoryError by remember { mutableStateOf("") }

    // Estados del ViewModel
    val allergens by viewModel.allergens.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val message by viewModel.message.collectAsState()
    val savedProducts by viewModel.savedProducts.collectAsState()

    // Estado para animación
    val alpha = remember { Animatable(1f) }
    var isAnimating by remember { mutableStateOf(false) }

    // OPTIMIZACIÓN: Cálculo costoso con remember()
    // Solo se recalcula cuando cambian los valores relevantes
    val expensiveFormScore = remember(productName, price, stock, category, allergens) {
        // Simula un cálculo costoso de puntuación del formulario
        val nameScore = if (productName.isNotBlank()) productName.length * 10 else 0
        val priceScore = price.toDoubleOrNull()?.let { (it * 100).toInt() } ?: 0
        val stockScore = stock * 5
        val categoryScore = if (category.isNotBlank()) 50 else 0
        val allergenScore = allergens.count { it.isPresent } * 20

        // Simula procesamiento pesado
        Thread.sleep(1000) // Solo para demostrar la optimización

        nameScore + priceScore + stockScore + categoryScore + allergenScore
    }

    // DERIVED STATE: Estado derivado para validación completa
    val isFormCompletelyValid by remember {
        derivedStateOf {
            productName.isNotBlank() &&
                    price.toDoubleOrNull() != null &&
                    price.toDoubleOrNull()!! > 0 &&
                    stock >= 0 &&
                    category.isNotBlank() &&
                    nameError.isEmpty() &&
                    priceError.isEmpty() &&
                    stockError.isEmpty() &&
                    categoryError.isEmpty()
        }
    }

    // DERIVED STATE: Resumen de alérgenos seleccionados
    val selectedAllergensText by remember {
        derivedStateOf {
            val selected = allergens.filter { it.isPresent }
            when {
                selected.isEmpty() -> context.getString(R.string.no_allergens_selected)
                selected.size == 1 -> context.getString(
                    R.string.one_allergen_selected,
                    selected.first().name
                )

                else -> context.getString(R.string.multiple_allergens_selected)
            }
        }
    }

    // LAUNCHED EFFECT: Animación continua cuando está cargando
    LaunchedEffect(isLoading) {
        isAnimating = isLoading
        if (isLoading) {
            while (isAnimating) {
                alpha.animateTo(0.5f, animationSpec = tween(1000))
                alpha.animateTo(1f, animationSpec = tween(1000))
            }
        } else {
            alpha.animateTo(1f)
        }
    }

    // LAUNCHED EFFECT: Mostrar mensajes en Snackbar
    LaunchedEffect(message) {
        if (message.isNotEmpty()) {
            snackbarHostState.showSnackbar(message)
        }
    }

    // LAUNCHED EFFECT: Validación automática con debounce
    LaunchedEffect(productName) {
        if (productName.isNotEmpty()) {
            delay(500) // Debounce de 500ms
            nameError = ValidationUtils.validateName(productName, context)
        }
    }

    LaunchedEffect(price) {
        if (price.isNotEmpty()) {
            delay(500)
            priceError = ValidationUtils.validatePrice(price, context)
        }
    }

    // SIDE EFFECT: Log cada vez que se recompone la pantalla
    SideEffect {
        Log.d("ProductFormScreen", "Screen recomposed with score: $expensiveFormScore")
    }

    // DISPOSABLE EFFECT: Observar lifecycle events
    DisposableEffect(lifecycleOwner) {
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> {
                    Log.d("ProductFormScreen", "Screen resumed")
                }

                Lifecycle.Event.ON_PAUSE -> {
                    Log.d("ProductFormScreen", "Screen paused")
                }

                else -> {}
            }
        }

        lifecycleOwner.lifecycle.addObserver(observer)

        onDispose {
            Log.d("ProductFormScreen", "Disposing ProductFormScreen observer")
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = stringResource(R.string.new_product_title)
                    )
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        ProductFormScreenContent(
            paddingValues = paddingValues,
            productName = productName,
            onNameChange = { newName ->
                productName = newName
                // Validación inmediata solo si ya había error
                if (nameError.isNotEmpty()) {
                    nameError = ValidationUtils.validateName(newName, context)
                }
            },
            nameError = nameError,
            price = price,
            onPriceChange = { newPrice ->
                price = newPrice
                if (priceError.isNotEmpty()) {
                    priceError = ValidationUtils.validatePrice(newPrice, context)
                }
            },
            priceError = priceError,
            stock = stock,
            onStockChange = { newStock ->
                stock = newStock
                stockError = ValidationUtils.validateStock(newStock, context)
            },
            stockError = stockError,
            category = category,
            onCategoryChange = { newCategory ->
                category = newCategory
                categoryError = ValidationUtils.validateCategory(newCategory, context)
            },
            categoryError = categoryError,
            allergens = allergens,
            onAllergensChange = { newAllergens ->
                viewModel.updateAllergens(newAllergens)
            },
            isLoading = isLoading,
            alpha = alpha.value,
            expensiveFormScore = expensiveFormScore,
            isFormCompletelyValid = isFormCompletelyValid,
            selectedAllergensText = selectedAllergensText,
            savedProductsCount = savedProducts.size,
            onSaveButtonClick = {
                // Validación final
                val errors =
                    ValidationUtils.validateForm(productName, price, stock, category, context)
                nameError = errors["name"] ?: ""
                priceError = errors["price"] ?: ""
                stockError = errors["stock"] ?: ""
                categoryError = errors["category"] ?: ""

                val isValid = ValidationUtils.isFormValid(errors)
                if (isValid) {
                    // Usar ViewModel para guardar
                    viewModel.saveProduct(productName, price, stock, category, allergens)

                    // Limpiar formulario
                    productName = ""
                    price = ""
                    stock = 0
                    category = ""
                    viewModel.resetAllergens()

                    // Limpiar errores
                    nameError = ""
                    priceError = ""
                    stockError = ""
                    categoryError = ""
                }
            },
            onLoadAllergensClick = {
                // Usar rememberCoroutineScope para operación manual
                scope.launch {
                    viewModel.loadAllergensFromServer()
                }
            },
            onResetFormClick = {
                scope.launch {
                    // Animación de reset
                    alpha.animateTo(0f, animationSpec = tween(300))

                    // Reset de valores
                    productName = ""
                    price = ""
                    stock = 0
                    category = ""
                    viewModel.resetAllergens()

                    // Limpiar errores
                    nameError = ""
                    priceError = ""
                    stockError = ""
                    categoryError = ""

                    alpha.animateTo(1f, animationSpec = tween(300))

                    snackbarHostState.showSnackbar(context.getString(R.string.form_reset_message))
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
    isLoading: Boolean,
    alpha: Float,
    expensiveFormScore: Int,
    isFormCompletelyValid: Boolean,
    selectedAllergensText: String,
    savedProductsCount: Int,
    onSaveButtonClick: () -> Unit,
    onLoadAllergensClick: () -> Unit,
    onResetFormClick: () -> Unit,
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .graphicsLayer(alpha = alpha)
    ) {
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(dimensionResource(R.dimen.padding_small)),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.content_spacing))
        ) {
            // Información de estado del formulario
            item {
                FormStatusCard(
                    expensiveFormScore = expensiveFormScore,
                    isFormCompletelyValid = isFormCompletelyValid,
                    selectedAllergensText = selectedAllergensText,
                    savedProductsCount = savedProductsCount
                )
            }

            // Campo Nombre
            item {
                NameField(
                    name = productName,
                    onNameChange = onNameChange,
                    nameError = nameError
                )
            }

            // Campo Precio
            item {
                PriceField(
                    price = price,
                    onPriceChange = onPriceChange,
                    priceError = priceError
                )
            }

            // Control de Stock
            item {
                StockControl(
                    stock = stock,
                    onStockChange = onStockChange,
                    stockError = stockError
                )
            }

            // Selector de Categoría
            item {
                CategorySelector(
                    category = category,
                    onCategoryChange = onCategoryChange,
                    categoryError = categoryError
                )
            }

            // Gestión de Alérgenos
            item {
                AllergenManager(
                    allergens = allergens,
                    onAllergensChange = onAllergensChange
                )
            }

            // Botones de acción
            item {
                ActionButtons(
                    isFormCompletelyValid = isFormCompletelyValid,
                    isLoading = isLoading,
                    onSaveButtonClick = onSaveButtonClick,
                    onLoadAllergensClick = onLoadAllergensClick,
                    onResetFormClick = onResetFormClick
                )
            }
        }

        // Indicador de carga superpuesto
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        }
    }
}

@Composable
private fun FormStatusCard(
    expensiveFormScore: Int,
    isFormCompletelyValid: Boolean,
    selectedAllergensText: String,
    savedProductsCount: Int,
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(dimensionResource(R.dimen.padding_medium))
        ) {
            Text(
                text = stringResource(R.string.form_status_title),
                style = MaterialTheme.typography.titleMedium
            )

            Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_small)))

            Text(
                text = stringResource(R.string.form_score_label, expensiveFormScore),
                style = MaterialTheme.typography.bodyMedium
            )

            Text(
                text = stringResource(
                    if (isFormCompletelyValid) R.string.form_valid else R.string.form_invalid
                ),
                style = MaterialTheme.typography.bodyMedium,
                color = if (isFormCompletelyValid)
                    MaterialTheme.colorScheme.primary
                else
                    MaterialTheme.colorScheme.error
            )

            Text(
                text = selectedAllergensText,
                style = MaterialTheme.typography.bodySmall
            )

            Text(
                text = stringResource(R.string.saved_products_count, savedProductsCount),
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
private fun ActionButtons(
    isFormCompletelyValid: Boolean,
    isLoading: Boolean,
    onSaveButtonClick: () -> Unit,
    onLoadAllergensClick: () -> Unit,
    onResetFormClick: () -> Unit,
) {
    Column(
        verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_medium))
    ) {
        // Botón guardar principal
        Button(
            onClick = onSaveButtonClick,
            modifier = Modifier.fillMaxWidth(),
            enabled = isFormCompletelyValid && !isLoading
        ) {
            Text(stringResource(R.string.save_product_action))
        }

        // Botones secundarios
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_medium))
        ) {
            OutlinedButton(
                onClick = onLoadAllergensClick,
                modifier = Modifier.weight(1f),
                enabled = !isLoading
            ) {
                Text(stringResource(R.string.load_allergens_action))
            }

            OutlinedButton(
                onClick = onResetFormClick,
                modifier = Modifier.weight(1f),
                enabled = !isLoading
            ) {
                Text(stringResource(R.string.reset_form_action))
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
                productName = "Producto de prueba",
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
                isLoading = false,
                alpha = 1f,
                expensiveFormScore = 1250,
                isFormCompletelyValid = true,
                selectedAllergensText = "2 alérgenos seleccionados",
                savedProductsCount = 3,
                onSaveButtonClick = {},
                onLoadAllergensClick = {},
                onResetFormClick = {}
            )
        }
    }
}
