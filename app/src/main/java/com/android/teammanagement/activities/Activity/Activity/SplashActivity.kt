package com.android.teammanagement.activities.Activity.Activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.WindowManager
import com.android.teammanagement.R
import com.android.teammanagement.activities.Activity.firebase.FirestoreClass


class SplashActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)

        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
        Handler().postDelayed({
            var currentUserID=FirestoreClass().getCurrentUserId()
            if (currentUserID.isNotEmpty()) {
                startActivity(Intent(this,MainActivity::class.java))
            }else{
                startActivity(Intent(this, IntroActivity::class.java))
            }
            finish()
        },3000)
    }
}
