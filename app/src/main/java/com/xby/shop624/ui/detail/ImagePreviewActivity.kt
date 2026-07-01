package com.xby.shop624.ui.detail

import android.graphics.Bitmap
import android.graphics.Canvas
import android.graphics.Color
import android.graphics.Matrix
import android.graphics.Paint
import android.graphics.PointF
import android.os.Bundle
import android.view.MotionEvent
import android.view.View
import android.view.ViewTreeObserver
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import coil.load
import com.xby.shop624.R
import com.xby.shop624.util.CoilConfig

class ImagePreviewActivity : AppCompatActivity() {

    private lateinit var ivPreview: ImageView
    private val matrix = Matrix()
    private val savedMatrix = Matrix()

    private val startPoint = PointF()
    private val midPoint = PointF()

    private var mode = NONE
    private var oldDistance = 1f

    companion object {
        const val EXTRA_IMAGE_URL = "extra_image_url"
        const val EXTRA_EMOJI = "extra_emoji"
        private const val NONE = 0
        private const val DRAG = 1
        private const val ZOOM = 2
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_image_preview)

        hideSystemUI()

        ivPreview = findViewById(R.id.iv_preview)
        ivPreview.scaleType = ImageView.ScaleType.MATRIX

        val imageUrl = intent.getStringExtra(EXTRA_IMAGE_URL)
        val emoji = intent.getStringExtra(EXTRA_EMOJI)

        ivPreview.loadProductImage(imageUrl, emoji)

        ivPreview.viewTreeObserver.addOnGlobalLayoutListener(object :
            ViewTreeObserver.OnGlobalLayoutListener {
            override fun onGlobalLayout() {
                ivPreview.viewTreeObserver.removeOnGlobalLayoutListener(this)
                fitImageToScreen()
            }
        })

        ivPreview.setOnTouchListener { _, event ->
            handleTouchEvent(event)
        }

        ivPreview.setOnClickListener {
            finish()
        }
    }

    private fun hideSystemUI() {
        WindowCompat.setDecorFitsSystemWindows(window, false)
        WindowInsetsControllerCompat(window, window.decorView).let { controller ->
            controller.hide(WindowInsetsCompat.Type.systemBars())
            controller.systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }

    private fun fitImageToScreen() {
        val drawable = ivPreview.drawable ?: return

        val imageWidth = drawable.intrinsicWidth.toFloat()
        val imageHeight = drawable.intrinsicHeight.toFloat()
        val screenWidth = ivPreview.width.toFloat()
        val screenHeight = ivPreview.height.toFloat()

        val scaleX = screenWidth / imageWidth
        val scaleY = screenHeight / imageHeight
        val scale = minOf(scaleX, scaleY, 1f)

        matrix.setScale(scale, scale)

        val translateX = (screenWidth - imageWidth * scale) / 2
        val translateY = (screenHeight - imageHeight * scale) / 2
        matrix.postTranslate(translateX, translateY)

        ivPreview.imageMatrix = matrix
    }

    private fun handleTouchEvent(event: MotionEvent): Boolean {
        when (event.action and MotionEvent.ACTION_MASK) {
            MotionEvent.ACTION_DOWN -> {
                savedMatrix.set(matrix)
                startPoint.set(event.x, event.y)
                mode = DRAG
            }
            MotionEvent.ACTION_POINTER_DOWN -> {
                oldDistance = getDistance(event)
                if (oldDistance > 10f) {
                    savedMatrix.set(matrix)
                    getMidPoint(midPoint, event)
                    mode = ZOOM
                }
            }
            MotionEvent.ACTION_MOVE -> {
                if (mode == DRAG) {
                    val dx = event.x - startPoint.x
                    val dy = event.y - startPoint.y
                    matrix.set(savedMatrix)
                    matrix.postTranslate(dx, dy)
                    ivPreview.imageMatrix = matrix
                } else if (mode == ZOOM) {
                    val newDistance = getDistance(event)
                    if (newDistance > 10f) {
                        matrix.set(savedMatrix)
                        val scale = newDistance / oldDistance
                        matrix.postScale(scale, scale, midPoint.x, midPoint.y)
                        ivPreview.imageMatrix = matrix
                    }
                }
            }
            MotionEvent.ACTION_UP, MotionEvent.ACTION_POINTER_UP -> {
                mode = NONE
            }
        }
        return true
    }

    private fun getDistance(event: MotionEvent): Float {
        val dx = event.getX(0) - event.getX(1)
        val dy = event.getY(0) - event.getY(1)
        return kotlin.math.sqrt(dx * dx + dy * dy)
    }

    private fun getMidPoint(point: PointF, event: MotionEvent) {
        val x = (event.getX(0) + event.getX(1)) / 2
        val y = (event.getY(0) + event.getY(1)) / 2
        point.set(x, y)
    }
}

fun ImageView.loadProductImage(url: String?, emoji: String?) {
    if (!url.isNullOrBlank()) {
        if (url.startsWith("drawable://")) {
            val resourceName = url.removePrefix("drawable://")
            val resourceId = context.resources.getIdentifier(
                resourceName, "drawable", context.packageName
            )
            if (resourceId != 0) {
                load(resourceId, imageLoader = CoilConfig.imageLoader)
            } else {
                loadEmojiImage(emoji)
            }
        } else {
            load(url, imageLoader = CoilConfig.imageLoader) {
                crossfade(300)
            }
        }
    } else {
        loadEmojiImage(emoji)
    }
}

fun ImageView.loadEmojiImage(emoji: String?) {
    val text = emoji ?: "📦"
    val bitmap = Bitmap.createBitmap(512, 512, Bitmap.Config.ARGB_8888)
    val canvas = Canvas(bitmap)
    canvas.drawColor(Color.WHITE)
    val paint = Paint().apply {
        textSize = 256f
        textAlign = Paint.Align.CENTER
        isAntiAlias = true
    }
    canvas.drawText(text, 256f, 320f, paint)
    setImageBitmap(bitmap)
}