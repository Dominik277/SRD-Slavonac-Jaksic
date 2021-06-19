package hr.firestore

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import hr.ui.activities.LoginActivity
import hr.ui.activities.RegisterActivity
import hr.ui.activities.UserProfileActivity
import hr.model.User
import hr.util.Constants

class FirestoreClass {

    private val mFirestore = FirebaseFirestore.getInstance()

    fun registerUser(activity: RegisterActivity, userInfo: User){

        //The "users" is collection name. If the collection is already created then it will not create the same one
        mFirestore.collection(Constants.USERS)
            //Document ID for users fields. Here the document it is the User ID
            .document(userInfo.id)
            //Here the userInfo are Field and the SetOption is set to merge.
            .set(userInfo, SetOptions.merge())
            .addOnSuccessListener {
                activity.userRegistrationSuccess()
            }
            .addOnFailureListener{
                activity.hideProgressDialog()
            }
    }

    fun getCurrentUserID(): String{

        //Instance of currentUser using FirebaseAuth
        val currentUser = FirebaseAuth.getInstance().currentUser

        //A variable to assing the currentUserID if it is not null or else it will be blank
        var currentUserID = ""
        if (currentUser != null){
            currentUserID = currentUser.uid
        }
        return currentUserID
    }

    fun getUserDetails(activity: Activity){

        //Here we pass the collection name from where we want the data
        mFirestore.collection(Constants.USERS)
            .document(getCurrentUserID())
            .get()
            .addOnSuccessListener { document ->
                Log.i(activity.javaClass.simpleName, document.toString())

                //Here we have revived the document snapshot which is converted into the User data model.
                val user = document.toObject(User::class.java)!!

                val sharedPreferences = activity.getSharedPreferences(
                    Constants.MYSHOPPAL_PREFERENCES,
                    Context.MODE_PRIVATE
                )

                val editor: SharedPreferences.Editor = sharedPreferences.edit()
                //Key: LOGGED_IN_USERNAME
                //Value: First Name, Last Name
                editor.putString(
                    Constants.LOGGED_IN_USERNAME,
                    "${user.firstName} ${user.lastName}"
                )
                editor.apply()

                when(activity){
                    is LoginActivity -> {
                        //Call a function of base activity for transferring the result to it
                            activity.userLoggedInSuccess(user)
                    }
                }
            }
            .addOnFailureListener {
                when(activity){
                    is LoginActivity -> {
                        activity.hideProgressDialog()
                    }
                }
            }
    }

    fun updateUserProfileData(activity: Activity, userHashMap: HashMap<String, Any>){
        mFirestore
            .collection(Constants.USERS)
            .document(getCurrentUserID())
            .update(userHashMap)
            .addOnSuccessListener {
                when(activity){
                    is UserProfileActivity -> {
                        activity.userProfileUpdateSuccess()

                    }
                }
            }
            .addOnFailureListener { e ->
                when(activity){
                    is UserProfileActivity -> {
                        //Hide the progress dialog if there is any error. And print the error in log
                        activity.hideProgressDialog()
                    }
                }
                Log.e(
                    activity.javaClass.simpleName,
                    "Error while updatin the user details",
                    e)
            }
    }

    fun uploadImageToCloudStorage(activity: Activity, imageFileURI: Uri?){
        val sRef: StorageReference = FirebaseStorage.getInstance().reference.child(
            Constants.USER_PROFILE_IMAGE + System.currentTimeMillis() +  "."
            + Constants.getFileExtension(
                activity,imageFileURI))

        sRef.putFile(imageFileURI!!).addOnSuccessListener { taskSnapshot ->
            //The image upload success
            Log.e("Firebase Image URL",
            taskSnapshot.metadata!!.reference!!.downloadUrl.toString())

            //Get the downloadable url from the task snapshot
            taskSnapshot.metadata!!.reference!!.downloadUrl
                .addOnSuccessListener { uri ->
                    Log.e("Downloadable Image URL", uri.toString())
                    when(activity){
                        is UserProfileActivity -> {
                            activity.imageUploadSuccess(uri.toString())
                        }
                    }
                }
        }
            .addOnFailureListener{ exception ->
                //Hide the progress dialog if there is any error. And print the error in log.
                when(activity){
                    is UserProfileActivity -> {
                        activity.hideProgressDialog()
                    }
                }
                Log.e(activity.javaClass.simpleName,
                exception.message,
                exception)
            }
    }
}