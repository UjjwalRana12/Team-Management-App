package com.android.teammanagement.activities.Activity.firebase

import android.app.Activity
import android.util.Log
import com.android.teammanagement.activities.Activity.Activity.MainActivity
import com.android.teammanagement.activities.Activity.Activity.MyProfileActivity
import com.android.teammanagement.activities.Activity.Activity.SignInActivity
import com.android.teammanagement.activities.Activity.Activity.SignUpActivity
import com.android.teammanagement.activities.Activity.models.User
import com.android.teammanagement.activities.Activity.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions

class FirestoreClass {
    private  val mFirestore = FirebaseFirestore.getInstance()


    fun registerUser(
        activity: SignUpActivity,
        userInfo: User
    ){
        mFirestore.collection(Constants.USERS)
            .document(getCurrentUserId()).set(userInfo, SetOptions.merge()).addOnSuccessListener {
                activity.userRegisteredSuccess()
            }.addOnFailureListener {
                e->
                Log.e(activity.javaClass.simpleName,"error registering");
            }

    }
    fun getCurrentUserId():String{
        var currentUser=FirebaseAuth.getInstance().currentUser
        var currentUserID=""
        if(currentUser!=null){
            currentUserID=currentUser.uid

        }
        return currentUserID
            }
// yeh or kahi bhi use hona chaiye kuch error h
    fun loadUserData(activity: Activity){
        mFirestore.collection(Constants.USERS)
            .document(getCurrentUserId())
            .get().addOnSuccessListener {document->
               val loggedInUser=document.toObject(User::class.java)!!

                when(activity){
                    is SignInActivity->{
                        activity.signInSuccess{loggedInUser}
                    }
                    is MainActivity->{
                        activity.updateNavigationUserDetails(loggedInUser)
                }
                    is MyProfileActivity->{
                        activity.setUserDatInUI(loggedInUser)
                    }

            }



            }.addOnFailureListener {
                    e-> when(activity){
                is SignInActivity->{
                    activity.hideProgressDialogue()
                }
                is MainActivity->{
                    activity.hideProgressDialogue()
                }

            }
                Log.e("SignInUser","error registering")

            }
    }
}