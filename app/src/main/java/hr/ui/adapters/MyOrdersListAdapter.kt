package hr.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import hr.dominik.ribolovnodrustvojaksic.databinding.ItemListLayoutBinding
import hr.model.Order
import hr.util.GlideLoader

open class MyOrdersListAdapter(
    private val context: Context,
    private var list: ArrayList<Order>
): RecyclerView.Adapter<MyOrdersListAdapter.ViewHolder>() {
    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ViewHolder {
        return ViewHolder(
            ItemListLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: MyOrdersListAdapter.ViewHolder, position: Int) {
        val model = list[position]

        GlideLoader(context).loadProductPicture(
            model.image,
            holder.binding.ivItemImage
        )

        holder.binding.tvItemName.text = model.title
        holder.binding.tvItemPrice.text = "$${model.total_amount}"
        holder.binding.ibDeleteProduct.visibility = View.GONE

    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder(val binding: ItemListLayoutBinding):
            RecyclerView.ViewHolder(binding.root)

}