package com.wz.jetpackdemo.adapter

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.wz.jetpackdemo.R

class MinePagerAdapter:RecyclerView.Adapter<MinePagerAdapter.MinePagerHolder>() {
    var mDatas: List<Int> = mutableListOf()
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MinePagerHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.mine_pager_item, parent, false)
        return MinePagerHolder(itemView)
    }

    override fun onBindViewHolder(holder: MinePagerHolder, position: Int) {
        val imgId = mDatas.get(position)
        holder.screen.setImageResource(imgId)
    }

    override fun getItemCount(): Int {
        return mDatas.size
    }

    class MinePagerHolder(view: View) : RecyclerView.ViewHolder(view) {
        val screen = view.findViewById<ImageView>(R.id.iv_screen)
    }
}

