package com.rigrex.wallpaperapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.makeramen.roundedimageview.RoundedImageView
import com.rigrex.wallpaperapp.R
import com.rigrex.wallpaperapp.database.dataModels.Favorite
import kotlin.reflect.typeOf

class FavoriteAdapter(var corners: Int, var list: List<Favorite>, var type: Int = HORIZONTAL, var onClick: (Favorite) -> Unit) :
        RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    companion object {
        val HORIZONTAL = 1
        val VERTICAL = 0
    }

    class WallpaperViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var wallpaper = view.findViewById<ImageView>(R.id.image)
    }

    class FavoriteViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var image: RoundedImageView = view.findViewById(R.id.image)
    }

    override fun getItemViewType(position: Int): Int {
        return type
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return if (viewType == HORIZONTAL)
            FavoriteViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.favorite_item, parent, false))
        else
            WallpaperViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.wallpaper_view, parent, false))
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        when (holder) {
            is FavoriteViewHolder -> {
                Glide
                        .with(holder.image.context)
                        .load(list[position].previewImageUrl)
                        .transform(RoundedCorners(corners*2))
                        .into(holder.image)
            }
            is WallpaperViewHolder -> {
                Glide
                        .with(holder.wallpaper.context)
                        .load(list[position].previewImageUrl)
                        .placeholder(R.drawable.black_round)
                        .transform(RoundedCorners(corners))
                        .into(holder.wallpaper)
            }
        }
        holder.itemView.setOnClickListener { onClick(list[position]) }
    }

    fun setData(list: List<Favorite>) {
        this.list = list
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return list.size
    }
}