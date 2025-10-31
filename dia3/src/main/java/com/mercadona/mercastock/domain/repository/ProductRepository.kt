package com.mercadona.mercastock.domain.repository

import com.mercadona.mercastock.domain.model.Product
import kotlinx.coroutines.flow.Flow

interface ProductRepository {
    
    fun getAllProducts(): Flow<List<Product>>
    
    suspend fun getProductById(id: String): Result<Product>
    
    suspend fun saveProduct(product: Product): Result<Product>
    
    suspend fun deleteProduct(id: String): Result<Unit>
    
    fun searchProducts(query: String): Flow<List<Product>>
}
