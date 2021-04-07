package com.rigrex.wallpaperapp.helpUtility.transactions

import androidx.fragment.app.Fragment
import com.rigrex.wallpaperapp.fragments.FavoriteFragment
import com.rigrex.wallpaperapp.fragments.HomeFragment
import com.rigrex.wallpaperapp.fragments.TrendingFragment

abstract class FragmentFactory<T> {
    abstract fun provideFragment(
            clazz: Class<T>,
            new: Boolean = false
    ): T
}

class DefaultFactory<T : Fragment> : FragmentFactory<T>() {
    lateinit var homeFragment: HomeFragment
    lateinit var trendingFragment: TrendingFragment
    lateinit var favoriteFragment: FavoriteFragment

    override fun provideFragment(
            clazz: Class<T>,
            new: Boolean
    ) =
            (when (clazz) {
                HomeFragment::class.java -> {
                    if (!::homeFragment.isInitialized) {
                        homeFragment = HomeFragment.newInstance()
                    }
                    homeFragment
                }
                TrendingFragment::class.java -> {
                    if (!::trendingFragment.isInitialized) {
                        trendingFragment = TrendingFragment.newInstance()
                    }
                    trendingFragment
                }
                FavoriteFragment::class.java -> {
                    if (!::favoriteFragment.isInitialized) {
                        favoriteFragment = FavoriteFragment.newInstance()
                    }
                    favoriteFragment
                }
                else -> throw Exception("Wrong class can't find it")
            } as T)
}