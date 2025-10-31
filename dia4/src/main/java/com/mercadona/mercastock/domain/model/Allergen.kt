package com.mercadona.mercastock.domain.model

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Allergen(
    val id: Int,
    val name: String,
    val isPresent: Boolean
) : Parcelable
