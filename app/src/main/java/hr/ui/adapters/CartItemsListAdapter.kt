package hr.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.core.content.ContextCompat
import androidx.recyclerview.widget.RecyclerView
import hr.dominik.ribolovnodrustvojaksic.R
import hr.dominik.ribolovnodrustvojaksic.databinding.ItemCartLayoutBinding
import hr.firestore.FirestoreClass
import hr.model.CartItem
import hr.ui.activities.CartListActivity
import hr.util.Constants
import hr.util.GlideLoader

open class CartItemsListAdapter(
    private val context: Context,
    private var list: ArrayList<CartItem>,
    private val updateCartItems: Boolean
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
            holder.binding.ibRemoveCartItem.visibility = View.GONE
            holder.binding.ibAddCartItem.visibility = View.GONE

            if (updateCartItems){
                holder.binding.ibDeleteCartItem.visibility = View.VISIBLE
            }else{
                holder.binding.ibDeleteCartItem.visibility = View.GONE
            }

            holder.binding.tvCartQuantity.text =
                context.resources.getString(R.string.lbl_out_of_stock)

            holder.binding.tvCartQuantity.setTextColor(
                ContextCompat.getColor(
                    context,
                    R.color.design_default_color_error
                )
            )
        }else{
            if (updateCartItems){
                holder.binding.ibRemoveCartItem.visibility = View.VISIBLE
                holder.binding.ibAddCartItem.visibility = View.VISIBLE
                holder.binding.ibDeleteCartItem.visibility = View.VISIBLE
            }else{
                holder.binding.ibRemoveCartItem.visibility = View.GONE
                holder.binding.ibAddCartItem.visibility = View.GONE
                holder.binding.ibDeleteCartItem.visibility = View.GONE
            }

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

        holder.binding.ibRemoveCartItem.setOnClickListener {
            if (model.cart_quantity == "1"){
                FirestoreClass().removeItemFromCart(context, model.id)
            }else{
                val cartQuantity: Int = model.cart_quantity.toInt()
                val itemHashMap = HashMap<String,Any>()

                itemHashMap[Constants.CART_QUANTITY] = (cartQuantity - 1).toString()

                if (context is CartListActivity){
                    context.showProgressDialog()
                }
                FirestoreClass().updateMyCart(context, model.id, itemHashMap)
            }
        }

        holder.binding.ibAddCartItem.setOnClickListener {
            val cartQuantity: Int = model.cart_quantity.toInt()
            if (cartQuantity < model.stock_quantity.toInt()){
                val itemHashMap = HashMap<String,Any>()
                itemHashMap[Constants.CART_QUANTITY] = (cartQuantity + 1).toString()

                if (context is CartListActivity){
                    context.showProgressDialog()
                }
                FirestoreClass().updateMyCart(context, model.id, itemHashMap)
            }else{
                if (context is CartListActivity){
                    context.showErrorSnackBar(
                        context.resources.getString(
                            R.string.msg_for_available_stock,
                            model.stock_quantity
                    ),
                    true)
                }
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder(val binding: ItemCartLayoutBinding): RecyclerView.ViewHolder(binding.root)


}