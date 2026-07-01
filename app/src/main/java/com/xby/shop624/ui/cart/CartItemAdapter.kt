package com.xby.shop624.ui.cart

import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.graphics.RectF
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.ListAdapter
import androidx.recyclerview.widget.RecyclerView
import com.xby.shop624.databinding.ItemCartBinding
import com.xby.shop624.model.CartItemUi
import com.xby.shop624.util.loadNetworkImage
import java.math.BigDecimal
import java.math.RoundingMode

class CartItemAdapter(
    private val onSelectedChanged: (Long, Boolean) -> Unit,
    private val onIncrease: (Long) -> Unit,
    private val onDecrease: (Long) -> Unit,
    private val onItemClick: (Int) -> Unit
) : ListAdapter<CartItemUi, CartItemAdapter.ViewHolder>(DiffCallback) {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemCartBinding.inflate(
            LayoutInflater.from(parent.context), parent, false
        )
        return ViewHolder(binding, onSelectedChanged, onIncrease, onDecrease, onItemClick)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.bind(getItem(position))
    }

    class ViewHolder(
        private val binding: ItemCartBinding,
        private val onSelectedChanged: (Long, Boolean) -> Unit,
        private val onIncrease: (Long) -> Unit,
        private val onDecrease: (Long) -> Unit,
        private val onItemClick: (Int) -> Unit
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(item: CartItemUi) {
            binding.cbSelect.setOnCheckedChangeListener(null)
            binding.cbSelect.isChecked = item.selected
            binding.ivProduct.loadNetworkImage(item.imageUrl)
            binding.tvName.text = item.name
            binding.tvPrice.text = binding.root.context.getString(
                com.xby.shop624.R.string.price_wholesale,
                formatPrice(item.price)
            )
            binding.tvQuantity.text = item.quantity.toString()
            binding.tvSubtotal.text = "¥${formatPrice(item.subtotal)}"

            binding.cbSelect.setOnCheckedChangeListener { _, checked ->
                onSelectedChanged(item.id, checked)
            }
            binding.btnIncrease.setOnClickListener { onIncrease(item.id) }
            binding.btnDecrease.setOnClickListener { onDecrease(item.id) }
            binding.contentLayout.setOnClickListener { onItemClick(item.productId) }
        }

        private fun formatPrice(value: Double): String {
            return BigDecimal.valueOf(value)
                .setScale(2, RoundingMode.HALF_UP)
                .stripTrailingZeros()
                .toPlainString()
        }
    }

    private object DiffCallback : DiffUtil.ItemCallback<CartItemUi>() {
        override fun areItemsTheSame(a: CartItemUi, b: CartItemUi) = a.id == b.id
        override fun areContentsTheSame(a: CartItemUi, b: CartItemUi) = a == b
    }
}

class CartSwipeCallback(
    private val adapter: CartItemAdapter,
    private val onDelete: (Long) -> Unit
) : ItemTouchHelper.SimpleCallback(0, ItemTouchHelper.LEFT) {

    private val deletePaint = Paint().apply {
        color = Color.parseColor("#FF4444")
        isAntiAlias = true
    }
    private val textPaint = Paint().apply {
        color = Color.WHITE
        textSize = 36f
        isAntiAlias = true
    }

    override fun onMove(
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        target: RecyclerView.ViewHolder
    ) = false

    override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
        val position = viewHolder.bindingAdapterPosition
        if (position == RecyclerView.NO_POSITION) return
        val item = adapter.currentList[position]
        onDelete(item.id)
    }

    override fun onChildDraw(
        c: Canvas,
        recyclerView: RecyclerView,
        viewHolder: RecyclerView.ViewHolder,
        dX: Float,
        dY: Float,
        actionState: Int,
        isCurrentlyActive: Boolean
    ) {
        if (actionState == ItemTouchHelper.ACTION_STATE_SWIPE) {
            val itemView = viewHolder.itemView
            val height = itemView.height.toFloat()
            val width = itemView.width.toFloat()
            if (dX < 0) {
                val rect = RectF(
                    itemView.right + dX,
                    itemView.top.toFloat(),
                    itemView.right.toFloat(),
                    itemView.bottom.toFloat()
                )
                c.drawRect(rect, deletePaint)
                c.drawText(
                    "删除",
                    itemView.right - 120f,
                    itemView.top + height / 2 + 12f,
                    textPaint
                )
            }
        }
        super.onChildDraw(c, recyclerView, viewHolder, dX, dY, actionState, isCurrentlyActive)
    }
}
