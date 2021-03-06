package hr.firestore

import android.app.Activity
import android.content.Context
import android.content.SharedPreferences
import android.net.Uri
import android.util.Log
import androidx.fragment.app.Fragment
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import com.google.firebase.storage.FirebaseStorage
import com.google.firebase.storage.StorageReference
import hr.model.*
import hr.ui.activities.*
import hr.ui.fragments.DashboardFragment
import hr.ui.fragments.OrdersFragment
import hr.ui.fragments.ProductsFragment
import hr.ui.fragments.SoldProductsFragment
import hr.util.Constants

class FirestoreClass {

    private val mFirestore = FirebaseFirestore.getInstance()

    fun registerUser(activity: RegisterActivity, userInfo: User){

        //The "users" is collection name. If the collection is already created then it will not create the same one
        mFirestore.collection(Constants.USERS)
            //Document ID for users fields. Here the document it is the User ID
            .document(userInfo.id)
            //Here the userInfo are Field and the SetOption is set to merge.
            .set(userInfo, SetOptions.merge())
            .addOnSuccessListener {
                activity.userRegistrationSuccess()
            }
            .addOnFailureListener{
                activity.hideProgressDialog()
            }
    }

    fun getCurrentUserID(): String{

        //Instance of currentUser using FirebaseAuth
        val currentUser = FirebaseAuth.getInstance().currentUser

        //A variable to assing the currentUserID if it is not null or else it will be blank
        var currentUserID = ""
        if (currentUser != null){
            currentUserID = currentUser.uid
        }
        return currentUserID
    }

    fun getUserDetails(activity: Activity){

        //Here we pass the collection name from where we want the data
        mFirestore.collection(Constants.USERS)
            .document(getCurrentUserID())
            .get()
            .addOnSuccessListener { document ->
                Log.i(activity.javaClass.simpleName, document.toString())

                //Here we have revived the document snapshot which is converted into the User data model.
                val user = document.toObject(User::class.java)!!

                val sharedPreferences = activity.getSharedPreferences(
                    Constants.MYSHOPPAL_PREFERENCES,
                    Context.MODE_PRIVATE
                )

                val editor: SharedPreferences.Editor = sharedPreferences.edit()
                //Key: LOGGED_IN_USERNAME
                //Value: First Name, Last Name
                editor.putString(
                    Constants.LOGGED_IN_USERNAME,
                    "${user.firstName} ${user.lastName}"
                )
                editor.apply()

                when(activity){
                    is LoginActivity -> {
                        //Call a function of base activity for transferring the result to it
                            activity.userLoggedInSuccess(user)
                    }
                    is SettingsActivity -> {
                        activity.userDetailSuccess(user)
                    }
                }
            }
            .addOnFailureListener {
                when(activity){
                    is LoginActivity -> {
                        activity.hideProgressDialog()
                    }
                    is SettingsActivity -> {
                        activity.hideProgressDialog()
                    }
                }
            }
    }

    fun updateUserProfileData(activity: Activity, userHashMap: HashMap<String, Any>){
        mFirestore
            .collection(Constants.USERS)
            .document(getCurrentUserID())
            .update(userHashMap)
            .addOnSuccessListener {
                when(activity){
                    is UserProfileActivity -> {
                        activity.userProfileUpdateSuccess()

                    }
                }
            }
            .addOnFailureListener { e ->
                when(activity){
                    is UserProfileActivity -> {
                        //Hide the progress dialog if there is any error. And print the error in log
                        activity.hideProgressDialog()
                    }
                }
                Log.e(
                    activity.javaClass.simpleName,
                    "Error while updatin the user details",
                    e)
            }
    }

