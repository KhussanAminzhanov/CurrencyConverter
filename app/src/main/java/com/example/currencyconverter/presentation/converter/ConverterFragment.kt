package com.example.currencyconverter.presentation.converter

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import androidx.activity.OnBackPressedCallback
import androidx.core.view.MenuHost
import androidx.core.view.MenuProvider
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.recyclerview.widget.ItemTouchHelper
import com.example.currencyconverter.R
import com.example.currencyconverter.data.database.Currency
import com.example.currencyconverter.databinding.FragmentCurrenciesBinding
import com.example.currencyconverter.presentation.currencyselector.CurrencySelectorBottomSheet
import com.example.currencyconverter.presentation.history.HistoryViewModel
import org.koin.androidx.viewmodel.ext.android.sharedViewModel

class ConverterFragment : Fragment() {

    private var _binding: FragmentCurrenciesBinding? = null
    private val binding get() = _binding!!

    private val currencyViewModel: CurrencyViewModel by sharedViewModel()
    private val historyViewModel: HistoryViewModel by sharedViewModel()
    private lateinit var adapter: CurrencyListAdapter
    private lateinit var menuHost: MenuHost
    private lateinit var menuProvider: MenuProvider

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCurrenciesBinding.inflate(inflater, container, false)

        setupLayout()
        setupObservers()
        setupRecyclerViewAdapter()
        setupRecyclerView()
        setupOnBackButtonPres()

        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupMenuOptions()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        menuHost.removeMenuProvider(menuProvider)
        currencyViewModel.setItemSelected(false)
        _binding = null
    }

    private fun setupLayout() {
        binding.etCurrencyValue.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}
            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}
            override fun afterTextChanged(value: Editable?) {
                if (value == null || value.isEmpty()) return
                currencyViewModel.setBalance(value.toString().toDouble())
            }
        })
    }

    private fun setupObservers() {
        currencyViewModel.currencies.observe(viewLifecycleOwner) {
            adapter.submitList(currencyViewModel.getCurrenciesSorted(it))
        }
        currencyViewModel.sortingType.observe(viewLifecycleOwner) {
            val currencies = currencyViewModel.currencies.value ?: return@observe
            adapter.submitList(currencies)
        }
    }

    private fun setupRecyclerViewAdapter() {
        adapter = CurrencyListAdapter(
            currencyViewModel.balance,
            currencyViewModel.isItemSelected,
            ::onAdapterItemMove,
            ::onAdapterItemDismiss,
            ::onAdapterItemLongClick,
            ::onAdapterItemCheck,
            ::onAdapterItemUncheck,
            ::isAdapterItemChecked
        )
    }

    private fun onAdapterItemDismiss(currency: Currency) = currencyViewModel.deleteCurrency(currency)
    private fun onAdapterItemMove(from: Int, to: Int) = currencyViewModel.moveCurrencies(from, to)
    private fun onAdapterItemLongClick() = currencyViewModel.setItemSelected(true)
    private fun onAdapterItemCheck(currency: Currency) = currencyViewModel.addCheckedCurrency(currency)
    private fun onAdapterItemUncheck(currency: Currency) = currencyViewModel.removeCheckedCurrency(currency)
    private fun isAdapterItemChecked(currency: Currency) = currencyViewModel.containsCheckedCurrency(currency)

    private fun setupRecyclerView() {
        val callback = CustomItemTouchHelperCallback(adapter)
        val touchHelper = ItemTouchHelper(callback)
        binding.currenciesListRecyclerView.adapter = adapter
        touchHelper.attachToRecyclerView(binding.currenciesListRecyclerView)
    }

    private fun setupOnBackButtonPres() {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (currencyViewModel.isItemSelected.value == true) {
                    currencyViewModel.setItemSelected(false)
                    currencyViewModel.clearCheckCurrencies()
                } else activity?.finish()
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }

    private fun setupMenuOptions() {
        menuHost = requireActivity()
        menuProvider = getMenuProvider()
        menuHost.addMenuProvider(menuProvider)
    }

    private fun getMenuProvider() = object : MenuProvider {
        override fun onCreateMenu(menu: Menu, menuInflater: MenuInflater) {
            currencyViewModel.isItemSelected.observe(viewLifecycleOwner) {
                val menuLayoutId = if (it) {
                    R.menu.menu_fragment_currencies_currency_selected
                } else {
                    R.menu.menu_fragment_currencies_normal
                }
                menu.clear()
                menuInflater.inflate(menuLayoutId, menu)
            }
        }

        override fun onMenuItemSelected(menuItem: MenuItem): Boolean {
            when (menuItem.itemId) {
                R.id.menu_add_currency -> showCurrencySelectorBottomSheet()
                R.id.menu_alphabet -> currencyViewModel.setSortingType(CurrencyViewModel.SortType.ALPHABET)
                R.id.menu_value -> currencyViewModel.setSortingType(CurrencyViewModel.SortType.VALUE)
                R.id.menu_reset -> currencyViewModel.setSortingType(CurrencyViewModel.SortType.UNSORTED)
                R.id.menu_delete_item -> showDeleteConfirmationDialog(parentFragmentManager)
            }
            return true
        }
    }

    private fun showCurrencySelectorBottomSheet() {
        CurrencySelectorBottomSheet().show(childFragmentManager, CurrencySelectorBottomSheet.TAG)
    }

    private fun showDeleteConfirmationDialog(fragmentManager: FragmentManager) {
        val dialog = DeleteConfirmationDialogFragment(currencyViewModel::deleteCurrencies)
        dialog.show(fragmentManager, DeleteConfirmationDialogFragment.TAG)
    }
}