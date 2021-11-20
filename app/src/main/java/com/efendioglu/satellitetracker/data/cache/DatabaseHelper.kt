/*
 * Copyright (c) 2021. Satellite Tracker
 * @Author M. Kemal Efendioglu
 */

package com.efendioglu.satellitetracker.data.cache

import com.efendioglu.satellitetracker.Database
import com.efendioglu.satellitetracker.data.model.Position
import com.efendioglu.satellitetracker.data.model.Satellite
import com.efendioglu.satellitetracker.data.model.SatelliteDetail
import com.efendioglu.satellitetracker.data.model.SatellitePositions

class DatabaseHelper(database: Database) {
    private val dbQuery = database.appDatabaseQueries

    internal fun getSatellites(): List<Satellite> = dbQuery.getSatellites(::map).executeAsList()

    internal fun getSatelliteDetail(id: Int): SatelliteDetail? = dbQuery.getSatelliteDetail(id, ::map).executeAsOneOrNull()

    internal fun getSatellitePositions(id: Int): SatellitePositions {
        val positions = dbQuery.getSatellitePositions(id) { _, posX, posY ->
            Position(posX, posY)
        }.executeAsList()
        return SatellitePositions(id, positions)
    }

    internal fun createSatellites(satellites: List<Satellite>) = dbQuery.transaction {
        satellites.forEach { satellite ->
            insertSatellite(satellite)
        }
    }

    internal fun createSatelliteDetail(satelliteDetail: SatelliteDetail) = dbQuery.insertSatelliteDetail(
        id = satelliteDetail.id,
        costPerLaunch = satelliteDetail.costPerLaunch,
        firstFlight = satelliteDetail.firstFlight,
        height = satelliteDetail.height.toLong(),
        mass = satelliteDetail.mass.toLong()
    )

    internal fun createSatellitePositions(satellitePositions: SatellitePositions) = dbQuery.transaction {
        satellitePositions.positions.forEach { position ->
            dbQuery.insertSatellitePosition(satellitePositions.id, position.posX, position.posY)
        }
    }

    internal fun clearSatellites() = dbQuery.removeAllSatellites()

    internal fun clearSatelliteDetail(id: Int) = dbQuery.removeSatelliteDetailById(id)

    internal fun clearSatellitePositions(id: Int) = dbQuery.removePositionsById(id)

    private fun insertSatellite(satellite: Satellite) = dbQuery.insertSatellite(
        id = satellite.id,
        active = satellite.active,
        name = satellite.name
    )

    private fun map(
        id: Int,
        costPerLaunch: Int,
        firstFlight: String,
        height: Long,
        mass: Long
    ): SatelliteDetail = SatelliteDetail(id, costPerLaunch, firstFlight, height.toInt(), mass.toInt())

    private fun map(id: Int, active: Boolean, name: String): Satellite = Satellite(id, active, name)
}