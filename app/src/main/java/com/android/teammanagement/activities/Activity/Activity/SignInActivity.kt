package com.android.teammanagement.activities.Activity.Activity

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.WindowManager
import androidx.appcompat.widget.Toolbar
import com.android.teammanagement.R

class SignInActivity : AppCompatActivity() {
    private lateinit var signIntoolbar: Toolbar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sign_in)
        signIntoolbar=findViewById(R.id.signIn_toolbar)
        window.setFlags(
            WindowManager.LayoutParams.FLAG_FULLSCREEN,
            WindowManager.LayoutParams.FLAG_FULLSCREEN
        )
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
}