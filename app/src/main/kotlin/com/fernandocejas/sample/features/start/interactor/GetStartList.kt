package com.fernandocejas.sample.features.start.interactor

import com.fernandocejas.sample.core.interactor.UseCase
import com.fernandocejas.sample.core.interactor.UseCase.None
import com.fernandocejas.sample.features.start.data.StartRepository

class GetStartList(
    private val repository: StartRepository
) : UseCase<List<StartModel>, None>() {

    override suspend fun run(params: None) = repository.getAll()
}
