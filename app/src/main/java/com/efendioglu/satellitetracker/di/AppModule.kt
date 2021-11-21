package com.efendioglu.satellitetracker.di

import com.efendioglu.satellitetracker.ui.detail.DetailViewModel
import com.efendioglu.satellitetracker.ui.main.MainViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val uiModule = module {
    viewModel { MainViewModel(get()) }
    viewModel { DetailViewModel(get()) }
}
