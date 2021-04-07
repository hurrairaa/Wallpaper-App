package com.rigrex.wallpaperapp.adapters

import android.graphics.Typeface
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.rigrex.wallpaperapp.R
import com.rigrex.wallpaperapp.database.dataModels.Favorite

class CategoryAdapter(var list: List<String>, var onSelected: (String) -> Unit) :
        RecyclerView.Adapter<CategoryAdapter.CategoryViewHolder>() {

    private var selected = 0

    class CategoryViewHolder(var view: View) : RecyclerView.ViewHolder(view) {
        var title: TextView
        var line: View

        init {
            title = view.findViewById(R.id.text)
            line = view.findViewById(R.id.bottom_view)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CategoryViewHolder {
        return CategoryViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.category_item, parent, false))
    }

    fun setData(list: List<String>) {
        this.list = list
        notifyDataSetChanged()
    }

    fun getCurrentCat() = list[selected]

    override fun onBindViewHolder(holder: CategoryViewHolder, position: Int) {
        holder.title.text = list[position]
        if (selected == position) {
            holder.title.setTypeface(null, Typeface.BOLD)
            holder.line.visibility = View.VISIBLE
        } else {
            holder.title.setTypeface(null, Typeface.NORMAL)
            holder.line.visibility = View.GONE
        }
        holder.view.setOnClickListener {
            selected = position
            onSelected(list[position])
            notifyDataSetChanged()
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }
}