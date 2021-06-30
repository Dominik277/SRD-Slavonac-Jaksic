package hr.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import hr.dominik.ribolovnodrustvojaksic.R
import hr.model.SoldProduct
import hr.util.Constants

class SoldProductDetailsActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_sold_product_details)

        var productDetails: SoldProduct = SoldProduct()
        if (intent.hasExtra(Constants.EXTRA_SOLD_PRODUCT_DETAILS)){
            productDetails = intent.getParcelableExtra(Constants.EXTRA_SOLD_PRODUCT_DETAILS)!!
        }
    }
}