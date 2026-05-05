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
package com.fernandocejas.sample.features.movies.data

import com.fernandocejas.sample.core.failure.Failure
import com.fernandocejas.sample.core.failure.Failure.NetworkConnection
import com.fernandocejas.sample.core.failure.Failure.ServerError
import com.fernandocejas.sample.core.functional.Either
import com.fernandocejas.sample.core.functional.toLeft
import com.fernandocejas.sample.core.functional.toRight
import com.fernandocejas.sample.core.network.NetworkHandler
import com.fernandocejas.sample.features.movies.data.local.MovieDao
import com.fernandocejas.sample.features.movies.data.local.MovieDetailsDao
import com.fernandocejas.sample.features.movies.data.local.MovieDetailsLocalEntity
import com.fernandocejas.sample.features.movies.data.local.MovieLocalEntity
import com.fernandocejas.sample.features.movies.interactor.Movie
import com.fernandocejas.sample.features.movies.interactor.MovieDetails

interface MoviesRepository {
    suspend fun movies(): Either<Failure, List<Movie>>
    suspend fun movieDetails(movieId: Int): Either<Failure, MovieDetails>

    class Network(
        private val networkHandler: NetworkHandler,
        private val service: MoviesService,
        private val movieDao: MovieDao,
        private val movieDetailsDao: MovieDetailsDao
    ) : MoviesRepository {

        /**
         * Network-first strategy:
         * - Online  → fetch fresh data, save to cache, return result
         * - Offline → return cached data if available, else NetworkConnection failure
         */
        override suspend fun movies(): Either<Failure, List<Movie>> {
            return if (networkHandler.isNetworkAvailable()) {
                try {
                    val entities = service.movies()
                    val movies = entities.map { it.toMovie() }
                    movieDao.insertAll(movies.map { MovieLocalEntity(it.id, it.poster) })
                    movies.toRight()
                } catch (e: Exception) {
                    ServerError.toLeft()
                }
            } else {
                val cached = movieDao.getAll()
                if (cached.isNotEmpty()) cached.map { it.toMovie() }.toRight()
                else NetworkConnection.toLeft()
            }
        }

        override suspend fun movieDetails(movieId: Int): Either<Failure, MovieDetails> {
            return if (networkHandler.isNetworkAvailable()) {
                try {
                    val entity = service.movieDetails(movieId)
                    val details = entity.toMovieDetails()
                    movieDetailsDao.insert(
                        MovieDetailsLocalEntity(
                            details.id, details.title, details.poster, details.summary,
                            details.cast, details.director, details.year, details.trailer
                        )
                    )
                    details.toRight()
                } catch (e: Exception) {
                    ServerError.toLeft()
                }
            } else {
                val cached = movieDetailsDao.getById(movieId)
                cached?.toMovieDetails()?.toRight() ?: NetworkConnection.toLeft()
            }
        }
    }
}
