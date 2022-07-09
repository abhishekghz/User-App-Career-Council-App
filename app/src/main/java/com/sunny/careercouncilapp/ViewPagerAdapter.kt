package com.sunny.careercouncilapp

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView

class ViewPagerAdapter(private var cards: List<Int>) : RecyclerView.Adapter<ViewPagerAdapter.Pager2ViewHolder>() {




    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewPagerAdapter.Pager2ViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_page,parent,false)
        return Pager2ViewHolder(itemView)

    }

    override fun onBindViewHolder(holder: ViewPagerAdapter.Pager2ViewHolder, position: Int) {
        holder.itemImage.setImageResource(cards[position])
    }

    override fun getItemCount(): Int {
        return cards.size
    }


    public class Pager2ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)
    {

        val itemImage : ImageView = itemView.findViewById(R.id.the_card)

        init {


            itemImage.setOnClickListener{ v: View ->
                val position = adapterPosition
                val a = position+1

                intenting(a)

            }
        }

        fun intenting(a: Int)
        {
            if(a==1)
            {
                val intent = Intent(itemImage.context, ChatwithCounselors::class.java)
                itemImage.context.startActivity(intent)
            }
            else if(a==2)
            {
                val intent = Intent(itemImage.context, ResumeBuilding::class.java)
                itemImage.context.startActivity(intent)
            }
            else if(a==3)
            {
                val intent = Intent(itemImage.context, jobapplication::class.java)
                    itemImage.context.startActivity(intent)

            }
            else if(a==4)
            {
                val intent = Intent(itemImage.context, onlineprofile::class.java)
                    itemImage.context.startActivity(intent)

            }
            else if(a==7)
            {
                val intent = Intent(itemImage.context, ws::class.java)
                itemImage.context.startActivity(intent)

            }
            else if(a==5)
            {
                val intent = Intent(itemImage.context, Customized_Career_Guidance::class.java)
                itemImage.context.startActivity(intent)

            }
            else if(a==6)
            {
                val intent = Intent(itemImage.context, campustocorporate::class.java)
                itemImage.context.startActivity(intent)

            }
            else if(a==8)
            {
                val intent = Intent(itemImage.context, Newsfeed::class.java)
                itemImage.context.startActivity(intent)

            }
        }


    }

}