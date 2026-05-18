package com.fernandocejas.sample.features.home.ui

import androidx.lifecycle.viewModelScope
import com.fernandocejas.sample.core.failure.Failure
import com.fernandocejas.sample.core.interactor.UseCase.None
import com.fernandocejas.sample.core.platform.BaseViewModel
import com.fernandocejas.sample.features.home.interactor.GetHomeList
import com.fernandocejas.sample.features.home.interactor.HomeModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

class HomeViewModel(
    private val getHomeList: GetHomeList
) : BaseViewModel() {

    private val _uiState = MutableStateFlow<HomeUiState>(HomeUiState.Loading)
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    fun load() = getHomeList(None(), viewModelScope) {
        it.fold(::onError, ::onSuccess)
    }

    private fun onSuccess(items: List<HomeModel>) {
        _uiState.value = HomeUiState.Success(items)
    }

    private fun onError(failure: Failure) {
        _uiState.value = HomeUiState.Error(failure)
        handleFailure(failure)
    }
}
