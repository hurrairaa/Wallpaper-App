package com.rigrex.wallpaperapp.uiComponents

import android.view.View


abstract class ViewWatcherWindow<T>(var title: String) {
    internal var t: T? = null
    internal var onSelected: ((T) -> Unit)? = null
    internal var onShow: ((T) -> Unit)? = null
    internal var onHover: ((String) -> Unit)? = null

    fun injectHover(hover: (String) -> Unit) {
        this.onHover = hover
    }

    fun onSelected(onSelected: (T) -> Unit) {
        this.onSelected = onSelected
    }

    fun onShow(onShow: (T) -> Unit) {
        this.onShow = onShow
    }

    fun setModel(t: T) {
        this.t = t
    }

    /**
     * This will be called by ViewWatcher to adjust the delays of animations to original level
     * **/
    abstract fun adjustDelays()

    /**
     * This is used when you want to add delay to an window animation to show up cool effects.
     *
     *@param delay it is the delay you should add to your animation if you have multiple windows to show
     * and you want to show animation one after another the delay can be added.
     * The delay can be multiple of delay passed along its upto you
     * but standard delay is passed to show all windows sequentially.
     *
     * **/
    abstract fun addDelay(delay: Long)

    /**
     * Called by the ViewWatcher to hide the windows when the action is done.
     * Here you should wrap up the operations or start a reverse animation if you have any.
     * TO hide window with animation.
     * If you want to invoke selected action it is the best place to check if user have selected
     * the window when hide is invoked.
     *
     * **/
    abstract fun hideWindow()
    abstract fun removeHover()
    abstract fun listenForHover(x: Int, y: Int): Boolean
    abstract fun showWindow(view: View, x: Int, y: Int)
}