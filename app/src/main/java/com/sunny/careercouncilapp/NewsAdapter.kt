package com.sunny.careercouncilapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class NewsAdapter(private val nlist: ArrayList<News_data_class>): RecyclerView.Adapter<NewsAdapter.myViewholder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NewsAdapter.myViewholder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.news_item, parent, false)
        return myViewholder(itemView)
    }

    override fun onBindViewHolder(holder: NewsAdapter.myViewholder, position: Int) {
        val ns: News_data_class = nlist[position]
        val link= ns.img
        holder.titlenews.text = ns.title
        Picasso.get()
            .load(link)
            .into(holder.image);
        holder.descnews.text = ns.desc

    }

    override fun getItemCount(): Int {
        return nlist.size

    }
    public class myViewholder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val titlenews: TextView = itemView.findViewById(R.id.titlenews)
        val descnews: TextView = itemView.findViewById(R.id.descnews)
        val image: ImageView = itemView.findViewById(R.id.image)


    }

}