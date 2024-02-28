package com.android.teammanagement.activities.Activity.Activity

import android.Manifest
import android.app.Activity
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.widget.Toolbar
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import com.android.teammanagement.R
import com.android.teammanagement.activities.Activity.utils.Constants
import com.bumptech.glide.Glide
import java.io.IOException

class CreateBoardActivity : AppCompatActivity() {
    private var mSelectedImageFileUri: Uri? = null
    private lateinit var cb_toolbar:Toolbar
    private lateinit var iv_board_image: ImageView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_create_board)
        iv_board_image=findViewById(R.id.iv_board_image)
        cb_toolbar=findViewById(R.id.toolbar_create_board_activity)
        setupActionBar()

        iv_board_image.setOnClickListener {
            if (ContextCompat.checkSelfPermission(this,
                    Manifest.permission.READ_EXTERNAL_STORAGE)
                == PackageManager.PERMISSION_GRANTED) {

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