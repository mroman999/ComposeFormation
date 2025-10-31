package com.mercadona.mercastock.presentation.ui.model

import androidx.compose.ui.graphics.vector.ImageVector
import com.mercadona.mercastock.presentation.navigation.NavigationDestination

data class BottomBarItem(
    val titleRes: Int,
    val selectedIcon: ImageVector,
    val unselectedIcon: ImageVector,
    val route: NavigationDestination,
)