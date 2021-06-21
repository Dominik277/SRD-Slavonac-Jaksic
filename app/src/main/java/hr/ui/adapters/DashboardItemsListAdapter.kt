package hr.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import hr.dominik.ribolovnodrustvojaksic.databinding.ItemDashboardLayoutBinding
import hr.dominik.ribolovnodrustvojaksic.databinding.ItemListLayoutBinding
import hr.model.Product
import hr.util.GlideLoader

open class DashboardItemsListAdapter(
    private val context: Context,
    private var list: ArrayList<Product>
): RecyclerView.Adapter<DashboardItemsListAdapter.ViewHolder>() {

    private var onClickListener: OnClickListener? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemDashboardLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    fun setOnClickListener(onClickListener: OnClickListener){
        this.onClickListener = onClickListener
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = list[position]
        GlideLoader(context).loadProductPicture(model.image,holder.binding.ivDashboardItemImage)
        holder.binding.tvDashboardItemTitle.text = model.title
        holder.binding.tvDashboardItemPrice.text = "$${model.price}"

        holder.itemView.setOnClickListener {
            if (onClickListener != null){
                onClickListener!!.onClick(position,model)
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder(val binding: ItemDashboardLayoutBinding):
        RecyclerView.ViewHolder(binding.root)

    interface OnClickListener{
        fun onClick(position: Int,product: Product)
    }
}