/*
 * Copyright (c) 2021. Satellite Tracker
 * @Author M. Kemal Efendioglu
 */

package com.efendioglu.satellitetracker.ui.main.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.efendioglu.satellitetracker.R
import com.efendioglu.satellitetracker.data.model.Satellite
import com.efendioglu.satellitetracker.databinding.ItemviewSatelliteBinding

//typealias ActionCallback = SatellitesAdapter.(Int) -> Unit

//fun interface Callback {
//    fun call(data: List<RocketLaunch>?, error: Throwable?)
//}

class SatellitesAdapter(private var satellites: List<Satellite>, val onClickItem: (item: Satellite) -> Unit): RecyclerView.Adapter<SatellitesAdapter.SatelliteViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SatelliteViewHolder {
        return SatelliteViewHolder(
            LayoutInflater.from(parent.context)
                .inflate(R.layout.itemview_satellite, parent,false), onClickItem)
    }

    override fun onBindViewHolder(holder: SatelliteViewHolder, position: Int) {
        with(holder) {
            bind(satellites[position])
        }
    }

    override fun getItemCount(): Int = satellites.size

    internal fun setData(satellites: List<Satellite>) {
        this.satellites = satellites
        notifyDataSetChanged()
    }

    class SatelliteViewHolder(itemView: View, val callback: (item: Satellite) -> Unit): RecyclerView.ViewHolder(itemView) {

        private val binding = ItemviewSatelliteBinding.bind(itemView)


        fun bind(satellite: Satellite) {
            with(binding) {
                nameView.text = satellite.name
                statusView.text = if (satellite.active) "Active" else "Passive"

                indicatorView.isActivated = satellite.active
                nameView.isActivated = satellite.active
                statusView.isActivated = satellite.active
            }

            itemView.setOnClickListener {
                callback(satellite)
            }
        }
    }
}