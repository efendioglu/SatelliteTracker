/*
 * Copyright (c) 2021. Satellite Tracker
 * @Author M. Kemal Efendioglu
 */

package com.efendioglu.satellitetracker

import com.efendioglu.satellitetracker.data.api.ApiServiceImpl
import io.ktor.client.*
import io.ktor.client.features.json.*
import io.ktor.client.features.json.serializer.*
import io.ktor.client.features.logging.*
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import org.junit.Test
import kotlin.test.BeforeTest
import kotlin.test.assertEquals


class ApiServiceTest {

    private lateinit var json: Json
    private lateinit var httpClient: HttpClient
    private lateinit var api: ApiServiceImpl


    @BeforeTest
    fun setup() {
        json = Json{
            ignoreUnknownKeys = true; useAlternativeNames = false
        }
        httpClient = HttpClient {
            install(JsonFeature) {
                serializer = KotlinxSerializer(json)
            }
            install(Logging) {
                logger = Logger.DEFAULT
                level = LogLevel.ALL
            }
        }
        api = ApiServiceImpl(httpClient, "http://46.101.172.63:8080")
    }


    @Test
    fun `return 3 satellites on getSatellites`() {
        runBlocking {
            // GIVEN
            var satelliteCount = 0

            // WHEN
            val response = api.getSatellites()

            // THEN
            if (response.error != null) {
                println("GetSatellites Error: ${response.error}")
            } else {
                val data = response.data ?: emptyList()
                satelliteCount = data.size
            }

            println("Satellite Size: $satelliteCount")

            assertEquals(3, satelliteCount, "Unfortunately, 3 satellites not received from getSatellites service")
        }
    }

    @Test
    fun `return detail of satellite with 2 IDs on getSatelliteDetail(2)`() {
        runBlocking {
            // GIVEN
            var id = -1

            // WHEN
            val response = api.getSatelliteDetail(2)

            // THEN
            if (response.error != null) {
                println("getSatelliteDetail Error: ${response.error}")
            } else {
                response.data?.apply {
                    id = this.id
                }
            }

            println("Satellite Id: $id")
            assertEquals(2, id, "Unfortunately, satellite detail with 2 IDs could not be obtained")
        }
    }

    @Test
    fun `return positions of satellite with 3 IDs on getSatellitePositions(3)`() {
        runBlocking {
            // GIVEN
            var id = -1

            // WHEN
            val response = api.getSatellitePositions(3)

            // THEN
            if (response.error != null) {
                println("getSatellitePositions Error: ${response.error}")
            } else {
                response.data?.apply {
                    id = this.id
                }
            }

            println("Satellite Id: $id")
            assertEquals(3, id, "Unfortunately, satellite positions with 3 IDs could not be obtained")
        }
    }
}