/*
 * Copyright (c) 2021. Satellite Tracker
 * @Author M. Kemal Efendioglu
 */

package com.efendioglu.satellitetracker.di

import com.efendioglu.satellitetracker.Database
import com.efendioglu.satellitetracker.data.api.ApiService
import com.efendioglu.satellitetracker.data.api.ApiServiceImpl
import com.efendioglu.satellitetracker.data.cache.DatabaseHelper
import com.efendioglu.satellitetracker.data.repository.MainRepository
import com.efendioglu.satellitetracker.data.repository.Repository
import com.squareup.sqldelight.android.AndroidSqliteDriver
import io.ktor.client.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.logging.*
import kotlinx.serialization.json.Json
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidFileProperties
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration
import org.koin.dsl.module
import org.koin.fileProperties

fun initKoin(appModule: KoinAppDeclaration = {}) = startKoin {
    appModule()
    modules(
        coreModule
    )
}

val coreModule = module {
    single { Json { ignoreUnknownKeys = true; useAlternativeNames = false } }
    single { createHttpClient(get(), true) }
    single<ApiService> { ApiServiceImpl(get(), getProperty("server_url")) }

    single {
        val driver = AndroidSqliteDriver(Database.Schema, get(), "test.db")
        DatabaseHelper(Database(driver))
    }

    single<Repository> { MainRepository(get(), get()) }
}

fun createHttpClient(json: Json, enableNetworkLogs: Boolean) = HttpClient {
    install(JsonFeature) {
        serializer = KotlinxSerializer(json)
    }

    if (enableNetworkLogs) {
        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.ALL
        }
    }
}