package com.fernandocejas.sample.features.home

import androidx.room.Room
import com.fernandocejas.sample.core.Feature
import com.fernandocejas.sample.features.home.data.HomeRepository
import com.fernandocejas.sample.features.home.data.HomeService
import com.fernandocejas.sample.features.home.data.local.HomeDatabase
import com.fernandocejas.sample.features.home.interactor.GetHomeList
import com.fernandocejas.sample.features.home.ui.HomeViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

fun homeFeature() = object : Feature {
    override fun name() = "home"
    override fun diModule() = module {

        // Room
        single {
            Room.databaseBuilder(androidContext(), HomeDatabase::class.java, "home-db")
                .fallbackToDestructiveMigration()
                .build()
        }
        single { get<HomeDatabase>().homeDao() }

        // Data
        factoryOf(::HomeService)
        factory { HomeRepository.Network(get(), get(), get()) } bind HomeRepository::class

        // Domain
        factoryOf(::GetHomeList)

        // UI
        viewModelOf(::HomeViewModel)
    }
}
