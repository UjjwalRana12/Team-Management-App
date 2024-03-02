package com.android.teammanagement.activities.Activity.firebase

import android.app.Activity
import android.util.Log
import android.widget.Toast
import com.android.teammanagement.activities.Activity.Activity.CreateBoardActivity
import com.android.teammanagement.activities.Activity.Activity.MainActivity
import com.android.teammanagement.activities.Activity.Activity.MyProfileActivity
import com.android.teammanagement.activities.Activity.Activity.SignInActivity
import com.android.teammanagement.activities.Activity.Activity.SignUpActivity
import com.android.teammanagement.activities.Activity.Activity.TaskListActivtiy
import com.android.teammanagement.activities.Activity.models.Board
import com.android.teammanagement.activities.Activity.models.User
import com.android.teammanagement.activities.Activity.utils.Constants
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.firestore.toObject

class FirestoreClass {
    private val mFirestore = FirebaseFirestore.getInstance()


    fun registerUser(
        activity: SignUpActivity,
        userInfo: User
    ) {
        mFirestore.collection(Constants.USERS)
            .document(getCurrentUserId()).set(userInfo, SetOptions.merge()).addOnSuccessListener {
                activity.userRegisteredSuccess()
            }.addOnFailureListener { e ->
                Log.e(activity.javaClass.simpleName, "error registering");
            }

    }

    fun getCurrentUserId(): String {
        var currentUser = FirebaseAuth.getInstance().currentUser
        var currentUserID = ""
        if (currentUser != null) {
            currentUserID = currentUser.uid

        }
        return currentUserID
    }


    fun updateUserProfileData(activity: MyProfileActivity, userHashMap: HashMap<String, Any>) {
        mFirestore.collection(Constants.USERS)
            .document(getCurrentUserId())
            .update(userHashMap)
            .addOnSuccessListener {
                Log.i(activity.javaClass.simpleName, "profile data updated successfully")
                Toast.makeText(activity, "Updated profile", Toast.LENGTH_LONG).show()
                activity.profileUpdateSuccess()
            }.addOnFailureListener { e ->
                activity.hideProgressDialogue()
                Log.i(activity.javaClass.simpleName, "error while creating a board", e)
                Toast.makeText(activity, "error while updating the profile", Toast.LENGTH_LONG)
                    .show()


            }
    }

    fun getBoardsList(activity: MainActivity) {
        mFirestore.collection(Constants.BOARDS)
            .whereArrayContains(Constants.ASSIGNED_TO, getCurrentUserId())
            .get()
            .addOnSuccessListener { document ->
                Log.i(activity.javaClass.simpleName, document.documents.toString())
                val boardList: ArrayList<Board> = ArrayList()

                for (i in document.documents) {
                    val board = i.toObject(Board::class.java)!!
                    board.documentId = i.id
                    boardList.add(board)
                }

                activity.populateBoardsListToUI(boardList)
            }.addOnFailureListener { e ->
                activity.hideProgressDialogue()
                Log.e(activity.javaClass.simpleName, "error while createing board", e)

            }
    }

    fun createBoard(activity: CreateBoardActivity, board: Board) {
        mFirestore.collection(Constants.BOARDS)
            .document()
            .set(board, SetOptions.merge())
            .addOnSuccessListener {
                Log.e(activity.javaClass.simpleName, "Board Created successfully")
                Toast.makeText(activity, "Board Created successfully", Toast.LENGTH_LONG).show()
                activity.boardCreatedSuccessfully()
            }.addOnFailureListener { exception ->
                activity.hideProgressDialogue()
                Log.e(activity.javaClass.simpleName, "error while creating a board")
            }
    }

    fun getBoardDetails(activity: TaskListActivtiy, documentId: String) {
        mFirestore.collection(Constants.BOARDS)
            .document(documentId)
            .get()
            .addOnSuccessListener { document ->
                Log.i(activity.javaClass.simpleName, document.toString())
                activity.boardDetails(document.toObject(Board::class.java)!!)
            }.addOnFailureListener { e ->
                activity.hideProgressDialogue()
                Log.e(activity.javaClass.simpleName, "error while createing board", e)

            }
    }

    fun loadUserData(activity: Activity, readBoardsList: Boolean = false) {
        mFirestore.collection(Constants.USERS)
            .document(getCurrentUserId())
            .get().addOnSuccessListener { document ->
                val loggedInUser = document.toObject(User::class.java)!!

                when (activity) {
                    is SignInActivity -> {
                        activity.signInSuccess { loggedInUser }
                    }

                    is MainActivity -> {
                        activity.updateNavigationUserDetails(loggedInUser, readBoardsList)
                    }

                    is MyProfileActivity -> {
                        activity.setUserDatInUI(loggedInUser)
                    }

                }


            }.addOnFailureListener { e ->
                when (activity) {
                    is SignInActivity -> {
                        activity.hideProgressDialogue()
                    }

                    is MainActivity -> {
                        activity.hideProgressDialogue()
                    }

                }
                Log.e("SignInUser", "error registering")

            }
    }
}