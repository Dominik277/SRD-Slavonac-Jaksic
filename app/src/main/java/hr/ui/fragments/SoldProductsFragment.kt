package hr.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import hr.dominik.ribolovnodrustvojaksic.R
import hr.dominik.ribolovnodrustvojaksic.databinding.FragmentSoldProductsBinding
import hr.firestore.FirestoreClass
import hr.model.SoldProduct
import hr.ui.adapters.SoldProductsListAdapter

class SoldProductsFragment: BaseFragment() {

    private lateinit var binding: FragmentSoldProductsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_sold_products, container,false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSoldProductsBinding.bind(view)
    }

    override fun onResume() {
        super.onResume()
        getSoldProductList()
    }

    private fun getSoldProductList(){
        showProgressDialog()
        FirestoreClass().getSoldProductList(this)
    }

    fun successSoldProductList(soldProductsList: ArrayList<SoldProduct>){
        hideProgressDialog()
        if (soldProductsList.size > 0){
            binding.rvSoldProductItems.visibility = View.VISIBLE
            binding.tvNoSoldProductsFound.visibility = View.GONE

            binding.rvSoldProductItems.layoutManager = LinearLayoutManager(activity)
            binding.rvSoldProductItems.setHasFixedSize(true)

            val soldProductListAdapter =
                SoldProductsListAdapter(requireActivity(), soldProductsList)
            binding.rvSoldProductItems.adapter = soldProductListAdapter
        }else{
            binding.rvSoldProductItems.visibility = View.GONE
            binding.tvNoSoldProductsFound.visibility = View.VISIBLE
        }
    }
}