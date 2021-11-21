/*
 * Copyright (c) 2021. Satellite Tracker
 * @Author M. Kemal Efendioglu
 */

package com.efendioglu.satellitetracker

import android.app.Application
import com.efendioglu.satellitetracker.di.initKoin
import com.efendioglu.satellitetracker.di.uiModule
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidFileProperties
import org.koin.android.ext.koin.androidLogger

class MainApp: Application() {
    override fun onCreate() {
        super.onCreate()

        initKoin {
            androidLogger()
            androidContext(this@MainApp)
            androidFileProperties()
            modules(uiModule)
        }
    }
}