package com.example.currencyconverter.presentation.converter

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import androidx.activity.OnBackPressedCallback
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.currencyconverter.R
import com.example.currencyconverter.databinding.FragmentCurrenciesBinding
import com.example.currencyconverter.presentation.currencyselector.CurrencySelectorBottomSheet
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class ConverterFragment : Fragment() {

    private var _binding: FragmentCurrenciesBinding? = null
    private val binding get() = _binding!!

    private val model: CurrencyViewModel by sharedViewModel()
    private lateinit var adapter: CurrencyListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        adapter = CurrencyListAdapter(model)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCurrenciesBinding.inflate(inflater, container, false)

        setupObservers()
        setupRecyclerView()
        setupOnBackButtonPresses()

        binding.etCurrencyValue.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(value: Editable?) {
                if (value == null || value.isEmpty()) return
                model.setBalance(value.toString().toDouble())
            }
        })

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
        model.setItemSelected(false)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        model.isItemSelected.observe(viewLifecycleOwner) {
            val menuLayoutId = if (it) {
                R.menu.fragment_currencies_currency_selected
            } else {
                R.menu.fragment_currencies_normal
            }
            menu.clear()
            inflater.inflate(menuLayoutId, menu)
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_add_currency -> addCurrency()
            R.id.menu_alphabet -> model.setSortingType(CurrencyViewModel.SortType.ALPHABET)
            R.id.menu_value -> model.setSortingType(CurrencyViewModel.SortType.VALUE)
            R.id.menu_reset -> model.setSortingType(CurrencyViewModel.SortType.UNSORTED)
            R.id.menu_delete_item -> showDeleteConfirmationDialog(parentFragmentManager)
            else -> return super.onOptionsItemSelected(item)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setupRecyclerView() {
        adapter.submitList(model.currencies.value)

        val callback = CurrenciesItemTouchHelperCallback(adapter)
        val touchHelper = ItemTouchHelper(callback)

        binding.currenciesListRecyclerView.adapter = adapter
        binding.currenciesListRecyclerView.layoutManager =
            LinearLayoutManager(context, LinearLayoutManager.VERTICAL, false)

        touchHelper.attachToRecyclerView(binding.currenciesListRecyclerView)
    }

    private fun setupObservers() {
        model.currencies.observe(viewLifecycleOwner) {
            adapter.submitList(model.getCurrenciesSorted(it))
        }
        model.sortingType.observe(viewLifecycleOwner) { sortingType ->
            adapter.submitList(model.currencies.value?.let { it -> model.getCurrenciesSorted(it) })
        }
    }

    private fun setupOnBackButtonPresses() {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (model.isItemSelected.value == true) {
                    model.setItemSelected(false)
                    model.clearCheckCurrencies()
                } else {
                    activity?.finish()
                }
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }

    private fun addCurrency() {
        CurrencySelectorBottomSheet().show(childFragmentManager, CurrencySelectorBottomSheet.TAG)
    }

    private fun showDeleteConfirmationDialog(fragmentManager: FragmentManager) {
        val dialog = DeleteConfirmationDialogFragment(model::deleteCurrencies)
        dialog.show(fragmentManager, DeleteConfirmationDialogFragment.TAG)
    }
}