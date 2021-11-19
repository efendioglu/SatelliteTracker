/*
 * Copyright (c) 2021. Satellite Tracker
 * @Author M. Kemal Efendioglu
 */

package com.efendioglu.satellitetracker.data.api

import com.efendioglu.satellitetracker.data.model.Response
import com.efendioglu.satellitetracker.data.model.Satellite
import com.efendioglu.satellitetracker.data.model.SatelliteDetail
import com.efendioglu.satellitetracker.data.model.SatellitePositions
import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.http.*


class ApiServiceImpl(private val httpClient: HttpClient, val baseUrl: String): ApiService {

    override suspend fun getSatellites(): Response<List<Satellite>> = get("/satellite/list")

    override suspend fun getSatelliteDetail(id: Int): Response<SatelliteDetail> = get("/satellite/$id")

    override suspend fun getSatellitePositions(id: Int): Response<SatellitePositions> = get("/satellite/$id/position/list")

    private suspend fun <T> ApiServiceImpl.get(path: String): Response<T> = httpClient.get {
        HttpRequestBuilder {
            takeFrom(baseUrl)
            encodedPath = path
        }
    }

    private fun HttpRequestBuilder.get(path: String) = url {
        takeFrom(baseUrl)
        encodedPath = path
    }
}

