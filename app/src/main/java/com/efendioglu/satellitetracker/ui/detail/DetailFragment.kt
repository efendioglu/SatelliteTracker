/*
 * Copyright (c) 2021. Satellite Tracker
 * @Author M. Kemal Efendioglu
 */

package com.efendioglu.satellitetracker.ui.detail

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import com.efendioglu.satellitetracker.databinding.DetailFragmentBinding
import kotlinx.coroutines.flow.collect
import org.koin.androidx.viewmodel.ext.android.viewModel

class DetailFragment : Fragment() {

    companion object {
        const val KEY_ID = "key_id"
        const val KEY_NAME = "key_name"

        fun newBundle(satelliteId: Int, satelliteName: String) = Bundle().apply {
            putInt(KEY_ID, satelliteId)
            putString(KEY_NAME, satelliteName)
        }
    }

    private val viewModel: DetailViewModel by viewModel()

    private var _binding: DetailFragmentBinding? = null
    private val binding get() = _binding!!



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = DetailFragmentBinding.inflate(inflater, container, false)
        val view = binding.root

        initStateObservers()

        arguments?.apply {
            val id = getInt(KEY_ID)
            val name = getString(KEY_NAME)

            binding.nameView.text = name

            viewModel.sendIntent(DetailContract.Intent.FetchSatelliteDetail(id))
            viewModel.sendIntent(DetailContract.Intent.FetchSatellitePositions(id))
        }

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }



    private fun initStateObservers() = lifecycleScope.launchWhenStarted {
        viewModel.state.collect {
            when(it.state) {
                is DetailContract.DetailState.Idle -> { binding.progressView.isVisible = false }
                is DetailContract.DetailState.Loading -> { binding.progressView.isVisible = true }
                is DetailContract.DetailState.DetailInfo -> handleDetailState(it.state)
                is DetailContract.DetailState.CurrentPosition -> handlePositionState(it.state)
                is DetailContract.DetailState.Error -> handleErrorState(it.state)
            }
        }
    }

    private fun handleDetailState(state: DetailContract.DetailState.DetailInfo) {
        val detail = state.detail
        with(binding) {
            progressView.isVisible = false
            firstFlightView.text = detail.firstFlight
            heightMassView.text = "${detail.height}/${detail.mass}"
            costView.text = "${detail.costPerLaunch}"
        }
    }

    private fun handlePositionState(state: DetailContract.DetailState.CurrentPosition) {
        val position = state.position
        with(binding) {
            progressView.isVisible = false
            positionView.text = "(${position.posX}, ${position.posY})"
        }
    }

    private fun handleErrorState(state: DetailContract.DetailState.Error) {
        state.message?.also { msg ->
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
        }
    }
}