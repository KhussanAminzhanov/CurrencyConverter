package com.example.currencyconverter.ui.converter

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.*
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.currencyconverter.R
import com.example.currencyconverter.adapter.CurrencyListAdapter
import com.example.currencyconverter.database.CurrencyDatabase
import com.example.currencyconverter.databinding.FragmentCurrenciesBinding
import com.example.currencyconverter.network.CurrencyApiNetworkIml
import com.example.currencyconverter.repository.CurrenciesRepository
import com.example.currencyconverter.ui.currencyselector.CurrencySelectorBottomSheet
import com.example.currencyconverter.ui.main.MainActivity
import com.example.currencyconverter.viewmodel.CurrencyViewModel
import com.google.android.material.snackbar.Snackbar
import org.koin.android.ext.android.inject
import org.koin.androidx.viewmodel.ext.android.viewModel
import org.koin.core.parameter.parametersOf

class ConverterFragment : Fragment() {

    private var _binding: FragmentCurrenciesBinding? = null
    private val binding get() = _binding!!
    private val toolbar by lazy { (activity as MainActivity).toolbar }
    private val bottomNav by lazy { (activity as MainActivity).bottomNav }

    private val database: CurrencyDatabase by inject { parametersOf(context) }
    private val repository: CurrenciesRepository by inject { parametersOf(database, CurrencyApiNetworkIml)}
    private val model: CurrencyViewModel by viewModel { parametersOf(repository) }
    private lateinit var adapter: CurrencyListAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
        adapter = CurrencyListAdapter(model, activity as LifecycleOwner)
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

    override fun onStart() {
        super.onStart()
        if (!isInternetAvailable()) {
            Snackbar.make(binding.root, "Network ERROR!", Snackbar.LENGTH_LONG)
                .setAnchorView(binding.etCurrencyValue)
                .show()
        }
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
        model.isNetworkError.observe(viewLifecycleOwner) {
            if (it) {
                Snackbar.make(binding.root, "Network ERROR!", Snackbar.LENGTH_LONG)
                    .setAnchorView(binding.etCurrencyValue)
                    .show()
            }
        }
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
                if (model.isItemSelected.value == true) {
                    model.isItemSelected.value = false
                    model.checkedCurrencyPositions.clear()
                } else {
                    activity?.finish()
                }
            }
        }
        requireActivity().onBackPressedDispatcher.addCallback(viewLifecycleOwner, callback)
    }

    private fun isInternetAvailable(): Boolean {
        var result = false
        val connectivityManager =
            requireContext().getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            val networkCapabilities = connectivityManager.activeNetwork ?: return false
            val activityNetwork =
                connectivityManager.getNetworkCapabilities(networkCapabilities) ?: return false
            result = when {
                activityNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true
                activityNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true
                activityNetwork.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET) -> true
                else -> false
            }
        } else {
            connectivityManager.run {
                connectivityManager.activeNetworkInfo?.run {
                    result = when (type) {
                        ConnectivityManager.TYPE_WIFI -> true
                        ConnectivityManager.TYPE_MOBILE -> true
                        ConnectivityManager.TYPE_ETHERNET -> true
                        else -> false
                    }
                }
            }
        }
        return result
    }

    private fun changeLayout(colorId: Int, titleId: Int, bottomNavVisibility: Int) {
        toolbar.setBackgroundColor(ContextCompat.getColor(requireContext(), colorId))
        toolbar.title = getString(titleId)
        bottomNav.visibility = bottomNavVisibility
    }
}