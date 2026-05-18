package com.fernandocejas.sample.features.start.ui

import androidx.lifecycle.viewModelScope
import com.fernandocejas.sample.core.failure.Failure
import com.fernandocejas.sample.core.interactor.UseCase.None
import com.fernandocejas.sample.core.platform.BaseViewModel
import com.fernandocejas.sample.features.start.interactor.GetStartList
import com.fernandocejas.sample.features.start.interactor.StartModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class StartViewModel(
    private val getStartList: GetStartList
) : BaseViewModel() {

    private val _uiState = MutableStateFlow<StartUiState>(StartUiState.Loading)
    val uiState: StateFlow<StartUiState> = _uiState.asStateFlow()

    fun load() = getStartList(None(), viewModelScope) {
        it.fold(::onError, ::onSuccess)
    }

    private fun onSuccess(items: List<StartModel>) {
        _uiState.value = StartUiState.Success(items)
    }

    private fun onError(failure: Failure) {
        _uiState.value = StartUiState.Error(failure)
        handleFailure(failure)
    }
}
