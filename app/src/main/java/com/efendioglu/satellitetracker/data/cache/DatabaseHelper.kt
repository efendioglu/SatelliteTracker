/*
 * Copyright (c) 2021. Satellite Tracker
 * @Author M. Kemal Efendioglu
 */

package com.efendioglu.satellitetracker.data.cache

import com.efendioglu.satellitetracker.Database
import com.efendioglu.satellitetracker.data.model.Position
import com.efendioglu.satellitetracker.data.model.SatellitePositions

class DatabaseHelper(database: Database) {
    private val dbQuery = database.appDatabaseQueries

    internal fun clearDatabase() {
        dbQuery.transaction {
            dbQuery.removeAllSatellites()
            dbQuery.removeAllSatelliteDetails()
            dbQuery.removeAllPositions()
        }
    }

    internal fun getSatellites(): List<Satellite> {
        return dbQuery.getSatellites(::map).executeAsList()
    }

    internal fun getSatelliteDetail(id: Int): SatelliteDetail {
        return dbQuery.getSatelliteDetail(id, ::map).executeAsOne()
    }

    internal fun getSatellitePositions(id: Int): SatellitePositions {
        val positions = dbQuery.getSatellitePositions(id, ::map).executeAsList()
        return SatellitePositions(id, positions)
    }

    private fun map(id: Int, posX: Double, posY: Double): Position {
        return Position(posX, posY)
    }

    private fun map(
        id: Int,
        costPerLaunch: Int,
        firstFlight: String,
        height: Long,
        mass: Long
    ): SatelliteDetail {
        return SatelliteDetail(id, costPerLaunch, firstFlight, height, mass)
    }

    private fun map(id: Int, active: Boolean, name: String): Satellite {
        return Satellite(id, active, name)
    }
}