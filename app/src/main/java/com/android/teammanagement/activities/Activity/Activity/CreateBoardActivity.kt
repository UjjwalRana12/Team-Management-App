package com.android.teammanagement.activities.Activity.Activity

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.android.teammanagement.R
import com.android.teammanagement.activities.Activity.firebase.FirestoreClass
import com.android.teammanagement.activities.Activity.models.Board
import com.android.teammanagement.activities.Activity.utils.Constants
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.IOException

class CreateBoardActivity : BaseActivity() {
    private var mSelectedImageFileUri: Uri? = null
    private lateinit var cb_toolbar:Toolbar
    private lateinit var et_board_name:EditText
    private lateinit var btn_board_name: Button
    private lateinit var iv_board_image: ImageView
    private lateinit var mUserName: String
    private var  mBoardImageURL: String = ""
    override fun onCreate(savedInstanceState: Bundle?){
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_board)
        iv_board_image=findViewById(R.id.iv_board_image)
        et_board_name=findViewById(R.id.et_board_name)
        btn_board_name=findViewById(R.id.btn_board_name)
        cb_toolbar=findViewById(R.id.toolbar_create_board_activity)
        setupActionBar()

        if(intent.hasExtra(Constants.NAME)){
            mUserName= intent.getStringExtra(Constants.NAME)!!

        }

        iv_board_image.setOnClickListener {
            if(ContextCompat.checkSelfPermission(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED){

                Constants.showImageChoser(this)
            }
            else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    Constants.READ_STORAGE_PERMISSION_CODE
                )

            }
        }

        btn_board_name.setOnClickListener {
            if(mSelectedImageFileUri!=null){
                uploadBoardImage()
            }
            else{
                showProgressDialogue("Please Wait")
            createBoard()
            }
        }
    }

    private fun createBoard(){
       val assignedUsersArrayList:ArrayList<String> = ArrayList()
       assignedUsersArrayList.add(getCurrentUserId())

        var board = Board(
            et_board_name.text.toString(),
            mBoardImageURL,
            mUserName,
            assignedUsersArrayList
        )
        FirestoreClass().createBoard(this,board)
    }


        private fun uploadBoardImage(){
            showProgressDialogue("Please Wait...")
            val sRef: StorageReference = FirebaseStorage.getInstance()
                .reference.child("BOARD_IMAGE"+System.currentTimeMillis()+
                        "."+Constants.getFileExtension(this,mSelectedImageFileUri))

            sRef.putFile(mSelectedImageFileUri!!)
                .addOnSuccessListener{
                    taskSnapshot->
                Log.i("Board Image Url",
                    taskSnapshot.metadata!!.reference!!.downloadUrl.toString())

                taskSnapshot.metadata!!.reference!!.downloadUrl.addOnSuccessListener {
                        uri ->
                    Log.e("Downloadable Image Url",uri.toString())
                    mBoardImageURL=uri.toString()

                    createBoard()

                }
            }.addOnFailureListener{
                    exception->
                Toast.makeText(this,exception.message, Toast.LENGTH_SHORT).show()
                hideProgressDialogue()
            }
        }
    fun boardCreatedSuccessfully(){
        hideProgressDialogue()
        finish()
    }



    private fun setupActionBar() {
        setSupportActionBar(cb_toolbar)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.baseline_arrow_back_ios_new_24)
            actionBar.title = "Create Board"
        }
        cb_toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == Constants.READ_STORAGE_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("Permission", "Read storage permission granted")
                Constants.showImageChoser(this)
            }
        } else {
            Toast.makeText(this, "Permission denied", Toast.LENGTH_LONG).show()
            Log.d("Permission", "Read storage permission denied")
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == Constants.PICK_IMAGE_REQUEST_CODE && data!!.data != null) {
            mSelectedImageFileUri = data.data
            try {
                Glide
                    .with(this)
                    .load(mSelectedImageFileUri)
                    .centerCrop()
                    .placeholder(R.drawable.baseline_dashboard_24)
                    .into(iv_board_image)
            } catch (e: IOException) {
                e.printStackTrace()
            }


        }
    }

}