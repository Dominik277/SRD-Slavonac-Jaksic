package hr.ui.activities

import android.os.Bundle
import android.view.View
import hr.dominik.ribolovnodrustvojaksic.R
import hr.dominik.ribolovnodrustvojaksic.databinding.ActivityProductDetailsBinding
import hr.firestore.FirestoreClass
import hr.model.Product
import hr.util.Constants
import hr.util.GlideLoader

class ProductDetailsActivity : BaseActivity(),View.OnClickListener {

    private lateinit var binding: ActivityProductDetailsBinding
    private var mProductId: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductDetailsBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setupActionBar()

        binding.btnAddToCart.setOnClickListener(this)

        if (intent.hasExtra(Constants.EXTRA_PRODUCT_ID)){
            mProductId = intent.getStringExtra(Constants.EXTRA_PRODUCT_ID)!!
        }
        var productOwnerId: String = ""

        if (intent.hasExtra(Constants.EXTRA_PRODUCT_OWNER_ID)){
            productOwnerId =
                intent.getStringExtra(Constants.EXTRA_PRODUCT_OWNER_ID)!!
        }

        if (FirestoreClass().getCurrentUserID() == productOwnerId){
            binding.btnAddToCart.visibility = View.GONE
        }else{
            binding.btnAddToCart.visibility = View.VISIBLE
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

    override fun onClick(view: View?) {

    }

}