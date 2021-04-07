package com.rigrex.wallpaperapp.fragments

import android.animation.ValueAnimator
import android.app.ActivityOptions
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.KeyEvent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.rigrex.wallpaperapp.R
import com.rigrex.wallpaperapp.activities.SearchActivity
import com.rigrex.wallpaperapp.activities.WalPaperInfoActivity
import com.rigrex.wallpaperapp.adapters.CategoryAdapter
import com.rigrex.wallpaperapp.adapters.FavoriteAdapter
import com.rigrex.wallpaperapp.adapters.MainItemDecorator
import com.rigrex.wallpaperapp.adapters.WallPaperAdapter
import com.rigrex.wallpaperapp.database.RoomRepository
import com.rigrex.wallpaperapp.database.dataModels.WallPaperModel
import com.rigrex.wallpaperapp.databinding.FragmentHomeBinding
import com.rigrex.wallpaperapp.helpUtility.HelpUtil
import com.rigrex.wallpaperapp.helpUtility.ListProvider
import com.rigrex.wallpaperapp.helpUtility.transactions.FragmentTransactionHelper
import com.rigrex.wallpaperapp.network.helpUtil.NetworkHelpNew
import com.rigrex.wallpaperapp.uiComponents.RecyclerViewWatcher
import com.rigrex.wallpaperapp.uiComponents.ViewGroupExtension
import com.rigrex.wallpaperapp.uiComponents.ViewWatcher

class HomeFragment : Fragment(), HelpUtil.FLushData {

    private lateinit var binding: FragmentHomeBinding
    private lateinit var favAdapter: FavoriteAdapter
    private lateinit var catAdapter: CategoryAdapter
    private lateinit var mainAdapter: WallPaperAdapter
    private var inProgress = false

    override fun onCreateView(
            inflater: LayoutInflater, container: ViewGroup?,
            savedInstanceState: Bundle?
    ): View {
        binding = FragmentHomeBinding.inflate(layoutInflater)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initAdapters()
        initializeSearchView()
    }

    fun initializeSearchView() {
//        binding.viewMore.setOnClickListener {
//            FragmentTransactionHelper.startFragment(
//                    FavoriteFragment::class.java,
//                    requireContext(),
//                    botNav = true
//            )
//        }
        binding.searchView.doOnTextChanged { _, _, _, _ ->
            if ("${binding.searchView.text}".isEmpty()) {
                binding.searchCancel.setImageResource(R.drawable.search_icon)
            } else {
                binding.searchCancel.setImageResource(R.drawable.close_icon)
            }
        }
        binding.searchCancel.setOnClickListener {
            if (binding.searchView.text.isNotEmpty()) {
                binding.searchView.setText("")
            }
        }
        binding.searchView.setImeActionLabel("Search", KeyEvent.KEYCODE_ENTER)
        binding.searchView.setOnEditorActionListener { v, actionId, event ->
            startActivity(
                    Intent(requireContext(), SearchActivity::class.java)
                            .putExtra(SearchActivity.SEARCHED_WORD, binding.searchView.text.toString()),
                    ActivityOptions.makeSceneTransitionAnimation(
                            requireActivity(),
                            android.util.Pair(binding.searchView, "search_tr_name"),
                            android.util.Pair(binding.searchCancel, "search_icon_tr_name")
                    ).toBundle()
            )
            true
        }
    }

    fun initAdapters() {
        initializeCategoryAdapter()
        initializeMainAdapter()
//        initializeFavorite()
    }

//    private fun runUI(handler: () -> Unit) {
//        Handler(Looper.getMainLooper()).post(handler)
//    }

    private fun injectViewWatcher(): ViewWatcher<WallPaperModel> {
        return HelpUtil.getInstance().apply {
            windowsToShow(*HelpUtil.getDefaultWindows(requireContext(), layoutInflater).toTypedArray())
            removeAllExtension()
            addExtensions(
                    *HelpUtil.getExtensions().toTypedArray(),
                    RecyclerViewWatcher(binding.mainRecView),
                    ViewGroupExtension(
                            arrayOf(
                                    binding.searchView,
                                    binding.searchCancel,
                                    binding.categoriesRecView,
/*                                    binding.viewMore,
                                    binding.favText,
                                    binding.favRecList*/
                            )
                    )
            )
        }
    }

