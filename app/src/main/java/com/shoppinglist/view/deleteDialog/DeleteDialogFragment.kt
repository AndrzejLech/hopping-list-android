package com.shoppinglist.view.deleteDialog

import android.app.AlertDialog
import android.app.Dialog
import android.os.Bundle
import androidx.fragment.app.DialogFragment
import androidx.fragment.app.viewModels
import com.shoppinglist.R
import com.shoppinglist.models.Product
import com.shoppinglist.view.deleteDialog.viewmodel.DeleteDialogViewModel
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class DeleteDialogFragment(
    val product: Product,
) : DialogFragment() {
    private val viewModel by viewModels<DeleteDialogViewModel>()
    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        val builder: AlertDialog.Builder = AlertDialog.Builder(requireContext())

        builder.setMessage(getString(R.string.delete_dialog_message, product.name))
            .setPositiveButton(
                R.string.delete_dialog_positive_button
            ) { dialog, _ ->
                viewModel.delete(product)
                dialog.dismiss()
            }
            .setNegativeButton(
                R.string.delete_dialog_negative_button
            ) { dialog, _ ->
                dialog.dismiss()
            }

        return builder.create()
    }
}