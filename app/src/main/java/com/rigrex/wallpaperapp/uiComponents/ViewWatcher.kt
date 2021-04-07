package com.rigrex.wallpaperapp.uiComponents

import android.animation.ValueAnimator
import android.annotation.SuppressLint
import android.content.Context
import android.graphics.*
import android.os.Build
import android.os.Handler
import android.os.Looper
import android.util.DisplayMetrics
import android.view.*
import kotlin.math.PI
import kotlin.math.cos
import kotlin.math.sin

@SuppressLint("ClickableViewAccessibility")
open class ViewWatcher<T>(private var settings: ViewWatcherSettings, viewGroup: ViewGroup, private var handler: (Int) -> Unit) {

    private var view: View? = null
    private var currentElevation = 0f
    private var scaleAnimator: ValueAnimator? = null
    private var elevateAnimator: ValueAnimator? = null
    private var xPositionOnScreen = 0
    private var yPositionOnScreen = 0
    private var gapHeight = 0
    private var currentTouchX = -1f
    private var currentTouchY = -1f
    private var gapWidth = 0
    private var anchorSheet: View
    private var bitmap: Bitmap? = null
    private var canvas: Canvas? = null
    private var path: Path? = null
    private var paint = Paint()
    private var cornerMode = FULL_ROUND
    private var currentText = ""
    private var screenSize = IntArray(2)
    private var windows = mutableListOf<ViewWatcherWindow<T>>()
    private var extensions = mutableListOf<ViewWatcherExtension>()

    constructor(viewGroup: ViewGroup, handler: (Int) -> Unit) : this(ViewWatcherSettings.SettingsBuilder(viewGroup.resources!!).build(), viewGroup, handler)

    companion object {
        const val START = 0
        const val END = 1
        const val UPPER_ROUND = 2
        const val LOWER_ROUND = 3
        const val FULL_ROUND = 4
        const val RIGHT_SIDE = 5
        const val LEFT_SIDE = 6
    }

