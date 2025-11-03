package com.mercadona.mercastock

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.assertIsEnabled
import androidx.compose.ui.test.assertIsNotEnabled
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.compose.ui.test.performTextInput
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mercadona.mercastock.domain.model.Allergen
import com.mercadona.mercastock.domain.model.Product
import com.mercadona.mercastock.presentation.ui.components.NameField
import com.mercadona.mercastock.presentation.ui.components.PriceField
import com.mercadona.mercastock.presentation.ui.components.ProductCard
import com.mercadona.mercastock.presentation.ui.components.StockControl
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

@RunWith(AndroidJUnit4::class)
class AccessibilityComponentsUITest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val sampleProduct = Product(
        id = "test_product",
        name = "Producto de Prueba",
        price = 5.99,
        stock = 15,
        category = "Categoría Test",
        allergens = listOf(
            Allergen(1, "Lactosa", true),
            Allergen(2, "Gluten", true)
        )
    )

    private val lowStockProduct = Product(
        id = "low_stock_product",
        name = "Producto Stock Bajo",
        price = 3.50,
        stock = 2, // Stock bajo
        category = "Test",
        allergens = emptyList()
    )

    @Test
    fun productCard_accessibilityDescription() {
        composeTestRule.setContent {
            ProductCard(
                product = sampleProduct,
                onEdit = {},
                onDelete = {}
            )
        }

        composeTestRule.onNode(
            hasContentDescription("Producto Producto de Prueba", substring = true) and
            hasContentDescription("categoría Categoría Test", substring = true) and
            hasContentDescription("precio 5.99 euros", substring = true) and
            hasContentDescription("stock 15 unidades", substring = true) and
            hasContentDescription("alérgenos: Lactosa, Gluten", substring = true)
        ).assertIsDisplayed()
    }

    @Test
    fun productCard_lowStockAccessibility() {
        composeTestRule.setContent {
            ProductCard(
                product = lowStockProduct,
                onEdit = {},
                onDelete = {}
            )
        }

        composeTestRule.onNode(
            hasContentDescription("stock bajo", substring = true)
        ).assertIsDisplayed()
    }

    @Test
    fun productCard_editButtonAccessibility() {
        composeTestRule.setContent {
            ProductCard(
                product = sampleProduct,
                onEdit = {},
                onDelete = {}
            )
        }

        composeTestRule.onNodeWithContentDescription("Editar producto Producto de Prueba")
            .assertIsDisplayed()
    }

    @Test
    fun productCard_deleteButtonAccessibility() {
        composeTestRule.setContent {
            ProductCard(
                product = sampleProduct,
                onEdit = {},
                onDelete = {}
            )
        }

        composeTestRule.onNodeWithContentDescription("Eliminar producto Producto de Prueba")
            .assertIsDisplayed()
    }

    @Test
    fun productCard_buttonInteractions() {
        var editClicked = false
        var deleteClicked = false

        composeTestRule.setContent {
            ProductCard(
                product = sampleProduct,
                onEdit = { editClicked = true },
                onDelete = { deleteClicked = true }
            )
        }

        composeTestRule.onNodeWithTag("edit_product_test_product").performClick()
        assert(editClicked)

        composeTestRule.onNodeWithTag("delete_product_test_product").performClick()
        assert(deleteClicked)
    }

    @Test
    fun nameField_accessibilityAndValidation() {
        var nameValue = ""
        
        composeTestRule.setContent {
            NameField(
                name = nameValue,
                onNameChange = { nameValue = it },
                nameError = "Error de prueba"
            )
        }

        // Verificar que el campo está presente y es accesible
        composeTestRule.onNodeWithTag("name_field")
            .assertIsDisplayed()

        // Verificar que el mensaje de error se muestra
        composeTestRule.onNodeWithText("Error de prueba").assertIsDisplayed()

        // Probar entrada de texto
        composeTestRule.onNodeWithTag("name_field")
            .performTextInput("Nuevo Nombre")
        
        // Verificar que el callback se llamó (el valor se actualiza en el callback)
        composeTestRule.waitForIdle()
        assert(nameValue == "Nuevo Nombre")
    }

    @Test
    fun priceField_accessibilityAndValidation() {
        var priceValue = ""
        
        composeTestRule.setContent {
            PriceField(
                price = priceValue,
                onPriceChange = { priceValue = it },
                priceError = "Precio inválido"
            )
        }

        // Verificar que el campo está presente
        composeTestRule.onNodeWithTag("price_field").assertIsDisplayed()

        // Verificar mensaje de error
        composeTestRule.onNodeWithText("Precio inválido").assertIsDisplayed()

        // Probar entrada de precio
        composeTestRule.onNodeWithTag("price_field")
            .performTextInput("12.99")
        
        assert(priceValue == "12.99")
    }

    @Test
    fun stockControl_accessibilityAndInteraction() {
        var stockValue = 10
        var callbackValue = 0
        
        composeTestRule.setContent {
            StockControl(
                stock = stockValue,
                onStockChange = { callbackValue = it }
            )
        }

        // Verificar que los controles están presentes
        composeTestRule.onNodeWithTag("stock_decrement_button").assertIsDisplayed()
        composeTestRule.onNodeWithTag("stock_increment_button").assertIsDisplayed()
        composeTestRule.onNodeWithTag("stock_display").assertIsDisplayed()

        // Probar incremento
        composeTestRule.onNodeWithTag("stock_increment_button").performClick()
        composeTestRule.waitForIdle()
        assert(callbackValue == 11)

        // Probar decremento (resetear para probar desde valor inicial)
        callbackValue = 0
        composeTestRule.onNodeWithTag("stock_decrement_button").performClick()
        composeTestRule.waitForIdle()
        assert(callbackValue == 9) // 10 - 1
    }

    @Test
    fun stockControl_accessibilityDescriptions() {
        composeTestRule.setContent {
            StockControl(
                stock = 5,
                onStockChange = {}
            )
        }

        // Verificar descripciones de accesibilidad de los botones
        composeTestRule.onNodeWithContentDescription("Disminuir stock")
            .assertIsDisplayed()

        composeTestRule.onNodeWithContentDescription("Aumentar stock")
            .assertIsDisplayed()
    }

    @Test
    fun stockControl_minimumValueHandling() {
        var stockValue = 0
        
        composeTestRule.setContent {
            StockControl(
                stock = stockValue,
                onStockChange = { stockValue = it }
            )
        }

        // El botón de decremento debería estar deshabilitado en 0
        composeTestRule.onNodeWithTag("stock_decrement_button")
            .assertIsNotEnabled()

        // El botón de incremento debería funcionar
        composeTestRule.onNodeWithTag("stock_increment_button")
            .assertIsEnabled()
            .performClick()
        
        assert(stockValue == 1)
    }

    @Test
    fun productCard_testTagsPresent() {
        composeTestRule.setContent {
            ProductCard(
                product = sampleProduct,
                onEdit = {},
                onDelete = {}
            )
        }

        // Verificar que todos los test tags están presentes
        composeTestRule.onNodeWithTag("product_card_test_product").assertExists()
        composeTestRule.onNodeWithTag("edit_product_test_product").assertExists()
        composeTestRule.onNodeWithTag("delete_product_test_product").assertExists()
    }

    @Test
    fun productCard_semanticMerging() {
        composeTestRule.setContent {
            ProductCard(
                product = sampleProduct,
                onEdit = {},
                onDelete = {}
            )
        }

        // Verificar que la información se agrupa semánticamente
        // (mergeDescendants = true debería hacer que toda la información del producto
        // se lea como una sola descripción)
        composeTestRule.onNodeWithTag("product_card_test_product")
            .assertIsDisplayed()
    }

    @Test
    fun components_keyboardNavigation() {
        composeTestRule.setContent {
            NameField(
                name = "",
                onNameChange = {},
                nameError = ""
            )
        }

        // Verificar que el campo está presente y puede recibir interacciones
        composeTestRule.onNodeWithTag("name_field")
            .assertIsDisplayed()
        
        // Probar entrada de texto básica para verificar navegación por teclado
        composeTestRule.onNodeWithTag("name_field")
            .performTextInput("Test")
        
        // Verificar que el campo sigue siendo accesible
        composeTestRule.onNodeWithTag("name_field")
            .assertIsDisplayed()
    }

    @Test
    fun productCard_noAllergensHandling() {
        val productWithoutAllergens = sampleProduct.copy(allergens = emptyList())
        
        composeTestRule.setContent {
            ProductCard(
                product = productWithoutAllergens,
                onEdit = {},
                onDelete = {}
            )
        }

        // Verificar que la tarjeta se muestra correctamente sin alérgenos
        composeTestRule.onNodeWithTag("product_card_test_product")
            .assertIsDisplayed()
        
        // Verificar que el producto se muestra con su información básica (nombre correcto)
        composeTestRule.onNodeWithText("Producto de Prueba").assertIsDisplayed()
        
        // Verificar que se muestra la categoría
        composeTestRule.onNodeWithText("Categoría Test").assertIsDisplayed()
    }
}
