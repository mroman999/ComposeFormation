package com.mercadona.mercastock.presentation.ui.components

import androidx.compose.material3.SnackbarDuration
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class SnackbarManager @Inject constructor() {

    private val _snackbarData = MutableSharedFlow<SnackbarData>()

    val snackbarData: SharedFlow<SnackbarData> = _snackbarData

    suspend fun showSnackbar(message: String, duration: SnackbarDuration = SnackbarDuration.Short) {
        _snackbarData.emit(SnackbarData(message, duration))
    }
}

data class SnackbarData(val message: String, val duration: SnackbarDuration)