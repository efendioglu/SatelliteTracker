/*
 * Copyright (c) 2021. Satellite Tracker
 * @Author M. Kemal Efendioglu
 */

package com.efendioglu.satellitetracker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.efendioglu.satellitetracker.ui.detail.DetailFragment
import com.efendioglu.satellitetracker.ui.main.MainFragment

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.main_activity)
        if (savedInstanceState == null) {
            supportFragmentManager.beginTransaction()
                .replace(R.id.container, MainFragment.newInstance { item ->
                    supportFragmentManager.beginTransaction().replace(R.id.container, DetailFragment.newInstance(item.id, item.name)).commitNow()
                })
                .commitNow()
        }
    }
}