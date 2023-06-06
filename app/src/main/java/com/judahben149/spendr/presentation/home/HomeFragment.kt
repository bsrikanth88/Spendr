package com.judahben149.spendr.presentation.home

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.viewModels
import androidx.navigation.fragment.findNavController
import com.judahben149.spendr.R
import com.judahben149.spendr.databinding.FragmentHomeBinding
import com.judahben149.spendr.utils.DateUtils
import com.judahben149.spendr.utils.extensions.abbreviateNumber
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class HomeFragment : Fragment() {

    private var _binding: FragmentHomeBinding? = null
    val binding get() = _binding!!

    val navController by lazy {
        findNavController()
    }
    private val viewModel: HomeViewModel by viewModels()

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        viewModel.getBalance()

        binding.tvDate.text = DateUtils.getCurrentFriendlyDate()

        viewModel.state.observe(viewLifecycleOwner) { state ->
            binding.tvAmountBalance.text =
                state.inflowBalance.minus(state.outflowBalance).abbreviateNumber(requireContext())

            binding.tvAmountBalance.setTextColor(
                if (state.inflowBalance.minus(state.outflowBalance) < 0.0) resources.getColor(
                    R.color.syracuse_red_orange
                ) else resources.getColor(R.color.pigment_green)
            )
        }

        binding.itemSummaryNeumorphicCard.setOnClickListener {
            navController.navigate(R.id.action_homeFragment_to_cashFlowSummaryFragment)
        }
        binding.cardTransactions.setOnClickListener {
            navController.navigate(R.id.action_homeFragment_to_entryListParentFragment)
        }
        binding.cardBudget.setOnClickListener {
            navController.navigate(R.id.action_homeFragment_to_budgetFragment)
        }
        binding.cardAddEntry.setOnClickListener {
            navController.navigate(R.id.action_homeFragment_to_addCashEntryFragment)
        }
        binding.cardExtras.setOnClickListener {
            navController.navigate(R.id.action_homeFragment_to_extrasFragment)
        }
        binding.cardSettings.setOnClickListener {
            navController.navigate(R.id.action_homeFragment_to_settingsFragment)
        }
    }
}