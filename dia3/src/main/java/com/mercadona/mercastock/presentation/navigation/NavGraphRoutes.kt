package com.mercadona.mercastock.presentation.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed class NavGraphRoutes(
    val root: NavigationDestination
) {
    @Serializable
    data object DashboardNavGraphRoute: NavGraphRoutes(root = NavigationDestination.Dashboard)

    @Serializable
    data object HelpNavGraphRoute: NavGraphRoutes(root = NavigationDestination.Help)
}