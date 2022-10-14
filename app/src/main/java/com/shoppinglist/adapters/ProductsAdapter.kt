package com.shoppinglist.adapters

import android.content.Context
import android.graphics.Paint
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.shoppinglist.databinding.ItemProductBinding
import com.shoppinglist.models.Product
import com.shoppinglist.view.list.ListFragment
import com.shoppinglist.view.list.viewmodel.ListViewModel

class ProductsAdapter(
    val fragment: ListFragment,
    val viewModel: ListViewModel
) : ListAdapter<Product, ProductsAdapter.ViewHolder>(ProductDiffUtil()) {

    var setOnClick: (Product) -> Unit = {}

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(fragment.requireContext(), item, setOnClick)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val binding = ItemProductBinding.inflate(layoutInflater, parent, false)

        return ViewHolder(binding)
    }

    class ViewHolder constructor(private val binding: ItemProductBinding) :
        RecyclerView.ViewHolder(binding.root) {
        private val defaultColor = binding.productName.textColors
        private val productName = binding.productName

        fun bind(
            context: Context,
            product: Product,
            onCLick: (Product) -> Unit
        ) {
            binding.productName.text = product.name
            binding.productName.isChecked = product.inCart

            changeState(context)

            productName.setOnClickListener {
                onCLick.invoke(product)
                binding.productName.isChecked = product.inCart
                changeState(context)
            }
        }

        private fun changeState(context: Context){
            if (productName.isChecked) {
                productName.paintFlags = (binding.productName.paintFlags or Paint.STRIKE_THRU_TEXT_FLAG)
                productName.setTextColor(context.getColor(android.R.color.darker_gray))
            } else {
                productName.paintFlags = (binding.productName.paintFlags and Paint.STRIKE_THRU_TEXT_FLAG.inv())
                productName.setTextColor(defaultColor.defaultColor)
            }
        }
    }

    fun getProductAt(position: Int): Product {
        return this.getItem(position)
    }

    class ProductDiffUtil : DiffUtil.ItemCallback<Product>() {
        override fun areItemsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.id == newItem.id && oldItem.inCart == newItem.inCart
        }

        override fun areContentsTheSame(oldItem: Product, newItem: Product): Boolean {
            return oldItem.name == newItem.name && oldItem.inCart == newItem.inCart
        }
    }
}
