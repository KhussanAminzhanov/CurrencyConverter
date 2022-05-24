package com.example.currencyconverter

import android.app.AlertDialog
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.LifecycleOwner
import androidx.recyclerview.widget.ListAdapter
import com.google.android.material.snackbar.Snackbar

class CurrenciesAdapter(
    val viewModel: CurrenciesViewModel,
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
        viewModel.moveCurrencies(fromPosition, toPosition)
        notifyItemMoved(fromPosition, toPosition)
    }

    override fun onItemDismiss(position: Int, view: View) {
        val deletedCurrency = viewModel.deleteCurrency(position)
        notifyItemRemoved(position)

        Snackbar.make(view, "Currency deleted!", Snackbar.LENGTH_LONG)
            .setAnchorView(R.id.add_currency_button)
            .setAction("Undo") {
                notifyItemInserted(itemCount)
                viewModel.addCurrency(deletedCurrency)
            }.show()
    }

    fun getContext(): Context = mContext

    fun showDeleteConfirmationDialog(fragmentManager: FragmentManager) {
        val dialog = DeleteCurrencyConfirmationDialogFragment { deleteCurrencies() }
        dialog.show(fragmentManager, DeleteCurrencyConfirmationDialogFragment.TAG)
    }

    private fun deleteCurrencies() {
        viewModel.deleteCurrencies(checkedCurrencyPositions)
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