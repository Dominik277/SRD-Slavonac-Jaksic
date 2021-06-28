package hr.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import hr.dominik.ribolovnodrustvojaksic.R
import hr.dominik.ribolovnodrustvojaksic.databinding.ActivityAddEditAddressBinding
import hr.dominik.ribolovnodrustvojaksic.databinding.ActivityAddressListBinding

class AddEditAddressActivity : AppCompatActivity() {

    private lateinit var binding: ActivityAddEditAddressBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddEditAddressBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setupActionBar()
    }

    private fun setupActionBar(){
        setSupportActionBar(binding.toolbarAddEditAddressActivity)

        val actionBar = supportActionBar
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_color_black_24)
        }

        binding.toolbarAddEditAddressActivity.setNavigationOnClickListener { onBackPressed() }
    }

}