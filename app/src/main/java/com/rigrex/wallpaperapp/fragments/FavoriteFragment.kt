package com.rigrex.wallpaperapp.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.rigrex.wallpaperapp.R
import com.rigrex.wallpaperapp.activities.WalPaperInfoActivity
import com.rigrex.wallpaperapp.adapters.FavoriteAdapter
import com.rigrex.wallpaperapp.adapters.MainItemDecorator
import com.rigrex.wallpaperapp.database.RoomRepository
import com.rigrex.wallpaperapp.databinding.FragmentFavoriteBinding
import com.rigrex.wallpaperapp.helpUtility.HelpUtil

class FavoriteFragment : Fragment(), HelpUtil.FLushData {

    private lateinit var binding: FragmentFavoriteBinding
    private lateinit var favAdapter: FavoriteAdapter

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        binding = FragmentFavoriteBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeFavorite()
    }

    private fun initializeFavorite() {
        favAdapter = FavoriteAdapter(resources.getDimension(R.dimen.dp_10).toInt(),
                mutableListOf(), type = FavoriteAdapter.VERTICAL) {
            startActivity(
                    Intent(requireContext(), WalPaperInfoActivity::class.java).putExtra(
                            WalPaperInfoActivity.MODEL,
                            it.castToModel()
                    )
            )
        }

        binding.favRecList.setHasFixedSize(true)
        binding.favRecList.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        binding.favRecList.addItemDecoration(
                MainItemDecorator(
                        resources.getDimension(R.dimen.dp_10).toInt()
                )
        )
        binding.favRecList.adapter = favAdapter
        RoomRepository.getFavorites(this) {
            favAdapter.setData(it)
            if (it.isEmpty()) {
                binding.noContentAvl.visibility = View.VISIBLE
            } else {
                binding.noContentAvl.visibility = View.GONE
            }
        }
    }

    companion object {
        @JvmStatic
        fun newInstance() =
                FavoriteFragment().apply {
                    arguments = Bundle()
                }
    }

    override fun flushData() {
        favAdapter.setData(mutableListOf())
        initializeFavorite()
    }
}