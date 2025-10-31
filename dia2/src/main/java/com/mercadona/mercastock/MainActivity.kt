package com.mercadona.mercastock

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.mercadona.mercastock.ui.screens.ProductFormScreen
import com.mercadona.mercastock.ui.theme.MercaStockTheme
import dagger.hilt.android.AndroidEntryPoint

/**
 * Main Activity for Día 2 - Advanced Product Form
 * 
 * Demonstrates advanced Jetpack Compose concepts:
 * - ViewModel for state management
 * - rememberSaveable for configuration changes
 * - Performance optimization with remember()
 * - LaunchedEffect, SideEffect, DisposableEffect
 * - derivedStateOf for computed state
 * - rememberCoroutineScope for manual coroutines
 */
@AndroidEntryPoint
class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        installSplashScreen()

        setContent {
            MercaStockTheme {
                ProductFormScreen()
            }
        }
    }
}