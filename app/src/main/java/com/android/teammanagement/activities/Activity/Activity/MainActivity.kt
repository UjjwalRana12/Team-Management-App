package com.android.teammanagement.activities.Activity.Activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.widget.Toolbar
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.view.GravityCompat
import androidx.drawerlayout.widget.DrawerLayout
import com.android.teammanagement.R
import com.android.teammanagement.activities.Activity.firebase.FirestoreClass
import com.android.teammanagement.activities.Activity.models.User
import com.bumptech.glide.Glide
import com.google.android.material.appbar.AppBarLayout
import com.google.android.material.navigation.NavigationView
import com.google.firebase.auth.FirebaseAuth

class MainActivity : BaseActivity(),NavigationView.OnNavigationItemSelectedListener {
    lateinit var toolbar_main_activity: Toolbar
    lateinit var nav_view: NavigationView
    lateinit var nav_user_image:ImageView
    lateinit var tv_username: TextView


    companion object{
        const val MY_PROFILE_REQUEST_CODE:Int=11
    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val appBarLayout = findViewById<AppBarLayout>(R.id.myappBarLayout)
        toolbar_main_activity = appBarLayout.findViewById<Toolbar>(R.id.toolbar_mainActivity)
        val navView = findViewById<NavigationView>(R.id.nav_view)
        val headerView = navView.getHeaderView(0)
      // toolbar_main_activity = findViewById(R.id.toolbar_mainActivity)


        nav_view=findViewById(R.id.nav_view)
        nav_user_image=headerView.findViewById(R.id.nav_user_image)
        tv_username=headerView.findViewById(R.id.tv_username)
        setupActionBar()

        nav_view.setNavigationItemSelectedListener(this)
        FirestoreClass().loadUserData(this)
    }

    private fun setupActionBar() {
        setSupportActionBar(toolbar_main_activity)
        toolbar_main_activity.setNavigationIcon(R.drawable.baseline_menu_24)

        toolbar_main_activity.setNavigationOnClickListener() {
            toggleDrawer()
        }
    }

    private fun toggleDrawer() {
        val drawer_layout = findViewById<DrawerLayout>(R.id.drawer_layout)
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            drawer_layout.openDrawer(GravityCompat.START)
        }
    }


    override fun onBackPressed() {
        val drawer_layout = findViewById<DrawerLayout>(R.id.drawer_layout)
        if (drawer_layout.isDrawerOpen(GravityCompat.START)) {
            drawer_layout.closeDrawer(GravityCompat.START)
        } else {
            doubleBackToExit()
            super.onBackPressed()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)
        if(resultCode== Activity.RESULT_OK && requestCode== MY_PROFILE_REQUEST_CODE){
            FirestoreClass().loadUserData(this)
        }
        else
            Log.e("cancelled", "cancelled")
    }
    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        val drawer_layout = findViewById<DrawerLayout>(R.id.drawer_layout)
        when(item.itemId){
            R.id.nav_my_profile->{
              //  Toast.makeText(this@MainActivity,"My Profile", Toast.LENGTH_SHORT).show()
                startActivityForResult(
                    Intent(this,MyProfileActivity::class.java),
                    MY_PROFILE_REQUEST_CODE)
            }
            R.id.nav_sign_out->{
                FirebaseAuth.getInstance().signOut()
                val intent= Intent(this,IntroActivity::class.java)
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_NEW_TASK)
                startActivity(intent)
                finish()
            }

    }
        drawer_layout.closeDrawer(GravityCompat.START)
        return true
}

fun updateNavigationUserDetails(user: User){

    Glide
        .with(this)
        .load(user.image)
        .centerCrop()
        .placeholder(R.drawable.profile)
        .into(nav_user_image);

    tv_username.text=user.name

}


}