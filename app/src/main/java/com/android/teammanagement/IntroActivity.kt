package com.android.teammanagement

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class IntroActivity : AppCompatActivity() {
    private lateinit var signInButton: Button
    private lateinit var signUpButton: Button
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_intro)
        signInButton = findViewById(R.id.button_SignIn)
        signUpButton = findViewById(R.id.buttonSignUp)

        signUpButton.setOnClickListener{
            startActivity(Intent(this, SignUpActivity::class.java))
        }
    }
}