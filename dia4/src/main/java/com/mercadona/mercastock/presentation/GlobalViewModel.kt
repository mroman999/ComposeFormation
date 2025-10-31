package com.mercadona.mercastock.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mercadona.mercastock.presentation.ui.components.SnackbarData
import com.mercadona.mercastock.presentation.ui.components.SnackbarManager
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class GlobalViewModel @Inject constructor(
    snackbarManager: SnackbarManager,
) : ViewModel() {
    private val _currentSnackbarData = MutableStateFlow<SnackbarData?>(null)
    val currentSnackbarData: StateFlow<SnackbarData?> = _currentSnackbarData

    init {
        viewModelScope.launch {
            snackbarManager.snackbarData.collect { data ->
                _currentSnackbarData.value = data
            }
        }
    }
}