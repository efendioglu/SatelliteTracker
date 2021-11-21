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
import androidx.lifecycle.ViewModelProviders
import androidx.lifecycle.lifecycleScope
import com.efendioglu.satellitetracker.Database
import com.efendioglu.satellitetracker.data.api.ApiServiceImpl
import com.efendioglu.satellitetracker.data.cache.DatabaseHelper
import com.efendioglu.satellitetracker.databinding.DetailFragmentBinding
import com.efendioglu.satellitetracker.utils.ViewModelFactory
import com.squareup.sqldelight.android.AndroidSqliteDriver
import io.ktor.client.*
import io.ktor.client.features.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.logging.*
import kotlinx.coroutines.flow.collect

class DetailFragment : Fragment() {

    companion object {
        const val KEY_ID = "key_id"
        const val KEY_NAME = "key_name"

        fun newBundle(satelliteId: Int, satelliteName: String) = Bundle().apply {
            putInt(KEY_ID, satelliteId)
            putString(KEY_NAME, satelliteName)
        }
    }

    private lateinit var viewModel: DetailViewModel

    private var _binding: DetailFragmentBinding? = null
    private val binding get() = _binding!!


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
    ): View? {
        _binding = DetailFragmentBinding.inflate(inflater, container, false)
        val view = binding.root

        initViewModel()
        initObservers()

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

    private fun initViewModel() {
        val driver = AndroidSqliteDriver(Database.Schema, requireContext(), "test.db")
        viewModel = ViewModelProviders
            .of(this, ViewModelFactory( api, DatabaseHelper(Database(driver)) ))
            .get(DetailViewModel::class.java)
    }

    private fun initObservers() {
        lifecycleScope.launchWhenStarted {
            viewModel.state.collect {
                when(it.state) {
                    is DetailContract.DetailState.Idle -> { binding.progressView.isVisible = false }
                    is DetailContract.DetailState.Loading -> { binding.progressView.isVisible = true }
                    is DetailContract.DetailState.DetailInfo -> {
                        val detail = it.state.detail
                        with(binding) {
                            progressView.isVisible = false
                            firstFlightView.text = detail.firstFlight
                            heightMassView.text = "${detail.height}/${detail.mass}"
                            costView.text = "${detail.costPerLaunch}"
                        }
                    }
                    is DetailContract.DetailState.CurrentPosition -> {
                        val position = it.state.position
                        with(binding) {
                            progressView.isVisible = false
                            positionView.text = "(${position.posX}, ${position.posY})"
                        }
                    }
                    is DetailContract.DetailState.Error -> {
                        it.state.message?.also { msg ->
                            Toast.makeText(context, msg, Toast.LENGTH_SHORT).show()
                        }
                    }
                }
            }
        }
    }
}