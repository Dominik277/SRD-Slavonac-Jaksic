package hr.ui.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.Toast
import hr.dominik.ribolovnodrustvojaksic.R
import hr.dominik.ribolovnodrustvojaksic.databinding.ActivityAddEditAddressBinding
import hr.dominik.ribolovnodrustvojaksic.databinding.ActivityAddressListBinding
import hr.firestore.FirestoreClass
import hr.model.Address
import hr.util.Constants

class AddEditAddressActivity : BaseActivity() {

    private lateinit var binding: ActivityAddEditAddressBinding
    private var mAddressDetails: Address? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityAddEditAddressBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        setupActionBar()

        if (intent.hasExtra(Constants.EXTRA_ADDRESS_DETAILS)){
            mAddressDetails = intent.getParcelableExtra(Constants.EXTRA_ADDRESS_DETAILS)
        }

        binding.btnSubmitAddress.setOnClickListener { saveAddressToFirestore() }
        binding.rgType.setOnCheckedChangeListener {_, checkedId ->
            if (checkedId == R.id.rb_other){
                binding.tilOtherDetails.visibility = View.VISIBLE
                binding.btnSubmitAddress.visibility = View.VISIBLE
            }else{
                binding.tilOtherDetails.visibility = View.GONE
                binding.btnSubmitAddress.visibility = View.VISIBLE
            }
        }
    }

    private fun saveAddressToFirestore(){
        val fullName: String = binding.etFullName.text.toString().trim{ it <= ' '}
        val phoneNumber: String = binding.etPhoneNumber.text.toString().trim{ it <= ' '}
        val address: String = binding.etAddress.text.toString().trim{ it <= ' '}
        val zipCode: String = binding.etZipCode.text.toString().trim{ it <= ' '}
        val additionalNote: String = binding.etAdditionalNote.text.toString().trim{ it <= ' '}
        val otherDetails: String = binding.etOtherDetails.text.toString().trim{ it <= ' '}

        if (validateData()){
            showProgressDialog()

            val addressType: String = when{
                binding.rbHome.isChecked -> {
                    Constants.HOME
                }
                binding.rbOffice.isChecked -> {
                    Constants.OFFICE
                }
                else -> {
                    Constants.OTHER
                }
            }
            val addressModel = Address(
                FirestoreClass().getCurrentUserID(),
                fullName,
                phoneNumber,
                address,
                zipCode,
                additionalNote,
                addressType,
                otherDetails
            )
            FirestoreClass().addAddress(this,addressModel)

        }
    }

    fun addUpdatedAddressSuccess(){
        hideProgressDialog()
        Toast.makeText(
            this,
            resources.getString(R.string.err_your_address_added_successfully),
            Toast.LENGTH_LONG).show()
        finish()
    }

    private fun setupActionBar() {
        setSupportActionBar(binding.toolbarAddEditAddressActivity)

        val actionBar = supportActionBar
        if (actionBar != null) {
            actionBar.setDisplayHomeAsUpEnabled(true)
            actionBar.setHomeAsUpIndicator(R.drawable.ic_white_color_black_24)
        }

        binding.toolbarAddEditAddressActivity.setNavigationOnClickListener { onBackPressed() }
    }

    private fun validateData(): Boolean {
        return when {
            TextUtils.isEmpty(binding.etFullName.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(
                    resources.getString(R.string.err_msg_please_enter_full_name),
                    true
                )
                false
            }
            TextUtils.isEmpty(binding.etPhoneNumber.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(
                    resources.getString(R.string.err_msg_please_enter_phone_number),
                    true
                )
                false
            }
            TextUtils.isEmpty(binding.etAddress.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(
                    resources.getString(R.string.err_msg_please_enter_address),
                    true
                )
                false
            }
            TextUtils.isEmpty(binding.etZipCode.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(
                    resources.getString(R.string.err_msg_please_enter_zip_code),
                    true
                )
                false
            }
            binding.rbOther.isChecked && TextUtils.isEmpty(
                binding.etZipCode.text.toString().trim { it <= ' ' }) -> {
                showErrorSnackBar(
                    resources.getString(R.string.err_msg_please_enter_zip_code),
                    true
                )
                false
            }
            else -> {
                true
            }
        }
    }

}