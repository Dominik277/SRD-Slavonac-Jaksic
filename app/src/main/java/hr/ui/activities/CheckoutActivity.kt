package hr.ui.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import hr.dominik.ribolovnodrustvojaksic.R
import hr.dominik.ribolovnodrustvojaksic.databinding.ActivityCheckoutBinding
import hr.firestore.FirestoreClass
import hr.model.Address
import hr.model.CartItem
import hr.model.Order
import hr.model.Product
import hr.ui.adapters.CartItemsListAdapter
import hr.util.Constants

class CheckoutActivity : BaseActivity() {

    private lateinit var binding: ActivityCheckoutBinding
    private var mAddressDetails: Address? = null
    private lateinit var mProductList: ArrayList<Product>
    private lateinit var mCartItemsList: ArrayList<CartItem>
    private var mSubTotal: Double = 0.0
    private var mTotalAmount: Double = 0.0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCheckoutBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setupActionBar()
        getProductList()

        if (intent.hasExtra(Constants.EXTRA_SELECTED_ADDRESS)){
            mAddressDetails = intent.getParcelableExtra<Address>(Constants.EXTRA_SELECTED_ADDRESS)
        }

        if (mAddressDetails != null){
            binding.tvCheckoutAddressType.text = mAddressDetails?.type
            binding.tvCheckoutFullName.text = mAddressDetails?.name
            binding.tvCheckoutAddress.text = "${mAddressDetails!!.address}, ${mAddressDetails!!.zipCode}"
            binding.tvCheckoutAdditionalNote.text = mAddressDetails?.additionalNote

            if (mAddressDetails?.otherDetails!!.isNotEmpty()){
                binding.tvCheckoutOtherDetails.text = mAddressDetails?.otherDetails
            }
            binding.tvCheckoutMobileNumber.text = mAddressDetails?.mobileNumber
        }

        getProductList()
        binding.btnPlaceOrder.setOnClickListener{
            placeAnOrder()
        }
    }

    private fun getProductList(){
        showProgressDialog()
        FirestoreClass().getAllProductList(this)
    }

    fun allDetailsUpdatedSuccessfully(){
        hideProgressDialog()
        Toast.makeText(this,
            "Your order placed successfully.",
            Toast.LENGTH_LONG)
            .show()

        val intent = Intent(this,DashboardActivity::class.java)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        startActivity(intent)
        finish()
    }

    fun orderPlacedSuccess(){
        FirestoreClass().updateAllDetails(this,mCartItemsList)
    }

    fun successProductListFromFirestore(productList: ArrayList<Product>){
        mProductList = productList
        getCartItemList()
    }

    private fun getCartItemList(){
        FirestoreClass().getCartList(this)
    }

    private fun placeAnOrder(){
        showProgressDialog()

        if (mAddressDetails != null){
            val order = Order(
                FirestoreClass().getCurrentUserID(),
                mCartItemsList,
                mAddressDetails!!,
                "My order ${System.currentTimeMillis()}",
                mCartItemsList[0].image,
                mSubTotal.toString(),
                "10.00",
                mTotalAmount.toString(),
                System.currentTimeMillis()
            )

            FirestoreClass().placeOrder(this,order)
        }
    }

    fun successCartItemList(cartList: ArrayList<CartItem>){
        hideProgressDialog()
        for (product in mProductList){
            for (cartItem in cartList){
                if (product.product_id == cartItem.product_id){
                    cartItem.stock_quantity = product.stock_quantity
                }
            }
        }

        mCartItemsList = cartList

        binding.rvCartListItems.layoutManager = LinearLayoutManager(this)
        binding.rvCartListItems.setHasFixedSize(true)

        val cartListAdapter = CartItemsListAdapter(this,mCartItemsList,false)
        binding.rvCartListItems.adapter = cartListAdapter

        for (item in mCartItemsList){
            val availableQuantity = item.stock_quantity.toInt()
            if (availableQuantity > 0 ){
                val price = item.price.toDouble()
                val quantity = item.cart_quantity.toInt()
                mSubTotal += (price * quantity)
            }
        }

        binding.tvCheckoutSubTotal.text = "$$mSubTotal"
        binding.tvCheckoutShippingCharge.text = "$10.00"

        if (mSubTotal > 0){
            binding.llCheckoutPlaceOrder.visibility = View.VISIBLE
             mTotalAmount = mSubTotal + 10.0
            binding.tvCheckoutTotalAmount.text = "$$mTotalAmount"
        }else{
            binding.llCheckoutPlaceOrder.visibility = View.GONE
        }

    }

    private fun setupActionBar() {

        setSupportActionBar(binding.toolbarCheckoutActivity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_black_color_back_24)
        }

        binding.toolbarCheckoutActivity.setNavigationOnClickListener { onBackPressed() }

    }

}