    fun uploadImageToCloudStorage(activity: Activity, imageFileURI: Uri?, imageType: String){
        val sRef: StorageReference = FirebaseStorage.getInstance().reference.child(
            imageType + System.currentTimeMillis() +  "."
            + Constants.getFileExtension(
                activity,imageFileURI))

        sRef.putFile(imageFileURI!!).addOnSuccessListener { taskSnapshot ->
            //The image upload success
            Log.e("Firebase Image URL",
            taskSnapshot.metadata!!.reference!!.downloadUrl.toString())

            //Get the downloadable url from the task snapshot
            taskSnapshot.metadata!!.reference!!.downloadUrl
                .addOnSuccessListener { uri ->
                    Log.e("Downloadable Image URL", uri.toString())
                    when(activity){
                        is UserProfileActivity -> {
                            activity.imageUploadSuccess(uri.toString())
                        }
                        is AddProductActivity -> {
                            activity.imageUploadSuccess(uri.toString())
                        }
                    }
                }
        }
            .addOnFailureListener{ exception ->
                //Hide the progress dialog if there is any error. And print the error in log.
                when(activity){
                    is UserProfileActivity -> {
                        activity.hideProgressDialog()
                    }
                    is AddProductActivity -> {
                        activity.hideProgressDialog()
                    }
                }
                Log.e(activity.javaClass.simpleName,
                exception.message,
                exception)
            }
    }

    fun uploadProductDetails(activity: AddProductActivity,productInfo: Product){
        mFirestore.collection(Constants.PRODUCTS)
            .document()
            .set(productInfo, SetOptions.merge())
            .addOnSuccessListener {
                activity.productUploadSuccess()
            }
            .addOnFailureListener { e ->
                activity.hideProgressDialog()
                Log.e(
                    activity.javaClass.simpleName,
                    "Error while uploading the product details.",
                    e)
            }
    }

    fun getProductsList(fragment: Fragment){
        mFirestore.collection(Constants.PRODUCTS)
            .whereEqualTo(Constants.USER_ID, getCurrentUserID())
            .get()
            .addOnSuccessListener { document ->
                Log.e("Products List", document.documents.toString())
                val productsList: ArrayList<Product> = ArrayList()
                for (i in document.documents){
                    val product = i.toObject(Product::class.java)
                    product!!.product_id = i.id

                    productsList.add(product)
                }
                when(fragment){
                    is ProductsFragment -> {
                        fragment.successProductsListFromFireStore(productsList)
                    }
                }
            }
    }

    fun getProductDetails(activity: ProductDetailsActivity,productID: String){
        mFirestore.collection(Constants.PRODUCTS)
            .document(productID)
            .get()
            .addOnSuccessListener { document ->
                val product = document.toObject(Product::class.java)
                if (product != null) {
                    activity.productDetailsSuccess(product)
                }
            }
            .addOnFailureListener { e ->
                activity.hideProgressDialog()
            }
    }

    fun addCartItems(activity: ProductDetailsActivity, addToCart: CartItem){
        mFirestore.collection(Constants.CART_ITEMS)
            .document()
            .set(addToCart, SetOptions.merge())
            .addOnSuccessListener {
                activity.addToCartSuccess()
            }
            .addOnFailureListener {
                activity.hideProgressDialog()
            }
    }

    fun deleteProduct(fragment: ProductsFragment,productID: String){
        mFirestore.collection(Constants.PRODUCTS)
            .document(productID)
            .delete()
            .addOnSuccessListener {
                fragment.productDeleteSuccess()
            }
            .addOnFailureListener { e ->
                fragment.hideProgressDialog()
                Log.e(
                    fragment.requireActivity().javaClass.simpleName,
                    "Error while deleting the product",
                    e
                )
            }
    }

