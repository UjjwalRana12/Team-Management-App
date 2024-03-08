package com.android.teammanagement.activities.Activity.Activity

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.view.Menu
import android.view.MenuItem
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.teammanagement.R
import com.android.teammanagement.activities.Activity.adapters.TaskListItemAdapter
import com.android.teammanagement.activities.Activity.firebase.FirestoreClass
import com.android.teammanagement.activities.Activity.models.Board
import com.android.teammanagement.activities.Activity.models.Card
import com.android.teammanagement.activities.Activity.models.Task
import com.android.teammanagement.activities.Activity.models.User
import com.android.teammanagement.activities.Activity.utils.Constants

class TaskListActivtiy : BaseActivity() {
    lateinit var toolbar_task_list: Toolbar
    lateinit var rv_task_list: RecyclerView
    private lateinit var  mBoardDetails: Board

    private lateinit var mBoardDocumentId: String
    private lateinit var mAssignedMemberDetailList:ArrayList<User>
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_list_activtiy)
        toolbar_task_list=findViewById(R.id.toolbar_task_list_activity)
        rv_task_list=findViewById(R.id.rv_task_list)


        if(intent.hasExtra(Constants.DOCUMENT_ID)){
            mBoardDocumentId = intent.getStringExtra(Constants.DOCUMENT_ID)!!
        }
        showProgressDialogue("Please Wait...")
        FirestoreClass().getBoardDetails(this, mBoardDocumentId)
    }
//  todo it reloads screen again and again therefore needs to be checked but prefer ways
//
//    override fun onResume() {
//        super.onResume()
//        showProgressDialogue("Please Wait...")
//        FirestoreClass().getBoardDetails(this, mBoardDocumentId)
//    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode == Activity.RESULT_OK && requestCode == MEMBER_REQUEST_CODE || requestCode== CARD_DETAILS_REQUEST_CODE){
            showProgressDialogue("Please Wait...")
            FirestoreClass().getBoardDetails(this, mBoardDocumentId)
        }
        else{
            Log.e("Cancelled","Cancelled")
        }
    }

    private fun setupActionBar() {
        setSupportActionBar(toolbar_task_list)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.baseline_arrow_back_ios_new_24)
            actionBar.title = mBoardDetails.name
        }
        toolbar_task_list.setNavigationOnClickListener {
            onBackPressed()
        }
    }
    fun boardDetails(board: Board){

        mBoardDetails=board

        hideProgressDialogue()
        setupActionBar()

        val addtaskList = Task("add list")
        board.taskList.add(addtaskList)

        rv_task_list.layoutManager=LinearLayoutManager(this,LinearLayoutManager.HORIZONTAL,false)
        rv_task_list.setHasFixedSize(true)

        val adapter= TaskListItemAdapter(this,board.taskList)
        rv_task_list.adapter = adapter

        showProgressDialogue("Please Wait")
        FirestoreClass().getAssignedMembersListDetails(this,mBoardDetails.assignedTo)

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_members, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.action_members->{
                val intent =Intent(this,Members::class.java)
                intent.putExtra(Constants.BOARD_DETAILS,mBoardDetails)
                startActivityForResult(intent, MEMBER_REQUEST_CODE)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }
    fun addUpdateTaskListSuccess(){
        hideProgressDialogue()
        showProgressDialogue("Please wait ...")
        FirestoreClass().getBoardDetails(this, mBoardDetails.documentId)
    }

    fun createTaskList(taskListName: String){
        val task = Task(taskListName,FirestoreClass().getCurrentUserId())

        mBoardDetails.taskList.add(0 , task)
        mBoardDetails.taskList.removeAt(mBoardDetails.taskList.size-1)
        showProgressDialogue("Please wait ...")

        FirestoreClass().addUpdateTaskList(this, mBoardDetails)
    }

    fun updateTaskList(position: Int, listName: String,  model:Task){

        val task = Task(listName,model.createdBy)

        mBoardDetails.taskList[position]=task
        mBoardDetails.taskList.removeAt(mBoardDetails.taskList.size-1)

        showProgressDialogue("Please wait ...")

        FirestoreClass().addUpdateTaskList(this, mBoardDetails)
    }

    fun deleteTaskList(position: Int){
        mBoardDetails.taskList.removeAt(position)
        mBoardDetails.taskList.removeAt(mBoardDetails.taskList.size-1)

        showProgressDialogue("Please wait ...")

        FirestoreClass().addUpdateTaskList(this, mBoardDetails)
    }

    fun cardDetails(taskListPosition: Int, cardPostion: Int){
        val intent = Intent(this,CardDetailsActivity::class.java)
        intent.putExtra(Constants.BOARD_DETAILS,mBoardDetails)
        intent.putExtra(Constants.TASK_LIST_ITEM_POSITION,taskListPosition)
        intent.putExtra(Constants.CARD_LIST_ITEM_POSITION,cardPostion)
        intent.putExtra(Constants.BOARD_MEMBERS_LIST,mAssignedMemberDetailList)
        startActivityForResult(intent, CARD_DETAILS_REQUEST_CODE)

    }

    fun addCardToTaskList(position: Int,cardName: String){
        mBoardDetails.taskList.removeAt(mBoardDetails.taskList.size-1)

        val cardAssignedUsersList : ArrayList<String> = ArrayList()
        cardAssignedUsersList.add(FirestoreClass().getCurrentUserId())

        val card = Card(cardName,FirestoreClass().getCurrentUserId(),cardAssignedUsersList)

        val cardsList = mBoardDetails.taskList[position].cards
        cardsList.add(card)

        val task = Task(
            mBoardDetails.taskList[position].title,
            mBoardDetails.taskList[position].createdBy,
            cardsList
        )


        mBoardDetails.taskList[position] = task

        showProgressDialogue("Please wait ...")

        FirestoreClass().addUpdateTaskList(this,mBoardDetails)


    }
    fun boardMembersDdetailsList(list:ArrayList<User>){
        mAssignedMemberDetailList= list
        hideProgressDialogue()
    }
    companion object{
      const val MEMBER_REQUEST_CODE :Int = 13
        const val CARD_DETAILS_REQUEST_CODE :Int = 14

    }

}