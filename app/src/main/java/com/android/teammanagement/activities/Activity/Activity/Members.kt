package com.android.teammanagement.activities.Activity.Activity

import android.annotation.SuppressLint
import android.app.Dialog
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
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
    private lateinit var mAssignedMembersList:ArrayList<User>
    private var anyChangesMade:Boolean = false

    @SuppressLint("MissingInflatedId")
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

        mAssignedMembersList =list
        hideProgressDialogue()

        rv_members_list.layoutManager=LinearLayoutManager(this)
        rv_members_list.setHasFixedSize(true)

        val adapter =MemberListItemAdapter(this,list)
        rv_members_list.adapter = adapter

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {

        menuInflater.inflate(R.menu.menu_add_member,menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_add_member ->{
                dialogueSearchMember()
                return true
            }

        }
        return super.onOptionsItemSelected(item)
    }

    private fun dialogueSearchMember(){


        val dialogue = Dialog(this)
        dialogue.setContentView(R.layout.dialogue_search_members)
        val tv_Add = dialogue.findViewById<TextView>(R.id.tv_add)
        val tv_Cancel = dialogue.findViewById<TextView>(R.id.tv_cancel)
        val et_email_search_member =dialogue.findViewById<EditText>(R.id.et_email_search)
        tv_Add.setOnClickListener {
            val email = et_email_search_member.text.toString()
            if(email.isNotEmpty()){
                dialogue.dismiss()
                showProgressDialogue("Please wait")
                FirestoreClass().getMemberDetails(this,email)
            }
            else{
                Toast.makeText(this@Members,"please enter valid email address", Toast.LENGTH_SHORT).show()
            }
        }
        tv_Cancel.setOnClickListener {
            dialogue.dismiss()
        }
        dialogue.show()
    }


    fun memberDetails(user: User) {
        mBoardDetails.assignedTo.add(user.id)
        FirestoreClass().assignedMemberToBoard(this,mBoardDetails,user)
    }

    override fun onBackPressed() {
        if(anyChangesMade){
            setResult(RESULT_OK)

        }
        super.onBackPressed()
    }
    fun memberAssignSuccess(user:User){
        hideProgressDialogue()
        mAssignedMembersList.add(user)

        anyChangesMade =true
        setUpMembersList(mAssignedMembersList)
    }
}