package com.mercadona.mercastock

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.DeviceConfigurationOverride
import androidx.compose.ui.test.ExperimentalTestApi
import androidx.compose.ui.test.FontScale
import androidx.compose.ui.test.ForcedSize
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.assertTextContains
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.StateRestorationTester
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performCustomAccessibilityActionWithLabel
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performScrollToNode
import androidx.compose.ui.test.performTextInput
import androidx.compose.ui.test.then
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mercadona.mercastock.presentation.features.product.ProductFormScreenContent
import com.mercadona.mercastock.presentation.features.product.ProductFormUiState
import com.mercadona.mercastock.presentation.ui.theme.MercaStockTheme
import com.mercadona.mercastock.utils.Constants
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class ProductFormScreenTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val validUiState = ProductFormUiState(
        name = "",
        price = "9.99",
        stock = 25,
        category = "Categoría Test",
        allergens = Constants.DEFAULT_ALLERGENS,
        isFormValid = true,
        nameError = "",
        priceError = "",
        categoryError = ""
    )

    private val invalidUiState = ProductFormUiState(
        name = "",
        price = "12.99",
        stock = 10,
        category = "Test",
        allergens = Constants.DEFAULT_ALLERGENS,
        isFormValid = false,
        nameError = "El nombre es requerido",
        priceError = "",
        categoryError = ""
    )

    @Test
    fun when_name_is_introduced_nameField_is_changed() {
        var name = ""

        composeTestRule.setContent {
            ProductFormScreenContent(
                uiState = validUiState.copy(name = ""),
                isLoading = false,
                onNameChange = { name = it },
                onPriceChange = {},
                onStockChange = {},
                onCategoryChange = {},
                onAllergensChange = {},
                onSave = {}
            )
        }

        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithTag("name_field")
            .assertIsDisplayed()
            .performTextInput("Nuevo Producto")

        assert(name == "Nuevo Producto")
    }

    @Test
    fun productForm_saveButtonDisabledIfNoName() {
        composeTestRule.setContent {

            MercaStockTheme {
                ProductFormScreenContent(
                    uiState = invalidUiState,
                    onNameChange = { },
                    onPriceChange = { },
                    onStockChange = { },
                    onCategoryChange = { },
                    onAllergensChange = { },
                    onSave = { },
                    isLoading = false
                )
            }
        }

        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithTag("product_form")
            .performScrollToNode(hasTestTag("save_product_button"))

        composeTestRule.onNodeWithTag("save_product_button")
            .assertIsDisplayed()
            .assertIsNotEnabled()
    }

    @OptIn(ExperimentalTestApi::class)
    @Test
    fun productForm_saveButtonEnabledDisabled() {
        composeTestRule.setContent {
            var formName by remember { mutableStateOf("") }

            // Validación simple: todos los campos requeridos deben estar llenos
            val isFormValid = formName.isNotBlank()

            MercaStockTheme {
                ProductFormScreenContent(
                    uiState = invalidUiState.copy(
                        name = formName,
                        isFormValid = isFormValid,
                        nameError = if (formName.isBlank()) "El nombre es requerido" else "",
                    ),
                    onNameChange = { formName = it },
                    onPriceChange = { },
                    onStockChange = { },
                    onCategoryChange = { },
                    onAllergensChange = { },
                    onSave = { },
                    isLoading = false
                )
            }
        }

        composeTestRule.waitForIdle()

        // ESTADO INICIAL: Formulario vacío - botón deshabilitado
        composeTestRule.onNodeWithTag("product_form")
            .performScrollToNode(hasTestTag("save_product_button"))

        composeTestRule.onNodeWithTag("save_product_button")
            .assertIsDisplayed()
            .assertIsNotEnabled() // DESHABILITADO

        // PASO 1: Llenar nombre - aún inválido
        composeTestRule.onNodeWithTag("product_form")
            .performScrollToNode(hasTestTag("name_field"))

        composeTestRule.onNodeWithTag("name_field")
            .performTextInput("Nuevo producto")

        composeTestRule.waitForIdle()

        composeTestRule.onNodeWithTag("product_form")
            .performScrollToNode(hasTestTag("save_product_button"))

        // RESULTADO FINAL: Botón habilitado
        composeTestRule.onNodeWithTag("save_product_button")
            .assertIsDisplayed()
            .assertIsEnabled() // HABILITADO

        composeTestRule.onNode(
            hasContentDescription("Producto Producto de Prueba", substring = true)
        ).performCustomAccessibilityActionWithLabel("Etiqueta talkback")
    }

    @Test
    fun productForm_restoresCompleteFormState() {
        val stateRestorationTester = StateRestorationTester(composeTestRule)

        stateRestorationTester.setContent {
            MaterialTheme {
                var formName by rememberSaveable { mutableStateOf("") }
                var formPrice by rememberSaveable { mutableStateOf("") }
                var formStock by rememberSaveable { mutableStateOf(0) }
                var formCategory by rememberSaveable { mutableStateOf("") }

                ProductFormScreenContent(
                    uiState = validUiState.copy(
                        name = formName,
                        price = formPrice,
                        stock = formStock,
                        category = formCategory,
                        isFormValid = formName.isNotBlank() &&
                                formPrice.isNotBlank() &&
                                formCategory.isNotBlank()
                    ),
                    onNameChange = { newName -> formName = newName },
                    onPriceChange = { newPrice -> formPrice = newPrice },
                    onStockChange = { newStock -> formStock = newStock },
                    onCategoryChange = { newCategory -> formCategory = newCategory },
                    onAllergensChange = { },
                    onSave = { },
                    isLoading = false
                )
            }
        }

        // Esperar a que el formulario se renderice
        composeTestRule.waitForIdle()

        // Verificar que el formulario está presente inicialmente
        composeTestRule.onNodeWithTag("product_form")
            .assertIsDisplayed()

        // Llenar el formulario usando scroll correcto para LazyColumn
        composeTestRule.onNodeWithTag("product_form")
            .performScrollToNode(hasTestTag("name_field"))

        composeTestRule.onNodeWithTag("name_field")
            .performTextInput("Producto Test")

        composeTestRule.onNodeWithTag("product_form")
            .performScrollToNode(hasTestTag("price_field"))

        composeTestRule.onNodeWithTag("price_field")
            .performTextInput("15.99")

        // Incrementar stock usando scroll
        composeTestRule.onNodeWithTag("product_form")
            .performScrollToNode(hasTestTag("stock_increment_button"))

        composeTestRule.onNodeWithTag("stock_increment_button")
            .performClick()
            .performClick() // Stock = 2

        // Simular cambio de configuración (rotación de pantalla, etc.)
        stateRestorationTester.emulateSavedInstanceStateRestore()

        // Esperar a que se complete la restauración
        composeTestRule.waitForIdle()

        // Verificar que el formulario se restauró correctamente
        composeTestRule.onNodeWithTag("product_form")
            .assertIsDisplayed()

        // Verificar que los campos principales están presentes después de la restauración
        composeTestRule.onNodeWithTag("product_form")
            .performScrollToNode(hasTestTag("name_field"))

        composeTestRule.onNodeWithTag("name_field")
            .assertIsDisplayed()
            .assertTextContains("Producto Test")

        composeTestRule.onNodeWithTag("product_form")
            .performScrollToNode(hasTestTag("price_field"))

        composeTestRule.onNodeWithTag("price_field")
            .assertIsDisplayed()
            .assertTextContains("15.99")

        composeTestRule.onNodeWithTag("product_form")
            .performScrollToNode(hasTestTag("stock_increment_button"))

        composeTestRule.onNodeWithTag("stock_increment_button")
            .assertIsDisplayed()

        // Verificar que el botón de guardar está accesible después de la restauración
        composeTestRule.onNodeWithTag("product_form")
            .performScrollToNode(hasTestTag("save_product_button"))

        composeTestRule.onNodeWithTag("save_product_button")
            .assertIsDisplayed()
    }

    @Test
    fun productForm_fieldsAccessibleWithScroll() {
        composeTestRule.setContent {
            // Configurar pantalla muy pequeña para forzar scroll
            DeviceConfigurationOverride(
                DeviceConfigurationOverride.ForcedSize(DpSize(100.dp, 200.dp)) then
                        DeviceConfigurationOverride.FontScale(2f)
            ) {
                MaterialTheme {
                    var uiState by remember { mutableStateOf(validUiState) }

                    ProductFormScreenContent(
                        uiState = uiState,
                        onNameChange = { },
                        onPriceChange = { },
                        onStockChange = { },
                        onCategoryChange = { },
                        onAllergensChange = { },
                        onSave = { },
                        isLoading = false
                    )
                }
            }
        }

        // Esperar a que el formulario se renderice
        composeTestRule.waitForIdle()

        // Verificar que el formulario está presente
        composeTestRule.onNodeWithTag("product_form")
            .assertIsDisplayed()

        // Verificar que los campos son accesibles con scroll
        composeTestRule.onNodeWithTag("product_form")
            .performScrollToNode(hasTestTag("name_field"))
            .assertIsDisplayed()

        composeTestRule.onNodeWithTag("product_form")
            .performScrollToNode(hasTestTag("price_field"))
            .assertIsDisplayed()

        composeTestRule.onNodeWithTag("product_form")
            .performScrollToNode(hasTestTag("stock_increment_button"))
            .assertIsDisplayed()

        // Opción 1: Usar performScrollToNode específicamente para LazyColumn
        composeTestRule.onNodeWithTag("product_form")
            .performScrollToNode(hasTestTag("save_product_button"))

        composeTestRule.onNodeWithTag("save_product_button")
            .assertIsDisplayed()
    }
}