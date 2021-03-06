package hr.ui.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import hr.dominik.ribolovnodrustvojaksic.databinding.ItemListLayoutBinding
import hr.model.SoldProduct
import hr.ui.activities.SoldProductDetailsActivity
import hr.util.Constants
import hr.util.GlideLoader

class SoldProductsListAdapter(
    private val context: Context,
    private var list: ArrayList<SoldProduct>
) : RecyclerView.Adapter<SoldProductsListAdapter.ViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemListLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = list[position]

        GlideLoader(context).loadProductPicture(
            model.image,
            holder.binding.ivItemImage)
        holder.binding.tvItemName.text = model.title
        holder.binding.tvItemPrice.text = "$${model.price}"
        holder.binding.ibDeleteProduct.visibility = View.GONE

        holder.itemView.setOnClickListener {
            val intent = Intent(context, SoldProductDetailsActivity::class.java)
            intent.putExtra(Constants.EXTRA_SOLD_PRODUCT_DETAILS,model)
            context.startActivity(intent)
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder(val binding: ItemListLayoutBinding) :
        RecyclerView.ViewHolder(binding.root)

}