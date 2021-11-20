/*
 * Copyright (c) 2021. Satellite Tracker
 * @Author M. Kemal Efendioglu
 */

package com.efendioglu.satellitetracker.ui.main

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.DividerItemDecoration
import androidx.recyclerview.widget.LinearLayoutManager
import com.efendioglu.satellitetracker.Database
import com.efendioglu.satellitetracker.R
import com.efendioglu.satellitetracker.data.api.ApiServiceImpl
import com.efendioglu.satellitetracker.data.cache.DatabaseHelper
import com.efendioglu.satellitetracker.databinding.MainFragmentBinding
import com.efendioglu.satellitetracker.ui.main.adapter.SatellitesAdapter
import com.efendioglu.satellitetracker.utils.ViewModelFactory
import com.squareup.sqldelight.android.AndroidSqliteDriver
import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.logging.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.launch

class MainFragment : Fragment() {

    companion object {
        fun newInstance() = MainFragment()
    }

    private var _binding: MainFragmentBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: MainViewModel

    private val adapter = SatellitesAdapter(arrayListOf())


    private val json = kotlinx.serialization.json.Json {
        ignoreUnknownKeys = true; useAlternativeNames = false
    }
    private val httpClient = HttpClient {
        install(JsonFeature) {
            serializer = KotlinxSerializer(json)
        }
        install(Logging) {
            logger = Logger.DEFAULT
            level = LogLevel.ALL
        }
        install(HttpTimeout) {
            requestTimeoutMillis = 5000
        }
    }
    private val api = ApiServiceImpl(httpClient, "http://192.168.88.231:3000")




    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = MainFragmentBinding.inflate(inflater, container, false)
        val view = binding.root

        setupUI()
        initViewModel()
        initObservers()

        binding.swipeContainer.setOnRefreshListener {
            viewModel.sendIntent(MainContract.Intent.OnPullToRefresh)
        }

        viewModel.sendIntent(MainContract.Intent.OnSearchSatellitesByName(""))

        return view
    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun initViewModel() {
        val driver = AndroidSqliteDriver(Database.Schema, requireContext(), "test.db")
        viewModel = ViewModelProviders
            .of(this, ViewModelFactory( api, DatabaseHelper(Database(driver)) ))
            .get(MainViewModel::class.java)
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

    private fun initObservers() {
        lifecycleScope.launchWhenStarted {
            viewModel.state.collect {
                println("Collect : ${it.state}")
                when(it.state) {
                    is MainContract.MainState.Idle -> { binding.progressView.isVisible = false }
                    is MainContract.MainState.Loading -> { binding.progressView.isVisible = false }
                    is MainContract.MainState.Satellites -> {
                        adapter.setData(it.state.satellites)
                        binding.satelliteListView.adapter = adapter

                        binding.progressView.isVisible = false
                        binding.swipeContainer.isRefreshing = false
                    }
                }
            }
        }
    }
}