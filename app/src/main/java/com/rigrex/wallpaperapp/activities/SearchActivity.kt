package com.rigrex.wallpaperapp.activities

import android.animation.ValueAnimator
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.KeyEvent
import android.view.View
import android.view.ViewGroup
import androidx.appcompat.app.AppCompatActivity
import androidx.core.widget.doOnTextChanged
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.rigrex.wallpaperapp.R
import com.rigrex.wallpaperapp.adapters.MainItemDecorator
import com.rigrex.wallpaperapp.adapters.WallPaperAdapter
import com.rigrex.wallpaperapp.database.dataModels.WallPaperModel
import com.rigrex.wallpaperapp.databinding.ActivitySearchBinding
import com.rigrex.wallpaperapp.helpUtility.HelpUtil
import com.rigrex.wallpaperapp.network.helpUtil.NetworkHelpNew
import com.rigrex.wallpaperapp.uiComponents.RecyclerViewWatcher
import com.rigrex.wallpaperapp.uiComponents.ViewGroupExtension
import com.rigrex.wallpaperapp.uiComponents.ViewWatcher
import com.rigrex.wallpaperapp.uiComponents.ViewWatcherSettings

class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding
    private lateinit var mainAdapter: WallPaperAdapter
    private var inProgress = false
    private var searchedtext = ""
    private var value = 0f
    private lateinit var startAnim: ValueAnimator
    private lateinit var endAnim: ValueAnimator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        supportActionBar?.hide()
        value = resources.getDimension(R.dimen.dp_90)
        initAnims()
        initializeSearchView()
        initMainAdapter()
        searchData(intent?.getStringExtra(SEARCHED_WORD) ?: "")
    }

    private fun initAnims() {
        startAnim = ValueAnimator.ofFloat(0f, value).apply {
            duration = 300
            addUpdateListener {
                var temp = (it.animatedValue as Float)
                binding.searchView.translationY = -temp
                binding.searchCancel.translationY = -temp
            }
        }
        endAnim = ValueAnimator.ofFloat(0f, value).apply {
            duration = 300
            addUpdateListener {
                var temp = (it.animatedValue as Float)
                binding.searchView.translationY = (temp - value)
                binding.searchCancel.translationY = (temp - value)
            }
        }
    }

    private fun hideAnim() {
        if (binding.searchView.translationY != -value) {
            endAnim.cancel()
            startAnim.start()
        }
    }

    private fun showAnim() {
        if (binding.searchView.translationY != 0f) {
            startAnim.cancel()
            endAnim.start()
        }
    }

    fun searchData(query: String) {
        if (query.isEmpty())
            return
        searchedtext = query
        Log.d("searchActivity", "searchData: $query")
        NetworkHelpNew.getNetworkWithOfflineSearch(query, offline = true) {
                    if (it.isEmpty()) {
                        binding.noContentAvl.visibility = View.VISIBLE
                    } else {
                        binding.noContentAvl.visibility = View.GONE
                    }
                    mainAdapter.changeData(it)
                }
    }

    private fun getViewWatcher(): ViewWatcher<WallPaperModel> {
        return ViewWatcher<WallPaperModel>(
                ViewWatcherSettings
                        .SettingsBuilder(resources)
                        .enableExtensions()
                        .build(), binding.root.parent as ViewGroup
        ) {}.apply {
            setExtensions(
                    RecyclerViewWatcher(binding.mainRecView),
                    ViewGroupExtension(arrayOf(binding.searchCancel, binding.searchView))
            )
            HelpUtil
                    .getDefaultWindows(this@SearchActivity, layoutInflater)
                    .forEach { windowToShow(it) }
        }
    }

    private fun initMainAdapter() {
        mainAdapter = WallPaperAdapter(mutableListOf()) {
            startActivity(
                    Intent(this, WalPaperInfoActivity::class.java).putExtra(
                            WalPaperInfoActivity.MODEL,
                            it
                    )
            )
        }
        binding.mainRecView.postDelayed({ mainAdapter.injectViewWatcher(getViewWatcher()) }, 1000)
        binding.mainRecView.addItemDecoration(MainItemDecorator(resources.getDimension(R.dimen.dp_10).toInt()))
        binding.mainRecView.setHasFixedSize(true)
        binding.mainRecView.layoutManager = StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
        binding.mainRecView.adapter = mainAdapter
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
                                searchedtext,
                                offline = false,
                                {
                                    hideLoading()
                                    mainAdapter.addMoreItems(it)
                                    inProgress = false
                                }
                        ) { inProgress = false }
                    }
                }
            }
        })
        binding.mainRecView.onFlingListener = object : RecyclerView.OnFlingListener() {
            override fun onFling(velocityX: Int, velocityY: Int): Boolean {
                if (velocityY < 0) {
                    showAnim()
                } else {
                    hideAnim()
                }

                return false
            }
        }
        hideLoading()
    }

    fun showLoading() {

    }

    fun hideLoading() {

    }

    private fun initializeSearchView() {
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
        binding.searchView.setText(intent?.getStringExtra(SEARCHED_WORD))
        binding.searchView.setImeActionLabel("Search", KeyEvent.KEYCODE_ENTER)
        binding.searchView.setOnEditorActionListener { v, actionId, event ->
            if (binding.searchView.text.toString().isNotEmpty()) {
                searchData(binding.searchView.text.toString())
            }
            true
        }
    }

    companion object {
        const val SEARCHED_WORD = "searched_text"
    }
}