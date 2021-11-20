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

class MainViewModel(val repository: Repository) : BaseViewModel<MainContract.State, MainContract.Intent>() {
    override fun createInitialState(): MainContract.State = MainContract.State(MainContract.MainState.Idle)


    override fun handleIntent(intent: MainContract.Intent) {
        when(intent) {
            is MainContract.Intent.OnPullToRefresh -> fetchSatellites(true)
            is MainContract.Intent.OnSearchSatellitesByName -> fetchSatellites(false)
        }
    }

    private fun fetchSatellites(forceReload: Boolean) {
        viewModelScope.launch {
            setState(MainContract.MainState.Loading)

            try {
                val response = repository.getSatellites(forceReload)
                if (response.error != null) {
                    setState(MainContract.MainState.Error(response.error))
                } else {
                    response.data?.also {
                        setState(MainContract.MainState.Satellites(it))
                    }
                }
            } catch (e: Exception) {
                Log.e("SATELLITES_", "ERROR", e)
            }

        }
    }
}

fun MainViewModel.setState(mainState: MainContract.MainState) {
    setState {
        copy(state = mainState)
    }
}