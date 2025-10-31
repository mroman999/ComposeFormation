package com.mercadona.mercastock.presentation.ui.model

data class ProductStatisticsItem(
    val totalProducts: Int,
    val totalValue: Double,
    val lowStockCount: Int,
    val averagePrice: Double
)