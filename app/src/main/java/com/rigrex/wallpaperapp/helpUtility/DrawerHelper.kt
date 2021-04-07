package com.rigrex.wallpaperapp.helpUtility

import android.content.Context
import android.content.Intent
import android.graphics.Color
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.ActionBarDrawerToggle
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.rigrex.wallpaperapp.R
import com.rigrex.wallpaperapp.activities.DownloadsActivity
import com.rigrex.wallpaperapp.activities.SelectWallPaperActivity
import com.rigrex.wallpaperapp.databinding.MainContentBinding
import com.rigrex.wallpaperapp.fragments.FavoriteFragment
import com.rigrex.wallpaperapp.fragments.HomeFragment
import com.rigrex.wallpaperapp.fragments.TrendingFragment
import com.rigrex.wallpaperapp.helpUtility.transactions.FragmentTransactionHelper

class DrawerHelper {
    companion object {
        private lateinit var drawerLayout: DrawerLayout
        private var handleSearchIntent: (() -> Unit)? = null

        fun submitHandler(handler: (() -> Unit)) {
            handleSearchIntent = handler
        }

        fun initializeDrawer(
                drawerLayout: DrawerLayout,
                openButton: ImageView,
                context: Context
        ) {
            var toggle = ActionBarDrawerToggle(
                    context as AppCompatActivity,
                    drawerLayout,
                    R.string.navigation_drawer_open,
                    R.string.navigation_drawer_close
            ) /*{
                private val scaleFactor = 6f
                override fun onDrawerSlide(drawerView: View, slideOffset: Float) {
                    super.onDrawerSlide(drawerView, slideOffset)
                    content.root.translationX = drawerView.width * slideOffset
                    content.root.scaleX = 1 - (slideOffset/scaleFactor)
                    content.root.scaleY = 1 - (slideOffset/scaleFactor)
                }
            }*/
            drawerLayout.setScrimColor(Color.TRANSPARENT)
            this.drawerLayout = drawerLayout
            drawerLayout.addDrawerListener(toggle)
            toggle.syncState()
            drawerLayout.overScrollMode
            openButton.setOnClickListener { open() }
            setListeners(context)
        }

        private fun setListeners(context: Context) {
            drawerLayout
                    .findViewById<TextView>(R.id.drawer_about)
                    .setOnClickListener {

                    }
            drawerLayout
                    .findViewById<TextView>(R.id.drawer_feedback)
                    .setOnClickListener {

                    }
            drawerLayout
                    .findViewById<TextView>(R.id.drawer_share)
                    .setOnClickListener {

                    }
            drawerLayout
                    .findViewById<TextView>(R.id.drawer_rate)
                    .setOnClickListener {

                    }
            drawerLayout
                    .findViewById<TextView>(R.id.drawer_reloads)
                    .setOnClickListener {
                        HelpUtil.reloadAllData(context)
//                        HelpUtil.restartApp(context)
                    }
            drawerLayout
                    .findViewById<TextView>(R.id.drawer_downloads)
                    .setOnClickListener {
                        context.startActivity(Intent(context, DownloadsActivity::class.java))
                    }
            drawerLayout
                    .findViewById<ImageView>(R.id.drawer_back_button)
                    .setOnClickListener { close() }
            drawerLayout
                    .findViewById<TextView>(R.id.drawer_settings)
                    .setOnClickListener {

                    }
            drawerLayout
                    .findViewById<TextView>(R.id.drawer_auto)
                    .setOnClickListener {
                        context.startActivity(Intent(context, SelectWallPaperActivity::class.java))
                    }
            drawerLayout
                    .findViewById<TextView>(R.id.drawer_home)
                    .setOnClickListener {
                        FragmentTransactionHelper.startFragment(
                                HomeFragment::class.java,
                                context,
                                botNav = true
                        )
                    }
            drawerLayout
                    .findViewById<TextView>(R.id.drawer_favorite)
                    .setOnClickListener {
                        FragmentTransactionHelper.startFragment(
                                FavoriteFragment::class.java,
                                context,
                                botNav = true
                        )
                    }
        }

        private fun open() {
            if (!drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.openDrawer(GravityCompat.START, true)
            }
        }

        private fun close() {
            if (drawerLayout.isDrawerOpen(GravityCompat.START)) {
                drawerLayout.closeDrawer(GravityCompat.START, true)
            }
        }
    }
}