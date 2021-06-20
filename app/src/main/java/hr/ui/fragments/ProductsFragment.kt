package hr.ui.fragments

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.*
import hr.dominik.ribolovnodrustvojaksic.R
import hr.dominik.ribolovnodrustvojaksic.databinding.FragmentProductsBinding
import hr.firestore.FirestoreClass
import hr.model.Product
import hr.ui.activities.AddProductActivity

class ProductsFragment : BaseFragment() {

    private var _binding: FragmentProductsBinding? = null
    private val binding get() = _binding!!

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentProductsBinding.inflate(inflater, container, false)

        return binding.root
    }

    override fun onResume() {
        super.onResume()
        getProductsFromFirestore()
    }

    fun successProductsListFromFireStore(productsList: ArrayList<Product>){
        hideProgressDialog()

        for (i in productsList){
            Log.i("Product name",i.title)
        }
    }

    private fun getProductsFromFirestore(){
        showProgressDialog()
        FirestoreClass().getProductsList(this)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.add_product_menu,menu)
        super.onCreateOptionsMenu(menu, inflater)

    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when(item.itemId){
            R.id.action_add_product -> {
                startActivity(Intent(activity, AddProductActivity::class.java))
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}