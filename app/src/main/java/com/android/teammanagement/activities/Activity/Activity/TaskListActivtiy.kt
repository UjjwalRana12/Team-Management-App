package com.android.teammanagement.activities.Activity.Activity

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import com.android.teammanagement.R
import com.android.teammanagement.activities.Activity.firebase.FirestoreClass
import com.android.teammanagement.activities.Activity.models.Board
import com.android.teammanagement.activities.Activity.utils.Constants

class TaskListActivtiy : BaseActivity() {
    lateinit var toolbar_task_list: Toolbar
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_task_list_activtiy)
        toolbar_task_list=findViewById(R.id.toolbar_task_list_activity)

        var boardDocumentId=""
        if(intent.hasExtra(Constants.DOCUMENT_ID)){
            boardDocumentId = intent.getStringExtra(Constants.DOCUMENT_ID)!!
        }
        showProgressDialogue("Please Wait...")
        FirestoreClass().getBoardDetails(this, boardDocumentId)
    }

    private fun setupActionBar(title: String) {
        setSupportActionBar(toolbar_task_list)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.baseline_arrow_back_ios_new_24)
            actionBar.title = title
        }
        toolbar_task_list.setNavigationOnClickListener {
            onBackPressed()
        }
    }
    fun boardDetails(board: Board){
        hideProgressDialogue()
        setupActionBar(board.name)
    }
}