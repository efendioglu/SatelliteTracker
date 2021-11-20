/*
 * Copyright (c) 2021. Satellite Tracker
 * @Author M. Kemal Efendioglu
 */

package com.efendioglu.satellitetracker.utils

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.efendioglu.satellitetracker.data.api.ApiService
import com.efendioglu.satellitetracker.data.cache.DatabaseHelper
import com.efendioglu.satellitetracker.data.repository.MainRepository
import com.efendioglu.satellitetracker.ui.main.MainViewModel

class ViewModelFactory(private val api: ApiService, private val db: DatabaseHelper) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MainViewModel::class.java)) {
            return MainViewModel(MainRepository(api, db)) as T
        }
        throw IllegalArgumentException("Unknown class name")
    }
}