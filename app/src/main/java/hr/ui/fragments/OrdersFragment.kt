package hr.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import hr.dominik.ribolovnodrustvojaksic.databinding.FragmentOrdersBinding
import hr.firestore.FirestoreClass
import hr.model.Order

class OrdersFragment : BaseFragment() {

    private var _binding: FragmentOrdersBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentOrdersBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    fun populateOrderListInUI(orderList: ArrayList<Order>){
        hideProgressDialog()


    }

    private fun getMyOrdersList(){
        showProgressDialog()
        FirestoreClass().getMyOrderList(this)
    }

}