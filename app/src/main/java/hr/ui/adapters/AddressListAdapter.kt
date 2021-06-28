package hr.ui.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import hr.dominik.ribolovnodrustvojaksic.databinding.ItemAddressLayoutBinding
import hr.model.Address

open class AddressListAdapter(
    private val context: Context,
    private var list: ArrayList<Address>
): RecyclerView.Adapter<AddressListAdapter.ViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        return ViewHolder(
            ItemAddressLayoutBinding.inflate(
                LayoutInflater.from(parent.context),
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = list[position]

        holder.binding.tvAddressFullName.text = model.name
        holder.binding.tvAddressType.text = model.type
        holder.binding.tvAddressDetails.text = "${model.address}, ${model.zipCode}"
        holder.binding.tvAddressMobileNumber.text = model.mobileNumber
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder(val binding: ItemAddressLayoutBinding):
        RecyclerView.ViewHolder(binding.root)


}