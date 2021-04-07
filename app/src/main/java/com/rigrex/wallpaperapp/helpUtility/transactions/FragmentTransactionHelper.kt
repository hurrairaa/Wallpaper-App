package com.rigrex.wallpaperapp.helpUtility.transactions

import android.content.Context
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.rigrex.wallpaperapp.R
import com.rigrex.wallpaperapp.fragments.FavoriteFragment
import com.rigrex.wallpaperapp.fragments.HomeFragment
import com.rigrex.wallpaperapp.fragments.TrendingFragment


class FragmentTransactionHelper {
    companion object {
        lateinit var defaultFactory: DefaultFactory<Fragment>
        lateinit var navigationView: BottomNavigationView

        private fun startTransaction(
                context: Context,
                fragment: Fragment,
                addToStack: String? = null,
                containerId: Int = R.id.nav_host_fragment
        ) {
            var transaction =
                    (context as AppCompatActivity).supportFragmentManager.beginTransaction()
            try {
                if (!context.isFinishing) {
                    transaction
                            .replace(containerId, fragment, addToStack)
                            .addToBackStack(addToStack)
                            .commit()
                }
            } catch (e: Exception) {
            }
        }

        fun getVisibleFragment(context: Context): Class<Fragment>? {
            (context as AppCompatActivity)
                    .supportFragmentManager
                    .fragments
                    .forEach {
                        if (it != null && it.isVisible) return it.javaClass
                    }

            return null
        }

        fun <T : Fragment> startFragment(
                clazz: Class<T>,
                context: Context,
                addToStack: String? = null,
                containerId: Int = R.id.nav_host_fragment,
                replaceIfVisible: Boolean = false,
                botNav: Boolean = false
        ) {
            if (!Companion::defaultFactory.isInitialized)
                defaultFactory = DefaultFactory()
            if (!replaceIfVisible) {
                if (getVisibleFragment(context) == clazz)
                    return
            }
            if (botNav) {
                onBackPressed(context)
                return
            }
            startTransaction(
                    context,
                    defaultFactory.provideFragment(clazz as Class<Fragment>),
                    addToStack,
                    containerId
            )
        }

        fun onBackPressed(context: Context) {
            var id = when (getVisibleFragment(context)) {
                FavoriteFragment::class.java -> R.id.navigation_favorite
                HomeFragment::class.java -> R.id.navigation_home
                TrendingFragment::class.java -> R.id.navigation_trending
                else -> R.id.navigation_home
            }
            navigationView.selectedItemId = id
        }

        fun setUpNavigationLIstener(
                navigationView: BottomNavigationView,
                context: Context,
                changeListener: (Int) -> Unit
        ) {
            Companion.navigationView = navigationView
            startFragment(HomeFragment::class.java, context)
            navigationView.setOnNavigationItemSelectedListener {
                changeListener(it.itemId)
                when (it.itemId) {
                    R.id.navigation_home -> {
                        startFragment(
                                HomeFragment::class.java,
                                context
                        )
                        true
                    }
                    R.id.navigation_favorite -> {
                        startFragment(FavoriteFragment::class.java, context)
                        true
                    }
                    R.id.navigation_trending -> {
                        startFragment(
                                TrendingFragment::class.java,
                                context
                        )
                        true
                    }
                    else -> false
                }
            }
        }
    }
}