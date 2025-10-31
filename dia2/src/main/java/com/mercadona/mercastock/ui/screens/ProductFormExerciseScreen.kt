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
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.ui.focus.FocusManager
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleEventObserver
import androidx.lifecycle.compose.LocalLifecycleOwner
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.mercadona.mercastock.dia2.R
import com.mercadona.mercastock.model.Allergen
import com.mercadona.mercastock.ui.components.AllergenManager
import com.mercadona.mercastock.ui.components.CategorySelector
import com.mercadona.mercastock.ui.components.NameField
import com.mercadona.mercastock.ui.components.PriceField
import com.mercadona.mercastock.ui.components.StockControl
import com.mercadona.mercastock.ui.theme.MercaStockTheme
import com.mercadona.mercastock.ui.viewmodel.ProductFormViewModel
import com.mercadona.mercastock.utils.ValidationUtils
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

/**
 * 🎯 EJERCICIO PRÁCTICO - FORMULARIO DE PRODUCTOS
 * 
 * Este es el mismo formulario completo del día 2, pero con TODOs para completar
 * los 7 conceptos clave de la formación:
 * 
 * INSTRUCCIONES:
 * - Busca cada TODO en el código
 * - Completa el código siguiendo las pistas
 * - Elimina las "SOLUCIONES TEMPORALES" cuando completes cada TODO
 * - Prueba la funcionalidad después de cada cambio
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductFormExerciseScreen(
    viewModel: ProductFormViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val lifecycleOwner = LocalLifecycleOwner.current

    // TODO 7: FOCUS MANAGER FOR KEYBOARD NAVIGATION
    // Completa la configuración de gestión de focus para navegación por teclado
    // Pista: Usa LocalFocusManager y FocusRequester para cada campo
    
    // val focusManager = ??? // ¿Cómo obtener el FocusManager?
    // val nameFocusRequester = remember { ??? } // ¿Cómo crear un FocusRequester?
    // val priceFocusRequester = remember { ??? } // ¿Qué función usar?
    // val categoryFocusRequester = remember { ??? } // ¿Mismo patrón?
    
    // SOLUCIÓN TEMPORAL (eliminar cuando completes TODO 7):
    val focusManager = LocalFocusManager.current
    val nameFocusRequester = remember { FocusRequester() }
    val priceFocusRequester = remember { FocusRequester() }
    val categoryFocusRequester = remember { FocusRequester() }

    // TODO 1: VIEWMODEL STATE MANAGEMENT
    // Completa la lectura del estado desde el ViewModel
    // Pista: Usa collectAsState() para convertir StateFlow a State
    
    // val allergens by viewModel.??? // ¿Qué propiedad leer para los alérgenos?
    // val isLoading by viewModel.??? // ¿Cómo acceder al estado de carga?
    // val message by viewModel.??? // ¿Qué propiedad para los mensajes?
    // val savedProducts by viewModel.??? // ¿Dónde están los productos guardados?
    
    // SOLUCIÓN TEMPORAL (eliminar cuando completes TODO 1):
    val allergens by viewModel.allergens.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val message by viewModel.message.collectAsState()
    val savedProducts by viewModel.savedProducts.collectAsState()

    // TODO 2: REMEMBER SAVEABLE FOR CONFIGURATION CHANGES
    // Completa estos estados para que persistan durante rotaciones de pantalla
    // Pista: Usa rememberSaveable en lugar de remember para persistir estado
    
    // var productName by ??? { mutableStateOf("") } // ¿remember o rememberSaveable?
    // var price by ??? { mutableStateOf("") } // ¿Qué función usar para persistir?
    // var stock by ??? { mutableIntStateOf(0) } // ¿Cómo persistir un Int?
    // var category by ??? { mutableStateOf("") } // ¿Qué tipo de remember usar?
    
    // SOLUCIÓN TEMPORAL (eliminar cuando completes TODO 2):
    var productName by rememberSaveable { mutableStateOf("") }
    var price by rememberSaveable { mutableStateOf("") }
    var stock by rememberSaveable { mutableIntStateOf(0) }
    var category by rememberSaveable { mutableStateOf("") }

    // Estados de error (no necesitan persistir porque son recalculados - solo durante la sesión actual)
    var nameError by remember { mutableStateOf("") }
    var priceError by remember { mutableStateOf("") }
    var stockError by remember { mutableStateOf("") }
    var categoryError by remember { mutableStateOf("") }

    // Estado para animación
    val alpha = remember { Animatable(1f) }
    var isAnimating by remember { mutableStateOf(false) }

    // TODO 3: PERFORMANCE OPTIMIZATION WITH REMEMBER()
    // Completa este cálculo costoso para que solo se ejecute cuando sea necesario
    // Pista: Usa remember(key1, key2, ...) especificando las dependencias exactas
    
    // val expensiveFormScore = remember(???, ???, ???, ???, ???) { // ¿Qué dependencias necesita?
    //     // Simula un cálculo costoso de puntuación del formulario
    //     val nameScore = if (productName.isNotBlank()) productName.length * 10 else 0
    //     val priceScore = price.toDoubleOrNull()?.let { (it * 100).toInt() } ?: 0
    //     val stockScore = stock * 5
    //     val categoryScore = if (category.isNotBlank()) 50 else 0
    //     val allergenScore = allergens.count { it.isPresent } * 20
    //     
    //     // Simula procesamiento pesado
    //     Thread.sleep(10) // Solo para demostrar la optimización
    //     
    //     nameScore + priceScore + stockScore + categoryScore + allergenScore
    // }
    
    // SOLUCIÓN TEMPORAL (eliminar cuando completes TODO 3):
    val expensiveFormScore = remember(productName, price, stock, category, allergens) {
        val nameScore = if (productName.isNotBlank()) productName.length * 10 else 0
        val priceScore = price.toDoubleOrNull()?.let { (it * 100).toInt() } ?: 0
        val stockScore = stock * 5
        val categoryScore = if (category.isNotBlank()) 50 else 0
        val allergenScore = allergens.count { it.isPresent } * 20

        Thread.sleep(10) // Simula procesamiento pesado
        nameScore + priceScore + stockScore + categoryScore + allergenScore
    }

    // TODO 5: DERIVED STATE OF FOR COMPUTED STATE
    // Completa estos estados derivados para optimizar las recomposiciones
    // Pista: Usa derivedStateOf dentro de remember para estado computado eficiente
    
    // val isFormCompletelyValid by remember {
    //     derivedStateOf {
    //         // ¿Qué condiciones debe cumplir un formulario válido?
    //         productName.isNotBlank() && 
    //         ??? && // ¿Cómo validar el precio?
    //         ??? && // ¿Cómo validar el stock?
    //         ??? && // ¿Cómo validar la categoría?
    //         ??? && // ¿Cómo verificar que no hay errores?
    //         ???
    //     }
    // }
    
    // SOLUCIÓN TEMPORAL (eliminar cuando completes TODO 5):
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

    // TODO 4: LAUNCHED EFFECT, SIDE EFFECT, DISPOSABLE EFFECT
    
    // TODO 4a: LaunchedEffect para animación continua
    // LaunchedEffect(???) { // ¿Cuándo debe ejecutarse esta animación?
    //     isAnimating = isLoading
    //     if (isLoading) {
    //         while (isAnimating) {
    //             alpha.animateTo(???, animationSpec = tween(1000)) // ¿A qué valor animar?
    //             alpha.animateTo(???, animationSpec = tween(1000)) // ¿Y después?
    //         }
    //     } else {
    //         alpha.animateTo(???) // ¿A qué valor cuando no está cargando?
    //     }
    // }
    
    // SOLUCIÓN TEMPORAL (eliminar cuando completes TODO 4a):
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

    // TODO 4b: LaunchedEffect para mostrar mensajes
    // LaunchedEffect(???) { // ¿Cuándo mostrar el mensaje?
    //     if (message.isNotEmpty()) {
    //         ??? // ¿Cómo mostrar el mensaje en el Snackbar?
    //     }
    // }
    
    // SOLUCIÓN TEMPORAL (eliminar cuando completes TODO 4b):
    LaunchedEffect(message) {
        if (message.isNotEmpty()) {
            snackbarHostState.showSnackbar(message)
        }
    }

    // TODO 4c: LaunchedEffect para validación automática con debounce
    // LaunchedEffect(???) { // ¿Cuándo validar el nombre?
    //     if (productName.isNotEmpty()) {
    //         delay(???) // ¿Cuánto tiempo de debounce?
    //         nameError = ValidationUtils.validateName(productName, context)
    //     }
    // }
    
    // SOLUCIÓN TEMPORAL (eliminar cuando completes TODO 4c):
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

    // TODO 4d: SideEffect para logging
    // SideEffect {
    //     Log.d("ProductFormExercise", "Screen recomposed. Score: ???") // ¿Qué loggear?
    // }
    
    // SOLUCIÓN TEMPORAL (eliminar cuando completes TODO 4d):
    SideEffect {
        Log.d("ProductFormExercise", "Screen recomposed. Score: $expensiveFormScore")
    }

    // TODO 4e: DisposableEffect para observar lifecycle
    // DisposableEffect(???) { // ¿Qué key usar?
    //     val observer = LifecycleEventObserver { _, event ->
    //         when (event) {
    //             Lifecycle.Event.ON_RESUME -> Log.d("ProductFormExercise", "???") // ¿Qué loggear?
    //             Lifecycle.Event.ON_PAUSE -> Log.d("ProductFormExercise", "???") // ¿Qué loggear?
    //             else -> {}
    //         }
    //     }
    //     
    //     lifecycleOwner.lifecycle.addObserver(observer)
    //     
    //     onDispose {
    //         Log.d("ProductFormExercise", "???") // ¿Qué loggear al limpiar?
    //         lifecycleOwner.lifecycle.removeObserver(observer)
    //     }
    // }

    
    // SOLUCIÓN TEMPORAL (eliminar cuando completes TODO 4e):
    DisposableEffect(lifecycleOwner) {
        // Este código se ejecuta la primera vez que se recompone la vista y
        // cada vez que cambie su clave (lifecycleOwner)
        val observer = LifecycleEventObserver { _, event ->
            when (event) {
                Lifecycle.Event.ON_RESUME -> Log.d("ProductFormExercise", "Form screen resumed")
                Lifecycle.Event.ON_PAUSE -> Log.d("ProductFormExercise", "Form screen paused")
                else -> {}
            }
        }
        
        lifecycleOwner.lifecycle.addObserver(observer)

        // Este código se ejecuta SOLO cuando se elimina la vista de la jerarquía de UI
        onDispose {
            Log.d("ProductFormExercise", "Disposing form screen observer")
            lifecycleOwner.lifecycle.removeObserver(observer)
        }
    }

    // TODO 7c: FOCUS MANAGEMENT - Focus inicial al abrir el formulario
    // Completa este LaunchedEffect para dar focus inicial al primer campo
    // Pista: Usa Unit como key para ejecutar solo una vez al crear el composable
    
    // LaunchedEffect(???) { // ¿Qué key usar para ejecutar solo una vez?
    //     ??? // ¿Cómo dar focus al campo nombre?
    // }
    
    // SOLUCIÓN TEMPORAL (eliminar cuando completes TODO 7c):
    LaunchedEffect(Unit) {
        nameFocusRequester.requestFocus()
    }

    // DERIVED STATE: Resumen de alérgenos seleccionados
    val selectedAllergensText by remember {
        derivedStateOf {
            val selected = allergens.filter { it.isPresent }
            when {
                selected.isEmpty() -> context.getString(R.string.no_allergens_selected)
                selected.size == 1 -> context.getString(R.string.one_allergen_selected, selected.first().name)
                else -> context.getString(R.string.multiple_allergens_selected)
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { 
                    Text(
                        if (productName.isBlank()) 
                            stringResource(R.string.new_product_title)
                        else 
                            productName
                    )
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) }
    ) { paddingValues ->
        ProductFormExerciseContent(
            paddingValues = paddingValues,
            productName = productName,
            onProductNameChange = { productName = it },
            price = price,
            onPriceChange = { price = it },
            stock = stock,
            onStockChange = { stock = it },
            category = category,
            onCategoryChange = { category = it },
            allergens = allergens,
            onAllergensChange = { viewModel.updateAllergens(it) },
            nameError = nameError,
            priceError = priceError,
            stockError = stockError,
            categoryError = categoryError,
            expensiveFormScore = expensiveFormScore,
            isFormCompletelyValid = isFormCompletelyValid,
            selectedAllergensText = selectedAllergensText,
            savedProductsCount = savedProducts.size,
            isLoading = isLoading,
            alpha = alpha.value,
            nameFocusRequester = nameFocusRequester,
            priceFocusRequester = priceFocusRequester,
            categoryFocusRequester = categoryFocusRequester,
            focusManager = focusManager,
            onSaveProduct = {
                if (isFormCompletelyValid) {
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
            onLoadAllergens = {
                viewModel.loadAllergensFromServer()
            },
            onResetForm = {
                // TODO 6: REMEMBER COROUTINE SCOPE
                // Completa esta función usando rememberCoroutineScope
                // Pista: Usa scope.launch para operaciones asíncronas desde event handlers
                
                // scope.launch {
                //     // Animación de reset
                //     alpha.animateTo(???, animationSpec = tween(300)) // ¿A qué valor animar?
                //     
                //     // Reset de valores
                //     productName = ???
                //     price = ???
                //     stock = ???
                //     category = ???
                //     viewModel.resetAllergens()
                //     
                //     // Limpiar errores
                //     nameError = ???
                //     priceError = ???
                //     stockError = ???
                //     categoryError = ???
                //     
                //     alpha.animateTo(???, animationSpec = tween(300)) // ¿A qué valor final?
                //     
                //     snackbarHostState.showSnackbar(???) // ¿Qué mensaje mostrar?
                // }
                
                // SOLUCIÓN TEMPORAL (eliminar cuando completes TODO 6):
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
private fun ProductFormExerciseContent(
    paddingValues: PaddingValues,
    productName: String,
    onProductNameChange: (String) -> Unit,
    price: String,
    onPriceChange: (String) -> Unit,
    stock: Int,
    onStockChange: (Int) -> Unit,
    category: String,
    onCategoryChange: (String) -> Unit,
    allergens: List<Allergen>,
    onAllergensChange: (List<Allergen>) -> Unit,
    nameError: String,
    priceError: String,
    stockError: String,
    categoryError: String,
    expensiveFormScore: Int,
    isFormCompletelyValid: Boolean,
    selectedAllergensText: String,
    savedProductsCount: Int,
    isLoading: Boolean,
    alpha: Float,
    nameFocusRequester: FocusRequester,
    priceFocusRequester: FocusRequester,
    categoryFocusRequester: FocusRequester,
    focusManager: FocusManager,
    onSaveProduct: () -> Unit,
    onLoadAllergens: () -> Unit,
    onResetForm: () -> Unit
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
                .padding(dimensionResource(R.dimen.padding_medium)),
            verticalArrangement = Arrangement.spacedBy(dimensionResource(R.dimen.spacing_extra_large))
        ) {
            // Encabezado con información de ejercicios
            item {
                ExerciseHeaderCard()
            }

            // Card de estado del formulario (muestra conceptos avanzados)
            item {
                FormStatusCard(
                    expensiveFormScore = expensiveFormScore,
                    isFormCompletelyValid = isFormCompletelyValid,
                    selectedAllergensText = selectedAllergensText,
                    savedProductsCount = savedProductsCount
                )
            }

            // Campo de nombre
            item {
                // TODO 7d: FOCUS MANAGEMENT - Configurar navegación del campo nombre
                // Completa la configuración del NameField con focus y navegación
                // Pista: Usa focusRequester y onNext para la navegación secuencial
                
                NameField(
                    name = productName,
                    onNameChange = onProductNameChange,
                    nameError = nameError
                    // focusRequester = ??? // ¿Qué FocusRequester usar?
                    // onNext = { ??? } // ¿Cómo navegar al siguiente campo?
                )
                
                // SOLUCIÓN TEMPORAL (eliminar cuando completes TODO 7d):
                // NameField(
                //     name = productName,
                //     onNameChange = onProductNameChange,
                //     nameError = nameError,
                //     focusRequester = nameFocusRequester,
                //     onNext = { priceFocusRequester.requestFocus() }
                // )
            }

            // Campo de precio
            item {
                // TODO 7e: FOCUS MANAGEMENT - Configurar navegación del campo precio
                // Completa la configuración del PriceField con focus y navegación
                // Pista: Similar al NameField, pero navega al campo categoría
                
                PriceField(
                    price = price,
                    onPriceChange = onPriceChange,
                    priceError = priceError
                    // focusRequester = ??? // ¿Qué FocusRequester usar?
                    // onNext = { ??? } // ¿Cómo navegar al selector de categoría?
                )
                
                // SOLUCIÓN TEMPORAL (eliminar cuando completes TODO 7e):
                // PriceField(
                //     price = price,
                //     onPriceChange = onPriceChange,
                //     priceError = priceError,
                //     focusRequester = priceFocusRequester,
                //     onNext = { categoryFocusRequester.requestFocus() }
                // )
            }

            // Control de stock
            item {
                StockControl(
                    stock = stock,
                    onStockChange = onStockChange,
                    stockError = stockError
                )
            }

            // Selector de categoría
            item {
                // TODO 7f: FOCUS MANAGEMENT - Configurar navegación del selector categoría
                // Completa la configuración del CategorySelector con focus y cierre de teclado
                // Pista: Es el último campo, usa focusManager.clearFocus() para cerrar el teclado
                
                CategorySelector(
                    category = category,
                    onCategoryChange = onCategoryChange,
                    categoryError = categoryError
                    // focusRequester = ??? // ¿Qué FocusRequester usar?
                    // onNext = { ??? } // ¿Cómo cerrar el teclado al final?
                )
                
                // SOLUCIÓN TEMPORAL (eliminar cuando completes TODO 7f):
                // CategorySelector(
                //     category = category,
                //     onCategoryChange = onCategoryChange,
                //     categoryError = categoryError,
                //     focusRequester = categoryFocusRequester,
                //     onNext = { focusManager.clearFocus() }
                // )
            }

            // Gestión de alérgenos
            item {
                AllergenManager(
                    allergens = allergens,
                    onAllergensChange = onAllergensChange
                )
            }

            // Botones de acción
            item {
                ActionButtonsCard(
                    isFormCompletelyValid = isFormCompletelyValid,
                    isLoading = isLoading,
                    onSaveProduct = onSaveProduct,
                    onLoadAllergens = onLoadAllergens,
                    onResetForm = onResetForm
                )
            }

            // Espaciado final
            item {
                Spacer(modifier = Modifier.height(dimensionResource(R.dimen.spacing_extra_large)))
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
private fun ExerciseHeaderCard() {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = MaterialTheme.colorScheme.primaryContainer
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = "🎯 Ejercicio Práctico - Formulario Completo",
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Completa los 7 TODOs en el código para practicar:",
                style = MaterialTheme.typography.bodyMedium,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "• TODO 1: ViewModel State Management\n• TODO 2: rememberSaveable\n• TODO 3: Performance Optimization\n• TODO 4: LaunchedEffect & Effects\n• TODO 5: derivedStateOf\n• TODO 6: rememberCoroutineScope\n• TODO 7: FocusManager & Navigation",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onPrimaryContainer
            )
        }
    }
}

@Composable
private fun FormStatusCard(
    expensiveFormScore: Int,
    isFormCompletelyValid: Boolean,
    selectedAllergensText: String,
    savedProductsCount: Int
) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        colors = CardDefaults.cardColors(
            containerColor = if (isFormCompletelyValid) 
                MaterialTheme.colorScheme.primaryContainer 
            else 
                MaterialTheme.colorScheme.surfaceVariant
        )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(
                text = stringResource(R.string.form_status_title),
                style = MaterialTheme.typography.titleMedium,
                color = if (isFormCompletelyValid) 
                    MaterialTheme.colorScheme.onPrimaryContainer 
                else 
                    MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(8.dp))
            
            // Puntuación del formulario (remember optimization)
            Text(
                text = stringResource(R.string.form_score_label, expensiveFormScore),
                style = MaterialTheme.typography.bodyMedium
            )
            
            // Estado de validación (derivedStateOf)
            Text(
                text = if (isFormCompletelyValid) 
                    stringResource(R.string.form_valid) 
                else 
                    stringResource(R.string.form_invalid),
                style = MaterialTheme.typography.bodyMedium
            )
            
            // Resumen de alérgenos (derivedStateOf)
            Text(
                text = selectedAllergensText,
                style = MaterialTheme.typography.bodySmall
            )
            
            // Productos guardados (ViewModel state)
            Text(
                text = stringResource(R.string.saved_products_count, savedProductsCount),
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Composable
private fun ActionButtonsCard(
    isFormCompletelyValid: Boolean,
    isLoading: Boolean,
    onSaveProduct: () -> Unit,
    onLoadAllergens: () -> Unit,
    onResetForm: () -> Unit
) {
    Card(
        modifier = Modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            // Botón principal de guardar
            Button(
                onClick = onSaveProduct,
                enabled = isFormCompletelyValid && !isLoading,
                modifier = Modifier.fillMaxWidth()
            ) {
                Text(stringResource(R.string.save_product_action))
            }
            
            // Botones secundarios
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                OutlinedButton(
                    onClick = onLoadAllergens,
                    enabled = !isLoading,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(stringResource(R.string.load_allergens_action))
                }
                
                OutlinedButton(
                    onClick = onResetForm,
                    enabled = !isLoading,
                    modifier = Modifier.weight(1f)
                ) {
                    Text(stringResource(R.string.reset_form_action))
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun ProductFormExerciseScreenPreview() {
    MercaStockTheme {
        ProductFormExerciseContent(
            paddingValues = PaddingValues(0.dp),
            productName = "Producto de prueba",
            onProductNameChange = {},
            price = "12.99",
            onPriceChange = {},
            stock = 10,
            onStockChange = {},
            category = "Alimentación",
            onCategoryChange = {},
            allergens = emptyList(),
            onAllergensChange = {},
            nameError = "",
            priceError = "",
            stockError = "",
            categoryError = "",
            expensiveFormScore = 1250,
            isFormCompletelyValid = true,
            selectedAllergensText = "2 alérgenos seleccionados",
            savedProductsCount = 3,
            isLoading = false,
            alpha = 1f,
            nameFocusRequester = FocusRequester(),
            priceFocusRequester = FocusRequester(),
            categoryFocusRequester = FocusRequester(),
            focusManager = LocalFocusManager.current,
            onSaveProduct = {},
            onLoadAllergens = {},
            onResetForm = {}
        )
    }
}
