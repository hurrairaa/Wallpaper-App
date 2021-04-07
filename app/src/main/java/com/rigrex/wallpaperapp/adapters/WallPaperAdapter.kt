package com.rigrex.wallpaperapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.rigrex.wallpaperapp.R
import com.rigrex.wallpaperapp.database.dataModels.WallPaperModel
import com.rigrex.wallpaperapp.uiComponents.ViewWatcher

class WallPaperAdapter(
        var list: MutableList<WallPaperModel>,
        var onClick: (WallPaperModel) -> Unit
) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    init {
        if (list.isNotEmpty()) {
            list.add(WallPaperModel())
        }
    }

    private var viewWatcher: ViewWatcher<WallPaperModel>? = null

    companion object {
        const val LOADING = 0
        const val NORMAL_VIEW = 1
    }

    class WallpaperViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var wallpaper = view.findViewById<ImageView>(R.id.image)
    }

    class LoadingViewHolder(view: View) : RecyclerView.ViewHolder(view)

    override fun getItemViewType(position: Int): Int {
        return if (position == list.size - 1) {
            LOADING
        } else {
            NORMAL_VIEW
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == LOADING)
            LoadingViewHolder(
                    LayoutInflater.from(parent.context).inflate(R.layout.loading_view, parent, false)
            )
        else
            WallpaperViewHolder(
                    LayoutInflater.from(parent.context).inflate(R.layout.wallpaper_view, parent, false)
            )
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        if (holder is WallpaperViewHolder) {
            Glide
                    .with(holder.wallpaper.context)
                    .load(list[position].previewImageUrl)
                    .placeholder(R.drawable.black_round)
                    .transform(
                            RoundedCorners(
                                    holder.wallpaper.context.resources?.getDimension(R.dimen.dp_10)?.toInt()
                                            ?: 0
                            )
                    )
                    .into(holder.wallpaper)
            holder.itemView.setOnClickListener { onClick(list[position]) }
            holder.itemView.setOnLongClickListener { viewWatcher?.startWatch(it, list[position]);false }
        } else if (holder is LoadingViewHolder) {
            (holder.itemView.layoutParams as StaggeredGridLayoutManager.LayoutParams).isFullSpan =
                    true
        }
    }

    fun injectViewWatcher(viewWatcher: ViewWatcher<WallPaperModel>) {
        this.viewWatcher = viewWatcher
    }

    fun changeData(list: MutableList<WallPaperModel>) {
        this.list = list
        list.add(WallPaperModel())
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun addMoreItems(list: MutableList<WallPaperModel>) {
        var size = this.list.size
        list.removeLast()
        list.forEach { this.list.add(it) }
        notifyItemInserted(size)
    }
}