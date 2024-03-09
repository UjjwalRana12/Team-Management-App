package com.android.teammanagement.activities.Activity.Activity

import android.annotation.SuppressLint
import android.app.Activity
import android.app.AlertDialog
import android.app.DatePickerDialog
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.android.teammanagement.R
import com.android.teammanagement.activities.Activity.adapters.CardMembersListItemAdapter
import com.android.teammanagement.activities.Activity.dialogues.LabelColorDialogueClass
import com.android.teammanagement.activities.Activity.dialogues.MembersListDialogue
import com.android.teammanagement.activities.Activity.firebase.FirestoreClass
import com.android.teammanagement.activities.Activity.models.Board
import com.android.teammanagement.activities.Activity.models.Card
import com.android.teammanagement.activities.Activity.models.SelectedMembers
import com.android.teammanagement.activities.Activity.models.Task
import com.android.teammanagement.activities.Activity.models.User
import com.android.teammanagement.activities.Activity.utils.Constants
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

import kotlin.collections.ArrayList

class CardDetailsActivity : BaseActivity() {
    private lateinit var toolbar_card_details_activity:Toolbar
    private lateinit var et_name_card_details:EditText
    private lateinit var btn_update_card_details:Button
    private lateinit var tv_selected_label_color:TextView
    private lateinit var tv_selected_members:TextView
    private lateinit var tv_selected_due_date:TextView
    private lateinit var rv_selected_members_list:RecyclerView
    private lateinit var mBoardDetails :Board
    private lateinit var mMembersDetailList:ArrayList<User>
    private var mTaskListPosition = -1
    private var mCardPosition = -1
    private var mSelectedColor=""
    private var mSelectedDueDateMilliSeconds:Long=0
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_card_details)
        et_name_card_details=findViewById(R.id.et_name_card_details)
        btn_update_card_details=findViewById(R.id.btn_update_card_details)
        toolbar_card_details_activity=findViewById(R.id.toolbar_card_details_activity)
        tv_selected_label_color=findViewById(R.id.tv_selected_label_color)
        tv_selected_due_date=findViewById(R.id.tv_selected_due_date)
        tv_selected_members=findViewById(R.id.tv_selected_members)
        rv_selected_members_list = findViewById(R.id.rv_selected_members_list)
        getIntentData()
        setupActionBar()
        et_name_card_details.setText(mBoardDetails.taskList[mTaskListPosition].cards[mCardPosition].name)
        et_name_card_details.setSelection(et_name_card_details.text.toString().length)



       mSelectedColor =mBoardDetails.taskList[mTaskListPosition].cards[mCardPosition].labelColor
        if (mSelectedColor.isNotEmpty()){
            setColor()
        }




        btn_update_card_details.setOnClickListener{

            if(et_name_card_details.text.toString().isNotEmpty()){
                updateCardDetails()
            }
            else{
                Toast.makeText(this@CardDetailsActivity,"enter a card name",Toast.LENGTH_SHORT).show()
            }
        }

        tv_selected_label_color.setOnClickListener{
            labelColorListDialogue()
        }
        tv_selected_members.setOnClickListener{
            membersListDialog()
        }
        setupSelectedMemberList()

        tv_selected_due_date.setOnClickListener{
            showDataPicker()
        }

    }
    private fun setupActionBar() {
        setSupportActionBar(toolbar_card_details_activity)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.baseline_arrow_back_ios_new_24)
            actionBar.title = mBoardDetails.taskList[mTaskListPosition].cards[mCardPosition].name
        }
        toolbar_card_details_activity.setNavigationOnClickListener {
            onBackPressed()
        }
    }

    fun addUpdateTaskListSuccess(){
        hideProgressDialogue()

        setResult(Activity.RESULT_OK)
        finish()
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
        if(intent.hasExtra(Constants.BOARD_MEMBERS_LIST)){
                mMembersDetailList = intent.getParcelableArrayListExtra(Constants.BOARD_MEMBERS_LIST)!!
            }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_delete_card, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId) {
            R.id.action_delete_card ->{
                alertDialogueForDeleteCard(mBoardDetails.taskList[mTaskListPosition].cards[mCardPosition].name)
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    private fun updateCardDetails(){
        val card = Card(
            et_name_card_details.text.toString(),
            mBoardDetails.taskList[mTaskListPosition].cards[mCardPosition].createdBy,
            mBoardDetails.taskList[mTaskListPosition].cards[mCardPosition].assignedTo,
            mSelectedColor

        )

        val taskList :ArrayList<Task> = mBoardDetails.taskList
        taskList.removeAt(taskList.size - 1)




        mBoardDetails.taskList[mTaskListPosition].cards[mCardPosition] = card

        showProgressDialogue("Please Wait ...")
        FirestoreClass().addUpdateTaskList(this@CardDetailsActivity,mBoardDetails)

    }

    private fun deleteCard(){
        val cardsList:ArrayList<Card> = mBoardDetails.taskList[mTaskListPosition].cards

        cardsList.removeAt(mCardPosition)

        val taskList:ArrayList<Task> = mBoardDetails.taskList
        taskList.removeAt(taskList.size -1)

        taskList[mTaskListPosition].cards = cardsList

        showProgressDialogue("Please Wait ...")
        FirestoreClass().addUpdateTaskList(this@CardDetailsActivity,mBoardDetails)


    }

    private fun alertDialogueForDeleteCard(cardName: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Delete")
        builder.setMessage("Are you sure you want to delete $cardName?")
        builder.setIcon(R.drawable.baseline_warning_24)

        builder.setPositiveButton("Yes") { dialog, which ->
            dialog.dismiss()
            deleteCard()
        }

        builder.setNegativeButton("No") { dialog, which ->
            dialog.dismiss()
        }

        val alertDialog:AlertDialog = builder.create()
        alertDialog.setCancelable(false)
        alertDialog.show()
    }

    private fun colorList():ArrayList<String>{
        val colorsList : ArrayList<String> = ArrayList()
        colorsList.add("#43C86F")
        colorsList.add("#43C86F")
        colorsList.add("#43C86F")
        colorsList.add("#43C86F")
        colorsList.add("#43C86F")
        colorsList.add("#43C86F")
        colorsList.add("#43C86F")
        colorsList.add("#43C86F")

        return colorsList
    }

    private fun setColor(){
        tv_selected_label_color.text=""
        tv_selected_label_color.setBackgroundColor(Color.parseColor(mSelectedColor))
    }

    private fun labelColorListDialogue(){

        val colorsList :ArrayList<String> = colorList()

        val listDialog = object :
            LabelColorDialogueClass(this ,colorsList,"select label color",mSelectedColor){
            override fun onItemSelected(color: String) {
                mSelectedColor=color
                setColor()
            }

        }
        listDialog.show()

    }
         private fun membersListDialog(){
             var cardAssignedMembersList = mBoardDetails.taskList[mTaskListPosition].cards[mCardPosition].assignedTo

             if(cardAssignedMembersList.size>0){
                 for (i in mMembersDetailList.indices){
                     for(j in cardAssignedMembersList){
                         if(mMembersDetailList[i].id ==j){
                             mMembersDetailList[i].selected = true

                         }
                     }
                 }
             }else{
                 for(i in mMembersDetailList.indices){
                     mMembersDetailList[i].selected = false
                 }
             }
             val listDialog = object:MembersListDialogue(this,mMembersDetailList,"select members"){
                 override fun onItemSelected(user: User, action: String) {

                     if(action == Constants.SELECT){
                         if (mBoardDetails.taskList[mTaskListPosition].cards[mCardPosition].assignedTo.contains(user.id)){
                             (mBoardDetails.taskList[mTaskListPosition].
                             cards[mCardPosition].assignedTo.add(user.id))
                         }
                     }else{
                         mBoardDetails.taskList[mTaskListPosition].
                         cards[mCardPosition].assignedTo.remove(user.id)

                         for(i in mMembersDetailList.indices){
                             if(mMembersDetailList[i].id==user.id){
                                 mMembersDetailList[i].selected=false
                             }
                         }
                     }
                       setupSelectedMemberList()


                 }

             }
             listDialog.show()

         }

    private fun setupSelectedMemberList(){
        val cardsAssignedMemberList = mBoardDetails.taskList[mTaskListPosition].cards[mCardPosition].assignedTo

        val selectedMembersList:ArrayList<SelectedMembers> = ArrayList()

        for (i in mMembersDetailList.indices){
            for(j in cardsAssignedMemberList){
                if(mMembersDetailList[i].id ==j){
                    val selectedMember = SelectedMembers(
                        mMembersDetailList[i].id,
                        mMembersDetailList[i].image

                    )
                     selectedMembersList.add(selectedMember)
                }
            }
        }
        if(selectedMembersList.size>0){
            selectedMembersList.add(SelectedMembers("",""))
            tv_selected_members.visibility = View.GONE
            rv_selected_members_list.visibility = View.VISIBLE

            rv_selected_members_list.layoutManager = GridLayoutManager(this,6)

            val adapter = CardMembersListItemAdapter(this,selectedMembersList,true)

            rv_selected_members_list.adapter=adapter
            adapter.setOnClickListener(
                object :CardMembersListItemAdapter.OnClickListener{
                    override fun onClick() {
                        membersListDialog()
                    }
                }
            )
        }else{
            tv_selected_members.visibility = View.VISIBLE
            rv_selected_members_list.visibility = View.GONE
        }
    }

    private fun showDataPicker() {

        val c = Calendar.getInstance()
        val year = c.get(Calendar.YEAR)
        val month = c.get(Calendar.MONTH)
        val day = c.get(Calendar.DAY_OF_MONTH)
        val dpd = DatePickerDialog(
            this, DatePickerDialog.OnDateSetListener { view, year, monthOfYear, dayOfMonth ->
                val sDayOfMonth = if (dayOfMonth < 10) "0$dayOfMonth" else "$dayOfMonth"
                val sMonthOfYear =
                    if ((monthOfYear + 1) < 10) "0${monthOfYear + 1}" else "${monthOfYear + 1}"

                val selectedDate = "$sDayOfMonth/$sMonthOfYear/$year"
                tv_selected_due_date.text = selectedDate

                val sdf = SimpleDateFormat("dd//MM//yyyy", Locale.ENGLISH)
                val theDate = sdf.parse(selectedDate)
                mSelectedDueDateMilliSeconds = theDate!!.time
            },
            year, month, day
        )
        dpd.show()
    }
}












