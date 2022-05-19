package com.example.currencyconverter

import android.os.Bundle
import android.view.*
import androidx.core.content.ContextCompat
import androidx.core.view.isGone
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
    private val toolbar by lazy { (activity as MainScreen).getToolbar()}

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

        viewModel.itemSelected.observe(viewLifecycleOwner) { isItemSelected ->
            if (isItemSelected) binding.addCurrencyButton.visibility = View.GONE
        }

        binding.addCurrencyButton.setOnClickListener {
            viewModel.addCurrency(
                CurrencyItem(viewModel.currentId, "Lira", R.drawable.turkey_flag, 0L)
            )
            adapter.notifyItemInserted(adapter.itemCount - 1)
        }

        return view
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

    private fun setupItemSelectedToolbar() {
        toolbar.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.hint))
        toolbar.title = getString(R.string.currencies_list_item_selected)
    }

    private fun setupDefaultToolbar() {
        toolbar.setBackgroundColor(ContextCompat.getColor(requireContext(), R.color.primaryColor))
        toolbar.title = getString(R.string.currency)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        viewModel.itemSelected.observe(viewLifecycleOwner) {
            val menuLayoutId = if (it) {
                setupItemSelectedToolbar()
                R.menu.item_selected_menu
            } else {
                setupDefaultToolbar()
                R.menu.menu_currencies
            }
            menu.clear()
            inflater.inflate(menuLayoutId, menu)
        }
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