    fun getCartList(activity: Activity){
        mFirestore.collection(Constants.CART_ITEMS)
            .whereEqualTo(Constants.USER_ID,getCurrentUserID())
            .get()
            .addOnSuccessListener { document ->
                val list: ArrayList<CartItem> = ArrayList()

                for (i in document.documents){
                    val cartItem = i.toObject(CartItem::class.java)!!
                    cartItem.id = i.id

                    list.add(cartItem)
                }
                when(activity){
                    is CartListActivity -> {
                        activity.successCartItemsList(list)
                    }
                    is CheckoutActivity -> {
                        activity.successCartItemList(list)
                    }
                }
            }
            .addOnFailureListener {
                when(activity){
                    is CartListActivity -> {
                        activity.hideProgressDialog()
                    }
                    is CheckoutActivity -> {
                        activity.hideProgressDialog()
                    }
                }
            }
    }

    fun updateAllDetails(activity: CheckoutActivity, cartList: ArrayList<CartItem>, order: Order){
        val writeBatch = mFirestore.batch()
        for (cartItem in cartList){
            val soldProduct = SoldProduct(
                cartItem.product_owner_id,
                cartItem.title,
                cartItem.price,
                cartItem.cart_quantity,
                cartItem.image,
                order.title,
                order.order_dateTime,
                order.sub_total_amount,
                order.shipping_charge,
                order.total_amount,
                order.address
            )

            val documentReference = mFirestore.collection(Constants.SOLD_PRODUCTS)
                .document(cartItem.product_id)
            writeBatch.set(documentReference,soldProduct)
        }

        for (cartItem in cartList){
            val documentReference = mFirestore.collection(Constants.CART_ITEMS)
                .document(cartItem.id)
            writeBatch.delete(documentReference)
        }
        writeBatch.commit()
            .addOnSuccessListener {
                activity.allDetailsUpdatedSuccessfully()
            }
            .addOnFailureListener {
                activity.hideProgressDialog()
            }
    }

    fun getSoldProductList(fragment: SoldProductsFragment){
        mFirestore.collection(Constants.SOLD_PRODUCTS)
            .whereEqualTo(Constants.USER_ID, getCurrentUserID())
            .get()
            .addOnSuccessListener { document ->
                val list: ArrayList<SoldProduct> = ArrayList()
                for(i in document.documents){
                    val soldProduct = i.toObject(SoldProduct::class.java)!!
                    soldProduct.id = i.id

                    list.add(soldProduct)
                }
                fragment.successSoldProductList(list)
            }
            .addOnFailureListener {
                fragment.hideProgressDialog()
            }
    }

    fun getMyOrderList(fragment: OrdersFragment){
        mFirestore.collection(Constants.ORDERS)
            .whereEqualTo(Constants.USER_ID, getCurrentUserID())
            .get()
            .addOnSuccessListener { document ->
                val list: ArrayList<Order> = ArrayList()

                for (i in document.documents){
                    val orderItem = i.toObject(Order::class.java)!!
                    orderItem.id = i.id

                    list.add(orderItem)
                }
                fragment.populateOrderListInUI(list)

            }
            .addOnFailureListener {
                fragment.hideProgressDialog()
            }
    }

    fun placeOrder(activity: CheckoutActivity, order: Order){
        mFirestore.collection(Constants.ORDERS)
            .document()
            .set(order, SetOptions.merge())
            .addOnSuccessListener {
                activity.orderPlacedSuccess()
            }
            .addOnFailureListener {
                activity.hideProgressDialog()
            }
    }

    fun deleteAddress(activity: AddressListActivity, addressId: String){
        mFirestore.collection(Constants.ADDRESSES)
            .document(addressId)
            .delete()
            .addOnSuccessListener {
                activity.deleteAddressSuccess()
            }
            .addOnFailureListener {
                activity.hideProgressDialog()
            }
    }

    fun updateAddress(activity: AddEditAddressActivity, addressInfo: Address, addressId: String){
        mFirestore.collection(Constants.ADDRESSES)
            .document(addressId)
            .set(addressInfo, SetOptions.merge())
            .addOnSuccessListener {
                activity.addUpdatedAddressSuccess()
            }
            .addOnFailureListener {
                activity.hideProgressDialog()
            }
    }

