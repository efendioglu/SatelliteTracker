/*
 * Copyright (c) 2021. Satellite Tracker
 * @Author M. Kemal Efendioglu
 */

package com.efendioglu.satellitetracker.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.view.isVisible
import androidx.core.widget.addTextChangedListener
import androidx.fragment.app.Fragment
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.efendioglu.satellitetracker.R
import com.efendioglu.satellitetracker.databinding.MainFragmentBinding
import com.efendioglu.satellitetracker.ui.main.adapter.SatellitesAdapter
import com.efendioglu.satellitetracker.utils.closeKeyboard
import kotlinx.coroutines.flow.collect
import org.koin.androidx.viewmodel.ext.android.viewModel


class MainFragment : Fragment() {

    private var _binding: MainFragmentBinding? = null
    private val binding get() = _binding!!

    private val viewModel: MainViewModel by viewModel()

    private var adapter = SatellitesAdapter(arrayListOf())



    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MainFragmentBinding.inflate(inflater, container, false)
        val view = binding.root

        setupUI()

        initStateObservers()
        initIntentObservers()

        viewModel.sendIntent(MainContract.Intent.FetchSatellites)

        return view
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }


    private fun setupUI() {
        binding.satelliteListView.layoutManager = LinearLayoutManager(context)
        binding.satelliteListView.run {
            val divider = DividerItemDecoration(
                context, (binding.satelliteListView.layoutManager as LinearLayoutManager).orientation
            )
            divider.setDrawable(resources.getDrawable(R.drawable.ic_divider))
            addItemDecoration( divider )
        }
        binding.satelliteListView.adapter = adapter
    }


    private fun initIntentObservers() = with(binding) {
        swipeContainer.setOnRefreshListener {
            viewModel.sendIntent(MainContract.Intent.RefreshSatellites)
        }

        with(searchInputView) {
            addTextChangedListener {
                viewModel.sendIntent(MainContract.Intent.SearchSatellitesByName(it.toString()))
            }

            setOnEditorActionListener { view, _, _ ->
                view.closeKeyboard()
                false
            }
        }
    }


    private fun initStateObservers() = lifecycleScope.launchWhenStarted {
        viewModel.state.collect {
            when(it.state) {
                is MainContract.MainState.Idle -> { binding.progressView.isVisible = false }
                is MainContract.MainState.Loading -> { binding.progressView.isVisible = false }
                is MainContract.MainState.Success -> handleSuccessState(it.state)
                is MainContract.MainState.Error -> handleErrorState(it.state)
            }
        }
    }


    private fun handleSuccessState(state: MainContract.MainState.Success) {
        adapter.setData(state.satellites)

        with(binding) {
            satelliteListView.adapter = adapter
            progressView.isVisible = false
            swipeContainer.isRefreshing = false
        }
    }

    private fun handleErrorState(state: MainContract.MainState.Error) {
        state.message?.also { msg ->
            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
        }
    }
}