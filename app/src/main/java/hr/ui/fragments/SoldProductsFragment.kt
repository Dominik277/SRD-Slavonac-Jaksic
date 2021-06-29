package hr.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import hr.dominik.ribolovnodrustvojaksic.R
import hr.firestore.FirestoreClass
import hr.model.SoldProduct

class SoldProductsFragment: BaseFragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_sold_products, container,false)
    }

    private fun getSoldProductList(){
        showProgressDialog()
        FirestoreClass().getSoldProductList(this)
    }

    fun successSoldProductList(soldProductsList: ArrayList<SoldProduct>){
        hideProgressDialog()
        if (soldProductsList.size > 0){

        }else{

        }
    }

}