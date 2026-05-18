package com.fernandocejas.sample.features.language.interactor

import com.fernandocejas.sample.core.interactor.UseCase
import com.fernandocejas.sample.core.interactor.UseCase.None
import com.fernandocejas.sample.features.language.data.LanguageRepository

class GetLanguageList(
    private val repository: LanguageRepository
) : UseCase<List<LanguageModel>, None>() {

    override suspend fun run(params: None) = repository.getAll()
}