    private fun getScreenWidth(context: Context, array: IntArray) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            val windowMetrics = context.getSystemService(WindowManager::class.java).currentWindowMetrics
            array[0] = windowMetrics.bounds.width()
            array[1] = windowMetrics.bounds.height()
        } else {
            val displayMetrics = DisplayMetrics()
            context.display?.getRealMetrics(displayMetrics)
            array[0] = displayMetrics.widthPixels
            array[1] = displayMetrics.heightPixels
        }
    }

    fun windowsToShow(vararg window: ViewWatcherWindow<T>) {
        synchronized(window) {
            window.forEach {
                it.injectHover(::informTitle)
            }
            this.windows = window.toMutableList()
        }
    }

    fun windowToShow(window: ViewWatcherWindow<T>, position: Int) {
        window.injectHover(::informTitle)
        if (position >= this.windows.size) {
            this.windows.add(window)
        } else {
            this.windows[position] = window
        }
    }

    fun windowToShow(window: ViewWatcherWindow<T>) {
        window.injectHover(::informTitle)
        this.windows.add(window)
    }

    init {
        anchorSheet = generateAnchorSheet(viewGroup.context).apply {
            visibility = View.GONE
            layoutParams = ViewGroup.LayoutParams(
                    ViewGroup.LayoutParams.MATCH_PARENT,
                    ViewGroup.LayoutParams.MATCH_PARENT
            )
        }
        getScreenWidth(viewGroup.context, screenSize)
        bitmap = Bitmap.createBitmap(viewGroup.width, viewGroup.height, Bitmap.Config.ARGB_8888)
        canvas = Canvas(bitmap!!)
        path = Path()
        viewGroup.addView(anchorSheet)
        if (settings.enableExtensions) {
            enableExtensions()
        }
    }

    private fun enableExtensions() {
        val oldHandler = handler
        handler = {
            oldHandler.invoke(it)
            this.extensions.forEach { ext -> ext.getHandler().invoke(it, this.view!!) }
        }
    }

    fun removeAllExtension() {
        this.extensions.clear()
    }

    fun addExtensions(vararg recExtension: ViewWatcherExtension) {
        this.extensions.addAll(recExtension.toMutableList())
    }

    fun setExtensions(vararg recExtension: ViewWatcherExtension) {
        this.extensions = recExtension.toMutableList()
    }

    private fun generateAnchorSheet(context: Context): View {
        return object : View(context) {

            override fun onDraw(canvas: Canvas?) {
                super.onDraw(canvas)
                canvas?.apply {
                    this@ViewWatcher.canvas?.drawRect(
                            0f,
                            0f,
                            width.toFloat(),
                            height.toFloat(),
                            settings.blackRectangle
                    )
                    this@ViewWatcher.canvas?.drawPath(
                            makeRounded(
                                    xPositionOnScreen.toFloat() - (gapWidth * 0.025f),
                                    yPositionOnScreen.toFloat() - (gapHeight * 0.025f) - settings.statusBarHeight + getAdjustedhw(LOWER_ROUND),
                                    xPositionOnScreen + (gapWidth * 1.025f),
                                    yPositionOnScreen + (gapHeight * 1.025f) - settings.statusBarHeight - getAdjustedhw(UPPER_ROUND))!!,
                            settings.transparentPaint)
                    if (currentTouchX != -1f && currentTouchY != -1f)
                        this@ViewWatcher.canvas?.drawCircle(currentTouchX, currentTouchY, 50f, settings.circlePaint)
                    this@ViewWatcher.canvas?.drawText(currentText, (settings.heightAdjustment * 2), settings.textPaint.textSize + (settings.heightAdjustment * 3), settings.textPaint)
                    drawBitmap(bitmap!!, 0f, 0f, this@ViewWatcher.paint)
                }
            }
        }
    }

    private fun getAdjustedhw(forVar: Int): Float {
        return if (cornerMode == forVar) settings.heightAdjustment else 0f
    }

    private fun makeRounded(left: Float, top: Float, right: Float, bottom: Float): Path? {
        return makeRounded(RectF(left, top, right, bottom), getCorners(UPPER_ROUND), getCorners(LOWER_ROUND))
    }

    private fun makeRounded(rectF: RectF, top: Float, bottom: Float): Path? {
        return makeRounded(rectF, top, top, bottom, bottom)
    }

    private fun makeRounded(rect: RectF, topLeftDiameter: Float, topRightDiameter: Float, bottomRightDiameter: Float, bottomLeftDiameter: Float): Path? {
        path?.apply {
            reset()
            moveTo(rect.left + topLeftDiameter / 2, rect.top)
            lineTo(rect.right - topRightDiameter / 2, rect.top)
            quadTo(rect.right, rect.top, rect.right, rect.top + topRightDiameter / 2)
            lineTo(rect.right, rect.bottom - bottomRightDiameter / 2)
            quadTo(rect.right, rect.bottom, rect.right - bottomRightDiameter / 2, rect.bottom)
            lineTo(rect.left + bottomLeftDiameter / 2, rect.bottom)
            quadTo(rect.left, rect.bottom, rect.left, rect.bottom - bottomLeftDiameter / 2)
            lineTo(rect.left, rect.top + topLeftDiameter / 2)
            quadTo(rect.left, rect.top, rect.left + topLeftDiameter / 2, rect.top)
            close()
        }

        return path
    }

    private fun getCorners(type: Int): Float {
        return if (cornerMode == type || cornerMode == FULL_ROUND) {
            settings.cornerRadius
        } else {
            0f
        }
    }


    private fun setData(t: T) {
        this.windows.forEach { it.setModel(t) }
    }

    fun startWatch(view: View, t: T) {
        runOnUiThread {
            setData(t)
            saveViewData(view)
            addListener()
            showWindow()
            view.requestFocus()
        }
    }

    private fun extractViewData(view: View) {
        val rectangle = Rect()
        view.getGlobalVisibleRect(rectangle)
        gapHeight = rectangle.height()
        gapWidth = rectangle.width()
        val array = IntArray(2)
        view.getLocationOnScreen(array)
        currentElevation = view.elevation
        xPositionOnScreen = rectangle.left
        yPositionOnScreen = rectangle.top
        cornerMode = when {
            rectangle.height() == view.height -> FULL_ROUND
            yPositionOnScreen > array[1] -> LOWER_ROUND
            else -> UPPER_ROUND
        }
    }

    private fun getHorizontalSide(): Int {
        return if (currentTouchX < (screenSize[0] / 2))
            LEFT_SIDE
        else
            RIGHT_SIDE
    }

    private fun getStartAngle(): Float {
        val horizontalSide = getHorizontalSide()
        return when (cornerMode) {
            UPPER_ROUND -> if (horizontalSide == LEFT_SIDE) 270f else 180f
            LOWER_ROUND -> if (horizontalSide == LEFT_SIDE) 0f else 90f
            FULL_ROUND -> if (horizontalSide == LEFT_SIDE) 270f else 180f
            else -> throw IllegalArgumentException("The corner mode is unknown")
        }
    }

    private fun startDrawProcess() {
        addDelay()
        var startAngle = getStartAngle()
        windows.forEach {
            val array = getCircularPoints(settings.radius, startAngle)
            showWindow(it, array[0].toInt(), array[1].toInt())
            startAngle += 50f
        }
    }

    private fun getCircularPoints(radius: Float, angle: Float): Array<Float> {
        val x = (radius * cos((angle * PI) / 180f)).toFloat() + (currentTouchX - settings.statusBarHeight)
        val y = (radius * sin((angle * PI) / 180f)).toFloat() + (currentTouchY - settings.statusBarHeight)

        return arrayOf(x, y)
    }

    private fun showWindow(window: ViewWatcherWindow<T>, x: Int, y: Int) {
        window.showWindow(this.view!!, x, y)
    }

    private fun addDelay() {
        var first = 20L
        windows.forEach {
            it.apply { addDelay(first) }
            first += 20
        }
    }

    private fun listenForHover(x: Int, y: Int) {
        synchronized(windows) {
            for (i in 0 until windows.size) {
                if (windows[i].listenForHover(x, y)) {
                    activateHover(i)
                    return
                }
            }
        }
    }

    private fun activateHover(position: Int) {
        synchronized(windows) {
            for (i in 0 until windows.size) {
                if (position != i) {
                    windows[i].removeHover()
                }
            }
        }
    }

    private fun hideAllWindows() {
        windows.forEach {
            it.adjustDelays()
            it.hideWindow()
        }
    }

    private fun addListener() {
        this.view?.setOnTouchListener { _, event ->
            if (currentTouchX == -1f) {
                currentTouchX = event.rawX
                currentTouchY = event.rawY
                view?.invalidate()
                startDrawProcess()
            }
            when (event.action) {
                MotionEvent.ACTION_UP -> {
                    handler(END)
                    removeWatch()
                    return@setOnTouchListener true
                }
                MotionEvent.ACTION_DOWN -> {
                    showWindow()
                    handler(START)
                    return@setOnTouchListener true
                }
                MotionEvent.ACTION_MOVE -> {
                    if (currentTouchX != -1f) {
                        listenForHover(event.rawX.toInt(), event.rawY.toInt())
                        return@setOnTouchListener true
                    }
                }
            }

            false
        }
    }

    private fun showWindow() {
        elevateAnimator?.start()
        scaleAnimator?.start()
    }

    private fun hideWindow() {
        currentTouchX = -1f
        currentTouchY = -1f
        hideAllWindows()
        elevateAnimator?.reverse()
        scaleAnimator?.reverse()
    }

    private fun initAnimations() {
        if (scaleAnimator != null && elevateAnimator != null) return
        scaleAnimator = ValueAnimator.ofFloat(1f, 1.05f).apply {
            duration = 200
            addUpdateListener {
                val value = it.animatedValue as Float
                this@ViewWatcher.view?.apply {
                    scaleX = value
                    scaleY = value
                }
                if (value == 1.05f) {
                    anchorSheet.visibility = View.VISIBLE
                } else {
                    anchorSheet.visibility = View.GONE
                }
            }
        }
        elevateAnimator = ValueAnimator.ofFloat(currentElevation, settings.elevation).apply {
            duration = 200
            addUpdateListener {
                this@ViewWatcher.view?.elevation = it.animatedValue as Float
            }
        }
    }

    private fun saveViewData(view: View) {
        this.view = view
        extractViewData(view)
        initAnimations()
        handler(START)
    }

    private fun removeWatch() {
        runOnUiThread {
            currentText = ""
            hideWindow()
            view?.setOnTouchListener { v, event -> false }
            Handler(Looper.getMainLooper()).postDelayed({ view = null }, 250)
        }
    }

    private fun runOnUiThread(handler: () -> Unit) {
        Handler(Looper.getMainLooper()).post(handler)
    }

    private fun informTitle(title: String) {
        currentText = title
        runOnUiThread {
            this.anchorSheet.invalidate()
        }
    }

    interface ViewWatcherExtension {
        fun getHandler(): ((Int, View) -> Unit)
    }
}