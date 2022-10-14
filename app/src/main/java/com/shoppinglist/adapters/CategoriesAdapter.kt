package com.shoppinglist.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.shoppinglist.databinding.ItemCategoryBinding
import com.shoppinglist.models.Category
import com.shoppinglist.models.Product
import com.shoppinglist.view.deleteDialog.DeleteDialogFragment
import com.shoppinglist.view.list.ListFragment
import com.shoppinglist.view.list.viewmodel.ListViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.launch

class CategoriesAdapter(
    private val viewModel: ListViewModel,
    private val fragment: ListFragment
) : ListAdapter<Category, CategoriesAdapter.ViewHolder>(CategoryDiffUtil()) {

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, position)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int,
    ): ViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val layoutManager = LinearLayoutManager(parent.context)
        val binding = ItemCategoryBinding.inflate(layoutInflater, parent, false)

        return ViewHolder(binding, layoutManager, viewModel, fragment)
    }

    class ViewHolder constructor(
        private val binding: ItemCategoryBinding,
        private val layoutManager: LinearLayoutManager,
        private val viewModel: ListViewModel,
        private val fragment: ListFragment,
    ) : RecyclerView.ViewHolder(binding.root) {
        private val _products: MutableLiveData<Flow<List<Product>>> = MutableLiveData()
        private val products: LiveData<Flow<List<Product>>> = _products

        private fun getProducts(categoryId: Int) {
            CoroutineScope(Dispatchers.Main).launch {
                viewModel.shoppingListRepository.getProducts(categoryId).collect {
                    _products.value = viewModel.shoppingListRepository.getProducts(categoryId)
                }
            }
        }

        fun bind(category: Category, position: Int) {
            binding.categoryName.text = category.name
            val productRecycler = binding.productRecycler
            getProducts(category.id)

            val adapter = ProductsAdapter(fragment, viewModel)
            binding.productRecycler.layoutManager = layoutManager

            ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
                0,
                ItemTouchHelper.LEFT
            ) {
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return false
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    adapter.notifyItemChanged(viewHolder.layoutPosition)
                    val swipedProduct = adapter.getProductAt(viewHolder.layoutPosition)
                    viewModel.deleteProduct(swipedProduct)
                    adapter.notifyItemChanged(viewHolder.layoutPosition)
                }
            }).attachToRecyclerView(binding.productRecycler)

            adapter.setOnClick = {
                it.inCart = !it.inCart
                viewModel.updateProduct(it)
            }

            ItemTouchHelper(object : ItemTouchHelper.SimpleCallback(
                0,
                ItemTouchHelper.RIGHT
            ){
                override fun onMove(
                    recyclerView: RecyclerView,
                    viewHolder: RecyclerView.ViewHolder,
                    target: RecyclerView.ViewHolder
                ): Boolean {
                    return false
                }

                override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                    val dialog = DeleteDialogFragment(adapter.getProductAt(viewHolder.layoutPosition))
                    dialog.show(fragment.requireActivity().supportFragmentManager, "Delete Dialog opened")
                    adapter.notifyItemChanged(viewHolder.layoutPosition)
                }
            }
            ).attachToRecyclerView(productRecycler)

            binding.productRecycler.adapter = adapter
            products.observe(fragment.viewLifecycleOwner) { products ->
                CoroutineScope(Dispatchers.Main).launch {
                    products.collect { list ->
                        adapter.submitList(list)
                    }
                }
            }
        }
    }
}

class CategoryDiffUtil : DiffUtil.ItemCallback<Category>() {
    override fun areItemsTheSame(oldItem: Category, newItem: Category): Boolean {
        return oldItem.id == newItem.id
    }

    override fun areContentsTheSame(oldItem: Category, newItem: Category): Boolean {
        return oldItem.name == newItem.name
    }

}
