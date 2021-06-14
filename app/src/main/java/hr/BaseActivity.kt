package hr

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.core.content.ContextCompat
import com.google.android.material.snackbar.Snackbar
import hr.dominik.ribolovnodrustvojaksic.R
import hr.dominik.ribolovnodrustvojaksic.databinding.ActivityBaseBinding

open class BaseActivity : AppCompatActivity() {

    private lateinit var binding: ActivityBaseBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityBaseBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
    }

    fun showErrorSnackBar(message: String, errorMessage: Boolean){
        val snackBar = Snackbar.make(findViewById(android.R.id.content), message, Snackbar.LENGTH_LONG)
        val snackBarView = snackBar.view

        if (errorMessage){
            snackBarView.setBackgroundColor(
                ContextCompat.getColor(
                    this,
                    R.color.colorSnackBarError
                )
            )
        }else{
            snackBarView.setBackgroundColor(
                ContextCompat.getColor(
                    this,
                    R.color.colorSnackBarSuccess
                )
            )
        }
        snackBar.show()
    }

}