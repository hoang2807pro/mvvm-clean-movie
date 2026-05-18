package com.fernandocejas.sample.features.language.data

import com.fernandocejas.sample.core.failure.Failure
import com.fernandocejas.sample.core.failure.Failure.NetworkConnection
import com.fernandocejas.sample.core.failure.Failure.ServerError
import com.fernandocejas.sample.core.functional.Either
import com.fernandocejas.sample.core.functional.toLeft
import com.fernandocejas.sample.core.functional.toRight
import com.fernandocejas.sample.core.network.NetworkHandler
import com.fernandocejas.sample.features.language.data.local.LanguageDao
import com.fernandocejas.sample.features.language.data.local.LanguageLocalEntity
import com.fernandocejas.sample.features.language.interactor.LanguageModel

interface LanguageRepository {

    suspend fun getAll(): Either<Failure, List<LanguageModel>>

    class Network(
        private val networkHandler: NetworkHandler,
        private val service: LanguageService,
        private val dao: LanguageDao
    ) : LanguageRepository {

        // Online  → fetch API + lưu cache
        // Offline → đọc cache, báo lỗi nếu cache rỗng
        override suspend fun getAll(): Either<Failure, List<LanguageModel>> {
            return if (networkHandler.isNetworkAvailable()) {
                try {
                    val entities = service.getAll()
                    val models   = entities.map { it.toDomain() }
                    dao.insertAll(models.map { LanguageLocalEntity(it.id, it.name) })
                    models.toRight()
                } catch (e: Exception) {
                    ServerError.toLeft()
                }
            } else {
                val cached = dao.getAll()
                if (cached.isNotEmpty()) cached.map { it.toDomain() }.toRight()
                else NetworkConnection.toLeft()
            }
        }
    }
}
