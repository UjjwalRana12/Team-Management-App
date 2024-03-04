package com.android.teammanagement.activities.Activity.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.android.teammanagement.R
import com.android.teammanagement.activities.Activity.models.Board
import com.android.teammanagement.activities.Activity.models.Card
import com.bumptech.glide.Glide

class CardListItemsAdapter(private val context: Context, private val list:ArrayList<Card>):
    RecyclerView.Adapter<RecyclerView.ViewHolder>(){

    private var onClickListener:OnClickListener? = null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        return  MyViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_card, parent, false))

    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model=list[position]

        if(holder is MyViewHolder){


            holder.itemView.findViewById<TextView>(R.id.tv_card_name).text = model.name


        }
    }


    interface OnClickListener{
        fun onClick(position: Int,model: Board)
    }

    fun setOnClickListener(onClickListener: OnClickListener){
        this.onClickListener = onClickListener
    }
    private class MyViewHolder(view: View): RecyclerView.ViewHolder(view)
}