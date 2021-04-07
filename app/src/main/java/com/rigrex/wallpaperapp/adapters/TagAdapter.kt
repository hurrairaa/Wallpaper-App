package com.rigrex.wallpaperapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.rigrex.wallpaperapp.R

class TagAdapter(var list: List<String>, var inform: (String) -> Unit) :
    RecyclerView.Adapter<TagAdapter.TagViewHolder>() {


    inner class TagViewHolder(var view: View) : RecyclerView.ViewHolder(view) {
        var tag = view.findViewById<TextView>(R.id.title)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TagViewHolder {
        return TagViewHolder(
            LayoutInflater.from(parent.context).inflate(R.layout.tag_item, parent, false)
        )
    }

    override fun onBindViewHolder(holder: TagViewHolder, position: Int) {
        holder.tag?.text = list[position]
        holder.itemView.setOnClickListener { inform(list[position]) }
    }

    fun setItems(list: List<String>){
        this.list = list
        notifyDataSetChanged()
    }

    override fun getItemCount(): Int {
        return list.size
    }
}