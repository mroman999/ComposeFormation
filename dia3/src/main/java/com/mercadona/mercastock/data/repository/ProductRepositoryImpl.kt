package com.mercadona.mercastock.data.repository

import com.mercadona.mercastock.domain.model.Product
import com.mercadona.mercastock.domain.repository.ProductRepository
import com.mercadona.mercastock.domain.model.Allergen
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.map
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class ProductRepositoryImpl @Inject constructor() : ProductRepository {
    
    private val _products = MutableStateFlow(generateFakeProducts())
    
    override fun getAllProducts(): Flow<List<Product>> {
        return _products.asStateFlow()
    }
    
    override suspend fun getProductById(id: String): Result<Product> {
        return try {
            delay(500)
            
            val product = _products.value.find { it.id == id }
            if (product != null) {
                Result.success(product)
            } else {
                Result.failure(Exception("Producto no encontrado"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun saveProduct(product: Product): Result<Product> {
        return try {
            delay(1000)
            
            val currentProducts = _products.value.toMutableList()
            
            if (product.id.isEmpty()) {
                val newProduct = product.copy(id = generateNewId())
                currentProducts.add(newProduct)
                _products.value = currentProducts
                Result.success(newProduct)
            } else {
                val index = currentProducts.indexOfFirst { it.id == product.id }
                if (index != -1) {
                    currentProducts[index] = product
                    _products.value = currentProducts
                    Result.success(product)
                } else {
                    Result.failure(Exception("Producto no encontrado para actualizar"))
                }
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override suspend fun deleteProduct(id: String): Result<Unit> {
        return try {
            delay(800)
            
            val currentProducts = _products.value.toMutableList()
            val removed = currentProducts.removeIf { it.id == id }
            
            if (removed) {
                _products.value = currentProducts
                Result.success(Unit)
            } else {
                Result.failure(Exception("Producto no encontrado para eliminar"))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
    
    override fun searchProducts(query: String): Flow<List<Product>> {
        return _products.map { products ->
            if (query.isBlank()) {
                products
            } else {
                products.filter { product ->
                    product.name.contains(query, ignoreCase = true) ||
                    product.category.contains(query, ignoreCase = true)
                }
            }
        }
    }
    
    private fun generateNewId(): String {
        return "PROD_${System.currentTimeMillis()}"
    }
    
    private fun generateFakeProducts(): List<Product> {
        return listOf(
            Product(
                id = "PROD_001",
                name = "Leche Entera",
                price = 1.25,
                stock = 50,
                category = "Lácteos",
                allergens = listOf(
                    Allergen(1, "Lactosa", true),
                    Allergen(2, "Gluten", false)
                )
            ),
            Product(
                id = "PROD_002",
                name = "Pan Integral",
                price = 2.10,
                stock = 25,
                category = "Panadería",
                allergens = listOf(
                    Allergen(2, "Gluten", true),
                    Allergen(3, "Sésamo", false)
                )
            ),
            Product(
                id = "PROD_003",
                name = "Yogur Natural",
                price = 0.85,
                stock = 75,
                category = "Lácteos",
                allergens = listOf(
                    Allergen(1, "Lactosa", true)
                )
            ),
            Product(
                id = "PROD_004",
                name = "Manzanas Golden",
                price = 2.50,
                stock = 100,
                category = "Frutas",
                allergens = emptyList()
            ),
            Product(
                id = "PROD_005",
                name = "Aceite de Oliva",
                price = 4.75,
                stock = 30,
                category = "Aceites",
                allergens = emptyList()
            ),
            Product(
                id = "PROD_006",
                name = "Pasta Italiana",
                price = 1.95,
                stock = 40,
                category = "Pasta",
                allergens = listOf(
                    Allergen(2, "Gluten", true),
                    Allergen(4, "Huevo", false)
                )
            ),
            Product(
                id = "PROD_007",
                name = "Queso Manchego",
                price = 8.50,
                stock = 15,
                category = "Lácteos",
                allergens = listOf(
                    Allergen(1, "Lactosa", true)
                )
            ),
            Product(
                id = "PROD_008",
                name = "Tomates Cherry",
                price = 3.20,
                stock = 60,
                category = "Verduras",
                allergens = emptyList()
            )
        )
    }
}
