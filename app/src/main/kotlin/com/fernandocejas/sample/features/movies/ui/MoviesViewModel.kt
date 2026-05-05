/**
 * Copyright (C) 2020 Fernando Cejas Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.fernandocejas.sample.features.movies.ui

import androidx.lifecycle.viewModelScope
import com.fernandocejas.sample.core.failure.Failure
import com.fernandocejas.sample.core.interactor.UseCase.None
import com.fernandocejas.sample.core.platform.BaseViewModel
import com.fernandocejas.sample.features.movies.interactor.Movie
import com.fernandocejas.sample.features.movies.interactor.GetMovies
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

sealed class MoviesUiState {
    object Loading : MoviesUiState()
    data class Success(val movies: List<MovieView>) : MoviesUiState()
    data class Error(val failure: Failure) : MoviesUiState()
}

class MoviesViewModel(private val getMovies: GetMovies) : BaseViewModel() {

    private val _uiState = MutableStateFlow<MoviesUiState>(MoviesUiState.Loading)
    val uiState: StateFlow<MoviesUiState> = _uiState.asStateFlow()

    fun loadMovies() =
        getMovies(None(), viewModelScope) { it.fold(::handleMoviesFailure, ::handleMovieList) }

    private fun handleMovieList(movies: List<Movie>) {
        _uiState.value = MoviesUiState.Success(movies.map { MovieView(it.id, it.poster) })
    }

    private fun handleMoviesFailure(failure: Failure) {
        _uiState.value = MoviesUiState.Error(failure)
        handleFailure(failure)
    }
}
