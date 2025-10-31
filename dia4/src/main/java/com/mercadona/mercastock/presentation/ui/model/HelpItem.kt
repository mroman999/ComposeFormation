package com.mercadona.mercastock.presentation.ui.model

import androidx.compose.ui.graphics.vector.ImageVector

data class HelpItem(
    val title: String,
    val description: String,
    val icon: ImageVector,
    val onClick: () -> Unit,
)