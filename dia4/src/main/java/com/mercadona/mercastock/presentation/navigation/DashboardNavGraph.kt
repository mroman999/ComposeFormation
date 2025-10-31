package com.mercadona.mercastock.presentation.navigation

import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import com.mercadona.mercastock.presentation.features.dashboard.DashboardScreen
import com.mercadona.mercastock.presentation.features.product.ProductFormScreen
import androidx.navigation.compose.navigation

fun NavGraphBuilder.addDashboardNavGraph(navController: NavHostController) {
    navigation<NavGraphRoutes.DashboardNavGraphRoute>(
        startDestination = NavGraphRoutes.DashboardNavGraphRoute.root
    ) {
        composable<NavigationDestination.Dashboard> {
            DashboardScreen(
                onNavigateToProductForm = { productId ->
                    navController.navigate(
                        NavigationDestination.ProductForm(productId)
                    )
                },
                onCreateNewProduct = {
                    navController.navigate(
                        NavigationDestination.ProductForm()
                    )
                }
            )
        }

        composable<NavigationDestination.ProductForm> { backStackEntry ->
            ProductFormScreen(
                onNavigateBack = {
                    navController.popBackStack()
                }
            )
        }
    }
}