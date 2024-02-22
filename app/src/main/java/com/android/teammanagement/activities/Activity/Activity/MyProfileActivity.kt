package com.android.teammanagement.activities.Activity.Activity

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.EditText
import android.widget.ImageView
import androidx.appcompat.widget.Toolbar
import com.android.teammanagement.R
import com.android.teammanagement.activities.Activity.firebase.FirestoreClass
import com.android.teammanagement.activities.Activity.models.User
import com.bumptech.glide.Glide

class MyProfileActivity : BaseActivity() {
    lateinit var my_pro_toolbar:Toolbar
    lateinit var iv_user_image: ImageView
    lateinit var et_pro_name:EditText
    lateinit var et_pro_email:EditText
    lateinit var et_pro_phone:EditText

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_my_profile)
        my_pro_toolbar=findViewById(R.id.toolbar_my_profile_activty)
        iv_user_image=findViewById(R.id.iv_user_image)
        et_pro_name=findViewById(R.id.et_name_pro)
        et_pro_email=findViewById(R.id.et_email_pro)
        et_pro_phone=findViewById(R.id.et_phone_pro)
        setupActionBar()

        FirestoreClass().loadUserData(this)

    }
    private fun setupActionBar() {
        setSupportActionBar(my_pro_toolbar)
       val actionBar = supportActionBar
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.baseline_arrow_back_ios_new_24)
            actionBar.title="My Profile"
        }
        my_pro_toolbar.setNavigationOnClickListener{
            onBackPressed()
        }
    }

    fun setUserDatInUI(user: User){
        Glide
            .with(this@MyProfileActivity)
            .load(user.image)
            .centerCrop()
            .placeholder(R.drawable.profile)
            .into(iv_user_image)

        et_pro_name.setText(user.name)
        et_pro_email.setText(user.email)
        if(user.mobile!=0L){
            et_pro_phone.setText(user.mobile.toString())
        }
    }
}