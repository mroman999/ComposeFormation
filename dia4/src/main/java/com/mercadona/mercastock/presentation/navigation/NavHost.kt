package com.mercadona.mercastock.presentation.navigation

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.NavHostController
import androidx.navigation.compose.composable
import androidx.navigation.compose.navigation
import androidx.navigation.toRoute
import com.mercadona.mercastock.presentation.features.dashboard.DashboardScreen
import com.mercadona.mercastock.presentation.features.product.ProductFormScreen
import androidx.navigation.compose.NavHost as ComposeNavHost

@Composable
fun MercaStockNavHost(
    navController: NavHostController,
    modifier: Modifier = Modifier
) {
    ComposeNavHost(
        navController = navController,
        startDestination = NavGraphRoutes.DashboardNavGraphRoute,
        modifier = modifier
    ) {
        addDashboardNavGraph(navController)

        addHelpNavGraph(navController)
    }
}