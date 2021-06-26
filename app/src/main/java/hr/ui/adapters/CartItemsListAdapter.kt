package hr.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import hr.dominik.ribolovnodrustvojaksic.R
import hr.dominik.ribolovnodrustvojaksic.databinding.ItemCartLayoutBinding
import hr.firestore.FirestoreClass
import hr.model.CartItem
import hr.ui.activities.CartListActivity
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

        if (model.cart_quantity == "0"){
            holder.binding.ibRemoveCartItem.visibility = android.view.View.GONE
            holder.binding.ibAddCartItem.visibility = android.view.View.GONE

            holder.binding.tvCartQuantity.text =
                context.resources.getString(R.string.lbl_out_of_stock)

            holder.binding.tvCartQuantity.setTextColor(
                ContextCompat.getColor(
                    context,
                    R.color.design_default_color_error
                )
            )
        }else{
            holder.binding.ibRemoveCartItem.visibility = android.view.View.VISIBLE
            holder.binding.ibAddCartItem.visibility = android.view.View.VISIBLE

            holder.binding.tvCartQuantity.setTextColor(
                ContextCompat.getColor(
                    context,
                    R.color.browser_actions_bg_grey
                )
            )
        }
        holder.binding.ibDeleteCartItem.setOnClickListener {
            when(context){
                is CartListActivity -> {
                    context.showProgressDialog()
                }
            }
            FirestoreClass().removeItemFromCart(context, model.id)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder(val binding: ItemCartLayoutBinding): RecyclerView.ViewHolder(binding.root)


}