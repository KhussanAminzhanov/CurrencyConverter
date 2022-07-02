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
import com.example.currencyconverter.data.database.Currency
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
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCurrenciesBinding.inflate(inflater, container, false)

        setupObservers()
        setupRecyclerViewAdapter()
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

    private fun setupRecyclerViewAdapter() {
        adapter = CurrencyListAdapter(
            model.balance,
            model.isItemSelected,
            ::onAdapterItemMove,
            ::onAdapterItemDismiss,
            ::onAdapterItemLongClick,
            ::onAdapterItemCheck,
            ::onAdapterItemUncheck,
            ::isAdapterItemChecked
        )
    }

    private fun onAdapterItemDismiss(currency: Currency) = model.deleteCurrency(currency)
    private fun onAdapterItemMove(from: Int, to: Int) = model.moveCurrencies(from, to)
    private fun onAdapterItemLongClick() = model.setItemSelected(true)
    private fun onAdapterItemCheck(currency: Currency) = model.addCheckedCurrency(currency)
    private fun onAdapterItemUncheck(currency: Currency) = model.removeCheckedCurrency(currency)
    private fun isAdapterItemChecked(currency: Currency) = model.containsCheckedCurrency(currency)

    private fun setupRecyclerView() {
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
        model.sortingType.observe(viewLifecycleOwner) {
            val currencies = model.currencies.value ?: return@observe
            adapter.submitList(currencies)
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