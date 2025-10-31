package com.mercadona.mercastock

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.test.DeviceConfigurationOverride
import androidx.compose.ui.test.FontScale
import androidx.compose.ui.test.ForcedSize
import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.performScrollTo
import androidx.compose.ui.test.performScrollToNode
import androidx.compose.ui.test.performTouchInput
import androidx.compose.ui.test.swipeUp
import androidx.compose.ui.test.hasTestTag
import androidx.compose.ui.test.then
import androidx.compose.ui.unit.DpSize
import androidx.compose.ui.unit.dp
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mercadona.mercastock.presentation.features.product.ProductFormScreenContent
import com.mercadona.mercastock.presentation.features.product.ProductFormUiState
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Tests para validar DeviceConfigurationOverride en pantallas pequeñas
 */
@RunWith(AndroidJUnit4::class)
class DeviceConfigurationTest {

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

    @Test
    fun productForm_fieldsAccessibleWithScroll() {
        composeTestRule.setContent {
            // Configurar pantalla muy pequeña para forzar scroll
            DeviceConfigurationOverride(
                DeviceConfigurationOverride.ForcedSize(DpSize(100.dp, 200.dp)) then
                DeviceConfigurationOverride.FontScale(2f)
            ) {
                MaterialTheme {
                    var uiState by remember { mutableStateOf(initialUiState) }

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
            .assertIsNotEnabled()


    }
}
