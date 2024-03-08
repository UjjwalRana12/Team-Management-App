package com.android.teammanagement.activities.Activity.utils

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.provider.MediaStore
import android.util.Log
import android.webkit.MimeTypeMap
import com.android.teammanagement.activities.Activity.Activity.MyProfileActivity

object Constants {
    const val USERS:String="users"
    const val BOARDS:String="boards"
    const val IMAGE:String="image"
    const val NAME:String="name"
    const val ASSIGNED_TO:String="assignedTo"
    const val MOBILE:String="mobile"
     const val READ_STORAGE_PERMISSION_CODE = 1
     const val PICK_IMAGE_REQUEST_CODE = 2
    const val DOCUMENT_ID: String = "documentId"
    const val TASK_LIST:String="taskList"
    const val BOARD_DETAILS:String="board_detail"
    const val ID:String = "id"
    const val EMAIL:String="email"
    const val BOARD_MEMBERS_LIST :String="board_members_list"
    const val TASK_LIST_ITEM_POSITION : String = "task_list_item_position"
    const val CARD_LIST_ITEM_POSITION : String = "card_list_item_postion"

    const val SELECT:String="Select"
    const val UN_SELECT:String="UnSelect"


     fun showImageChoser(activity: Activity) {

        var galleryIntent = Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
        activity.startActivityForResult(galleryIntent, PICK_IMAGE_REQUEST_CODE)
        Log.d("Permission", "image chooser is triggered")
    }

     fun getFileExtension(activity: Activity,uri: Uri?): String?{
        return MimeTypeMap.getSingleton().
        getExtensionFromMimeType(activity.
        contentResolver.getType(uri!!))

    }

}