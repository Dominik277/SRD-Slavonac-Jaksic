package hr.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import hr.dominik.ribolovnodrustvojaksic.databinding.ItemListLayoutBinding
import hr.model.Product
import hr.util.GlideLoader

open class MyProductsListAdapter(
    private val context: Context,
    private var list: ArrayList<Product>
): RecyclerView.Adapter<MyProductsListAdapter.ViewHolder>() {

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
        if (holder is ViewHolder){
            GlideLoader(context).loadProductPicture(model.image,holder.binding.ivItemImage)
            holder.binding.tvItemName.text = model.title
            holder.binding.tvItemPrice.text = "$${model.price}"
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder(val binding: ItemListLayoutBinding):
        RecyclerView.ViewHolder(binding.root)
}