package hr.ui.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import hr.dominik.ribolovnodrustvojaksic.R
import hr.dominik.ribolovnodrustvojaksic.databinding.ActivityProductDetailsBinding
import hr.firestore.FirestoreClass
import hr.model.CartItem
import hr.model.Product
import hr.util.Constants
import hr.util.GlideLoader

class ProductDetailsActivity : BaseActivity(),View.OnClickListener {

    private lateinit var binding: ActivityProductDetailsBinding
    private var mProductId: String = ""
    private lateinit var mProductDetails: Product

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
            binding.btnGoToCart.visibility = View.GONE
        }else{
            binding.btnAddToCart.visibility = View.VISIBLE
            binding.btnGoToCart.visibility = View.GONE
        }

        getProductDetails()
        binding.btnAddToCart.setOnClickListener(this)
        binding.btnGoToCart.setOnClickListener(this)
    }

    private fun getProductDetails(){
        showProgressDialog()
        FirestoreClass().getProductDetails(this,mProductId)
    }

    fun productExistsInCart(){
        hideProgressDialog()
        binding.btnAddToCart.visibility = View.GONE
        binding.btnGoToCart.visibility = View.VISIBLE
    }

    fun productDetailsSuccess(product: Product){
        mProductDetails = product
        GlideLoader(this).loadUserPicture(
            product.image,
            binding.ivProductDetailImage
        )
        binding.tvProductDetailsTitle.text = product.title
        binding.tvProductDetailsPrice.text = "$${product.price}"
        binding.tvProductDetailsDescription.text = product.description
        binding.tvProductDetailsAvailableQuantity.text = product.stock_quantity

        if (FirestoreClass().getCurrentUserID() == product.user_id){
            hideProgressDialog()
        }else{
            FirestoreClass().checkIfItemExistInCart(this,mProductId)
        }
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

    private fun addToCart(){
        val cartItem = CartItem(
            FirestoreClass().getCurrentUserID(),
            mProductId,
            mProductDetails.title,
            mProductDetails.price,
            mProductDetails.image,
            Constants.DEFAULT_CART_QUANTITY
        )
        showProgressDialog()
        FirestoreClass().addCartItems(this,cartItem)
    }

    fun addToCartSuccess(){
        hideProgressDialog()
        Toast.makeText(
            this,
            resources.getString(R.string.success_message_item_added_to_cart),
            Toast.LENGTH_LONG).show()

        binding.btnAddToCart.visibility = View.GONE
        binding.btnGoToCart.visibility = View.VISIBLE
    }

    override fun onClick(view: View?) {
        if (view != null){
            when(view.id){
                R.id.btn_add_to_cart -> {
                    addToCart()
                }
                R.id.btn_go_to_cart -> {
                    startActivity(Intent(this,CartListActivity::class.java))
                }
            }
        }
    }
}