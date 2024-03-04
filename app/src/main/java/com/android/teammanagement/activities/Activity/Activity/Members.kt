package com.android.teammanagement.activities.Activity.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.teammanagement.R
import com.android.teammanagement.activities.Activity.adapters.MemberListItemAdapter
import com.android.teammanagement.activities.Activity.firebase.FirestoreClass
import com.android.teammanagement.activities.Activity.models.Board
import com.android.teammanagement.activities.Activity.models.User
import com.android.teammanagement.activities.Activity.utils.Constants

class Members : BaseActivity() {

    private lateinit var mBoardDetails: Board
    private lateinit var toolbar_members_activity:Toolbar
    private lateinit var rv_members_list:RecyclerView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_members)
        toolbar_members_activity=findViewById(R.id.toolbar_members_activity)
        rv_members_list = findViewById(R.id.rv_members_list)

        if(intent.hasExtra(Constants.BOARD_DETAILS)){
            mBoardDetails= intent.getParcelableExtra<Board>(Constants.BOARD_DETAILS)!!
        }
        setupActionBar()

        showProgressDialogue("Please Wait...")
        FirestoreClass().getAssignedMembersListDetails(this,mBoardDetails.assignedTo)
   }
    private fun setupActionBar() {
        setSupportActionBar(toolbar_members_activity)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.baseline_arrow_back_ios_new_24)
            actionBar.title = "Members"
        }
        toolbar_members_activity.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    fun setUpMembersList(list:ArrayList<User>){
        hideProgressDialogue()

        rv_members_list.layoutManager=LinearLayoutManager(this)
        rv_members_list.setHasFixedSize(true)

        val adapter =MemberListItemAdapter(this,list)
        rv_members_list.adapter = adapter

    }
}