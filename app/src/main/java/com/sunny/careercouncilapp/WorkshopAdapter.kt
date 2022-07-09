package com.sunny.careercouncilapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class WorkshopAdapter(private val wlist: ArrayList<Workshop_data_class>): RecyclerView.Adapter<WorkshopAdapter.myViewholder>() {

    private lateinit var mlis : onitemclicklis
    interface onitemclicklis{
        fun onitemclicklisf(num : String)
    }

    fun setonitemclicklis(listener: onitemclicklis)
    {
        mlis= listener
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): WorkshopAdapter.myViewholder {
        val itemView =
            LayoutInflater.from(parent.context).inflate(R.layout.workshop_item, parent, false)

        return myViewholder(itemView,mlis)
    }

    override fun onBindViewHolder(holder: WorkshopAdapter.myViewholder, position: Int) {
        val ws: Workshop_data_class = wlist[position]
        val link =ws.img
        holder.titlewshop.text = ws.title
        holder.descwshop.text = ws.desc
        holder.pricewshop.text = ws.price
        Picasso.get()
            .load(link)
            .into(holder.image);
    }

    override fun getItemCount(): Int {
        return wlist.size
    }

    public class myViewholder(itemView: View,listener: onitemclicklis) : RecyclerView.ViewHolder(itemView) {
        val titlewshop: TextView = itemView.findViewById(R.id.titlewshop)
        val descwshop: TextView = itemView.findViewById(R.id.descwshop)
        val pricewshop: TextView = itemView.findViewById(R.id.pricewshop)
        val btn: Button = itemView.findViewById(R.id.wsreq)
        val image: ImageView = itemView.findViewById(R.id.image)

        init {

            btn.setOnClickListener {
                listener.onitemclicklisf(titlewshop.text.toString())
            }
        }

    }
}