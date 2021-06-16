package hr

import android.content.Intent
import android.os.Build
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import com.google.firebase.auth.FirebaseAuth
import hr.dominik.ribolovnodrustvojaksic.R
import hr.dominik.ribolovnodrustvojaksic.databinding.ActivityLoginBinding
import java.nio.channels.InterruptedByTimeoutException

class LoginActivity : BaseActivity(), View.OnClickListener {

    private lateinit var binding: ActivityLoginBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
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

        binding.tvForgotPassword.setOnClickListener(this)
        binding.btnLogin.setOnClickListener(this)
        binding.tvRegister.setOnClickListener(this)

    }

    override fun onClick(view: View?) {
        if (view != null){
            when(view.id){
                R.id.tv_forgot_password -> {

                }
                R.id.btn_login -> {
                    logInRegisteredUser()
                }
                R.id.tv_register -> {
                    val intent = Intent(this,RegisterActivity::class.java)
                    startActivity(intent)
                }
            }
        }
    }

    private fun validateLoginDetails(): Boolean{
        return when {
            TextUtils.isEmpty(binding.etEmail.text.toString().trim { it <= ' '}) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_email),true)
                false
            }
            TextUtils.isEmpty(binding.etPassword.text.toString().trim { it <= ' '}) -> {
                showErrorSnackBar(resources.getString(R.string.err_msg_enter_password),true)
                false
            }
            else -> {
                true
            }
        }
    }

    private fun logInRegisteredUser(){
        if (validateLoginDetails()){

            //Show the progress dialog
            showProgressDialog()

            //Get the text from edittext and trim the space
            val email = binding.etEmail.text.toString().trim { it <= ' '}
            val password = binding.etPassword.text.toString().trim { it <= ' '}

            //LogIn using FirebaseAuth
            FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
                .addOnCompleteListener { task ->
                    hideProgressDialog()

                    if (task.isSuccessful){
                        showErrorSnackBar("You are logged in successfully",false)
                    }else{
                        showErrorSnackBar(task.exception!!.message.toString(),true)
                    }
                }
        }
    }

}