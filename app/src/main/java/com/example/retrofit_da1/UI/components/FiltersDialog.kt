package com.example.retrofit_da1.UI.components

import android.R
import android.app.Dialog
import android.content.DialogInterface
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.widget.AdapterView
import android.widget.ArrayAdapter
import androidx.appcompat.app.AlertDialog
import androidx.fragment.app.DialogFragment
import com.example.retrofit_da1.Model.CategorySingle
import com.example.retrofit_da1.UI.Main.MainActivity
import com.example.retrofit_da1.databinding.FiltersProductsBinding

class FiltersDialog (
    private val categories: List<CategorySingle>,
    private val precioMinimo: (String) -> Unit,
    private val precioMaximo: (String) -> Unit,
    private val categoriaSeleccionada: (Int) -> Unit
): DialogFragment() {

    private lateinit var binding : FiltersProductsBinding
    private lateinit var categoryMap: Map<String, Int>

    override fun onCreateDialog(savedInstanceState: Bundle?): Dialog {
        super.onCreate(savedInstanceState)
        binding = FiltersProductsBinding.inflate(LayoutInflater.from(context))



        val builder = AlertDialog.Builder(requireActivity())
        builder.setView(binding.root)

        setupSpinner()

        binding.btnAplicar.setOnClickListener{
            precioMinimo.invoke(binding.etMinimo.text.toString())
            precioMaximo.invoke(binding.etMaximo.text.toString())
            val selectedCategory = binding.etPrimero.selectedItem.toString()
            val selectedCategoryId = categoryMap[selectedCategory]
            selectedCategoryId?.let { id ->
                categoriaSeleccionada.invoke(id)
            }
            dismiss()
        }

        binding.btnCancelar.setOnClickListener{
            dismiss()
        }

        val dialog = builder.create()
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        return dialog
    }

    override fun onDismiss(dialog: DialogInterface) {
        super.onDismiss(dialog)
        (activity as? MainActivity)?.onDialogDismissed()
    }

    private fun setupSpinner() {
        categoryMap = categories.associate { it.name to it.id }
        val categoryNames = mutableListOf("Seleccione una categoría")
        categoryNames.addAll(categoryMap.keys)
        val adapter = ArrayAdapter(
            requireContext(),
            R.layout.simple_spinner_item,
            categoryNames
        )
        adapter.setDropDownViewResource(R.layout.simple_spinner_dropdown_item)
        binding.etPrimero.adapter = adapter
        binding.etPrimero.setSelection(0)
    }

}