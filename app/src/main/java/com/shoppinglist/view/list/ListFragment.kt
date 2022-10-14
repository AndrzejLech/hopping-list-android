package com.shoppinglist.view.list

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import com.shoppinglist.R
import com.shoppinglist.adapters.CategoriesAdapter
import com.shoppinglist.databinding.FragmentListBinding
import com.shoppinglist.view.MainActivity
import com.shoppinglist.view.list.viewmodel.ListViewModel
import com.shoppinglist.view.list.viewmodel.ListViewState
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

@AndroidEntryPoint
class ListFragment(
) : Fragment() {
    private val TAG = "ListFragment"

    private val viewModel by viewModels<ListViewModel>()
    private var _binding: FragmentListBinding? = null
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentListBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val categoriesAdapter = CategoriesAdapter(viewModel, this)
        val layoutManager = LinearLayoutManager(activity as MainActivity)

        viewModel.categories.observe(viewLifecycleOwner) { categories ->
            CoroutineScope(Dispatchers.Main).launch {
                categories.collect { list ->
                    Log.i(TAG, "Categories size:" + list.size.toString())
                    categoriesAdapter.submitList(list)
                }
            }
        }

        viewModel.error.observe(viewLifecycleOwner) { listViewState ->
            when (listViewState) {
                is ListViewState.EmptyList -> {
                }
                is ListViewState.UnexpectedError -> {
                    Toast.makeText(
                        requireContext(),
                        "Unexpected Error occurred",
                        Toast.LENGTH_SHORT
                    ).show()
                }
                is ListViewState.FailedToUpdate -> {
                    Toast.makeText(requireContext(), "Failed to update Entity", Toast.LENGTH_SHORT)
                        .show()
                }
                is ListViewState.Items -> {}
                else -> {}
            }
        }

        binding.categoryRecycler.layoutManager = layoutManager
        binding.categoryRecycler.adapter = categoriesAdapter

        binding.moveToCreateSection.setOnClickListener {
            findNavController().navigate(R.id.action_ListFragment_to_CreateSectionFragment)
        }

        if (savedInstanceState == null) {
            Log.i(TAG, "savedInstanceState is null, calling getCategories()")
            viewModel.getCategories()
        }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

