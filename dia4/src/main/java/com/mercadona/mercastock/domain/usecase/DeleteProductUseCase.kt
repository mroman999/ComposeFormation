package com.mercadona.mercastock.domain.usecase

import com.mercadona.mercastock.domain.repository.ProductRepository
import javax.inject.Inject

class DeleteProductUseCase @Inject constructor(
    private val productRepository: ProductRepository
) {
    suspend operator fun invoke(id: String): Result<Unit> {
        return if (id.isBlank()) {
            Result.failure(IllegalArgumentException("ID no puede estar vacío"))
        } else {
            productRepository.deleteProduct(id)
        }
    }
}
