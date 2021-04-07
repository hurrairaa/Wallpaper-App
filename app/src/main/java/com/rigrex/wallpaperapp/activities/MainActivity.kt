package com.rigrex.wallpaperapp.activities

import android.os.Bundle
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.iterator
import com.rigrex.wallpaperapp.R
import com.rigrex.wallpaperapp.database.dataModels.WallPaperModel
import com.rigrex.wallpaperapp.databinding.ActivityMainBinding
import com.rigrex.wallpaperapp.fragments.FavoriteFragment
import com.rigrex.wallpaperapp.fragments.HomeFragment
import com.rigrex.wallpaperapp.fragments.TrendingFragment
import com.rigrex.wallpaperapp.helpUtility.DrawerHelper
import com.rigrex.wallpaperapp.helpUtility.HelpUtil
import com.rigrex.wallpaperapp.helpUtility.transactions.FragmentTransactionHelper
import com.rigrex.wallpaperapp.uiComponents.ViewGroupExtension
import com.rigrex.wallpaperapp.uiComponents.ViewWatcher
import com.rigrex.wallpaperapp.uiComponents.ViewWatcherSettings
import kotlin.system.exitProcess

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    //    nstantiates a layout XML file into its corresponding View objects. It is never used directly.
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()
        binding.mainContent.root.post {
            HelpUtil.initializeWatcher((ViewWatcher<WallPaperModel>(ViewWatcherSettings.SettingsBuilder(resources).enableExtensions().build(), binding.mainContent.root.parent as ViewGroup) {
                if (it == ViewWatcher.START) {
                    binding.mainContent.bottomNav.menu.iterator().forEach { it.isEnabled = false }
                } else if (it == ViewWatcher.END) {
                    binding.mainContent.bottomNav.menu.iterator().forEach { it.isEnabled = true }
                }
            }).apply {
                HelpUtil.backupExtensions(ViewGroupExtension(arrayOf(binding.mainContent.menuButton, binding.mainContent.bottomNav)))
            })
            FragmentTransactionHelper.setUpNavigationLIstener(binding.mainContent.bottomNav, this) {
                when (it) {
                    R.id.navigation_home -> {
                        binding.mainContent.mainTitle.text = "WalRex"
                    }
                    R.id.navigation_trending -> {
                        binding.mainContent.mainTitle.text = "Trending"
                    }
                    R.id.navigation_favorite -> {
                        binding.mainContent.mainTitle.text = "Favorite"
                    }
                }
            }
            DrawerHelper.initializeDrawer(
                    binding.drawyer,
                    binding.mainContent.menuButton,
                    this
            )
        }
    }

    override fun onBackPressed() {
        if (FragmentTransactionHelper.getVisibleFragment(this) == HomeFragment::class.java) {
            finish()
        } else {
            super.onBackPressed()
            FragmentTransactionHelper.onBackPressed(this)
        }
    }
}