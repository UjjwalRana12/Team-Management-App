package com.android.teammanagement.activities.Activity.dialogues

import android.app.Dialog
import android.content.Context
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.teammanagement.R
import com.android.teammanagement.activities.Activity.adapters.LabelColorListItemAdapter
import com.android.teammanagement.activities.Activity.adapters.MemberListItemAdapter
import com.android.teammanagement.activities.Activity.models.User

abstract class MembersListDialogue(
    context: Context,
    private var list:ArrayList<User>,
    private val title:String=""

    ):Dialog(context){

        private var adapter:MemberListItemAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view = LayoutInflater.from(context).inflate(R.layout.dialog_list,null)

        setContentView(view)
        setCanceledOnTouchOutside(true)
        setCancelable(true)
        setUpRecyclerView(view)

    }
    private fun setUpRecyclerView(view: View) {
        view.findViewById<TextView>(R.id.tvTitle).text = title

        if (list.size>0){
            view.findViewById<RecyclerView>(R.id.rv_list).layoutManager=LinearLayoutManager(context)
            adapter = MemberListItemAdapter(context,list)
            view.findViewById<RecyclerView>(R.id.rv_list).adapter = adapter

            adapter!!.setOnClickListener(object :MemberListItemAdapter.OnClickListener{
                override fun onClick(position: Int, user: User,action: String){
                    dismiss()
                    onItemSelected(user,action)
                }
            })
        }
    }
    protected  abstract fun onItemSelected(user:User,action:String)
}