    fun getAddressesList(activity: AddressListActivity){
        mFirestore.collection(Constants.ADDRESSES)
            .whereEqualTo(Constants.USER_ID, getCurrentUserID())
            .get()
            .addOnSuccessListener { document ->
                val addressList: ArrayList<Address> = ArrayList()
                for (i in document.documents){
                    val address = i.toObject(Address::class.java)!!
                    address.id = i.id
                    addressList.add(address)
                }
                activity.successAddressListFromFirestore(addressList)
            }
            .addOnFailureListener {
                activity.hideProgressDialog()
            }
    }

    fun addAddress(activity: AddEditAddressActivity, addressInfo: Address){
        mFirestore.collection(Constants.ADDRESSES)
            .document()
            .set(addressInfo, SetOptions.merge())
            .addOnSuccessListener {
                activity.addUpdatedAddressSuccess()

            }
            .addOnFailureListener {
                activity.hideProgressDialog()
            }
    }

    fun updateMyCart(context: Context, cart_id: String, itemHashMap: HashMap<String, Any>){
        mFirestore.collection(Constants.CART_ITEMS)
            .document(cart_id)
            .update(itemHashMap)
            .addOnSuccessListener {
                when(context){
                    is CartListActivity -> {
                        context.itemUpdateSuccess()
                    }
                }
            }
            .addOnFailureListener {
                when(context){
                    is CartListActivity -> {
                        context.hideProgressDialog()
                    }
                }
            }
    }

    fun checkIfItemExistInCart(activity: ProductDetailsActivity,productID: String){
        mFirestore.collection(Constants.CART_ITEMS)
            .whereEqualTo(Constants.USER_ID, getCurrentUserID())
            .whereEqualTo(Constants.PRODUCT_ID,productID)
            .get()
            .addOnSuccessListener { document ->
                if (document.size() > 0){
                    activity.productExistsInCart()
                }else{
                    activity.hideProgressDialog()
                }
            }
            .addOnFailureListener {
                activity.hideProgressDialog()
            }
    }

    fun removeItemFromCart(context: Context, cart_id: String){
        mFirestore.collection(Constants.CART_ITEMS)
            .document(cart_id)
            .delete()
            .addOnSuccessListener {
                when(context){
                    is CartListActivity -> {
                        context.itemRemovedSuccess()
                    }
                }
            }
            .addOnFailureListener {
                when(context){
                    is CartListActivity -> {
                        context.hideProgressDialog()
                    }
                }
            }
    }

    fun getAllProductList(activity: Activity){
        mFirestore.collection(Constants.PRODUCTS)
            .get()
            .addOnSuccessListener { document ->
                val productList: ArrayList<Product> = ArrayList()
                for (i in document.documents){
                    val product = i.toObject(Product::class.java)
                    product!!.product_id = i.id

                    productList.add(product)
                }
                when(activity){
                    is CartListActivity -> {
                        activity.successProductsListFromFirestore(productList)
                    }
                    is CheckoutActivity -> {
                        activity.successProductListFromFirestore(productList)
                    }
                }
            }
            .addOnFailureListener {
                when(activity){
                    is CartListActivity -> {
                        activity.hideProgressDialog()
                    }
                    is CheckoutActivity -> {
                        activity.hideProgressDialog()
                    }
                }
            }
    }

    fun getDashboardItemsList(fragment: DashboardFragment){
        mFirestore.collection(Constants.PRODUCTS)
            .get()
            .addOnSuccessListener { document ->
                Log.e(fragment.javaClass.simpleName, document.documents.toString())
                val productsList: ArrayList<Product> = ArrayList()

                for (i in document.documents){
                    val product = i.toObject(Product::class.java)!!
                    product.product_id = i.id
                    productsList.add(product)
                }
                fragment.successDashboardItemsList(productsList)

            }
            .addOnFailureListener { e ->
                fragment.hideProgressDialog()
                Log.e(fragment.javaClass.simpleName,"Error while getting dashboard item list",e)
            }
    }

}