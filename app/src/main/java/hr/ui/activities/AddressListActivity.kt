package hr.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.recyclerview.widget.LinearLayoutManager
import hr.dominik.ribolovnodrustvojaksic.R
import hr.dominik.ribolovnodrustvojaksic.databinding.ActivityAddressListBinding
import hr.firestore.FirestoreClass
import hr.model.Address
import hr.ui.adapters.AddressListAdapter

class AddressListActivity : BaseActivity() {

    private lateinit var binding: ActivityAddressListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddressListBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setupActionBar()
        getAddressList()

        binding.tvAddAddress.setOnClickListener{
            val intent = Intent(this,AddEditAddressActivity::class.java)
            startActivity(intent)
        }
    }

    override fun onResume() {
        super.onResume()
        getAddressList()
    }

    private fun setupActionBar(){
        setSupportActionBar(binding.toolbarAddressListActivity)

        val actionBar = supportActionBar
        if (actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_color_black_24)
        }

        binding.toolbarAddressListActivity.setNavigationOnClickListener { onBackPressed() }
    }

    fun successAddressListFromFirestore(addressList: ArrayList<Address>){
        hideProgressDialog()
        if (addressList.size > 0){
            binding.rvAddressList.visibility = View.VISIBLE
            binding.tvNoAddress.visibility = View.GONE

            binding.rvAddressList.layoutManager = LinearLayoutManager(this)
            binding.rvAddressList.setHasFixedSize(true)

            val addressAdapter = AddressListAdapter(this, addressList)
            binding.rvAddressList.adapter = addressAdapter
        }else{
            binding.rvAddressList.visibility = View.GONE
            binding.tvNoAddress.visibility = View.VISIBLE
        }
    }

    private fun getAddressList(){
        showProgressDialog()
        FirestoreClass().getAddressesList(this)
    }

}