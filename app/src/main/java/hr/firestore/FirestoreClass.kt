package hr.firestore

import android.widget.Toast
import com.google.firebase.auth.UserInfo
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import hr.activities.RegisterActivity
import hr.model.User

class FirestoreClass {

    private val mFirestore = FirebaseFirestore.getInstance()

    fun registerUser(activity: RegisterActivity, userInfo: User){

        //The "users" is collection name. If the collection is already created then it will not create the same one
        mFirestore.collection("users")
            //Document ID for users fields. Here the document it is the User ID
            .document(userInfo.id)
            //Here the userInfo are Field and the SetOption is set to merge.
            .set(userInfo, SetOptions.merge())
            .addOnSuccessListener {
                activity.userRegistrationSuccess()
            }
            .addOnFailureListener{ e ->
                activity.hideProgressDialog()
            }
    }

}