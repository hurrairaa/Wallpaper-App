package com.rigrex.wallpaperapp.adapters

import android.animation.LayoutTransition
import android.graphics.Canvas
import android.graphics.Rect
import android.view.View
import androidx.recyclerview.widget.RecyclerView

class MainItemDecorator(var item: Int) : RecyclerView.ItemDecoration() {
    override fun getItemOffsets(
            outRect: Rect,
            view: View,
            parent: RecyclerView,
            state: RecyclerView.State
    ) {
        outRect.left = item
        outRect.right = item
        outRect.bottom = item
        if (parent.getChildAdapterPosition(view) == 0)
            outRect.top = item
    }

}