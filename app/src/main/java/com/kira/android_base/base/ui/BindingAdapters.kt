package com.kira.android_base.base.ui

import android.graphics.Bitmap
import android.graphics.drawable.ColorDrawable
import android.graphics.drawable.Drawable
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.widget.ImageView
import android.widget.NumberPicker
import androidx.core.graphics.drawable.toDrawable
import androidx.core.view.forEach
import androidx.databinding.BindingAdapter
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions.bitmapTransform
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.kira.android_base.base.supports.extensions.gone
import com.kira.android_base.base.supports.extensions.onAnimationStart
import com.kira.android_base.base.supports.extensions.visible
import jp.wasabeef.glide.transformations.BlurTransformation
import java.lang.reflect.Field

@BindingAdapter("imageUrl")
fun loadImage(imageView: ImageView, link: String) {
    Glide.with(imageView.context).load(link).into(imageView)
}

@BindingAdapter(
    value = ["max", "min", "default", "wrap_selector_wheel", "divider_color"],
    requireAll = false
)
fun setUpNumberPicker(
    numberPicker: NumberPicker,
    max: Int?,
    min: Int?,
    default: Int?,
    wrapSelectorWheel: Boolean = true,
    dividerColor: Int?
) {
    numberPicker.apply {
        max?.let { maxValue = it }
        min?.let { minValue = it }
        default?.let { value = it }
        this.wrapSelectorWheel = wrapSelectorWheel
        dividerColor?.let {
            try {
                val field: Field = NumberPicker::class.java.getDeclaredField("mSelectionDivider")
                field.isAccessible = true
                field.set(this, ColorDrawable(it))
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}

@BindingAdapter(value = ["backgroundUrl", "isBlur"], requireAll = false)
fun loadImageBackground(viewGroup: ViewGroup, backgroundUrl: String, isBlur: Boolean = false) {
    viewGroup.forEach { it.gone() }
    Glide.with(viewGroup.context)
        .asBitmap()
        .load(backgroundUrl)
        .centerCrop()
        .apply {
            if (isBlur) apply(bitmapTransform(BlurTransformation()))
        }
        .into(object : CustomTarget<Bitmap>() {
            override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
                viewGroup.background = resource.toDrawable(viewGroup.resources)
                viewGroup.startAnimation(
                    AlphaAnimation(0f, 1f).apply {
                        duration = 400
                        onAnimationStart {
                            viewGroup.forEach { it.visible() }
                        }
                    }
                )
            }

            override fun onLoadCleared(placeholder: Drawable?) {
            }
        })
}
