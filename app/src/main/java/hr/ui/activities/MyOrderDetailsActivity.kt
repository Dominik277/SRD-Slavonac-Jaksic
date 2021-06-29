package hr.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firestore.v1.StructuredQuery
import hr.dominik.ribolovnodrustvojaksic.R
import hr.dominik.ribolovnodrustvojaksic.databinding.ActivityMyOrderDetailsBinding
import hr.model.Order
import hr.util.Constants

class MyOrderDetailsActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMyOrderDetailsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMyOrderDetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        setupActionBar()

        var myOrderDetails: Order
        if (intent.hasExtra(Constants.EXTRA_MY_ORDERS_DETAILS)){
            intent.getParcelableExtra<Order>(Constants.EXTRA_MY_ORDERS_DETAILS)!!
        }
    }

    private fun setupActionBar() {
        setSupportActionBar(binding.toolbarMyOrderDetailsActivity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_color_black_24)
        }

        binding.toolbarMyOrderDetailsActivity.setNavigationOnClickListener { onBackPressed() }

    }
}