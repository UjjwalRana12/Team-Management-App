
package com.android.teammanagement.activities.Activity.Activity
import android.os.Bundle
import android.text.TextUtils
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.widget.Toolbar

import com.android.teammanagement.R
import com.android.teammanagement.activities.Activity.firebase.FirestoreClass
import com.android.teammanagement.activities.Activity.models.User

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser


class SignUpActivity : BaseActivity() {

    private lateinit var toolbar: Toolbar
    private lateinit var etName: EditText
    private lateinit var etEmail: EditText
    private lateinit var etPassword: EditText
    private lateinit var btnSignup: Button

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_up)

        // Find views

        toolbar = findViewById(R.id.signUp_toolbar)
        etName = findViewById(R.id.etname)
        etEmail = findViewById(R.id.etemail)
        etPassword = findViewById(R.id.etpassword)
        btnSignup = findViewById(R.id.btn_signup)

        // Set up action bar
        setSupportActionBar(toolbar)
        val actionBar = supportActionBar
        if(actionBar!=null) {

               actionBar.setDisplayHomeAsUpEnabled(true)
               actionBar.setHomeAsUpIndicator(R.drawable.baseline_arrow_back_ios_new_24)

        }


        toolbar.setNavigationOnClickListener { onBackPressed() }

        // Set click listener for signup button
        btnSignup.setOnClickListener {
            registerUser()
        }
    }

    private fun registerUser() {
        val name: String = etName.text.toString().trim()
        val email: String = etEmail.text.toString().trim()
        val password: String = etPassword.text.toString().trim()

        if (validateForm(name, email, password)) {
            showProgressDialogue("Please wait...")
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password).addOnCompleteListener { task ->
               // hideProgressDialogue()
                if (task.isSuccessful) {
                    val firebaseUser: FirebaseUser = task.result!!.user!!
                    val registeredEmail = firebaseUser.email!!
                    val user= User(firebaseUser.uid,name,registeredEmail)
                    FirestoreClass().registerUser(this,user)
                } else

                    Toast.makeText(this, "registration failed", Toast.LENGTH_SHORT).show()
                Toast.makeText(this, task.exception!!.message, Toast.LENGTH_SHORT).show()

            }
        }
    }

    private fun validateForm(name: String, email: String, password: String): Boolean {
        return when {
            TextUtils.isEmpty(name) -> {
                showErrorSnackBar("Please enter a name")
                false
            }
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

        fun userRegisteredSuccess(){
            Toast.makeText(this, "you have sucessfully registered", Toast.LENGTH_LONG).show()
            hideProgressDialogue()
            FirebaseAuth.getInstance().signOut()
            finish()
        }


}
