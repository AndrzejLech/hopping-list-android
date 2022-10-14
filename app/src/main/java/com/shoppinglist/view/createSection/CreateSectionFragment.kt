package com.shoppinglist.view.createSection

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.AdapterView
import android.widget.ArrayAdapter
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.shoppinglist.databinding.FragmentCreateSectionBinding
import com.shoppinglist.models.Category
import com.shoppinglist.models.Product
import com.shoppinglist.view.createSection.viewmodel.CreateSectionViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class CreateSectionFragment : Fragment() {
    private val TAG = "CreateSectionFragment"

    private val viewModel by viewModels<CreateSectionViewModel>()
    private var _binding: FragmentCreateSectionBinding? = null
    private val binding get() = _binding!!
    var currentCategories: List<Category> = emptyList()

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCreateSectionBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel.error.observe(viewLifecycleOwner) { viewState ->
            when (viewState) {
                is CreateSectionViewState.IsBlank -> {
                    Toast.makeText(
                        requireContext(),
                        "Entity name is empty!",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                is CreateSectionViewState.ToShortName -> {
                    Toast.makeText(
                        requireContext(),
                        "Entity name must be longer than 3 characters",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                is CreateSectionViewState.ResultSuccess -> {
                    Toast.makeText(
                        requireContext(),
                        "Entity created successfully",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                is CreateSectionViewState.NotUnique -> {
                    Toast.makeText(
                        requireContext(),
                        "Entity name must be unique",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                is CreateSectionViewState.UnexpectedError -> {
                    Toast.makeText(
                        requireContext(),
                        "Unexpected Error",
                        Toast.LENGTH_SHORT
                    ).show()
                }

                else -> {}
            }
        }

        binding.createCategoryButton.setOnClickListener {
            val categoryName = binding.categoryNameEditText
            viewModel.insertCategory(categoryName.text.toString(), currentCategories)
            categoryName.setText("")
        }

        val categorySpinner = binding.categoryProductSpinner
        val arrayAdapter = ArrayAdapter<String>(
            requireContext(),
            android.R.layout.simple_spinner_item
        )
        arrayAdapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        categorySpinner.adapter = arrayAdapter


        viewModel.categories.observe(viewLifecycleOwner) { categories ->
            CoroutineScope(Dispatchers.Main).launch {
                categories.collect { list ->
                    val nameList = mutableListOf<String>()
                    list.forEach {
                        nameList.add(it.name)
                    }
                    arrayAdapter.clear()
                    arrayAdapter.addAll(nameList)
                    arrayAdapter.notifyDataSetChanged()
                    currentCategories = list
                }
            }
        }

        val currentCategory = Category()

        categorySpinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                val textView = parent?.findViewById<TextView>(android.R.id.text1)
                currentCategory.name = textView?.text.toString()

                currentCategories.forEach { category ->
                    if (category.name == currentCategory.name) {
                        currentCategory.id = category.id
                    }
                }
                Log.i(TAG, "current category id: ${currentCategory.id}")
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {
            }
        }

        val productName = binding.productNameEditText
        var currentProducts = emptyList<Product>()
        binding.createProductButton.setOnClickListener {
            viewModel.products.observe(viewLifecycleOwner) { products ->
                CoroutineScope(Dispatchers.Main).launch {
                    products.collect { list ->
                        currentProducts = list
                    }
                }
            }
            viewModel.insertProduct(productName.text.toString(), currentCategory, currentProducts)
            productName.setText("")
            Log.i(TAG, "products size: ${currentProducts.size}")
        }

        if (savedInstanceState == null) {
            viewModel.getCategories()
            viewModel.getProducts(currentCategory.id)
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}