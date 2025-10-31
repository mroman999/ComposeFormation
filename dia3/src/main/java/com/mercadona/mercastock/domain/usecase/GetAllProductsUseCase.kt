package com.mercadona.mercastock.domain.usecase

import com.mercadona.mercastock.domain.model.Product
import com.mercadona.mercastock.domain.repository.ProductRepository
import kotlinx.coroutines.flow.Flow
import javax.inject.Inject

class GetAllProductsUseCase @Inject constructor(
    private val productRepository: ProductRepository
) {
    operator fun invoke(): Flow<List<Product>> {
        return productRepository.getAllProducts()
    }
}
