package hr.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import com.google.firebase.auth.FirebaseAuth
import hr.dominik.ribolovnodrustvojaksic.R
import hr.dominik.ribolovnodrustvojaksic.databinding.ActivitySettingsBinding
import hr.firestore.FirestoreClass
import hr.model.User
import hr.util.Constants
import hr.util.GlideLoader

class SettingsActivity : BaseActivity(),View.OnClickListener {

    private lateinit var binding: ActivitySettingsBinding
    private lateinit var mUserDetails: User

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySettingsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setupActionBar()

        binding.tvEdit.setOnClickListener(this)
        binding.btnLogout.setOnClickListener(this)
        binding.llAddress.setOnClickListener(this)
    }

    private fun setupActionBar(){
        setSupportActionBar(binding.toolbarSettingsActivity)

        val actionBar = supportActionBar
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_color_black_24)
        }

        binding.toolbarSettingsActivity.setNavigationOnClickListener { onBackPressed() }
    }

    private fun getUserDetails(){
        showProgressDialog()
        FirestoreClass().getUserDetails(this)
    }

    fun userDetailSuccess(user: User){
        mUserDetails = user
        hideProgressDialog()
        GlideLoader(this).loadUserPicture(user.image,binding.ivUserPhoto)
        binding.tvName.text = "${user.firstName} ${user.lastName}"
        binding.tvGender.text = user.gender
        binding.tvEmail.text = user.email
        binding.tvMobileNumber.text = "${user.mobile}"
    }

    override fun onResume() {
        super.onResume()
        getUserDetails()
    }

    override fun onClick(v: View?) {
        if (v != null){
            when(v.id){
                R.id.tv_edit -> {
                    val intent = Intent(this,UserProfileActivity::class.java)
                    intent.putExtra(Constants.EXTRA_USER_DETAILS, mUserDetails)
                    startActivity(intent)
                }
                R.id.btn_logout -> {
                    FirebaseAuth.getInstance().signOut()
                    val intent = Intent(this,LoginActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    finish()
                }
                R.id.ll_address -> {
                    val intent = Intent(this, AddressListActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }

}