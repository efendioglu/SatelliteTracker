package com.efendioglu.satellitetracker.ui.detail

import android.util.Log
import androidx.lifecycle.viewModelScope
import com.efendioglu.satellitetracker.data.repository.Repository
import com.efendioglu.satellitetracker.ui.base.BaseViewModel
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class DetailViewModel(private val repository: Repository) : BaseViewModel<DetailContract.State, DetailContract.Intent>() {
    override fun createInitialState(): DetailContract.State = DetailContract.State( DetailContract.DetailState.Idle )

    override fun handleIntent(intent: DetailContract.Intent) {
        when(intent) {
            is DetailContract.Intent.FetchSatelliteDetail -> fetchSatelliteDetail(intent.id)
            is DetailContract.Intent.FetchSatellitePositions -> fetchSatellitePositions(intent.id)
        }
    }

    private fun fetchSatelliteDetail(id: Int) = viewModelScope.launch {
        setState(DetailContract.DetailState.Loading)

        try {
            val response = repository.getSatelliteDetail(id, false)
            if (response.error != null) {
                setState(DetailContract.DetailState.Error(response.error))
            } else {
                response.data?.also {
                    setState(DetailContract.DetailState.DetailInfo(it))
                }
            }
        } catch (e: Exception) {
            setState(DetailContract.DetailState.Error(e.message))
        }
    }

    private fun fetchSatellitePositions(id: Int) = viewModelScope.launch {
        try {
            val response = repository.getSatellitePositions(id, false)
            if (response.error != null) {
                setState(DetailContract.DetailState.Error(response.error))
            } else {
                response.data?.also {
                    it.positions.forEach { position ->
                        setState(DetailContract.DetailState.CurrentPosition(position))
                        delay(3000)
                    }
                }
            }
        } catch (e: Exception) {
            Log.e("SATELLITES_DETAIL", "ERROR", e)
        }
    }
}

fun DetailViewModel.setState(detailState: DetailContract.DetailState) {
    setState {
        copy(state = detailState)
    }
}