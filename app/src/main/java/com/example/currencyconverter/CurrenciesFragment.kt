package com.example.currencyconverter

import android.os.Bundle
import android.view.*
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.currencyconverter.databinding.FragmentCurrenciesBinding

class CurrenciesFragment : Fragment() {

    private var _binding: FragmentCurrenciesBinding? = null
    private val binding get() = _binding!!

    private val viewModel by lazy { ViewModelProvider(this)[CurrenciesViewModel::class.java] }
    private val adapter by lazy { CurrenciesAdapter(viewModel) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        _binding = FragmentCurrenciesBinding.inflate(inflater, container, false)
        val view = binding.root

        setupRecyclerView()

        viewModel.currencies.observe(viewLifecycleOwner) { newValue ->
            adapter.submitList(newValue)
            binding.currenciesListRecyclerView.layoutManager?.scrollToPosition(adapter.currentList.size - 1)
        }

        binding.addCurrencyButton.setOnClickListener {
            viewModel.addCurrency(
                CurrencyItem(viewModel.currentId, "Lira", R.drawable.turkey_flag, 0L)
            )
            adapter.notifyItemInserted(adapter.itemCount - 1)
        }

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    private fun setupRecyclerView() {
        adapter.submitList(viewModel.currencies.value)

        val callback = CurrenciesItemTouchHelperCallback(adapter)
        val touchHelper = ItemTouchHelper(callback)

        binding.currenciesListRecyclerView.adapter = this@CurrenciesFragment.adapter
        binding.currenciesListRecyclerView.layoutManager =
            LinearLayoutManager(
                this@CurrenciesFragment.context,
                LinearLayoutManager.VERTICAL,
                false
            )

        touchHelper.attachToRecyclerView(binding.currenciesListRecyclerView)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.menu_currencies, menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        val sortType = when (item.itemId) {
            R.id.menu_alphabet -> CurrenciesViewModel.SortType.ALPHABET
            R.id.menu_value -> CurrenciesViewModel.SortType.VALUE
            R.id.menu_reset -> CurrenciesViewModel.SortType.UNSORTED
            else -> return false
        }
        viewModel.setSortingType(sortType)
        return super.onOptionsItemSelected(item)
    }
}