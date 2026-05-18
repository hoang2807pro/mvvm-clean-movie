package com.fernandocejas.sample.features.language

import androidx.room.Room
import com.fernandocejas.sample.core.Feature
import com.fernandocejas.sample.features.language.data.LanguageRepository
import com.fernandocejas.sample.features.language.data.LanguageService
import com.fernandocejas.sample.features.language.data.local.LanguageDatabase
import com.fernandocejas.sample.features.language.interactor.GetLanguageList
import com.fernandocejas.sample.features.language.ui.LanguageViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

fun languageFeature() = object : Feature {
    override fun name() = "language"
    override fun diModule() = module {

        // Room
        single {
            Room.databaseBuilder(androidContext(), LanguageDatabase::class.java, "language-db")
                .fallbackToDestructiveMigration()
                .build()
        }
        single { get<LanguageDatabase>().languageDao() }

        // Data
        factoryOf(::LanguageService)
        factory { LanguageRepository.Network(get(), get(), get()) } bind LanguageRepository::class

        // Domain
        factoryOf(::GetLanguageList)

        // UI
        viewModelOf(::LanguageViewModel)
    }
}
