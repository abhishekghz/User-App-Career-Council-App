package com.sunny.careercouncilapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.auth.FirebaseAuth

class MessageAdapter( val messageList: ArrayList<Message> ): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    val ITEM_RECEIVE=1
    val ITEM_SENT=2


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        if (viewType==2)
        {
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.sent_message, parent, false)
            return SentViewHolder(view)
        }
        else{
            val view =
                LayoutInflater.from(parent.context).inflate(R.layout.received_message, parent, false)
            return ReceiveViewHolder(view)
        }
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val currentMessage = messageList[position]
        if(holder.javaClass== SentViewHolder::class.java){

            val viewHolder = holder as SentViewHolder
            holder.sentmssg.text=currentMessage.message

        }
        else
        {
            val viewHolder = holder as ReceiveViewHolder
            holder.recmssg.text=currentMessage.message
        }
    }

    override fun getItemViewType(position: Int): Int {
        val currentMessage = messageList[position]

        if(FirebaseAuth.getInstance().currentUser?.uid.equals(currentMessage.senderId)){
            return ITEM_SENT
        }
        else
        {
            return ITEM_RECEIVE
        }

    }
    override fun getItemCount(): Int {
        return messageList.size
    }
    class SentViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val sentmssg: TextView = itemView.findViewById(R.id.receivedmssg)

    }
    class ReceiveViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView){
        val recmssg: TextView = itemView.findViewById(R.id.receivedmssg)

    }
}