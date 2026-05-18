package com.fernandocejas.sample.features.home.ui

import com.fernandocejas.sample.core.failure.Failure
import com.fernandocejas.sample.features.home.interactor.HomeModel

sealed class HomeUiState {
    object Loading                                     : HomeUiState()
    data class Success(val items: List<HomeModel>)   : HomeUiState()
    data class Error(val failure: Failure)              : HomeUiState()
}
