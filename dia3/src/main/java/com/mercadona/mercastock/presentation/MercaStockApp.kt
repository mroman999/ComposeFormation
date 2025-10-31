package com.mercadona.mercastock.presentation

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.outlined.Home
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavHostController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.mercadona.mercastock.dia3.R
import com.mercadona.mercastock.presentation.navigation.MercaStockNavHost
import com.mercadona.mercastock.presentation.navigation.NavigationDestination
import com.mercadona.mercastock.presentation.navigation.getCurrentNavigationDestination
import com.mercadona.mercastock.presentation.ui.model.BottomBarItem
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MercaStockApp(
    navController: NavHostController = rememberNavController(),
    globalViewModel: GlobalViewModel = hiltViewModel(),
) {
    val snackbarData by globalViewModel.currentSnackbarData.collectAsState()
    val scope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    LaunchedEffect(snackbarData) {
        if (snackbarData != null) {
            scope.launch {
                snackbarHostState.showSnackbar(
                    message = snackbarData!!.message,
                    duration = snackbarData!!.duration
                )
            }
        }
    }

    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentDestination = navBackStackEntry?.destination

    val currentNavigationDestination = getCurrentNavigationDestination(navBackStackEntry)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(stringResource(currentNavigationDestination.title))
                },
                navigationIcon = {
                    if (currentNavigationDestination.showBackButton) {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                contentDescription = null
                            )
                        }
                    }
                }
            )
        },
        bottomBar = {
            if (currentNavigationDestination.showBottomBar) {
                NavigationBar {
                    bottomBarItems.forEach { item ->
                        val isSelected = currentDestination?.hierarchy?.any {
                            it.route?.contains(item.route::class.simpleName ?: "") == true
                        } == true

                        NavigationBarItem(
                            icon = {
                                Icon(
                                    imageVector = if (isSelected) item.selectedIcon else item.unselectedIcon,
                                    contentDescription = null
                                )
                            },
                            label = { Text(stringResource(item.titleRes)) },
                            selected = isSelected,
                            onClick = {
                                navController.navigate(item.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        )
                    }
                }
            }
        },
        snackbarHost = { SnackbarHost(hostState = snackbarHostState) }
    ) { innerPadding ->
        MercaStockNavHost(
            navController = navController,
            modifier = Modifier.padding(innerPadding)
        )
    }
}

private val bottomBarItems =  listOf(
    BottomBarItem(
        titleRes = R.string.nav_dashboard,
        selectedIcon = Icons.Filled.Home,
        unselectedIcon = Icons.Outlined.Home,
        route = NavigationDestination.Dashboard
    ),
    BottomBarItem(
        titleRes = R.string.help_title,
        selectedIcon = Icons.Filled.Info,
        unselectedIcon = Icons.Outlined.Info,
        route = NavigationDestination.Help
    )
)

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
private fun TopAppBarWithBackButtonPreview() {
    MaterialTheme {
        TopAppBar(
            title = { Text(stringResource(R.string.help_faq_item_title)) },
            navigationIcon = {
                IconButton(onClick = {}) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = null
                    )
                }
            }
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Preview(showBackground = true)
@Composable
private fun TopAppBarMainPreview() {
    MaterialTheme {
        TopAppBar(
            title = { Text(stringResource(R.string.nav_dashboard)) }
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun BottomBarPreview() {
    MaterialTheme {
        NavigationBar {

            bottomBarItems.forEachIndexed { index, item ->
                NavigationBarItem(
                    icon = {
                        Icon(
                            imageVector = if (index == 0) item.selectedIcon else item.unselectedIcon,
                            contentDescription = null
                        )
                    },
                    label = { Text(stringResource(item.titleRes)) },
                    selected = index == 0,
                    onClick = {}
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun BottomBarItemPreview() {
    MaterialTheme {
        NavigationBar {
            NavigationBarItem(
                icon = {
                    Icon(
                        imageVector = Icons.Filled.Home,
                        contentDescription = null
                    )
                },
                label = { Text(stringResource(R.string.nav_dashboard)) },
                selected = true,
                onClick = {}
            )
        }
    }
}

