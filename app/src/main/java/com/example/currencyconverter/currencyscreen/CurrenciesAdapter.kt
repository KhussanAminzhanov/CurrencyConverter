package com.example.currencyconverter.currencyscreen

import android.content.Context
import android.util.Log
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.ListAdapter
import com.example.currencyconverter.R
import com.example.currencyconverter.database.CurrenciesData
import com.example.currencyconverter.database.CurrencyItem
import com.google.android.material.snackbar.Snackbar

const val TAG_ADAPTER_CURRENCY = "currency_adapter"

class CurrenciesAdapter(
    val viewModel: CurrenciesViewModel,
    val viewLifecycleOwner: LifecycleOwner
) : ListAdapter<CurrencyItem, CurrenciesItemViewHolder>(CurrenciesDiffItemCallback()),
    CurrencyItemTouchHelperAdapter {

    private lateinit var mContext: Context
    val checkedCurrencyPositions = mutableListOf<Int>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrenciesItemViewHolder {
        Log.i(TAG_ADAPTER_CURRENCY, "onCreateViewHolder called")
        mContext = parent.context
        return CurrenciesItemViewHolder.inflateFrom(parent, this)
    }

    override fun onBindViewHolder(holder: CurrenciesItemViewHolder, position: Int) {
        Log.i(TAG_ADAPTER_CURRENCY, "onBindViewHolder called")
        val item = getItem(position)
        holder.bind(item, holder.itemView.context)
    }

    override fun onItemMove(fromPosition: Int, toPosition: Int) {
        CurrenciesData.moveCurrencies(fromPosition, toPosition)
        notifyItemMoved(fromPosition, toPosition)
    }

    override fun onItemDismiss(position: Int, view: View) {
        val deletedCurrency = CurrenciesData.deleteCurrency(position)
        notifyItemRemoved(position)
        submitList(CurrenciesData.getCurrencies())

        Snackbar.make(view, "Currency deleted!", Snackbar.LENGTH_LONG)
            .setAnchorView(R.id.add_currency_button)
            .setAction("Undo") {
                notifyItemInserted(itemCount)
                CurrenciesData.addCurrency(deletedCurrency)
            }.show()
    }

    fun getContext(): Context = mContext

    fun showDeleteConfirmationDialog(fragmentManager: FragmentManager) {
        val dialog = DeleteConfirmationDialogFragment { deleteCurrencies() }
        dialog.show(fragmentManager, DeleteConfirmationDialogFragment.TAG)
    }

    private fun deleteCurrencies() {
        CurrenciesData.deleteCurrencies(checkedCurrencyPositions)
        val deletedCurrencyPositions = mutableListOf<Int>()
        checkedCurrencyPositions.forEach { oldPosition ->
            val currentPosition = getUpdatedPosition(oldPosition, deletedCurrencyPositions)
            deletedCurrencyPositions.add(oldPosition)
            notifyItemRemoved(currentPosition)
        }
        checkedCurrencyPositions.clear()
    }

    private fun getUpdatedPosition(
        oldPosition: Int,
        deletedCurrencyPositions: MutableList<Int>
    ): Int {
        var position = oldPosition
        deletedCurrencyPositions.forEach { if (oldPosition > it) position-- }
        return position
    }
}