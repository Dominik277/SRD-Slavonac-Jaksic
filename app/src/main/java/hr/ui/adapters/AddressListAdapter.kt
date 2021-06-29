package hr.ui.adapters

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.Toast
import androidx.recyclerview.widget.RecyclerView
import hr.dominik.ribolovnodrustvojaksic.databinding.ItemAddressLayoutBinding
import hr.model.Address
import hr.ui.activities.AddEditAddressActivity
import hr.ui.activities.CheckoutActivity
import hr.util.Constants

open class AddressListAdapter(
    private val context: Context,
    private var list: ArrayList<Address>,
    private val selectAddress: Boolean
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

    fun notifyEditItem(activity: Activity, position: Int){
        val intent = Intent(context, AddEditAddressActivity::class.java)
        intent.putExtra(Constants.EXTRA_ADDRESS_DETAILS, list[position])
        activity.startActivityForResult(intent,Constants.ADD_ADDRESS_REQUEST_CODE)
        notifyItemChanged(position)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val model = list[position]

        holder.binding.tvAddressFullName.text = model.name
        holder.binding.tvAddressType.text = model.type
        holder.binding.tvAddressDetails.text = "${model.address}, ${model.zipCode}"
        holder.binding.tvAddressMobileNumber.text = model.mobileNumber

        if (selectAddress){
            holder.itemView.setOnClickListener {
                val intent = Intent(context,CheckoutActivity::class.java)
                intent.putExtra(Constants.EXTRA_SELECTED_ADDRESS, model)
                context.startActivity(intent)
            }
        }
    }

    override fun getItemCount(): Int {
        return list.size
    }

    class ViewHolder(val binding: ItemAddressLayoutBinding):
        RecyclerView.ViewHolder(binding.root)


}