package hr.ui.activities

import android.os.Bundle
import hr.dominik.ribolovnodrustvojaksic.R
import hr.dominik.ribolovnodrustvojaksic.databinding.ActivityProductDetailsBinding
import hr.firestore.FirestoreClass
import hr.model.Product
import hr.util.Constants
import hr.util.GlideLoader

class ProductDetailsActivity : BaseActivity() {

    private lateinit var binding: ActivityProductDetailsBinding
    private var mProductId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductDetailsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setupActionBar()

        if (intent.hasExtra(Constants.EXTRA_PRODUCT_ID)){
            mProductId = intent.getStringExtra(Constants.EXTRA_PRODUCT_ID)!!

        }

        getProductDetails()
    }

    private fun getProductDetails(){
        showProgressDialog()
        FirestoreClass().getProductDetails(this,mProductId)
    }

    fun productDetailsSuccess(product: Product){
        hideProgressDialog()
        GlideLoader(this).loadUserPicture(
            product.image,
            binding.ivProductDetailImage
        )
        binding.tvProductDetailsTitle.text = product.title
        binding.tvProductDetailsPrice.text = "$${product.price}"
        binding.tvProductDetailsDescription.text = product.description
        binding.tvProductDetailsAvailableQuantity.text = product.stock_quantity
    }

    private fun setupActionBar(){
        setSupportActionBar(binding.toolbarProductDetailsActivity)

        val actionBar = supportActionBar
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_black_color_back_24)
        }
        binding.toolbarProductDetailsActivity.setNavigationOnClickListener { onBackPressed() }
    }

}