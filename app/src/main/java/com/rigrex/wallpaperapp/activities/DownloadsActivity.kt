package com.rigrex.wallpaperapp.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.rigrex.wallpaperapp.R
import com.rigrex.wallpaperapp.adapters.DownloadAdapter
import com.rigrex.wallpaperapp.adapters.MainItemDecorator
import com.rigrex.wallpaperapp.database.RoomRepository
import com.rigrex.wallpaperapp.databinding.ActivityDownloadsBinding


class DownloadsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDownloadsBinding
    private var adapter: DownloadAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDownloadsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        initDownloadAdapter()
        binding.backBtn.setOnClickListener { finish() }
    }

    private fun initDownloadAdapter() {
        binding.mainRecView.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        binding.mainRecView.setHasFixedSize(true)
        adapter = DownloadAdapter(mutableListOf()) {
            startActivity(Intent(this, DownloadInfoActivity::class.java).putExtra(DownloadInfoActivity.DOWNLOAD_MODEL, it))
        }
        binding.mainRecView.adapter = adapter
        binding.mainRecView.addItemDecoration(
                MainItemDecorator(
                        resources.getDimension(R.dimen.dp_10).toInt()
                )
        )
        RoomRepository.getAllDownloadWallpapers { runOnUiThread { adapter?.changeData(it.toMutableList()) } }
    }
}