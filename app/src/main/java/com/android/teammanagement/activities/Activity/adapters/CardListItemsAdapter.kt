package com.android.teammanagement.activities.Activity.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.teammanagement.R
import com.android.teammanagement.activities.Activity.Activity.TaskListActivtiy
import com.android.teammanagement.activities.Activity.models.Board
import com.android.teammanagement.activities.Activity.models.Card
import com.android.teammanagement.activities.Activity.models.SelectedMembers
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

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, @SuppressLint("RecyclerView") position: Int) {
        val model=list[position]

        if(holder is MyViewHolder){

            if(model.labelColor.isNotEmpty()){
                holder.itemView.findViewById<View>(R.id.view_label_color).visibility=View.VISIBLE
                holder.itemView.findViewById<View>(R.id.view_label_color).setBackgroundColor(Color.parseColor(model.labelColor))
            }else{
                holder.itemView.findViewById<View>(R.id.view_label_color).visibility=View.GONE

            }
            holder.itemView.findViewById<TextView>(R.id.tv_card_name).text = model.name

            if((context as TaskListActivtiy).mAssignedMemberDetailList.size>0){

                val selectedMembersList:ArrayList<SelectedMembers> = ArrayList()

                for(i in context.mAssignedMemberDetailList.indices){
                    for(j in model.assignedTo){
                        if(context.mAssignedMemberDetailList[i].id==j){
                            val selectedMembers =SelectedMembers(
                                context.mAssignedMemberDetailList[i].id,
                                context.mAssignedMemberDetailList[i].image
                            )
                            selectedMembersList.add(selectedMembers)
                        }
                    }
                }
                if (selectedMembersList.size>0){
                    if (selectedMembersList.size ==1 && selectedMembersList[0].id==model.createdBy){
                        holder.itemView.findViewById<RecyclerView>(R.id.rv_card_selected_members_list).visibility=View.GONE
                    }else{
                        holder.itemView.findViewById<RecyclerView>(R.id.rv_card_selected_members_list).visibility=View.VISIBLE

                        holder.itemView.findViewById<RecyclerView>(R.id.rv_card_selected_members_list).layoutManager= GridLayoutManager(context,4)

                        val adapter = CardMembersListItemAdapter(context,selectedMembersList,false)

                        holder.itemView.findViewById<RecyclerView>(R.id.rv_card_selected_members_list).adapter = adapter

                        adapter.setOnClickListener(object :CardMembersListItemAdapter.OnClickListener{
                            override fun onClick() {

                                if(onClickListener!=null){
                                    onClickListener!!.onClick(position)
                                }
                            }
                        })

                    }
                } else{
                    holder.itemView.findViewById<RecyclerView>(R.id.rv_card_selected_members_list).visibility=View.GONE
                }

            }


            holder.itemView.setOnClickListener {
                if(onClickListener!=null){
                    onClickListener!!.onClick(position)
                }
            }

        }
    }


    interface OnClickListener{
        fun onClick(position: Int)
    }

    fun setOnClickListener(onClickListener: OnClickListener){
        this.onClickListener = onClickListener
    }
    private class MyViewHolder(view: View): RecyclerView.ViewHolder(view)
}