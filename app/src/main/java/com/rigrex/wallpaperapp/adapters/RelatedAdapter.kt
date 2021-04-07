package com.rigrex.wallpaperapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.bitmap.RoundedCorners
import com.rigrex.wallpaperapp.R
import com.rigrex.wallpaperapp.database.dataModels.WallPaperModel

class RelatedAdapter(
    var list: List<WallPaperModel>,
    var inform: (WallPaperModel) -> Unit
) :
    RecyclerView.Adapter<RelatedAdapter.RelatedViewHodler>() {

    class RelatedViewHodler(view: View) : RecyclerView.ViewHolder(view) {
        var imageView = view.findViewById<ImageView>(R.id.image)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RelatedViewHodler {
        return RelatedViewHodler(
            LayoutInflater.from(parent.context).inflate(R.layout.related_width, parent, false)
        )
    }

    override fun onBindViewHolder(holder: RelatedViewHodler, position: Int) {
        Glide
            .with(holder.imageView.context)
            .load(list[position].previewImageUrl)
            .placeholder(android.R.color.black)
            .transform(
                RoundedCorners(
                    holder.imageView.context.resources?.getDimension(R.dimen.dp_10)?.toInt()
                        ?: 0
                )
            )
            .into(holder.imageView)
        holder.itemView.setOnClickListener { inform(list[position]) }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    fun setData(list: List<WallPaperModel>){
        this.list = list
        notifyDataSetChanged()
    }
}