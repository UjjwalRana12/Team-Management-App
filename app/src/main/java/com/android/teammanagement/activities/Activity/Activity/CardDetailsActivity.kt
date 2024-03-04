package com.android.teammanagement.activities.Activity.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.widget.EditText
import androidx.appcompat.widget.Toolbar
import com.android.teammanagement.R
import com.android.teammanagement.activities.Activity.models.Board
import com.android.teammanagement.activities.Activity.utils.Constants

class CardDetailsActivity : AppCompatActivity() {
    private lateinit var toolbar_card_details_activity:Toolbar
    private lateinit var et_name_card_details:EditText
    private lateinit var mBoardDetails :Board
    private var mTaskListPosition = -1
    private var mCardPosition = -1
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_card_details)
        et_name_card_details=findViewById(R.id.et_name_card_details)
        toolbar_card_details_activity=findViewById(R.id.toolbar_card_details_activity)
        getIntentData()
        setupActionBar()
        et_name_card_details.setText(mBoardDetails.taskList[mTaskListPosition].cards[mCardPosition].name)
        et_name_card_details.setSelection(et_name_card_details.text.toString().length)
    }
    private fun setupActionBar() {
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            setHomeAsUpIndicator(R.drawable.baseline_arrow_back_ios_new_24)
            title=mBoardDetails.taskList[mTaskListPosition].cards[mCardPosition].name

        }
        toolbar_card_details_activity.setNavigationOnClickListener {
            onBackPressed()
        }

    }

    private fun getIntentData(){
        if(intent.hasExtra(Constants.BOARD_DETAILS)){
            mBoardDetails=intent.getParcelableExtra(Constants.BOARD_DETAILS)!!
        }

        if(intent.hasExtra(Constants.TASK_LIST_ITEM_POSITION)){
            mTaskListPosition=intent.getIntExtra(Constants.TASK_LIST_ITEM_POSITION,-1)
        }
        if(intent.hasExtra(Constants.CARD_LIST_ITEM_POSITION)){
            mCardPosition=intent.getIntExtra(Constants.CARD_LIST_ITEM_POSITION,-1)
        }

    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_delete_card,menu)
        return super.onCreateOptionsMenu(menu)
    }
}