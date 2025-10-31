package com.mercadona.mercastock

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.mercadona.mercastock.presentation.MercaStockApp
import com.mercadona.mercastock.presentation.ui.theme.MercaStockTheme
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            MercaStockTheme {
                MercaStockApp()
            }
        }
    }
}
