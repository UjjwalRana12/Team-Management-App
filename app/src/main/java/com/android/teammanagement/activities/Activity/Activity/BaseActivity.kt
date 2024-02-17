package com.android.teammanagement.activities.Activity.Activity


import android.annotation.SuppressLint
import android.app.Dialog
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import com.android.teammanagement.R
import com.google.android.material.snackbar.Snackbar
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser


open class BaseActivity : AppCompatActivity() {

    lateinit var tvProgress_text:TextView
   private var doubleBackToExitPressedOnce=false
    private lateinit var mPregressDialogue:Dialog


    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_base)
        tvProgress_text=findViewById(R.id.tvprogressText)
    }

    fun showProgressDialogue(text:String){
        mPregressDialogue= Dialog(this)

        mPregressDialogue.setContentView(R.layout.dialogue_layout)

        mPregressDialogue.show()
    }

    fun hideProgressDialogue(){
        mPregressDialogue.dismiss()
    }

    fun getCurrentUserId(): String {
        return FirebaseAuth.getInstance().currentUser!!.uid
    }

    fun doubleBackToExit() {
        if (doubleBackToExitPressedOnce) {
            super.onBackPressed()
            return
        }

        this.doubleBackToExitPressedOnce = true
        Toast.makeText(this, "Please click back again to exit", Toast.LENGTH_SHORT).show()

        Handler().postDelayed({
            doubleBackToExitPressedOnce = false
        }, 2000) // Delay in milliseconds
    }

    fun showErrorSnackBar(message: String){
        val snackBar= Snackbar.make(findViewById(androidx.appcompat.R.id.content),
            message, Snackbar.LENGTH_LONG)
        val snackBarView=snackBar.view
        snackBarView.setBackgroundColor(ContextCompat.getColor(this, R.color.snackBarColor))
        snackBar.show()
    }

}