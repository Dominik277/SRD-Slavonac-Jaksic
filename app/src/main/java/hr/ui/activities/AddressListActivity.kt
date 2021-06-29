package hr.ui.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import hr.dominik.ribolovnodrustvojaksic.R
import hr.dominik.ribolovnodrustvojaksic.databinding.ActivityAddressListBinding
import hr.firestore.FirestoreClass
import hr.model.Address
import hr.ui.adapters.AddressListAdapter
import hr.util.SwipeToDeleteCallback
import hr.util.SwipeToEditCallback

class AddressListActivity : BaseActivity() {

    private lateinit var binding: ActivityAddressListBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddressListBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setupActionBar()
        getAddressList()
        hideProgressDialog()

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

            val editSwipeHandler = object: SwipeToEditCallback(this){
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val adapter = binding.rvAddressList.adapter as AddressListAdapter
                    adapter.notifyEditItem(
                        this@AddressListActivity,
                        viewHolder.adapterPosition
                    )
                }
            }

            val editItemTouchHelper = ItemTouchHelper(editSwipeHandler)
            editItemTouchHelper.attachToRecyclerView(binding.rvAddressList)

            val deleteSwipeHandler = object: SwipeToDeleteCallback(this){
                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    showProgressDialog()
                    FirestoreClass().deleteAddress(this@AddressListActivity,
                        addressList[viewHolder.adapterPosition].id)
                }
            }

            val deleteItemTouchHelper = ItemTouchHelper(deleteSwipeHandler)
            deleteItemTouchHelper.attachToRecyclerView(binding.rvAddressList)

        }else{
            binding.rvAddressList.visibility = View.GONE
            binding.tvNoAddress.visibility = View.VISIBLE
        }
    }

    fun deleteAddressSuccess(){
        hideProgressDialog()
        Toast.makeText(
            this,
            resources.getString(R.string.err_your_address_deleted_successfully),
            Toast.LENGTH_LONG
        ).show()

        getAddressList()
    }

    private fun getAddressList(){
        showProgressDialog()
        FirestoreClass().getAddressesList(this)
    }

}