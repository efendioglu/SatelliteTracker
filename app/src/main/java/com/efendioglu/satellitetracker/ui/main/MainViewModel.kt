/*
 * Copyright (c) 2021. Satellite Tracker
 * @Author M. Kemal Efendioglu
 */

package com.efendioglu.satellitetracker.ui.main

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.efendioglu.satellitetracker.data.repository.Repository
import com.efendioglu.satellitetracker.ui.base.BaseViewModel
import kotlinx.coroutines.launch

class MainViewModel(private val repository: Repository) : BaseViewModel<MainContract.State, MainContract.Intent>() {
    override fun createInitialState(): MainContract.State = MainContract.State(MainContract.MainState.Idle)


    override fun handleIntent(intent: MainContract.Intent) {
        when(intent) {
            is MainContract.Intent.RefreshSatellites -> fetchSatellites("",true)
            is MainContract.Intent.FetchSatellites -> fetchSatellites("",false)
            is MainContract.Intent.SearchSatellitesByName -> fetchSatellites(intent.text, false)
        }
    }

    private fun fetchSatellites(query: String, forceReload: Boolean) = viewModelScope.launch {
        setState(MainContract.MainState.Loading)

        try {
            val response = repository.getSatellites(query, forceReload)
            if (response.error != null) {
                setState(MainContract.MainState.Error(response.error))
            } else {
                response.data?.also {
                    setState(MainContract.MainState.Success(it))
                }
            }
        } catch (e: Exception) {
            setState(MainContract.MainState.Error(e.message))
        }
    }
}

fun MainViewModel.setState(mainState: MainContract.MainState) {
    setState {
        copy(state = mainState)
    }
}