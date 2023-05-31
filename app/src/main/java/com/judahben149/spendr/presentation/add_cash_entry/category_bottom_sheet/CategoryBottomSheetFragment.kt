package com.judahben149.spendr.presentation.add_cash_entry.category_bottom_sheet

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.bottomsheet.BottomSheetDialogFragment
import com.judahben149.spendr.R
import com.judahben149.spendr.databinding.FragmentCategoryBottomSheetBinding
import com.judahben149.spendr.presentation.add_cash_entry.AddCashEntryViewModel
import com.judahben149.spendr.presentation.add_cash_entry.category_bottom_sheet.adapter.CategoryAdapter
import com.judahben149.spendr.utils.Constants
import timber.log.Timber

class CategoryBottomSheetFragment : Fragment() {

    private var _binding: FragmentCategoryBottomSheetBinding? = null
    val binding get() = _binding!!

    private val viewModel: CategoryViewModel by activityViewModels()
    private val addCashEntryViewModel: AddCashEntryViewModel by activityViewModels()
    private lateinit var adapter: CategoryAdapter
    private lateinit var recyclerView: RecyclerView

    private val navController by lazy {
        findNavController()
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentCategoryBottomSheetBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.setCategorySelected(isIncomeSelected = false)
        setupRecyclerView()
        viewModel.getCategories()

        viewModel.categoryState.observe(viewLifecycleOwner) { categoryState ->

            if (categoryState.isIncomeSelected) {
                val categoryList = viewModel.categoryState.value!!.categoryList.filter { category ->
                    category.isIncomeCategory
                }

                adapter.submitList(categoryList)
                Timber.tag(Constants.TIMBER_TAG).d("Just submitted adapter list for income")
                Timber.tag(Constants.TIMBER_TAG).d("List is $categoryList")
            } else {
                val categoryList = viewModel.categoryState.value!!.categoryList.filter { category ->
                    !category.isIncomeCategory
                }

                adapter.submitList(categoryList)
                Timber.tag(Constants.TIMBER_TAG).d("Just submitted adapter list")
            }
        }

        binding.itemAddNewCategory.setOnClickListener {
            navController.navigate(R.id.action_categoryBottomSheetFragment_to_newCategoryBottomSheetFragment)
        }
        binding.tvDone.setOnClickListener {
            navController.popBackStack()
        }
    }

    private fun setupRecyclerView() {
        recyclerView = binding.rvCategoryBottomSheet
        recyclerView.isNestedScrollingEnabled = false

        adapter = CategoryAdapter() {  selectedId ->
            addCashEntryViewModel.updateSelectedCategoryId(selectedId)
        }

        recyclerView.adapter = adapter
        recyclerView.layoutManager = GridLayoutManager(
            requireContext(),
            4,
            LinearLayoutManager.VERTICAL,
            false
        )
    }
}