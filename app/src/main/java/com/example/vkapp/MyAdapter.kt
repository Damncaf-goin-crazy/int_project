package com.example.vkapp

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.widget.AppCompatImageView
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions

class MyAdapter(private val onItemClicked: (WebSiteConfig) -> Unit) :
    ListAdapter<WebSiteConfig, MyAdapter.TodoCellsViewHolder>(CellDiffCallBack()) {

    class TodoCellsViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        private val webSite: TextView = itemView.findViewById(R.id.tvWebSite)
        private val login: TextView = itemView.findViewById(R.id.tvLogin)
        private val thumbnail: AppCompatImageView = itemView.findViewById(R.id.ivThumbnail)
        fun onBind(webSiteConfigItem: WebSiteConfig, onItemClicked: (WebSiteConfig) -> Unit) {
            itemView.setOnClickListener {
                onItemClicked(webSiteConfigItem)
            }

            webSite.text = webSiteConfigItem.webSite
            login.text = webSiteConfigItem.login
            Glide
                .with(itemView.context)
                .load(webSiteConfigItem.thumbnail)
                .into(thumbnail)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TodoCellsViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.each_cell, parent, false)
        return TodoCellsViewHolder(view)
    }

    override fun onBindViewHolder(holder: TodoCellsViewHolder, position: Int) {
        holder.onBind(getItem(position), onItemClicked)
    }
}

class CellDiffCallBack : DiffUtil.ItemCallback<WebSiteConfig>() {
    override fun areItemsTheSame(oldItem: WebSiteConfig, newItem: WebSiteConfig): Boolean =
        oldItem.id == newItem.id

    override fun areContentsTheSame(oldItem: WebSiteConfig, newItem: WebSiteConfig): Boolean =
        oldItem == newItem
}

