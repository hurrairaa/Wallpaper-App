package com.rigrex.wallpaperapp.fragments

import android.content.Intent
import android.graphics.Rect
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.rigrex.wallpaperapp.R
import com.rigrex.wallpaperapp.activities.WalPaperInfoActivity
import com.rigrex.wallpaperapp.adapters.WallPaperAdapter
import com.rigrex.wallpaperapp.database.dataModels.WallPaperModel
import com.rigrex.wallpaperapp.databinding.FragmentTrendingBinding
import com.rigrex.wallpaperapp.helpUtility.HelpUtil
import com.rigrex.wallpaperapp.network.helpUtil.NetworkHelpNew
import com.rigrex.wallpaperapp.uiComponents.RecyclerViewWatcher
import com.rigrex.wallpaperapp.uiComponents.ViewGroupExtension
import com.rigrex.wallpaperapp.uiComponents.ViewWatcher
import com.rigrex.wallpaperapp.uiComponents.WallPaperWindow

class TrendingFragment : Fragment(), HelpUtil.FLushData {

    private var inProgress = false
    private lateinit var binding: FragmentTrendingBinding
    private lateinit var mainAdapter: WallPaperAdapter

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        binding = FragmentTrendingBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initializeMainAdapter()
    }

    private fun injectViewWatcher(): ViewWatcher<WallPaperModel> {
        return HelpUtil.getInstance().apply {
            windowsToShow(*HelpUtil.getDefaultWindows(requireContext(), layoutInflater).toTypedArray())
            removeAllExtension()
            addExtensions(*HelpUtil.getExtensions().toTypedArray(), RecyclerViewWatcher(binding.mainRecView))
        }
    }

    private fun initMainRec() {
        var item = resources.getDimension(R.dimen.dp_10)
        mainAdapter = WallPaperAdapter(mutableListOf()) {
            startActivity(
                    Intent(requireContext(), WalPaperInfoActivity::class.java).putExtra(
                            WalPaperInfoActivity.MODEL,
                            it
                    )
            )
        }
        binding.mainRecView.addItemDecoration(object : RecyclerView.ItemDecoration() {
            override fun getItemOffsets(
                    outRect: Rect,
                    view: View,
                    parent: RecyclerView,
                    state: RecyclerView.State
            ) {
                outRect.left = item.toInt()
                outRect.right = item.toInt()
                outRect.bottom = item.toInt()
                if (parent.getChildAdapterPosition(view) == 0)
                    outRect.top = item.toInt()
            }
        })
        binding.mainRecView.setHasFixedSize(true)
        binding.mainRecView.layoutManager =
                StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        mainAdapter.injectViewWatcher(injectViewWatcher())
        binding.mainRecView.adapter = mainAdapter
        addScrollListener()
    }

    private fun addScrollListener() {
        binding.mainRecView.addOnScrollListener(object :
                RecyclerView.OnScrollListener() {
            override fun onScrollStateChanged(
                    recyclerView: RecyclerView,
                    newState: Int
            ) {
                super.onScrollStateChanged(recyclerView, newState)
                var manager = (recyclerView.layoutManager as StaggeredGridLayoutManager?)
                manager?.let {
                    if (inProgress)
                        return
                    var array = IntArray(2)
                    manager.findLastCompletelyVisibleItemPositions(array)
                    if (array[0] == (mainAdapter.itemCount - 1) || array[1] == (mainAdapter.itemCount - 1)) {
                        showLoading()
                        inProgress = true
                        NetworkHelpNew.getNetworkWithOffline(
                                "Trending",
                                offline = false, ::addData
                        ) { inProgress = false }
                    }
                }
            }
        })
    }

    fun addData(list: MutableList<WallPaperModel>) {
        hideLoading()
        mainAdapter.addMoreItems(list)
        inProgress = false
        binding.mainRecView.smoothScrollBy(0, 2000, AccelerateDecelerateInterpolator())
    }

    private fun initializeMainAdapter() {
        initMainRec()
        inProgress = true
        showLoading()
        NetworkHelpNew.getNetworkWithOffline("Trending", offline = true, ::addData) { hideLoading();Log.d("HomeFragment", "Error initializeMainAdapter: $it") }
    }

    fun showLoading() {

    }

    fun hideLoading() {

    }

    companion object {

        @JvmStatic
        fun newInstance() =
                TrendingFragment().apply {
                    arguments = Bundle()
                }
    }

    override fun flushData() {
        mainAdapter.changeData(mutableListOf())
        initMainRec()
    }
}