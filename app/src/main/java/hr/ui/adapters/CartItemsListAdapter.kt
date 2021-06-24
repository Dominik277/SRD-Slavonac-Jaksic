package hr.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import hr.dominik.ribolovnodrustvojaksic.databinding.ItemCartLayoutBinding
import hr.model.CartItem
import hr.util.GlideLoader

open class CartItemsListAdapter(
    private val context: Context,
    private var list: ArrayList<CartItem>
): RecyclerView.Adapter<CartItemsListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemCartLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = list[position]
        GlideLoader(context).loadProductPicture(model.image,holder.binding.ivCartItemImage)
        holder.binding.tvCartItemTitle.text = model.title
        holder.binding.tvCartItemPrice.text = "$${model.price}"
        holder.binding.tvCartQuantity.text = model.cart_quantity
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder(val binding: ItemCartLayoutBinding): RecyclerView.ViewHolder(binding.root)


}