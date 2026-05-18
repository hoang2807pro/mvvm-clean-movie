package com.fernandocejas.sample.features.start.ui

import com.fernandocejas.sample.core.failure.Failure
import com.fernandocejas.sample.features.start.interactor.StartModel

sealed class StartUiState {
    object Loading                                     : StartUiState()
    data class Success(val items: List<StartModel>)   : StartUiState()
    data class Error(val failure: Failure)              : StartUiState()
}
