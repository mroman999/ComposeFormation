package com.mercadona.mercastock

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.hasStateDescription
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mercadona.mercastock.presentation.features.product.ProductFormScreenContent
import com.mercadona.mercastock.presentation.features.product.ProductFormUiState
import com.mercadona.mercastock.utils.Constants
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Tests de UI para ProductFormScreen con enfoque en accesibilidad y validaciones
 */
@RunWith(AndroidJUnit4::class)
class ProductFormScreenUITest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val validUiState = ProductFormUiState(
        name = "Producto Test",
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
        price = "invalid",
        stock = 0,
        category = "",
        allergens = emptyList(),
        isFormValid = false,
        nameError = "El nombre es requerido",
        priceError = "Precio inválido",
        categoryError = "Selecciona una categoría"
    )

    @Test
    fun productFormScreen_displaysCorrectly() {
        composeTestRule.setContent {
            ProductFormScreenContent(
                uiState = validUiState,
                isLoading = false,
                onNameChange = {},
                onPriceChange = {},
                onStockChange = {},
                onCategoryChange = {},
                onAllergensChange = {},
                onSave = {}
            )
        }

        // Verificar que el formulario se muestra
        composeTestRule.onNodeWithTag("product_form").assertIsDisplayed()
        
        // Verificar que el botón de guardar está presente
        composeTestRule.onNodeWithTag("save_product_button").assertIsDisplayed()
    }

    @Test
    fun productFormScreen_showsLoadingState() {
        composeTestRule.setContent {
            ProductFormScreenContent(
                uiState = validUiState,
                isLoading = true,
                onNameChange = {},
                onPriceChange = {},
                onStockChange = {},
                onCategoryChange = {},
                onAllergensChange = {},
                onSave = {}
            )
        }

        // Verificar que el indicador de carga se muestra
        composeTestRule.onNodeWithTag("product_form_loading").assertIsDisplayed()
        
        // Verificar que el formulario no se muestra durante la carga
        composeTestRule.onNodeWithTag("product_form").assertDoesNotExist()
    }

    @Test
    fun productFormScreen_validFormEnablesSaveButton() {
        composeTestRule.setContent {
            ProductFormScreenContent(
                uiState = validUiState,
                isLoading = false,
                onNameChange = {},
                onPriceChange = {},
                onStockChange = {},
                onCategoryChange = {},
                onAllergensChange = {},
                onSave = {}
            )
        }

        // Verificar que el botón está habilitado para formulario válido
        composeTestRule.onNodeWithTag("save_product_button")
            .assertIsEnabled()
    }

    @Test
    fun productFormScreen_invalidFormDisablesSaveButton() {
        composeTestRule.setContent {
            ProductFormScreenContent(
                uiState = invalidUiState,
                isLoading = false,
                onNameChange = {},
                onPriceChange = {},
                onStockChange = {},
                onCategoryChange = {},
                onAllergensChange = {},
                onSave = {}
            )
        }

        // Verificar que el botón está deshabilitado para formulario inválido
        composeTestRule.onNodeWithTag("save_product_button")
            .assertIsNotEnabled()
    }

    @Test
    fun productFormScreen_saveButtonClickTriggersCallback() {
        var saveClicked = false
        
        composeTestRule.setContent {
            ProductFormScreenContent(
                uiState = validUiState,
                isLoading = false,
                onNameChange = {},
                onPriceChange = {},
                onStockChange = {},
                onCategoryChange = {},
                onAllergensChange = {},
                onSave = { saveClicked = true }
            )
        }

        composeTestRule.onNodeWithTag("save_product_button").performClick()
        
        assert(saveClicked)
    }

    @Test
    fun productFormScreen_nameFieldInteraction() {
        var nameChanged = ""
        
        composeTestRule.setContent {
            ProductFormScreenContent(
                uiState = validUiState.copy(name = ""),
                isLoading = false,
                onNameChange = { nameChanged = it },
                onPriceChange = {},
                onStockChange = {},
                onCategoryChange = {},
                onAllergensChange = {},
                onSave = {}
            )
        }

        // Buscar el campo de nombre y escribir en él
        composeTestRule.onNodeWithTag("name_field")
            .performTextInput("Nuevo Producto")
        
        assert(nameChanged == "Nuevo Producto")
    }

    @Test
    fun productFormScreen_priceFieldInteraction() {
        var priceChanged = ""
        
        composeTestRule.setContent {
            ProductFormScreenContent(
                uiState = validUiState.copy(price = ""),
                isLoading = false,
                onNameChange = {},
                onPriceChange = { priceChanged = it },
                onStockChange = {},
                onCategoryChange = {},
                onAllergensChange = {},
                onSave = {}
            )
        }

        // Buscar el campo de precio y escribir en él
        composeTestRule.onNodeWithTag("price_field")
            .performTextInput("15.99")
        
        assert(priceChanged == "15.99")
    }

    @Test
    fun productFormScreen_stockControlInteraction() {
        var stockChanged = 0
        
        composeTestRule.setContent {
            ProductFormScreenContent(
                uiState = validUiState.copy(stock = 10),
                isLoading = false,
                onNameChange = {},
                onPriceChange = {},
                onStockChange = { stockChanged = it },
                onCategoryChange = {},
                onAllergensChange = {},
                onSave = {}
            )
        }

        // Incrementar stock
        composeTestRule.onNodeWithTag("stock_increment_button").performClick()
        
        assert(stockChanged == 11)
    }

    @Test
    fun productFormScreen_accessibilityContentDescriptions() {
        composeTestRule.setContent {
            ProductFormScreenContent(
                uiState = validUiState,
                isLoading = false,
                onNameChange = {},
                onPriceChange = {},
                onStockChange = {},
                onCategoryChange = {},
                onAllergensChange = {},
                onSave = {}
            )
        }

        // Verificar descripciones de accesibilidad
        composeTestRule.onNodeWithContentDescription("Formulario de producto")
            .assertIsDisplayed()
        
        composeTestRule.onNodeWithContentDescription("Guardar producto")
            .assertIsDisplayed()
    }

    @Test
    fun productFormScreen_semanticStates() {
        composeTestRule.setContent {
            ProductFormScreenContent(
                uiState = validUiState,
                isLoading = false,
                onNameChange = {},
                onPriceChange = {},
                onStockChange = {},
                onCategoryChange = {},
                onAllergensChange = {},
                onSave = {}
            )
        }

        // Verificar estado semántico del formulario válido
        composeTestRule.onNode(hasStateDescription("Formulario válido"))
            .assertIsDisplayed()
        
        // Verificar estado del botón habilitado
        composeTestRule.onNode(hasStateDescription("Habilitado"))
            .assertIsDisplayed()
    }

    @Test
    fun productFormScreen_invalidFormSemanticStates() {
        composeTestRule.setContent {
            ProductFormScreenContent(
                uiState = invalidUiState,
                isLoading = false,
                onNameChange = {},
                onPriceChange = {},
                onStockChange = {},
                onCategoryChange = {},
                onAllergensChange = {},
                onSave = {}
            )
        }

        // Verificar estado semántico del formulario inválido
        composeTestRule.onNode(hasStateDescription("Formulario con errores"))
            .assertIsDisplayed()
        
        // Verificar estado del botón deshabilitado
        composeTestRule.onNode(hasStateDescription("Deshabilitado por errores en el formulario"))
            .assertIsDisplayed()
    }

    @Test
    fun productFormScreen_loadingStateAccessibility() {
        composeTestRule.setContent {
            ProductFormScreenContent(
                uiState = validUiState,
                isLoading = true,
                onNameChange = {},
                onPriceChange = {},
                onStockChange = {},
                onCategoryChange = {},
                onAllergensChange = {},
                onSave = {}
            )
        }

        // Verificar accesibilidad del estado de carga
        composeTestRule.onNodeWithContentDescription("Guardando producto")
            .assertIsDisplayed()
        
        composeTestRule.onNode(hasStateDescription("En proceso de guardado"))
            .assertIsDisplayed()
    }

    @Test
    fun productFormScreen_errorMessagesDisplay() {
        composeTestRule.setContent {
            ProductFormScreenContent(
                uiState = invalidUiState,
                isLoading = false,
                onNameChange = {},
                onPriceChange = {},
                onStockChange = {},
                onCategoryChange = {},
                onAllergensChange = {},
                onSave = {}
            )
        }

        // Verificar que los mensajes de error se muestran
        composeTestRule.onNodeWithText("El nombre es requerido").assertIsDisplayed()
        composeTestRule.onNodeWithText("Precio inválido").assertIsDisplayed()
        composeTestRule.onNodeWithText("Selecciona una categoría").assertIsDisplayed()
    }

    @Test
    fun productFormScreen_roleSemantics() {
        composeTestRule.setContent {
            ProductFormScreenContent(
                uiState = validUiState,
                isLoading = false,
                onNameChange = {},
                onPriceChange = {},
                onStockChange = {},
                onCategoryChange = {},
                onAllergensChange = {},
                onSave = {}
            )
        }

        // Verificar roles semánticos
        composeTestRule.onNodeWithTag("save_product_button")
            .assertIsDisplayed()
    }
}
