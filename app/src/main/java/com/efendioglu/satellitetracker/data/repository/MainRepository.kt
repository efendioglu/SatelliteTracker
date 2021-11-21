/*
 * Copyright (c) 2021. Satellite Tracker
 * @Author M. Kemal Efendioglu
 */

package com.efendioglu.satellitetracker.data.repository

import com.efendioglu.satellitetracker.data.api.ApiService
import com.efendioglu.satellitetracker.data.cache.DatabaseHelper
import com.efendioglu.satellitetracker.data.model.Response
import com.efendioglu.satellitetracker.data.model.Satellite
import com.efendioglu.satellitetracker.data.model.SatelliteDetail
import com.efendioglu.satellitetracker.data.model.SatellitePositions

class MainRepository(private val api: ApiService, private val database: DatabaseHelper): Repository {
    override suspend fun getSatellites(query: String, forceReload: Boolean): Response<List<Satellite>> {
        val satellites = database.getSatellites(query)
        return if (satellites.isNotEmpty() && !forceReload) {
            Response(data = satellites)
        } else {
            val response = api.getSatellites()
            response.data?.also {
                database.clearSatellites()
                database.createSatellites(it)
            }
            response
        }
    }

    override suspend fun getSatelliteDetail(id: Int, forceReload: Boolean): Response<SatelliteDetail> {
        val satelliteDetail = database.getSatelliteDetail(id)
        return if (satelliteDetail != null && !forceReload) {
            Response(data = satelliteDetail)
        } else {
            val response = api.getSatelliteDetail(id)
            response.data?.also {
                database.clearSatelliteDetail(id)
                database.createSatelliteDetail(it)
            }
            response
        }
    }

    override suspend fun getSatellitePositions(id: Int, forceReload: Boolean): Response<SatellitePositions> {
        val position = database.getSatellitePositions(id)
        return if (position.positions.isNotEmpty() && !forceReload) {
            Response(data = position)
        } else {
            val response = api.getSatellitePositions(id)
            response.data?.also {
                database.clearSatellitePositions(id)
                database.createSatellitePositions(it)
            }
            response
        }
    }
}