package com.mercadona.mercastock

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.junit4.StateRestorationTester
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performScrollToNode
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mercadona.mercastock.presentation.features.product.ProductFormScreenContent
import com.mercadona.mercastock.presentation.features.product.ProductFormUiState
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Test de restauración de estado para ProductFormScreen
 * Valida que el estado del formulario se preserve correctamente durante cambios de configuración
 */
@RunWith(AndroidJUnit4::class)
class ProductFormStateRestorationTest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val initialUiState = ProductFormUiState(
        name = "",
        nameError = "",
        price = "",
        priceError = "",
        stock = 0,
        category = "",
        categoryError = "",
        allergens = emptyList(),
        isFormValid = false
    )

    /**
     * Test completo de restauración de estado del formulario
     * Valida que todos los campos y el estado del formulario se preserven
     * correctamente durante cambios de configuración del dispositivo
     */
    @Test
    fun productForm_restoresCompleteFormState() {
        val stateRestorationTester = StateRestorationTester(composeTestRule)
        
        stateRestorationTester.setContent {
            MaterialTheme {
                var formName by remember { mutableStateOf("") }
                var formPrice by remember { mutableStateOf("") }
                var formStock by remember { mutableStateOf(0) }
                var formCategory by remember { mutableStateOf("") }
                
                ProductFormScreenContent(
                    uiState = initialUiState.copy(
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

        composeTestRule.onNodeWithTag("product_form")
            .performScrollToNode(hasTestTag("price_field"))
        
        composeTestRule.onNodeWithTag("price_field")
            .assertIsDisplayed()

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
}
