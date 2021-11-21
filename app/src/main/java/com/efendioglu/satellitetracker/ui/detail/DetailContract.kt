/*
 * Copyright (c) 2021. Satellite Tracker
 * @Author M. Kemal Efendioglu
 */

package com.efendioglu.satellitetracker.ui.detail

import com.efendioglu.satellitetracker.data.model.Position
import com.efendioglu.satellitetracker.data.model.SatelliteDetail
import com.efendioglu.satellitetracker.ui.base.UIIntent
import com.efendioglu.satellitetracker.ui.base.UIState

class DetailContract {
    sealed class Intent: UIIntent {
        data class FetchSatelliteDetail(val id: Int): Intent()
        data class FetchSatellitePositions(val id: Int): Intent()
    }

    data class State(
        val state: DetailState
    ) : UIState

    sealed class DetailState {
        object Idle: DetailState()
        object Loading: DetailState()
        data class DetailInfo(val detail: SatelliteDetail): DetailState()
        data class CurrentPosition(val position: Position): DetailState()
        data class Error(val message: String?): DetailState()
    }
}