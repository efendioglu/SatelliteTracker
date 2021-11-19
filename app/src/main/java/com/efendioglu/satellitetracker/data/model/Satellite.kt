/*
 * Copyright (c) 2021. Satellite Tracker
 * @Author M. Kemal Efendioglu
 */

package com.efendioglu.satellitetracker.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class Satellite(
    @SerialName("id") val id: Int,
    @SerialName("active") val active: Boolean,
    @SerialName("name") val name: String
)


@Serializable
data class SatelliteDetail(
    @SerialName("id") val id: Int,
    @SerialName("cost_per_launch") val costPerLaunch: Int,
    @SerialName("first_flight") val firstFlight: String,
    @SerialName("height") val height: Int,
    @SerialName("mass") val mass: Int,
)

@Serializable
data class SatellitePositions(
    @SerialName("id") val id: Int,
    @SerialName("positions") val positions: List<Position> = emptyList()
)

@Serializable
data class Position(
    @SerialName("posX") val posX: Double,
    @SerialName("posY") val posY: Double,
)