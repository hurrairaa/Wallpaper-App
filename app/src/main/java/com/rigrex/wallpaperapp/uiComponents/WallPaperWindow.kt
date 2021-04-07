package com.rigrex.wallpaperapp.uiComponents

import android.animation.ValueAnimator
import android.graphics.Color
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.view.Gravity
import android.view.View
import android.view.ViewGroup.LayoutParams.WRAP_CONTENT
import android.widget.PopupWindow
import com.rigrex.wallpaperapp.R
import com.rigrex.wallpaperapp.database.RoomRepository
import com.rigrex.wallpaperapp.database.dataModels.WallPaperModel
import com.rigrex.wallpaperapp.databinding.WallpaperWindowBinding
import com.rigrex.wallpaperapp.network.DownloadUtil

class WallPaperWindow<T>(var binding: WallpaperWindowBinding, iconRes: Int, title: String) : ViewWatcherWindow<T>(title) {

    private var window: PopupWindow
    private var size = 0f
    private var currentLocation = IntArray(2)
    private var endPoints = IntArray(2)
    private var maximumSize = 0f
    private var smallestXY = 0f
    private var animation: ValueAnimator? = null

    init {
        size = binding.root.context?.resources?.getDimension(R.dimen.dp_40) ?: 0f
        maximumSize = binding.root.context?.resources?.getDimension(R.dimen.dp_50) ?: 0f
        smallestXY = binding.root.context?.resources?.getDimension(R.dimen.dp_10) ?: 0f
        window = PopupWindow(binding.root, WRAP_CONTENT, WRAP_CONTENT, false)
        binding.shareCircle.setImageResource(iconRes)
        initializeAnimations()
    }

    private fun initializeAnimations() {
        animation = ValueAnimator.ofFloat(0f, 1f).apply {
            duration = 180
            addUpdateListener {
                val s = it.animatedValue as Float
                binding.shareCircle.scaleX = s
                binding.shareCircle.scaleY = s
            }
        }
    }

    fun changeIcon(iconRes: Int) {
        binding.shareCircle.setImageResource(iconRes)
    }

    override fun adjustDelays() {
        animation?.apply {
            startDelay = 0
            duration = 180
        }
    }

    override fun addDelay(delay: Long) {
        animation?.apply {
            startDelay = delay
            duration -= delay
        }
    }


    override fun hideWindow() {
        animation?.reverse()
        if (binding.root.isHovered) {
            t?.let { onSelected?.invoke(it) }
        }
        removeHover()
        Handler(Looper.getMainLooper()).postDelayed({ window.dismiss() }, 120)
    }

    private fun takeCurrentData(x: Int, y: Int) {
        currentLocation[0] = x - 50
        currentLocation[1] = y - 50
        if (binding.root.height == 0) {
            binding.root.measure(
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED),
                    View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED)
            )
            endPoints[0] = x + binding.root.measuredHeight + 50
            endPoints[1] = y + binding.root.measuredWidth + 50
        } else {
            endPoints[0] = x + binding.root.height + 50
            endPoints[1] = y + binding.root.width + 50
        }
    }

    override fun removeHover() {
        if (binding.root.isHovered) {
            binding.shareCircle.setColorFilter(Color.BLACK)
            binding.root.isHovered = false
        }
    }

    override fun listenForHover(x: Int, y: Int): Boolean {
        if (x in currentLocation[0]..endPoints[0] && y in currentLocation[1]..endPoints[1]) {
            if (!binding.root.isHovered) {
                binding.shareCircle.setColorFilter(Color.WHITE)
                binding.root.isHovered = true
                onHover?.invoke(title)
                return true
            }
        } else {
            if (binding.root.isHovered) {
                binding.shareCircle.setColorFilter(Color.BLACK)
                binding.root.isHovered = false
                onHover?.invoke("")
            }
        }
        return false
    }

    override fun showWindow(view: View, x: Int, y: Int) {
        t?.let { this.onShow?.invoke(it) }
        takeCurrentData(x, y)
        window.showAtLocation(view, Gravity.NO_GRAVITY, (x + smallestXY * 2).toInt(), (y + smallestXY * 2).toInt())
        animation?.start()
    }
}