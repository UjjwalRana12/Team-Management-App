package com.android.teammanagement.activities.Activity.Activity

import android.content.ContentValues.TAG
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.util.Log
import android.view.WindowManager
import android.widget.Button
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import com.android.teammanagement.R
import com.google.firebase.auth.FirebaseAuth

class SignInActivity : BaseActivity() {
    private lateinit var signIntoolbar: Toolbar
    private lateinit var et_email_signin:TextView
    private lateinit var et_password_signin:TextView
    private lateinit var btn_signIn:Button
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        signIntoolbar=findViewById(R.id.signIn_toolbar)
        et_email_signin=findViewById(R.id.et_email)
        et_password_signin=findViewById(R.id.et_password)
        btn_signIn=findViewById(R.id.signIn_btn)
        auth=FirebaseAuth.getInstance()

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        btn_signIn.setOnClickListener {
            signInRegisteredUser()
        }
        setUpActionBar()
    }
    private fun setUpActionBar() {
        setSupportActionBar(signIntoolbar)
        supportActionBar?.apply {
            setDisplayHomeAsUpEnabled(true)
            title = ""
            setHomeAsUpIndicator(R.drawable.baseline_arrow_back_ios_new_24)
        }
        signIntoolbar.setNavigationOnClickListener{onBackPressed()}
    }

    private fun signInRegisteredUser(){
        val email:String=et_email_signin.text.toString().trim()
        val password:String=et_password_signin.text.toString().trim()
        if(validateForm(email, password)){
            showProgressDialogue("please wait...")
            auth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this) { task ->
                    hideProgressDialogue()
                    if (task.isSuccessful) {
                        // Sign in success, update UI with the signed-in user's information
                        Log.d(TAG, "signInWithEmail:success")
                        val user = auth.currentUser
                        startActivity(Intent(this,MainActivity::class.java))
                    } else {
                        // If sign in fails, display a message to the user.
                        Log.w(TAG, "signInWithEmail:failure", task.exception)
                        Toast.makeText(
                            baseContext,
                            "Authentication failed.",
                            Toast.LENGTH_SHORT,
                        ).show()
                    }
                }

        }
    }
    private fun validateForm( email: String, password: String): Boolean {
        return when {
            TextUtils.isEmpty(email) -> {
                showErrorSnackBar("Please enter an email")
                false
            }
            TextUtils.isEmpty(password) -> {
                showErrorSnackBar("Please enter a password")
                false
            }
            else -> true
        }
    }
}