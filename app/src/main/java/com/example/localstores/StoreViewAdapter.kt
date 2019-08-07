package com.example.localstores

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.google.android.material.snackbar.Snackbar

class StoreViewAdapter(val userList: ArrayList<StoreInfo>) : RecyclerView.Adapter<StoreViewAdapter.ViewHolder>() {

    //this method is returning the view for each item in the list
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoreViewAdapter.ViewHolder {
        val v = LayoutInflater.from(parent.context).inflate(R.layout.item_store_list, parent, false)
        return ViewHolder(v)
    }

    //this method is binding the data on the list
    override fun onBindViewHolder(holder: StoreViewAdapter.ViewHolder, position: Int) =
        holder.bindItems(userList[position])

    //this method is giving the size of the list
    override fun getItemCount(): Int {
        return userList.size
    }

    //the class is hodling the list view
    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {


        init {

            itemView?.setOnClickListener {
                Snackbar.make(it, "hghh", Snackbar.LENGTH_LONG).show()
            }
        }

        fun bindItems(user: StoreInfo) {

            val textStoreName = itemView?.findViewById<TextView?>(R.id.textStoreName)
            textStoreName?.text = user?.storeName

            val textStoreAddress = itemView?.findViewById<TextView?>(R.id.textStoreAddress)
            textStoreAddress?.text = user?.address

            val imageView = itemView?.findViewById<ImageView>(R.id.storeImageView)
            Glide.with(itemView.context).load(user?.imageUrl).into(imageView)

            val intent = Intent(itemView.context, ProductListActivity::class.java)
            intent.putExtra("key", user.storeName)
            itemView.setOnClickListener {
                itemView.context.startActivity(intent)
            }
        }
    }
}