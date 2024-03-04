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

abstract class LabelColorDialogueClass(
    context: Context,
          private var list: ArrayList<String>,
     private var title: String="",
    private val mSelectedColor: String=""
) :Dialog(context){

    private var adapter: LabelColorListItemAdapter? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val view =LayoutInflater.from(context).inflate(R.layout.dialog_list,null)

        setContentView(view)
        setCanceledOnTouchOutside(true)
        setCancelable(true)
        setUpRecyclerView(view)

    }
    private fun setUpRecyclerView(view: View) {
        view.findViewById<TextView>(R.id.tvTitle).text = title
        view.findViewById<RecyclerView>(R.id.rv_list).layoutManager = LinearLayoutManager(context)
        adapter = LabelColorListItemAdapter(context,list,mSelectedColor)
        view.findViewById<RecyclerView>(R.id.rv_list).adapter=adapter

        adapter!!.onItemClickListener = object :LabelColorListItemAdapter.OnItemClickListener{
            override fun onClick(position: Int, color: String) {
                dismiss()
                onItemSelected(color)
            }

        }
    }
    protected  abstract fun onItemSelected(color:String)
}









