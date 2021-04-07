package com.rigrex.wallpaperapp.activities

import android.app.WallpaperManager
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.bumptech.glide.Glide
import com.rigrex.wallpaperapp.database.dataModels.DownloadModel
import com.rigrex.wallpaperapp.databinding.ActivityDownloadInfoBinding
import com.rigrex.wallpaperapp.helpUtility.WallPaperHelper
import com.rigrex.wallpaperapp.network.DownloadUtil
import java.io.File

class DownloadInfoActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDownloadInfoBinding
    private var downloadModel: DownloadModel? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDownloadInfoBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()

        downloadModel = intent?.getParcelableExtra(DOWNLOAD_MODEL)

        initListeners()
        initImage()
    }

    private fun initImage() {
        Glide
            .with(this)
            .load(File("${DownloadUtil.DOWNLOAD_PATH}${downloadModel?.getWallPaperName()}"))
            .into(binding.mainImage)
    }

    private fun initListeners() {
        binding.backBtn.setOnClickListener { finish() }
        binding.lockScreen.setOnClickListener {
            downloadModel?.also {
                WallPaperHelper.setWallPaper(
                    it, this,
                    WallpaperManager.FLAG_LOCK
                )
            }
        }
        binding.mainScreen.setOnClickListener {
            runOnUiThread {
                WallPaperHelper.createWalPaperChooser(this) {
                    downloadModel?.also { model ->
                        WallPaperHelper.setWallPaper(model, this, it)
                    }
                }
            }
        }
    }


    companion object {
        const val DOWNLOAD_MODEL = "download_model"
    }
}