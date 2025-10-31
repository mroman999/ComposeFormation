package com.mercadona.mercastock

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.core.splashscreen.SplashScreen.Companion.installSplashScreen
import com.mercadona.mercastock.ui.screens.ProductFormScreen
import com.mercadona.mercastock.ui.theme.MercaStockTheme

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
