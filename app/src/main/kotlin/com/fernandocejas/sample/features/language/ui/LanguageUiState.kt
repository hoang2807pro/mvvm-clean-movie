package com.fernandocejas.sample.features.language.ui

import com.fernandocejas.sample.core.failure.Failure
import com.fernandocejas.sample.features.language.interactor.LanguageModel

sealed class LanguageUiState {
    object Loading                                     : LanguageUiState()
    data class Success(val items: List<LanguageModel>)   : LanguageUiState()
    data class Error(val failure: Failure)              : LanguageUiState()
}
