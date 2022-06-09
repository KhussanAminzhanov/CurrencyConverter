package com.example.currencyconverter.currencyscreen

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.currencyconverter.MainActivity
import com.example.currencyconverter.R
import com.example.currencyconverter.currencyscreen.currencyselector.CurrencySelectorBottomSheet
import com.example.currencyconverter.database.CurrencyDatabase
import com.example.currencyconverter.databinding.FragmentCurrenciesBinding

class CurrencyFragment : Fragment() {

    private var _binding: FragmentCurrenciesBinding? = null
    private val binding get() = _binding!!
    private val adapter by lazy { CurrenciesAdapter(model, activity as LifecycleOwner) }
    private val toolbar by lazy { (activity as MainActivity).toolbar }
    private val bottomNav by lazy { (activity as MainActivity).bottomNav }
    private val application by lazy { requireNotNull(this.activity).application }
    private val dao by lazy { CurrencyDatabase.getInstance(application).currencyDao }
    private val viewModelFactory by lazy { CurrencyViewModelFactory(dao) }
    private val model by lazy {
        ViewModelProvider(
            this,
            viewModelFactory
        )[CurrencyViewModel::class.java]
    }

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
        setupRecyclerView()
        setupOnBackButtonPresses()

        binding.etCurrencyValue.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(value: Editable?) {
                if (value == null || value.isEmpty()) return
                model.balance.value = value.toString().toDouble()
            }
        })

        return binding.root
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        model.isItemSelected.observe(viewLifecycleOwner) {
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
            R.id.menu_add_currency -> addCurrency()
            R.id.menu_alphabet -> model.setSortingType(CurrencyViewModel.SortType.ALPHABET)
            R.id.menu_value -> model.setSortingType(CurrencyViewModel.SortType.VALUE)
            R.id.menu_reset -> model.setSortingType(CurrencyViewModel.SortType.UNSORTED)
            R.id.menu_delete_item -> model.showDeleteConfirmationDialog(parentFragmentManager)
            else -> return super.onOptionsItemSelected(item)
        }
        return super.onOptionsItemSelected(item)
    }

    private fun addCurrency() {
        CurrencySelectorBottomSheet(model).show(
            childFragmentManager,
            CurrencySelectorBottomSheet.TAG
        )
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
        model.sortingType.observe(viewLifecycleOwner) {
            adapter.submitList(model.currencies.value?.let { it1 -> model.getCurrenciesSorted(it1) })
        }
    }

    private fun setupOnBackButtonPresses() {
        val callback = object : OnBackPressedCallback(true) {
            override fun handleOnBackPressed() {
                if (model.isItemSelected.value!!) {
                    model.isItemSelected.value = false
                    model.checkedCurrencyPositions.clear()
                } else {
                    activity?.finish()
                }
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }

    private fun changeLayout(colorId: Int, titleId: Int, bottomNavVisibility: Int) {
        toolbar.setBackgroundColor(ContextCompat.getColor(requireContext(), colorId))
        toolbar.title = getString(titleId)
        bottomNav.visibility = bottomNavVisibility
    }
}