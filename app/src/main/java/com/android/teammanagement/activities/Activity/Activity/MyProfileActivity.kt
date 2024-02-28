package com.android.teammanagement.activities.Activity.Activity

import android.Manifest
import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
import android.util.Log
import android.webkit.MimeTypeMap
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

import com.android.teammanagement.R
import com.android.teammanagement.activities.Activity.firebase.FirestoreClass
import com.android.teammanagement.activities.Activity.models.User
import com.android.teammanagement.activities.Activity.utils.Constants
import com.bumptech.glide.Glide
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import java.io.IOException

class MyProfileActivity : BaseActivity() {

    companion object {
        private const val READ_STORAGE_PERMISSION_CODE = 1
        private const val PICK_IMAGE_REQUEST_CODE = 2

    }

    lateinit var my_pro_toolbar: Toolbar
    lateinit var iv_profile_user_image: ImageView
    lateinit var et_pro_name: EditText
    lateinit var et_pro_email: EditText
    lateinit var et_pro_phone: EditText
    lateinit var btn_update:Button

    private var mSelectedImageFileUri: Uri? = null
    private lateinit var mUserDetails:User
    private var mProfileImageURL: String? = ""


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_profile)
        my_pro_toolbar = findViewById(R.id.toolbar_my_profile_activty)
        iv_profile_user_image = findViewById(R.id.iv_profile_user_image)
        et_pro_name = findViewById(R.id.et_name_pro)
        et_pro_email = findViewById(R.id.et_email_pro)
        et_pro_phone = findViewById(R.id.et_phone_pro)
        btn_update=findViewById(R.id.btn_update)
        setupActionBar()

        FirestoreClass().loadUserData(this)

        iv_profile_user_image.setOnClickListener {
            Log.d("permission","image button is triggered")
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {

                showImageChoser()
            }
            else {
                ActivityCompat.requestPermissions(
                    this,
                    arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE),
                    READ_STORAGE_PERMISSION_CODE
                )
                    btn_update.setOnClickListener {
                        if(mSelectedImageFileUri!=null){
                            uploadUserImage()
                        }
                        else {
                            showProgressDialogue("please wait")
                            updateUserProfileData()
                        }
                    }
            }
        }

    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        if (requestCode == READ_STORAGE_PERMISSION_CODE) {
            if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                Log.d("Permission", "Read storage permission granted")
                showImageChoser()
            }
        } else {
            Toast.makeText(this, "Permission denied", Toast.LENGTH_LONG).show()
            Log.d("Permission", "Read storage permission denied")
        }
    }

    private fun setupActionBar() {
        setSupportActionBar(my_pro_toolbar)
        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.baseline_arrow_back_ios_new_24)
            actionBar.title = "My Profile"
        }
        my_pro_toolbar.setNavigationOnClickListener {
            onBackPressed()
        }
    }


    private fun showImageChoser() {

        var galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST_CODE)
        Log.d("Permission", "image choser is triggered")
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if (resultCode == Activity.RESULT_OK && requestCode == PICK_IMAGE_REQUEST_CODE && data!!.data != null) {
            mSelectedImageFileUri = data.data
            try {
                Glide
                    .with(this@MyProfileActivity)
                    .load(mSelectedImageFileUri)
                    .centerCrop()
                    .placeholder(R.drawable.profile)
                    .into(iv_profile_user_image)
            } catch (e: IOException) {
                e.printStackTrace()
            }


        }
    }

    fun setUserDatInUI(user: User) {

        mUserDetails=user

        Glide
            .with(this@MyProfileActivity)
            .load(user.image)
            .centerCrop()
            .placeholder(R.drawable.profile)
            .into(iv_profile_user_image)

        et_pro_name.setText(user.name)
        et_pro_email.setText(user.email)
        if (user.mobile != 0L) {
            et_pro_phone.setText(user.mobile.toString())
        }
    }
      private fun updateUserProfileData(){
            var userHashMap= HashMap<String,Any>()

            if(mProfileImageURL!!.isNotEmpty() && mProfileImageURL!= mUserDetails.image){

                userHashMap[Constants.IMAGE]= mProfileImageURL!!
            }
            if(et_pro_name.text.toString()!=mUserDetails.name){
                userHashMap[Constants.NAME]= et_pro_name.text.toString()

            }
            if(et_pro_phone.toString()!=mUserDetails.mobile.toString()){
                userHashMap[Constants.MOBILE]=et_pro_phone.text.toString().toLong()
            }
            FirestoreClass().updateUserProfileData(this,userHashMap)
        }

    private fun uploadUserImage(){
        showProgressDialogue("please wait")
        if (mSelectedImageFileUri!=null) {
            val sRef:StorageReference=FirebaseStorage.getInstance()
                .reference.child("USER_IMAGE"+System.currentTimeMillis()+
                        "."+getFileExtension(mSelectedImageFileUri))

            sRef.putFile(mSelectedImageFileUri!!).addOnSuccessListener{
                taskSnapshot->
                Log.i("FIrebase Image Url",
                    taskSnapshot.metadata!!.reference!!.downloadUrl.toString())

                taskSnapshot.metadata!!.reference!!.downloadUrl.addOnSuccessListener {
                    uri ->
                    Log.e("Downloadable Image Url",uri.toString())
                    mProfileImageURL=uri.toString()

                    updateUserProfileData()

                }
            }.addOnFailureListener{
                exception->
                Toast.makeText(this@MyProfileActivity,exception.message, Toast.LENGTH_SHORT).show()
                hideProgressDialogue()
            }

        }

    }
    private fun getFileExtension(uri: Uri?): String?{
        return MimeTypeMap.getSingleton().getExtensionFromMimeType(contentResolver.getType(uri!!))

    }

    fun profileUpdateSuccess(){
        hideProgressDialogue()
        setResult(Activity.RESULT_OK)
        finish()
    }
}