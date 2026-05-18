package com.fernandocejas.sample.features.language.ui

import androidx.lifecycle.viewModelScope
import com.fernandocejas.sample.core.failure.Failure
import com.fernandocejas.sample.core.interactor.UseCase.None
import com.fernandocejas.sample.core.platform.BaseViewModel
import com.fernandocejas.sample.features.language.interactor.GetLanguageList
import com.fernandocejas.sample.features.language.interactor.LanguageModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class LanguageViewModel(
    private val getLanguageList: GetLanguageList
) : BaseViewModel() {

    private val _uiState = MutableStateFlow<LanguageUiState>(LanguageUiState.Loading)
    val uiState: StateFlow<LanguageUiState> = _uiState.asStateFlow()

    fun load() = getLanguageList(None(), viewModelScope) {
        it.fold(::onError, ::onSuccess)
    }

    private fun onSuccess(items: List<LanguageModel>) {
        _uiState.value = LanguageUiState.Success(items)
    }

    private fun onError(failure: Failure) {
        _uiState.value = LanguageUiState.Error(failure)
        handleFailure(failure)
    }
}
