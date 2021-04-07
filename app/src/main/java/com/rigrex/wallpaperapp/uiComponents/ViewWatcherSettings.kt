package com.rigrex.wallpaperapp.uiComponents

import android.content.res.Resources
import android.graphics.*
import com.rigrex.wallpaperapp.R

class ViewWatcherSettings(var resources: Resources) {
    var radius = resources.getDimension(R.dimen.dp_90)
    var enableExtensions = false
    var heightAdjustment = resources.getDimension(R.dimen.dp_5)
    var cornerRadius = resources.getDimension(R.dimen.dp_25)
    var statusBarHeight = resources.getDimension(R.dimen.dp_24)
    var elevation = resources.getDimension(R.dimen.dp_10)

    var transparentPaint = Paint().apply {
        color = Color.TRANSPARENT
        xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC)
    }
    var textPaint = Paint().apply {
        color = Color.WHITE
        textSize = resources.getDimension(R.dimen.sp_20)
        typeface = Typeface.DEFAULT_BOLD
        style = Paint.Style.FILL
    }
    var circlePaint = Paint().apply {
        xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC)
        color = Color.RED
        strokeWidth = 3f
        style = Paint.Style.STROKE
    }

    var blackRectangle = Paint().apply {
        color = Color.BLACK
        xfermode = PorterDuffXfermode(PorterDuff.Mode.SRC)
    }

    class SettingsBuilder(var resources: Resources) {

        private var settings = ViewWatcherSettings(resources)

        fun addTextColor(color: Int): SettingsBuilder {
            settings.textPaint.apply {
                this.color = color
            }
            return this
        }

        fun addElevation(elevation: Float): SettingsBuilder {
            settings.elevation = elevation
            return this
        }


        fun enableExtensions(): SettingsBuilder {
            settings.enableExtensions = true
            return this
        }

        fun disableExtensions(): SettingsBuilder {
            settings.enableExtensions = false
            return this
        }

        fun addPointerColor(color: Int): SettingsBuilder {
            settings.circlePaint.apply {
                this.color = color
            }
            return this
        }

        fun addOverDrawnColor(color: Int): SettingsBuilder {
            settings.blackRectangle.apply {
                this.color = color
            }
            return this
        }

        fun addCornerRadius(corner: Float): SettingsBuilder {
            settings.cornerRadius = corner
            return this
        }

        fun addStatusBarHeight(height: Float): SettingsBuilder {
            settings.statusBarHeight = height
            return this
        }

        fun addRadius(radius: Float): SettingsBuilder {
            settings.radius = radius
            return this
        }

        fun addHeightAdjustment(adjustment: Float): SettingsBuilder {
            settings.heightAdjustment = adjustment
            return this
        }

        fun addTextSize(textSize: Float): SettingsBuilder {
            settings.textPaint.apply {
                this.textSize = textSize
            }
            return this
        }

        fun build(): ViewWatcherSettings {
            return settings
        }
    }
}
