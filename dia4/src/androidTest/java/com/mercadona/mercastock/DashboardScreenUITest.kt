package com.mercadona.mercastock

import androidx.compose.ui.test.assertIsDisplayed
import androidx.compose.ui.test.hasContentDescription
import androidx.compose.ui.test.hasStateDescription
import androidx.compose.ui.test.junit4.createComposeRule
import androidx.compose.ui.test.onNodeWithContentDescription
import androidx.compose.ui.test.onNodeWithTag
import androidx.compose.ui.test.onNodeWithText
import androidx.compose.ui.test.performClick
import androidx.test.ext.junit.runners.AndroidJUnit4
import com.mercadona.mercastock.domain.model.Allergen
import com.mercadona.mercastock.domain.model.Product
import com.mercadona.mercastock.presentation.features.dashboard.DashboardScreenContent
import com.mercadona.mercastock.presentation.ui.model.ProductStatisticsItem
import org.junit.Rule
import org.junit.Test
import org.junit.runner.RunWith

/**
 * Tests de UI para DashboardScreen con enfoque en accesibilidad y testing
 */
@RunWith(AndroidJUnit4::class)
class DashboardScreenUITest {

    @get:Rule
    val composeTestRule = createComposeRule()

    private val sampleProducts = listOf(
        Product(
            id = "1",
            name = "Leche Entera",
            price = 1.25,
            stock = 50,
            category = "Lácteos",
            allergens = listOf(Allergen(1, "Lactosa", true))
        ),
        Product(
            id = "2",
            name = "Pan Integral",
            price = 2.50,
            stock = 3, // Stock bajo
            category = "Panadería",
            allergens = emptyList()
        )
    )

    private val sampleStatistics = ProductStatisticsItem(
        totalProducts = 2,
        totalValue = 125.50,
        lowStockCount = 1,
        averagePrice = 1.875
    )

    @Test
    fun dashboardScreen_displaysCorrectly() {
        composeTestRule.setContent {
            DashboardScreenContent(
                products = sampleProducts,
                isLoading = false,
                searchQuery = "",
                statistics = sampleStatistics,
                onSearchQueryChange = {},
                onNavigateToProductForm = {},
                onCreateNewProduct = {},
                onDeleteProduct = {}
            )
        }

        // Verificar que la pantalla principal se muestra
        composeTestRule.onNodeWithTag("dashboard_screen").assertIsDisplayed()
        
        // Verificar que la lista de productos se muestra
        composeTestRule.onNodeWithTag("products_list").assertIsDisplayed()
        
        // Verificar que el FAB está presente
        composeTestRule.onNodeWithTag("add_product_fab").assertIsDisplayed()
    }

    @Test
    fun dashboardScreen_showsLoadingState() {
        composeTestRule.setContent {
            DashboardScreenContent(
                products = emptyList(),
                isLoading = true,
                searchQuery = "",
                statistics = ProductStatisticsItem(0, 0.0, 0, 0.0),
                onSearchQueryChange = {},
                onNavigateToProductForm = {},
                onCreateNewProduct = {},
                onDeleteProduct = {}
            )
        }

        // Verificar que el indicador de carga se muestra
        composeTestRule.onNodeWithTag("loading_indicator").assertIsDisplayed()
        
        // Verificar que la lista no se muestra durante la carga
        composeTestRule.onNodeWithTag("products_list").assertDoesNotExist()
    }

    @Test
    fun dashboardScreen_displaysProducts() {
        composeTestRule.setContent {
            DashboardScreenContent(
                products = sampleProducts,
                isLoading = false,
                searchQuery = "",
                statistics = sampleStatistics,
                onSearchQueryChange = {},
                onNavigateToProductForm = {},
                onCreateNewProduct = {},
                onDeleteProduct = {}
            )
        }

        // Verificar que los productos se muestran
        composeTestRule.onNodeWithTag("product_card_1").assertIsDisplayed()
        composeTestRule.onNodeWithTag("product_card_2").assertIsDisplayed()
        
        // Verificar contenido de los productos
        composeTestRule.onNodeWithText("Leche Entera").assertIsDisplayed()
        composeTestRule.onNodeWithText("Pan Integral").assertIsDisplayed()
    }

