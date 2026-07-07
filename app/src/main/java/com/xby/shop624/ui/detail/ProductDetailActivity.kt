package com.xby.shop624.ui.detail

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.isVisible
import androidx.recyclerview.widget.LinearLayoutManager
import com.google.android.material.chip.Chip
import com.google.android.material.dialog.MaterialAlertDialogBuilder
import com.xby.shop624.R
import com.xby.shop624.databinding.ActivityProductDetailBinding
import com.xby.shop624.databinding.DialogCommentInputBinding
import com.xby.shop624.databinding.DialogSpecsBinding
import com.xby.shop624.ui.cart.CartViewModel
import com.xby.shop624.ui.chat.ChatActivity
import com.xby.shop624.ui.main.MainActivity
import com.xby.shop624.util.loadNetworkImage
import com.xby.shop624.data.local.entity.ProductEntity

class ProductDetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityProductDetailBinding
    private val viewModel: ProductDetailViewModel by viewModels()
    private val cartViewModel: CartViewModel by viewModels()
    private val favoriteViewModel: FavoriteViewModel by viewModels()
    private val likeViewModel: LikeViewModel by viewModels()
    private var productId: Int = -1
    private var currentImageUrl: String? = null
    private var currentEmoji: String? = null
    private var selectedSpec: String = ""
    private var currentProduct: ProductEntity? = null

    private val commentAdapter = CommentAdapter { comment ->
        showInputDialog(getString(com.xby.shop624.R.string.detail_reply_to, comment.userName)) { content ->
            viewModel.replyComment(comment.id, content)
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityProductDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setSupportActionBar(binding.toolbar)
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        binding.toolbar.setNavigationOnClickListener { finish() }

        productId = intent.getIntExtra(EXTRA_PRODUCT_ID, -1)
        if (productId <= 0) {
            finish()
            return
        }

        binding.rvComments.layoutManager = LinearLayoutManager(this)
        binding.rvComments.adapter = commentAdapter

        binding.btnAddComment.setOnClickListener {
            showInputDialog(getString(com.xby.shop624.R.string.detail_add_comment)) { content ->
                viewModel.addComment(content)
            }
        }

        binding.btnKefu.setOnClickListener {
            startActivity(Intent(this, ChatActivity::class.java))
        }

        binding.ivFavorite.setOnClickListener {
            currentProduct?.let {
                favoriteViewModel.toggleFavorite(it)
            }
        }

        binding.ivLike.setOnClickListener {
            likeViewModel.toggleLike(productId)
        }

        binding.ivProduct.setOnClickListener {
            startActivity(
                Intent(this, ImagePreviewActivity::class.java).apply {
                    putExtra(ImagePreviewActivity.EXTRA_IMAGE_URL, currentImageUrl)
                    putExtra(ImagePreviewActivity.EXTRA_EMOJI, currentEmoji)
                }
            )
        }

        binding.btnAddCart.setOnClickListener {
            cartViewModel.addToCart(productId)
        }

        binding.btnGoCart.setOnClickListener {
            startActivity(
                Intent(this, MainActivity::class.java).apply {
                    flags = Intent.FLAG_ACTIVITY_CLEAR_TOP or Intent.FLAG_ACTIVITY_SINGLE_TOP
                    putExtra(MainActivity.EXTRA_TAB, MainActivity.TAB_CART)
                }
            )
            finish()
        }

        viewModel.product.observe(this) { product ->
            if (product == null) return@observe
            currentProduct = product
            binding.toolbar.title = product.name
            currentImageUrl = product.imageUrl
            currentEmoji = product.emoji
            binding.ivProduct.loadNetworkImage(product.imageUrl)
            binding.tvName.text = product.name
            binding.tvTag.text = product.tag
            binding.tvPrice.text = getString(com.xby.shop624.R.string.price_wholesale, product.price)
            binding.tvOriginalPrice.text = getString(com.xby.shop624.R.string.price_retail, product.originalPrice)
            binding.tvDescription.text = product.description
            binding.tvSales.text = getString(com.xby.shop624.R.string.detail_sales, product.sales)
            favoriteViewModel.checkFavorite(product.id)
            likeViewModel.checkLike(product.id)

            val specList = product.specs.takeIf { it.isNotBlank() }?.split("|") ?: emptyList()
            if (specList.isNotEmpty()) {
                selectedSpec = specList.first()
                binding.tvSelectedSpec.text = selectedSpec

                binding.cardSpecs.setOnClickListener {
                    showSpecsDialog(specList)
                }
            }
        }

        viewModel.comments.observe(this) { comments ->
            commentAdapter.submitList(comments)
            binding.tvCommentCount.text =
                getString(com.xby.shop624.R.string.detail_comment_count, comments.size)
            binding.tvNoComment.isVisible = comments.isEmpty()
        }

        viewModel.toastMessage.observe(this) { msg ->
            msg ?: return@observe
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
            viewModel.clearToast()
        }

        cartViewModel.toastMessage.observe(this) { msg ->
            msg ?: return@observe
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
            cartViewModel.clearToast()
        }

        cartViewModel.cartCount.observe(this) { count ->
            binding.tvCartBadge.isVisible = count > 0
            binding.tvCartBadge.text = if (count > 99) "99+" else count.toString()
        }

        favoriteViewModel.isFavorite.observe(this) { isFav ->
            binding.ivFavorite.setColorFilter(
                resources.getColor(if (isFav) R.color.price else R.color.text_secondary, null)
            )
        }

        favoriteViewModel.toastMessage.observe(this) { msg ->
            msg ?: return@observe
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
            favoriteViewModel.clearToast()
        }

        likeViewModel.isLiked.observe(this) { isLiked ->
            binding.ivLike.setColorFilter(
                resources.getColor(if (isLiked) R.color.price else R.color.text_secondary, null)
            )
        }

        likeViewModel.toastMessage.observe(this) { msg ->
            msg ?: return@observe
            Toast.makeText(this, msg, Toast.LENGTH_SHORT).show()
            likeViewModel.clearToast()
        }

        viewModel.load(productId)
    }

    private fun showInputDialog(title: String, onSubmit: (String) -> Unit) {
        val dialogBinding = DialogCommentInputBinding.inflate(LayoutInflater.from(this))
        MaterialAlertDialogBuilder(this)
            .setTitle(title)
            .setView(dialogBinding.root)
            .setPositiveButton(com.xby.shop624.R.string.detail_submit) { _, _ ->
                onSubmit(dialogBinding.etContent.text?.toString().orEmpty())
            }
            .setNegativeButton(com.xby.shop624.R.string.detail_cancel, null)
            .show()
    }

    private fun createSpecChip(text: String): Chip {
        return Chip(this).apply {
            this.text = text
            isCheckable = true
            isClickable = true
            setTextColor(resources.getColorStateList(R.color.selector_chip_text, null))
            chipBackgroundColor = resources.getColorStateList(R.color.selector_chip_bg, null)
            chipStrokeColor = resources.getColorStateList(R.color.selector_chip_stroke, null)
            chipStrokeWidth = 1f
            textSize = 12f
            setPadding(16, 8, 16, 8)
        }
    }

    private fun showSpecsDialog(specList: List<String>) {
        val dialogBinding = DialogSpecsBinding.inflate(LayoutInflater.from(this))
        dialogBinding.cgSpecs.isSingleSelection = true

        specList.forEachIndexed { index, spec ->
            val chip = createSpecChip(spec)
            chip.id = index
            dialogBinding.cgSpecs.addView(chip)
            if (spec == selectedSpec) {
                dialogBinding.cgSpecs.check(index)
            }
        }

        val dialog = MaterialAlertDialogBuilder(this)
            .setView(dialogBinding.root)
            .setCancelable(true)
            .create()

        dialogBinding.btnCancel.setOnClickListener {
            dialog.dismiss()
        }

        dialogBinding.btnConfirm.setOnClickListener {
            val checkedIds = dialogBinding.cgSpecs.checkedChipIds
            if (checkedIds.isNotEmpty()) {
                val position = checkedIds[0]
                selectedSpec = specList[position]
                binding.tvSelectedSpec.text = selectedSpec
            }
            dialog.dismiss()
        }

        dialog.show()
    }

    companion object {
        const val EXTRA_PRODUCT_ID = "extra_product_id"
    }
}
