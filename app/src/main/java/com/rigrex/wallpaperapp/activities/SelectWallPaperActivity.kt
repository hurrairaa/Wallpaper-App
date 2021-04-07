package com.rigrex.wallpaperapp.activities

import android.animation.ValueAnimator
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.AdapterView
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.rigrex.wallpaperapp.R
import com.rigrex.wallpaperapp.adapters.DownloadAdapter
import com.rigrex.wallpaperapp.adapters.MainItemDecorator
import com.rigrex.wallpaperapp.adapters.TimeSelectorAdapter
import com.rigrex.wallpaperapp.database.RoomRepository
import com.rigrex.wallpaperapp.database.dataModels.TimeModel
import com.rigrex.wallpaperapp.databinding.ActivitySelectWallPaperBinding
import com.rigrex.wallpaperapp.fragments.TimeInputFragment
import com.rigrex.wallpaperapp.helpUtility.Preferences
import com.rigrex.wallpaperapp.helpUtility.WallPaperHelper

class SelectWallPaperActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySelectWallPaperBinding
    private var adapter: DownloadAdapter? = null
    private var value = 0f
    private var original = 0f
    private lateinit var animation: ValueAnimator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySelectWallPaperBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        initDownloadAdapter()
        binding.backBtn.setOnClickListener { finish() }
    }

    private fun initDownloadAdapter() {
        binding.mainRecView.layoutManager =
                StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        binding.mainRecView.setHasFixedSize(true)
        binding.mainRecView.layoutManager =
                StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        adapter = DownloadAdapter(mutableListOf(), enableChecking = true) {

        }
        binding.mainRecView.adapter = adapter
        binding.mainRecView.addItemDecoration(
                MainItemDecorator(
                        resources.getDimension(R.dimen.dp_10).toInt()
                )
        )
        RoomRepository.getAllDownloadWallpapers { runOnUiThread { adapter?.changeData(it.toMutableList()) } }
    }

    override fun onPause() {
        super.onPause()
        RoomRepository.updateDownloadModels(adapter?.getSelectedWallPapers() ?: mutableListOf())
    }
}