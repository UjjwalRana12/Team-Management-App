package com.android.teammanagement.activities.Activity.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.appcompat.widget.Toolbar
import com.android.teammanagement.R
import com.android.teammanagement.activities.Activity.models.Board
import com.android.teammanagement.activities.Activity.utils.Constants

class Members : AppCompatActivity() {

    private lateinit var mBoardDetails: Board
    private lateinit var toolbar_members_activity:Toolbar
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_members)
        toolbar_members_activity=findViewById(R.id.toolbar_members_activity)

        if(intent.hasExtra(Constants.BOARD_DETAILS)){
            mBoardDetails= intent.getParcelableExtra<Board>(Constants.BOARD_DETAILS)!!
        }
        setupActionBar()
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
}