package com.example.github.repositories

import android.annotation.SuppressLint
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.github.repositories.data.LocalDataStore
import com.example.github.repositories.data.RepositoryDTO

class RepositoryAdapter(
    private var list: List<RepositoryDTO>,
    private val itemClick: (RepositoryDTO) -> Unit
) : RecyclerView.Adapter<RepositoryAdapter.ViewHolder>() {

    internal fun updateList(list: List<RepositoryDTO>) {
        this.list = list
        notifyItemRangeChanged(this.list.size, list.size)
    }

    override fun getItemCount(): Int = list.size

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.item, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bindData(list[position])
    }

    inner class ViewHolder(ItemView: View) : RecyclerView.ViewHolder(ItemView) {
        private val container: View = itemView.findViewById(R.id.news_container)
        private val titleTxt: TextView = itemView.findViewById(R.id.title)
        private val imageVw: ImageView = itemView.findViewById(R.id.image)
        private val descriptionTxt: TextView = itemView.findViewById(R.id.description)
        private val authorTxt: TextView = itemView.findViewById(R.id.author)

        @SuppressLint("SetTextI18n")
        fun bindData(item: RepositoryDTO) {
            titleTxt.text = itemView.context.getString(
                R.string.repo_title_with_number, adapterPosition + 1, item.full_name
            )
            item.description?.let {
                descriptionTxt.text = if (it.length > 150) it.take(150).plus("...")
                else item.description
            }
            authorTxt.text = item.owner?.login
            imageVw.setImageResource(
                if (LocalDataStore.instance.getBookmarks().contains(item))
                    R.drawable.baseline_bookmark_black_24
                else
                    R.drawable.baseline_bookmark_border_black_24
            )
            container.setOnClickListener { itemClick.invoke(item) }
        }
    }
}