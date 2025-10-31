package com.mercadona.mercastock.presentation.features.dashboard

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.semantics.*
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.mercadona.mercastock.dia4.R
import com.mercadona.mercastock.domain.model.Product
import com.mercadona.mercastock.domain.model.Allergen
import com.mercadona.mercastock.presentation.ui.model.ProductStatisticsItem
import com.mercadona.mercastock.presentation.ui.components.ProductCard
import com.mercadona.mercastock.presentation.ui.components.SearchBar
import com.mercadona.mercastock.presentation.ui.components.StatisticsCard

@Composable
fun DashboardScreen(
    onNavigateToProductForm: (String) -> Unit,
    onCreateNewProduct: () -> Unit,
    viewModel: DashboardViewModel = hiltViewModel()
) {
    val products by viewModel.products.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val searchQuery by viewModel.searchQuery.collectAsState()
    
    val statistics by remember {
        derivedStateOf {
            ProductStatisticsItem(
                totalProducts = products.size,
                totalValue = products.sumOf { it.getTotalStockValue() },
                lowStockCount = products.count { it.isLowStock() },
                averagePrice = if (products.isNotEmpty()) products.map { it.price }.average() else 0.0
            )
        }
    }

    DashboardScreenContent(
        products = products,
        isLoading = isLoading,
        searchQuery = searchQuery,
        statistics = statistics,
        onSearchQueryChange = viewModel::updateSearchQuery,
        onNavigateToProductForm = onNavigateToProductForm,
        onCreateNewProduct = onCreateNewProduct,
        onDeleteProduct = { productId -> viewModel.deleteProduct(productId) }
    )
}

@Composable
fun DashboardScreenContent(
    isLoading: Boolean,
    products: List<Product>,
    statistics: ProductStatisticsItem,
    searchQuery: String,
    onSearchQueryChange: (String) -> Unit,
    onNavigateToProductForm: (String) -> Unit,
    onCreateNewProduct: () -> Unit,
    onDeleteProduct: (String) -> Unit
) {
    val dashboardDescription = stringResource(R.string.accessibility_dashboard_screen)
    val loadingDescription = stringResource(R.string.accessibility_loading_products)
    val loadingStateDescription = stringResource(R.string.accessibility_state_loading)
    val productsListDescription = stringResource(R.string.accessibility_products_list)
    val emptyListStateDescription = stringResource(R.string.accessibility_state_list_empty)
    val productsAvailableStateDescription = stringResource(R.string.accessibility_state_products_available, products.size)
    val createProductDescription = stringResource(R.string.accessibility_create_new_product)
    val addProductIconDescription = stringResource(R.string.accessibility_add_product_icon)
    
    Box(
        modifier = Modifier
            .fillMaxSize()
            .testTag("dashboard_screen")
            .semantics {
                contentDescription = dashboardDescription
            }
    ) {
        if (isLoading) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .testTag("loading_indicator")
                    .semantics {
                        contentDescription = loadingDescription
                        stateDescription = loadingStateDescription
                    },
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator()
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
                    .testTag("products_list")
                    .semantics {
                        contentDescription = productsListDescription
                        stateDescription = if (products.isEmpty()) 
                            emptyListStateDescription 
                        else 
                            productsAvailableStateDescription
                    },
                verticalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                item {
                    SearchBar(
                        query = searchQuery,
                        hint = stringResource(R.string.search_products),
                        onQueryChange = onSearchQueryChange,
                        modifier = Modifier.fillMaxWidth()
                    )
                }
                
                item {
                    StatisticsCard(statistics = statistics)
                }
                
                items(products) { product ->
                    ProductCard(
                        product = product,
                        onEdit = { onNavigateToProductForm(product.id) },
                        onDelete = { onDeleteProduct(product.id) }
                    )
                }
            }
        }
        
        FloatingActionButton(
            onClick = onCreateNewProduct,
            modifier = Modifier
                .align(Alignment.BottomEnd)
                .padding(16.dp)
                .testTag("add_product_fab")
                .semantics {
                    contentDescription = createProductDescription
                    role = Role.Button
                }
        ) {
            Icon(
                imageVector = Icons.Default.Add,
                contentDescription = addProductIconDescription
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun DashboardScreenPreview() {
    MaterialTheme {
        DashboardScreenContent(
            products = sampleProducts,
            isLoading = false,
            searchQuery = "",
            statistics = ProductStatisticsItem(
                totalProducts = 5,
                totalValue = 125.50,
                lowStockCount = 2,
                averagePrice = 25.10
            ),
            onSearchQueryChange = {},
            onNavigateToProductForm = {},
            onCreateNewProduct = {},
            onDeleteProduct = {}
        )
    }
}

/**
 * Preview del Dashboard en estado de carga
 */
@Preview(showBackground = true)
@Composable
private fun DashboardScreenLoadingPreview() {
    MaterialTheme {
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
}

/**
 * Preview del Dashboard vacío
 */
@Preview(showBackground = true)
@Composable
private fun DashboardScreenEmptyPreview() {
    MaterialTheme {
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
}

private val sampleProducts = listOf(
    Product(
        id = "1",
        name = "Leche Entera",
        price = 1.25,
        stock = 50,
        category = "Lácteos",
        allergens = listOf(Allergen(1,"Lactosa", true))
    ),
    Product(
        id = "2", 
        name = "Pan Integral",
        price = 2.50,
        stock = 5,
        category = "Panadería",
        allergens = listOf(Allergen(1,"Lactosa", true))
    ),
    Product(
        id = "3",
        name = "Manzanas Golden",
        price = 3.20,
        stock = 0,
        category = "Frutas",
        allergens = emptyList()
    ),
    Product(
        id = "4",
        name = "Yogur Natural",
        price = 4.50,
        stock = 25,
        category = "Lácteos",
        allergens = listOf(Allergen(1,"Lactosa", true))
    ),
    Product(
        id = "5",
        name = "Aceite de Oliva",
        price = 8.90,
        stock = 15,
        category = "Aceites",
        allergens = listOf(Allergen(1,"Lactosa", true))
    )
)
