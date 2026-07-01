package com.xby.shop624.util

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Paint
import android.widget.ImageView
import coil.ImageLoader
import coil.disk.DiskCache
import coil.load
import coil.memory.MemoryCache
import coil.request.CachePolicy
import coil.request.ErrorResult
import coil.request.ImageRequest
import coil.request.SuccessResult
import coil.size.Scale
import android.util.Log
import com.xby.shop624.R
import com.xby.shop624.Shop624App

object CoilConfig {
    private val requestStartTimes = mutableMapOf<String, Long>()

    val imageLoader: ImageLoader by lazy {
        ImageLoader.Builder(Shop624App.instance)
            .memoryCache {
                MemoryCache.Builder(Shop624App.instance)
                    .maxSizePercent(0.25)
                    .build()
            }
            .diskCache {
                DiskCache.Builder()
                    .directory(Shop624App.instance.cacheDir.resolve("coil_cache"))
                    .maxSizeBytes(50 * 1024 * 1024) // 50MB
                    .build()
            }
            .crossfade(true)
            .eventListener(object : coil.EventListener {
                override fun onStart(request: ImageRequest) {
                    requestStartTimes[request.data.toString()] = System.currentTimeMillis()
                    Log.d("CoilTiming", "[START] ${request.data}")
                }

                override fun onSuccess(request: ImageRequest, result: SuccessResult) {
                    val startTime = requestStartTimes.remove(request.data.toString())
                    val duration = startTime?.let { System.currentTimeMillis() - it } ?: -1
                    val source = result.dataSource.name
                    Log.d("CoilTiming", "[SUCCESS] ${request.data} | 耗时: ${duration}ms | 来源: $source")
                }

                override fun onError(request: ImageRequest, result: ErrorResult) {
                    val startTime = requestStartTimes.remove(request.data.toString())
                    val duration = startTime?.let { System.currentTimeMillis() - it } ?: -1
                    Log.e("CoilTiming", "[ERROR] ${request.data} | 耗时: ${duration}ms | 异常: ${result.throwable?.message}")
                }

                override fun onCancel(request: ImageRequest) {
                    requestStartTimes.remove(request.data.toString())
                    Log.w("CoilTiming", "[CANCEL] ${request.data}")
                }
            })
            .build()
    }
}

fun ImageView.loadNetworkImage(
    url: String?,
    placeholder: Int = R.drawable.bg_image_placeholder,
    scale: Scale = Scale.FILL
) {
    loadNetworkImage(url, null, placeholder, scale)
}

fun ImageView.loadNetworkImage(
    url: String?,
    emoji: String?,
    placeholder: Int = R.drawable.bg_image_placeholder,
    scale: Scale = Scale.FILL
) {
    if (url.isNullOrBlank()) {
        setImageResource(placeholder)
        return
    }

    if (url.startsWith("drawable://")) {
        val resourceName = url.removePrefix("drawable://")
        val resourceId = Shop624App.instance.resources.getIdentifier(
            resourceName, "drawable", Shop624App.instance.packageName
        )
        if (resourceId != 0) {
            setImageResource(resourceId)
        } else {
            setImageResource(placeholder)
        }
        return
    }

    load(url, imageLoader = CoilConfig.imageLoader) {
        crossfade(300)
        placeholder(placeholder)
        error(placeholder)
        fallback(placeholder)
        scale(scale)
        memoryCachePolicy(CachePolicy.ENABLED)
        diskCachePolicy(CachePolicy.ENABLED)
    }
}

fun ImageView.loadEmojiImage(
    emoji: String?,
    backgroundColor: Int = Color.WHITE
) {
    if (emoji.isNullOrBlank()) {
        setBackgroundColor(backgroundColor)
        return
    }

    val size = 200
    val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    
    canvas.drawColor(backgroundColor)
    
    val paint = Paint().apply {
        textSize = 100f
        textAlign = Paint.Align.CENTER
        isAntiAlias = true
    }
    
    val x = size / 2f
    val y = (size / 2f) + (paint.textSize / 3f)
    
    canvas.drawText(emoji, x, y, paint)
    
    setImageBitmap(bitmap)
}

fun ImageView.loadProductImage(
    emoji: String?,
    name: String?,
    backgroundColor: Int = Color.WHITE
) {
    val size = 200
    val bitmap = Bitmap.createBitmap(size, size, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)

    canvas.drawColor(backgroundColor)

    val padding = 16f

    val emojiPaint = Paint().apply {
        textSize = 80f
        textAlign = Paint.Align.CENTER
        isAntiAlias = true
    }

    val x = size / 2f
    val emojiY = (size / 2f) - 20f

    canvas.drawText(emoji ?: "📦", x, emojiY, emojiPaint)

    val namePaint = Paint().apply {
        textSize = 14f
        textAlign = Paint.Align.CENTER
        isAntiAlias = true
        color = Color.parseColor("#333333")
    }

    val displayName = name?.let {
        if (it.length > 12) it.substring(0, 12) + "..." else it
    } ?: "商品"

    val nameY = size - padding

    canvas.drawText(displayName, x, nameY, namePaint)

    setImageBitmap(bitmap)
}
