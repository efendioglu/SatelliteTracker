/*
 * Copyright (c) 2021. Satellite Tracker
 * @Author M. Kemal Efendioglu
 */

package com.efendioglu.satellitetracker.ui.main

import com.efendioglu.satellitetracker.data.model.Satellite
import com.efendioglu.satellitetracker.ui.base.UIIntent
import com.efendioglu.satellitetracker.ui.base.UIState

class MainContract {
    sealed class Intent: UIIntent {
        object RefreshSatellites: Intent()
        object FetchSatellites: Intent()
        data class SearchSatellitesByName(val text: String): Intent()
    }

    data class State(
        val state: MainState
    ) : UIState

    sealed class MainState {
        object Idle: MainState()
        object Loading: MainState()
        data class Success(val satellites: List<Satellite>): MainState()
        data class Error(val message: String?): MainState()
    }
}