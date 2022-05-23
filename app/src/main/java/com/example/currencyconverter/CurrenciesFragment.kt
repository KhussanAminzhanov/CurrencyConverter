package com.example.currencyconverter

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.activity.OnBackPressedCallback
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.currencyconverter.databinding.FragmentCurrenciesBinding
import kotlin.random.Random

class CurrenciesFragment : Fragment() {

    private var _binding: FragmentCurrenciesBinding? = null
    private val binding get() = _binding!!

    private val viewModel by lazy { ViewModelProvider(this)[CurrenciesViewModel::class.java] }
    private val adapter by lazy { CurrenciesAdapter(viewModel, viewLifecycleOwner) }
    private val toolbar by lazy { (activity as MainScreen).toolbar }
    private val bottomNav by lazy { (activity as MainScreen).bottomNav }

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

        Log.i("currencies fragment", "onCreateView ${viewModel.isItemSelected.value}")

        setupRecyclerView()
        setupOnBackButtonPresses()

        viewModel.currencies.observe(viewLifecycleOwner) { newValue ->
            adapter.submitList(newValue)
        }

        viewModel.isItemSelected.observe(viewLifecycleOwner) { isItemSelected ->
            binding.addCurrencyButton.visibility = if (isItemSelected) View.GONE else View.VISIBLE
        }

        binding.addCurrencyButton.setOnClickListener {
            viewModel.addCurrency(randomCurrency())
            adapter.notifyItemInserted(adapter.itemCount - 1)
            binding.currenciesListRecyclerView.layoutManager?.scrollToPosition(adapter.currentList.size - 1)
        }

        return view
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        viewModel.isItemSelected.observe(viewLifecycleOwner) {
            val menuLayoutId = if (it) {
                changeLayout(R.color.hint, R.string.currencies_list_item_selected, View.GONE)
                R.menu.item_selected_menu
            } else {
                changeLayout(R.color.primaryColor, R.string.currency, View.VISIBLE)
                R.menu.menu_currencies
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
            R.id.menu_delete_item -> adapter.deleteSelectedCurrencies()
            else -> return super.onOptionsItemSelected(item)
        }
        return super.onOptionsItemSelected(item)
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

    private fun changeLayout(colorId: Int, titleId: Int, bottomNavVisibility: Int) {
        toolbar.setBackgroundColor(ContextCompat.getColor(requireContext(), colorId))
        toolbar.title = getString(titleId)
        bottomNav.visibility = bottomNavVisibility
    }

    private fun randomCurrency() : CurrencyItem {
        val list = listOf(
            CurrencyItem(viewModel.currentId, "Lira, Turkey", R.drawable.turkey_flag, viewModel.currentId.toLong()),
            CurrencyItem(viewModel.currentId, "Dollar, USA", R.drawable.usa_flag, viewModel.currentId.toLong()),
            CurrencyItem(viewModel.currentId, "Tenge, Kazakhstan", R.drawable.kazakhstan_flag, viewModel.currentId.toLong()),
            CurrencyItem(viewModel.currentId, "Euro, EU", R.drawable.europe_flag, viewModel.currentId.toLong())
        )
        return list.shuffled()[Random.nextInt(0, 4)]
    }

}