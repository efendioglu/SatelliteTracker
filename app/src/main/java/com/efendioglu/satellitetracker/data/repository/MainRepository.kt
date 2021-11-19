/*
 * Copyright (c) 2021. Satellite Tracker
 * @Author M. Kemal Efendioglu
 */

package com.efendioglu.satellitetracker.data.repository

import com.efendioglu.satellitetracker.data.api.ApiService
import com.efendioglu.satellitetracker.data.model.Response
import com.efendioglu.satellitetracker.data.model.Satellite
import com.efendioglu.satellitetracker.data.model.SatelliteDetail
import com.efendioglu.satellitetracker.data.model.SatellitePositions

class MainRepository(private val api: ApiService): Repository {
    override suspend fun getSatellites(): Response<List<Satellite>> = api.getSatellites()

    override suspend fun getSatelliteDetail(id: Int): Response<SatelliteDetail> = api.getSatelliteDetail(id)

    override suspend fun getSatellitePositions(id: Int): Response<SatellitePositions> = api.getSatellitePositions(id)
}