package com.android.teammanagement.activities.Activity.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import androidx.recyclerview.widget.RecyclerView
import com.android.teammanagement.R
import com.android.teammanagement.activities.Activity.models.SelectedMembers
import com.bumptech.glide.Glide

open class CardMembersListItemAdapter(private val context: Context
,private val list:ArrayList<SelectedMembers>): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    private var onClickListener:OnClickListener?=null
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

       return MyViewHolder( LayoutInflater.from(context).inflate(R.layout.item_card_selected_members,parent,false))

    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val model =list[position]

        if(holder is MyViewHolder){
            if(position==list.size - 1){
                holder.itemView.findViewById<ImageView>(R.id.iv_add_members).visibility=View.VISIBLE
                holder.itemView.findViewById<ImageView>(R.id.iv_selected_members_image).visibility=View.GONE
            }else{
                holder.itemView.findViewById<ImageView>(R.id.iv_add_members).visibility=View.GONE
                holder.itemView.findViewById<ImageView>(R.id.iv_selected_members_image).visibility=View.VISIBLE

                Glide
                    .with(context)
                    .load(model.image)
                    .centerCrop()
                    .placeholder(R.drawable.profile)
                    .into(holder.itemView.findViewById(R.id.iv_selected_members_image))

            }
            holder.itemView.setOnClickListener {
                if(onClickListener!=null){
                    onClickListener!!.onClick()
                }
            }
        }
    }
    private class MyViewHolder(view: View):RecyclerView.ViewHolder(view)


    interface OnClickListener{
        fun onClick()
    }

    fun setOnClickListener(onClickListener: OnClickListener){
        this.onClickListener=onClickListener
    }
}