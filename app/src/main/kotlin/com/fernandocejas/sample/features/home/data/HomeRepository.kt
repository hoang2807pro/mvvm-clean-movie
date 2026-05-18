package com.fernandocejas.sample.features.home.data

import com.fernandocejas.sample.core.failure.Failure
import com.fernandocejas.sample.core.failure.Failure.NetworkConnection
import com.fernandocejas.sample.core.failure.Failure.ServerError
import com.fernandocejas.sample.core.functional.Either
import com.fernandocejas.sample.core.functional.toLeft
import com.fernandocejas.sample.core.functional.toRight
import com.fernandocejas.sample.core.network.NetworkHandler
import com.fernandocejas.sample.features.home.data.local.HomeDao
import com.fernandocejas.sample.features.home.data.local.HomeLocalEntity
import com.fernandocejas.sample.features.home.interactor.HomeModel

interface HomeRepository {

    suspend fun getAll(): Either<Failure, List<HomeModel>>

    class Network(
        private val networkHandler: NetworkHandler,
        private val service: HomeService,
        private val dao: HomeDao
    ) : HomeRepository {

        // Online  → fetch API + lưu cache
        // Offline → đọc cache, báo lỗi nếu cache rỗng
        override suspend fun getAll(): Either<Failure, List<HomeModel>> {
            return if (networkHandler.isNetworkAvailable()) {
                try {
                    val entities = service.getAll()
                    val models   = entities.map { it.toDomain() }
                    dao.insertAll(models.map { HomeLocalEntity(it.id, it.name) })
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
