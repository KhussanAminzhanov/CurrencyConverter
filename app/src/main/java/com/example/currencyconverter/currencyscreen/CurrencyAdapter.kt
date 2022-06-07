package com.example.currencyconverter.currencyscreen

import android.content.Context
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.ListAdapter
import com.example.currencyconverter.R
import com.example.currencyconverter.database.CurrenciesData
import com.example.currencyconverter.database.CurrencyItem
import com.google.android.material.snackbar.Snackbar

class CurrenciesAdapter(
    val viewModel: CurrencyViewModel,
    val viewLifecycleOwner: LifecycleOwner
) : ListAdapter<CurrencyItem, CurrenciesItemViewHolder>(CurrencyDiffItemCallback()),
    CurrencyItemTouchHelperAdapter {

    private lateinit var mContext: Context
    val checkedCurrencyPositions = mutableListOf<Int>()

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CurrenciesItemViewHolder {
        mContext = parent.context
        return CurrenciesItemViewHolder.inflateFrom(parent, this)
    }

    override fun onBindViewHolder(holder: CurrenciesItemViewHolder, position: Int) {
        val item = getItem(position)
        holder.bind(item, holder.itemView.context)
    }

    override fun onItemMove(fromPosition: Int, toPosition: Int) {
        viewModel.moveCurrencies(getItem(fromPosition).currencyId, getItem(toPosition).currencyId)
//        notifyItemMoved(fromPosition, toPosition)
    }

    override fun onItemDismiss(position: Int, view: View) {
        val deletedCurrency = getItem(position)
        viewModel.delete(deletedCurrency)

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