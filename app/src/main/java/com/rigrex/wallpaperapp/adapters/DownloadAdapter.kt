package com.rigrex.wallpaperapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.rigrex.wallpaperapp.R
import com.rigrex.wallpaperapp.database.dataModels.DownloadModel
import com.rigrex.wallpaperapp.database.dataModels.WallPaperModel
import com.rigrex.wallpaperapp.network.DownloadUtil
import java.io.File

class DownloadAdapter(
        var list: MutableList<DownloadModel>,
        var enableChecking: Boolean = false,
        var onClick: (DownloadModel) -> Unit
) : RecyclerView.Adapter<DownloadAdapter.DownloadViewHolder>() {

    class DownloadViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var wallpaper = view.findViewById<ImageView>(R.id.image)
        var checkBox = view.findViewById<View>(R.id.check_box)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): DownloadViewHolder {
        return DownloadViewHolder(
                LayoutInflater.from(parent.context).inflate(R.layout.wallpaper_view, parent, false)
        )
    }

    override fun onBindViewHolder(holder: DownloadViewHolder, position: Int) {
        Glide
                .with(holder.wallpaper.context)
                .load(File("${DownloadUtil.DOWNLOAD_PATH}${list[position].getWallPaperName()}"))
                .placeholder(R.drawable.black_round)
                .transform(
                        RoundedCorners(
                                holder.wallpaper.context.resources?.getDimension(R.dimen.dp_10)?.toInt()
                                        ?: 0
                        )
                )
                .into(holder.wallpaper)
        holder.itemView.setOnClickListener {
            onClick(list[position])
            if (enableChecking) {
                list[position].isSelected = !list[position].isSelected
                holder.checkBox.isSelected = list[position].isSelected
            }
        }
        holder.checkBox.setOnClickListener {
            list[position].isSelected = !list[position].isSelected
            holder.checkBox.isSelected = list[position].isSelected
        }
        if (enableChecking) {
            holder.checkBox.visibility = View.VISIBLE
            holder.checkBox.isSelected = list[position].isSelected
        } else {
            holder.checkBox.visibility = View.GONE
        }
    }

    fun getSelectedWallPapers(): List<DownloadModel> {
        return list.filter { it.isSelected }
    }

    fun changeData(list: MutableList<DownloadModel>) {
        this.list = list
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return list.size
    }
}