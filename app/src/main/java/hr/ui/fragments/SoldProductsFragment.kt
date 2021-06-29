package hr.ui.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import hr.dominik.ribolovnodrustvojaksic.databinding.FragmentSoldProductsBinding

class SoldProductsFragment: BaseFragment() {

    private lateinit var binding: FragmentSoldProductsBinding

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return super.onCreateView(inflater, container, savedInstanceState)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        binding = FragmentSoldProductsBinding.bind(view)
    }

}