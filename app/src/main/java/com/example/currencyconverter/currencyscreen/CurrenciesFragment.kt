package com.example.currencyconverter.currencyscreen

import android.os.Bundle
import android.view.*
import androidx.activity.OnBackPressedCallback
import androidx.appcompat.widget.LinearLayoutCompat
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.currencyconverter.MainActivity
import com.example.currencyconverter.R
import com.example.currencyconverter.currencyscreen.currencyselector.CurrencySelectorBottomSheet
import com.example.currencyconverter.databinding.BottomSheetCurrencySelectorBinding
import com.example.currencyconverter.databinding.FragmentCurrenciesBinding
import com.google.android.material.bottomsheet.BottomSheetBehavior

class CurrenciesFragment : Fragment() {

    private var _binding: FragmentCurrenciesBinding? = null
    private val binding get() = _binding!!

    private val viewModel by lazy { ViewModelProvider(this)[CurrenciesViewModel::class.java] }
    private val adapter by lazy { CurrenciesAdapter(viewModel, activity as LifecycleOwner) }
    private val toolbar by lazy { (activity as MainActivity).toolbar }
    private val bottomNav by lazy { (activity as MainActivity).bottomNav }

    private lateinit var bottomSheetBehavior: BottomSheetBehavior<LinearLayoutCompat>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCurrenciesBinding.inflate(inflater, container, false)

        setupRecyclerView()
        setupOnBackButtonPresses()

        viewModel.currencies.observe(viewLifecycleOwner) { newValue ->
            adapter.submitList(newValue)
        }

        viewModel.isItemSelected.observe(viewLifecycleOwner) { isItemSelected ->
            binding.addCurrencyButton.visibility = if (isItemSelected) View.GONE else View.VISIBLE
        }

        binding.addCurrencyButton.setOnClickListener {
            CurrencySelectorBottomSheet().show(parentFragmentManager, CurrencySelectorBottomSheet.TAG)

//            IMPLEMENTATION USING STANDARD BOTTOM SHEET
//            setBottomSheetVisibility(true)

//            PREVIOUS VERSION OF ADDING CURRENCY
            addCurrency(viewModel.randomCurrency())
        }

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        viewModel.isItemSelected.observe(viewLifecycleOwner) {
            val menuLayoutId = if (it) {
                changeLayout(R.color.hint, R.string.currencies_list_item_selected, View.GONE)
                R.menu.fragment_currencies_currency_selected
            } else {
                changeLayout(R.color.primaryColor, R.string.currency, View.VISIBLE)
                R.menu.fragment_currencies_normal
            }
            menu.clear()
            inflater.inflate(menuLayoutId, menu)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_alphabet -> viewModel.setSortingType(CurrenciesViewModel.SortType.ALPHABET)
            R.id.menu_value -> viewModel.setSortingType(CurrenciesViewModel.SortType.VALUE)
            R.id.menu_reset -> viewModel.setSortingType(CurrenciesViewModel.SortType.UNSORTED)
            R.id.menu_delete_item -> adapter.showDeleteConfirmationDialog(parentFragmentManager)
            else -> return super.onOptionsItemSelected(item)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupRecyclerView() {
        adapter.submitList(viewModel.currencies.value)

        val callback = CurrenciesItemTouchHelperCallback(adapter)
        val touchHelper = ItemTouchHelper(callback)

        binding.currenciesListRecyclerView.adapter = adapter
        binding.currenciesListRecyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        touchHelper.attachToRecyclerView(binding.currenciesListRecyclerView)
    }

    private fun setupOnBackButtonPresses() {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (viewModel.isItemSelected.value!!) {
                    viewModel.setItemSelected(false)
                    adapter.checkedCurrencyPositions.clear()
                } else {
                    activity?.finish()
                }
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }

    private fun addCurrency(currency: CurrencyItem) {
        viewModel.addCurrency(currency)
        adapter.notifyItemInserted(adapter.itemCount - 1)
        binding.currenciesListRecyclerView.layoutManager?.scrollToPosition(adapter.currentList.size - 1)
    }

    private fun changeLayout(colorId: Int, titleId: Int, bottomNavVisibility: Int) {
        toolbar.setBackgroundColor(ContextCompat.getColor(requireContext(), colorId))
        toolbar.title = getString(titleId)
        bottomNav.visibility = bottomNavVisibility
    }
}