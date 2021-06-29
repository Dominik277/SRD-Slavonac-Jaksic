package hr.ui.adapters

import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import hr.dominik.ribolovnodrustvojaksic.databinding.ItemListLayoutBinding
import hr.model.Order
import hr.ui.activities.MyOrderDetailsActivity
import hr.util.Constants
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

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = list[position]

        GlideLoader(context).loadProductPicture(
            model.image,
            holder.binding.ivItemImage
        )

        holder.binding.tvItemName.text = model.title
        holder.binding.tvItemPrice.text = "$${model.total_amount}"
        holder.binding.ibDeleteProduct.visibility = View.GONE

        holder.itemView.setOnClickListener {
            val intent = Intent(context, MyOrderDetailsActivity::class.java)
            intent.putExtra(Constants.EXTRA_MY_ORDERS_DETAILS, model)
            context.startActivity(intent)
        }

    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder(val binding: ItemListLayoutBinding):
            RecyclerView.ViewHolder(binding.root)

}