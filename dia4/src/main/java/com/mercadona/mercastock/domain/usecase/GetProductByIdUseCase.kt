package com.mercadona.mercastock.domain.usecase

import com.mercadona.mercastock.domain.model.Product
import com.mercadona.mercastock.domain.repository.ProductRepository
import javax.inject.Inject

class GetProductByIdUseCase @Inject constructor(
    private val productRepository: ProductRepository
) {
    suspend operator fun invoke(id: String): Result<Product> {
        return if (id.isBlank()) {
            Result.failure(IllegalArgumentException("ID no puede estar vacío"))
        } else {
            productRepository.getProductById(id)
        }
    }
}
