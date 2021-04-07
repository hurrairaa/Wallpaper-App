package com.rigrex.wallpaperapp.adapters

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.BaseAdapter
import android.widget.TextView
import com.rigrex.wallpaperapp.R
import com.rigrex.wallpaperapp.database.dataModels.TimeModel

class TimeSelectorAdapter(var list: MutableList<TimeModel>) : BaseAdapter() {

    companion object {
        const val UPPER_ROUND = 0
        const val LOWER_ROUND = 1
        const val RECTANGLE = 2
    }

    init {
        list.add(getCustomItem())
    }

    private fun getCustomItem(): TimeModel {
        return TimeModel().apply { name = "Custom";id = 315 }
    }

    fun addItem(timeModel: TimeModel) {
        list.dropLast(1)
        list.toMutableList().add(timeModel)
        list.toMutableList().add(getCustomItem())
    }

    override fun getCount(): Int {
        return list.size
    }

    override fun getItem(position: Int): Any {
        return list[position]
    }

    override fun getItemId(position: Int): Long {
        return list[position].id.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return when (position) {
            0 -> {
                UPPER_ROUND
            }
            list.size - 1 -> {
                LOWER_ROUND
            }
            else -> {
                RECTANGLE
            }
        }
    }

    override fun getView(position: Int, convertView: View?, parent: ViewGroup?): View {
        return when (getItemViewType(position)) {
            UPPER_ROUND -> {
                LayoutInflater.from(parent?.context).inflate(R.layout.upper_round_view, parent, false)
            }
            LOWER_ROUND -> {
                LayoutInflater.from(parent?.context).inflate(R.layout.lower_round_view, parent, false)
            }
            else -> {
                LayoutInflater.from(parent?.context).inflate(R.layout.rectangle_item, parent, false)
            }
        }.apply {
            findViewById<TextView>(R.id.title).text = list[position].name
        }
    }
}