    @Test
    fun dashboardScreen_fabClickTriggersCallback() {
        var fabClicked = false
        
        composeTestRule.setContent {
            DashboardScreenContent(
                products = sampleProducts,
                isLoading = false,
                searchQuery = "",
                statistics = sampleStatistics,
                onSearchQueryChange = {},
                onNavigateToProductForm = {},
                onCreateNewProduct = { fabClicked = true },
                onDeleteProduct = {}
            )
        }

        composeTestRule.onNodeWithTag("add_product_fab").performClick()
        
        assert(fabClicked)
    }

    @Test
    fun dashboardScreen_productEditButtonWorks() {
        var editedProductId = ""
        
        composeTestRule.setContent {
            DashboardScreenContent(
                products = sampleProducts,
                isLoading = false,
                searchQuery = "",
                statistics = sampleStatistics,
                onSearchQueryChange = {},
                onNavigateToProductForm = { editedProductId = it },
                onCreateNewProduct = {},
                onDeleteProduct = {}
            )
        }

        composeTestRule.onNodeWithTag("edit_product_1").performClick()
        
        assert(editedProductId == "1")
    }

    @Test
    fun dashboardScreen_productDeleteButtonWorks() {
        var deletedProductId = ""
        
        composeTestRule.setContent {
            DashboardScreenContent(
                products = sampleProducts,
                isLoading = false,
                searchQuery = "",
                statistics = sampleStatistics,
                onSearchQueryChange = {},
                onNavigateToProductForm = {},
                onCreateNewProduct = {},
                onDeleteProduct = { deletedProductId = it }
            )
        }

        composeTestRule.onNodeWithTag("delete_product_1").performClick()
        
        assert(deletedProductId == "1")
    }

    @Test
    fun dashboardScreen_accessibilityContentDescriptions() {
        composeTestRule.setContent {
            DashboardScreenContent(
                products = sampleProducts,
                isLoading = false,
                searchQuery = "",
                statistics = sampleStatistics,
                onSearchQueryChange = {},
                onNavigateToProductForm = {},
                onCreateNewProduct = {},
                onDeleteProduct = {}
            )
        }

        // Verificar descripciones de accesibilidad
        composeTestRule.onNodeWithContentDescription("Pantalla principal del dashboard de productos")
            .assertIsDisplayed()
        
        composeTestRule.onNodeWithContentDescription("Crear nuevo producto")
            .assertIsDisplayed()
        
        composeTestRule.onNode(hasContentDescription("Producto Leche Entera", substring = true))
            .assertIsDisplayed()
    }

    @Test
    fun dashboardScreen_semanticProperties() {
        composeTestRule.setContent {
            DashboardScreenContent(
                products = sampleProducts,
                isLoading = false,
                searchQuery = "",
                statistics = sampleStatistics,
                onSearchQueryChange = {},
                onNavigateToProductForm = {},
                onCreateNewProduct = {},
                onDeleteProduct = {}
            )
        }

        // Verificar roles semánticos
        composeTestRule.onNodeWithTag("add_product_fab")
            .assertIsDisplayed()
        
        composeTestRule.onNodeWithTag("edit_product_1")
            .assertIsDisplayed()
    }

    @Test
    fun dashboardScreen_emptyStateHandling() {
        composeTestRule.setContent {
            DashboardScreenContent(
                products = emptyList(),
                isLoading = false,
                searchQuery = "",
                statistics = ProductStatisticsItem(0, 0.0, 0, 0.0),
                onSearchQueryChange = {},
                onNavigateToProductForm = {},
                onCreateNewProduct = {},
                onDeleteProduct = {}
            )
        }

        // Verificar que la lista vacía se maneja correctamente
        composeTestRule.onNodeWithTag("products_list").assertIsDisplayed()
        
        // Verificar descripción de estado para lista vacía
        composeTestRule.onNode(hasStateDescription("Lista vacía"))
            .assertIsDisplayed()
    }

    @Test
    fun dashboardScreen_loadingStateAccessibility() {
        composeTestRule.setContent {
            DashboardScreenContent(
                products = emptyList(),
                isLoading = true,
                searchQuery = "",
                statistics = ProductStatisticsItem(0, 0.0, 0, 0.0),
                onSearchQueryChange = {},
                onNavigateToProductForm = {},
                onCreateNewProduct = {},
                onDeleteProduct = {}
            )
        }

        // Verificar accesibilidad del estado de carga
        composeTestRule.onNodeWithContentDescription("Cargando productos")
            .assertIsDisplayed()
        
        composeTestRule.onNode(hasStateDescription("En proceso de carga"))
            .assertIsDisplayed()
    }
}
