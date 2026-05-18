package com.fernandocejas.sample.features.start

import androidx.room.Room
import com.fernandocejas.sample.core.Feature
import com.fernandocejas.sample.features.start.data.StartRepository
import com.fernandocejas.sample.features.start.data.StartService
import com.fernandocejas.sample.features.start.data.local.StartDatabase
import com.fernandocejas.sample.features.start.interactor.GetStartList
import com.fernandocejas.sample.features.start.ui.StartViewModel
import org.koin.android.ext.koin.androidContext
import org.koin.androidx.viewmodel.dsl.viewModelOf
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.bind
import org.koin.dsl.module

fun startFeature() = object : Feature {
    override fun name() = "start"
    override fun diModule() = module {

        // Room
        single {
            Room.databaseBuilder(androidContext(), StartDatabase::class.java, "start-db")
                .fallbackToDestructiveMigration()
                .build()
        }
        single { get<StartDatabase>().startDao() }

        // Data
        factoryOf(::StartService)
        factory { StartRepository.Network(get(), get(), get()) } bind StartRepository::class

        // Domain
        factoryOf(::GetStartList)

        // UI
        viewModelOf(::StartViewModel)
    }
}
