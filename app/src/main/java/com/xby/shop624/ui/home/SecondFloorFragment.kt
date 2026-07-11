*** Begin Patch
*** Update File: app/src/main/java/com/xby/shop624/ui/home/SecondFloorFragment.kt
@@
         class ViewHolder(private val binding: ItemSecondFloorProductBinding) :
             RecyclerView.ViewHolder(binding.root) {
 
             fun bind(product: ProductEntity) {
                 binding.ivProduct.loadNetworkImage(product.imageUrl)
                 binding.tvName.text = product.name
                 binding.tvPrice.text = binding.root.context.getString(
                     com.xby.shop624.R.string.price_wholesale,
                     product.price
                 )
                 binding.tvOriginalPrice.text = binding.root.context.getString(
                     com.xby.shop624.R.string.price_retail,
                     product.originalPrice
                 )
                 binding.tvTag.text = product.tag
+                // 点击跳转到商品详情页
+                binding.root.setOnClickListener {
+                    val ctx = binding.root.context
+                    val intent = android.content.Intent(ctx, com.xby.shop624.ui.detail.ProductDetailActivity::class.java).apply {
+                        putExtra(com.xby.shop624.ui.detail.ProductDetailActivity.EXTRA_PRODUCT_ID, product.id)
+                    }
+                    ctx.startActivity(intent)
+                }
             }
         }
*** End Patch
