package hr.ui.adapters

import android.content.Context
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import hr.dominik.ribolovnodrustvojaksic.databinding.ItemCartLayoutBinding
import hr.model.CartItem

open class CartItemsListAdapter(
    private val context: Context,
    private var list: ArrayList<CartItem>
): RecyclerView.Adapter<RecyclerView.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {

    }

    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {

    }

    override fun getItemCount(): Int {

    }

    class ViewHolder(val binding: ItemCartLayoutBinding): RecyclerView.ViewHolder(binding.root)

}