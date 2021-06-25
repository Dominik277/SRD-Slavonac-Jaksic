package hr.ui.activities

import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import hr.dominik.ribolovnodrustvojaksic.R
import hr.dominik.ribolovnodrustvojaksic.databinding.ActivityCartListBinding
import hr.firestore.FirestoreClass
import hr.model.CartItem
import hr.model.Product
import hr.ui.adapters.CartItemsListAdapter

class CartListActivity : BaseActivity() {

    private lateinit var binding: ActivityCartListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCartListBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setupActionBar()
    }

    override fun onResume() {
        super.onResume()
        getCartItemsList()
    }

    fun successCartItemsList(cartList: ArrayList<CartItem>) {
        hideProgressDialog()

        if (cartList.size > 0) {
            binding.rvCartItemsList.visibility = View.VISIBLE
            binding.llCheckout.visibility = View.VISIBLE
            binding.tvNoCartItemFound.visibility = View.GONE

            binding.rvCartItemsList.layoutManager = LinearLayoutManager(this)
            binding.rvCartItemsList.setHasFixedSize(true)
            val cartListAdapter = CartItemsListAdapter(this, cartList)
            binding.rvCartItemsList.adapter = cartListAdapter
            var subTotal: Double = 0.0
            for (item in cartList) {
                val price = item.price.toDouble()
                val quantity = item.cart_quantity.toInt()
                subTotal += (price * quantity)
            }
            binding.tvSubTotal.text = "$$subTotal"
            binding.tvShippingCharge.text = "$10.0"

            if (subTotal > 0) {
                binding.llCheckout.visibility = View.VISIBLE

                val total = subTotal + 10
                binding.tvTotalAmount.text = "$$total"
            } else {
                binding.llCheckout.visibility = View.GONE
            }
        } else {
            binding.rvCartItemsList.visibility = View.GONE
            binding.llCheckout.visibility = View.GONE
            binding.tvNoCartItemFound.visibility = View.VISIBLE
        }
    }

    fun successProductsListFromFirestore(productList: ArrayList<Product>) {

    }

    private fun getCartItemsList() {
        showProgressDialog()
        FirestoreClass().getCartList(this)
    }

    private fun setupActionBar() {

        setSupportActionBar(binding.toolbarCartListActivity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_black_color_back_24)
        }

        binding.toolbarCartListActivity.setNavigationOnClickListener { onBackPressed() }

    }
}