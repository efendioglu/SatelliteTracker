/*
 * Copyright (c) 2021. Satellite Tracker
 * @Author M. Kemal Efendioglu
 */

package com.efendioglu.satellitetracker.data.repository

import com.efendioglu.satellitetracker.data.model.Response
import com.efendioglu.satellitetracker.data.model.Satellite
import com.efendioglu.satellitetracker.data.model.SatelliteDetail
import com.efendioglu.satellitetracker.data.model.SatellitePositions

interface Repository {
    suspend fun getSatellites(): Response<List<Satellite>>

    suspend fun getSatelliteDetail(id: Int): Response<SatelliteDetail>

    suspend fun getSatellitePositions(id: Int): Response<SatellitePositions>
}