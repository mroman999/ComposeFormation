package com.mercadona.mercastock.domain.usecase

import com.mercadona.mercastock.domain.model.Product
import com.mercadona.mercastock.domain.repository.ProductRepository
import javax.inject.Inject

class SaveProductUseCase @Inject constructor(
    private val productRepository: ProductRepository
) {
    suspend operator fun invoke(product: Product): Result<Product> {
        return if (!product.isValid()) {
            Result.failure(IllegalArgumentException("Producto no válido"))
        } else {
            productRepository.saveProduct(product)
        }
    }
}