    private fun initMainRec() {
        mainAdapter = WallPaperAdapter(mutableListOf()) {
            startActivity(
                    Intent(requireContext(), WalPaperInfoActivity::class.java).putExtra(
                            WalPaperInfoActivity.MODEL,
                            it
                    )
            )
        }
        mainAdapter.injectViewWatcher(injectViewWatcher())
        binding.mainRecView.addItemDecoration(
                MainItemDecorator(
                        resources.getDimension(R.dimen.dp_10).toInt()
                )
        )
        binding.mainRecView.setHasFixedSize(true)
        binding.mainRecView.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
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
                val manager = (recyclerView.layoutManager as StaggeredGridLayoutManager?)
                manager?.let {
                    if (inProgress)
                        return
                    val array = IntArray(2)
                    manager.findLastCompletelyVisibleItemPositions(array)
                    if (array[0] == (mainAdapter.itemCount - 1) || array[1] == (mainAdapter.itemCount - 1)) {
                        showLoading()
                        inProgress = true
                        NetworkHelpNew.getNetworkWithOffline(
                                catAdapter.getCurrentCat(),
                                offline = false,
                                ::addData
                        ) { inProgress = false }
                    }
                }
            }
        })
    }

    private fun addData(list: MutableList<WallPaperModel>) {
        inProgress = false
        hideLoading()
        mainAdapter.addMoreItems(list)
        binding.mainRecView.smoothScrollBy(0, 2000, AccelerateDecelerateInterpolator())
    }

    private fun initializeMainAdapter() {
        initMainRec()
        inProgress = true
        NetworkHelpNew.getNetworkWithOffline(catAdapter.getCurrentCat(), offline = true, ::addData) {
            Log.d("HomeFragment", "Error initializeMainAdapter: $it")
        }
    }

    fun showLoading() {

    }

    fun hideLoading() {

    }

    private fun startAnim() {
        ValueAnimator.ofFloat(0f, 1f).apply {
            duration = 300
            addUpdateListener {
                binding.mainRecView.alpha = 1f - (it.animatedValue as Float)
            }
            start()
        }
    }

    private fun revAnim() {
        ValueAnimator.ofFloat(0f, 1f).apply {
            duration = 300
            addUpdateListener {
                binding.mainRecView.alpha = it.animatedValue as Float
            }
            start()
        }
    }

    private fun initializeCategoryAdapter() {
        catAdapter = CategoryAdapter(ListProvider.provideCatList()) {
            startAnim()
            NetworkHelpNew.getNetworkWithOffline(it, offline = true, {
                mainAdapter.changeData(it)
                revAnim()
            }) { revAnim() }
        }
        binding.categoriesRecView.adapter = catAdapter
        binding.categoriesRecView.setHasFixedSize(true)
        binding.categoriesRecView.layoutManager =
                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
    }

//    private fun initializeFavorite() {
//        favAdapter = FavoriteAdapter(resources.getDimension(R.dimen.dp_10).toInt(), mutableListOf()) {
//            startActivity(
//                    Intent(requireContext(), WalPaperInfoActivity::class.java).putExtra(
//                            WalPaperInfoActivity.MODEL,
//                            it.castToModel()
//                    )
//            )
//        }
//        binding.favRecList.adapter = favAdapter
//        binding.favRecList.setHasFixedSize(true)
//        binding.favRecList.layoutManager =
//                LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
//        RoomRepository.getFavorites(this) {
//            runUI {
//                favAdapter.setData(it)
//                if (it.isEmpty()) {
//                    binding.favRecList.visibility = View.GONE
//                    binding.favText.visibility = View.GONE
//                    binding.viewMore.visibility = View.GONE
//                }
//            }
//        }
//    }

    companion object {
        @JvmStatic
        fun newInstance() =
                HomeFragment().apply {
                    arguments = Bundle()
                }
    }

    override fun flushData() {
        mainAdapter.changeData(mutableListOf())
        favAdapter.setData(mutableListOf())
        catAdapter.setData(mutableListOf())
        initAdapters()
    }
}