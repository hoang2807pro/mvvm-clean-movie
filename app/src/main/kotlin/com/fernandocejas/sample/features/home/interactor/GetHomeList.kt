package com.fernandocejas.sample.features.home.interactor

import com.fernandocejas.sample.core.interactor.UseCase
import com.fernandocejas.sample.core.interactor.UseCase.None
import com.fernandocejas.sample.features.home.data.HomeRepository

class GetHomeList(
    private val repository: HomeRepository
) : UseCase<List<HomeModel>, None>() {

    override suspend fun run(params: None) = repository.getAll()
}
