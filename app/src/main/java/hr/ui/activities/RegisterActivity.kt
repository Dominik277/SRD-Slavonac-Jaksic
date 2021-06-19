package hr.ui.activities

import android.os.Build
import android.os.Bundle
import android.text.TextUtils
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import com.google.android.gms.tasks.OnCompleteListener
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import hr.dominik.ribolovnodrustvojaksic.R
import hr.dominik.ribolovnodrustvojaksic.databinding.ActivityRegisterBinding
import hr.firestore.FirestoreClass
import hr.model.User

class RegisterActivity : BaseActivity() {

    private lateinit var binding: ActivityRegisterBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R){
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        }else{
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }

        setupActionBar()

        binding.tvLogin.setOnClickListener {
            onBackPressed()
        }

        binding.btnRegister.setOnClickListener {
            registerUser()
        }

    }

    private fun setupActionBar(){
        setSupportActionBar(binding.toolbarRegisterActivity)

        val actionBar = supportActionBar
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_black_color_back_24)
        }
        binding.toolbarRegisterActivity.setNavigationOnClickListener { onBackPressed() }
    }

    private fun validateRegisterDetails(): Boolean{
        return when{
            TextUtils.isEmpty(binding.etFirstName.text.toString().trim{it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_first_name), true)
                false
            }
            TextUtils.isEmpty(binding.etLastName.text.toString().trim{it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_last_name), true)
                false
            }
            TextUtils.isEmpty(binding.etEmail.text.toString().trim{it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_email), true)
                false
            }
            TextUtils.isEmpty(binding.etPassword.text.toString().trim{it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_password), true)
                false
            }
            TextUtils.isEmpty(binding.etConfirmPassword.text.toString().trim{it <= ' ' }) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_confirm_password), true)
                false
            }
            binding.etPassword.text.toString().trim { it <= ' '} != binding.etConfirmPassword.text.toString().trim { it <= ' '} -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_password_and_confirm_password_mismatch), true)
                false
            }
            !binding.cbTermsAndCondition.isChecked -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_agree_terms_and_condition),true)
                false
            }
            else -> {
                //showErrorSnackBar("Your details are valid", false)
                true
            }
        }
    }

    private fun registerUser(){
        if(validateRegisterDetails()){

            showProgressDialog()

            val email: String = binding.etEmail.text.toString().trim{ it <= ' ' }
            val password: String = binding.etPassword.text.toString().trim{ it <= ' '}

            //Create an instance and create and register user with email and password
            FirebaseAuth.getInstance().createUserWithEmailAndPassword(email,password)
                .addOnCompleteListener(
                    OnCompleteListener<AuthResult> { task ->

                        //If registration is successfully done
                        if (task.isSuccessful){
                            val firebaseUser: FirebaseUser = task.result!!.user!!
                            val user = User(
                                firebaseUser.uid,
                                binding.etFirstName.text.toString().trim{ it <= ' '},
                                binding.etLastName.text.toString().trim{ it <= ' '},
                                binding.etEmail.text.toString().trim{ it <= ' '}
                            )

                            FirestoreClass().registerUser(this,user)

                            //FirebaseAuth.getInstance().signOut()
                            //finish()
                        }else{
                            hideProgressDialog()
                            //If the registering is not successful then show error message.
                            showErrorSnackBar(task.exception!!.message.toString(), true)
                        }
                    })
        }
    }

    fun userRegistrationSuccess(){
        //Hide the progress dialog
        hideProgressDialog()
        Toast.makeText(
            this,
            "You are registered successfully",
        Toast.LENGTH_LONG
        ).show()
    }

}