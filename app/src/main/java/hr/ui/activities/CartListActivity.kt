package hr.ui.activities

import android.os.Bundle
import hr.dominik.ribolovnodrustvojaksic.R
import hr.dominik.ribolovnodrustvojaksic.databinding.ActivityCartListBinding
import hr.firestore.FirestoreClass
import hr.model.CartItem

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

    fun successCartItemsList(cartList: ArrayList<CartItem>){
        hideProgressDialog()
    }

    private fun getCartItemsList(){
        showProgressDialog()
        FirestoreClass().getCartList(this)
    }

    private fun setupActionBar(){

        setSupportActionBar(binding.toolbarCartListActivity)

        val actionBar = supportActionBar
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_black_color_back_24)
        }

        binding.toolbarCartListActivity.setNavigationOnClickListener { onBackPressed() }

    }
}