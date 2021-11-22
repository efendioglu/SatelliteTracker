/*
 * Copyright (c) 2021. Satellite Tracker
 * @Author M. Kemal Efendioglu
 */

package com.efendioglu.satellitetracker

import com.efendioglu.satellitetracker.data.cache.AppDatabaseQueries
import com.squareup.sqldelight.sqlite.driver.JdbcSqliteDriver
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

class DatabaseTest {
    private lateinit var inMemorySqlDriver: JdbcSqliteDriver

    private lateinit var dbQuery: AppDatabaseQueries

    @BeforeTest
    fun setup() {
        inMemorySqlDriver = JdbcSqliteDriver(JdbcSqliteDriver.IN_MEMORY).apply {
            Database.Schema.create(this)
        }

        dbQuery = Database(inMemorySqlDriver).appDatabaseQueries
    }


    @Test
    fun `When the user enters the satellite list, at least 1 satellite should be displayed`() {
        // GIVEN
        var actualCount: Int
        val expectedCount = 1
        dbQuery.insertSatellite(1, true, "Satellite 1")

        // WHEN
        val satellites = dbQuery.getSatellites("").executeAsList()
        actualCount = satellites.size

        // THEN
        println("Satellite count from DB: $actualCount")

        assertEquals(expectedCount, actualCount, "Satellite count is not equal to 1")
    }


    @Test
    fun `When user clicks on the satellite with id 2, the detail is obtained successfully`() {
        // GIVEN
        val expectedId = 2
        var actualId: Int

        dbQuery.insertSatelliteDetail(expectedId, 1, "11/2021", 100, 100)

        // WHEN
        val satelliteDetail = dbQuery.getSatelliteDetail(expectedId).executeAsOne()
        actualId = satelliteDetail.id

        // THEN
        println("Satellite id: $actualId")

        assertEquals(expectedId, actualId, "Could not get satellite details with id 2")
    }

    @Test
    fun `When user enters satellite details with id 3 IDs, the positions of satellite is received successfully`() {
        // GIVEN
        val expectedCount = 1
        var actualCount: Int

        dbQuery.insertSatellitePosition(1, 0.9, 0.9)

        // WHEN
        val positions = dbQuery.getSatellitePositions(1).executeAsList()
        actualCount = positions.size

        // THEN
        println("Satellite id: $actualCount")

        assertEquals(expectedCount, actualCount, "Could not get satellite position information with id 3")
    }
}