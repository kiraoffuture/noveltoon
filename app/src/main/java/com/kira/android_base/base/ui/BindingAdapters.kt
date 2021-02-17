package com.kira.android_base.base.ui

import android.graphics.Bitmap
import android.graphics.drawable.Drawable
import android.view.ViewGroup
import android.view.animation.AlphaAnimation
import android.widget.ImageView
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

@BindingAdapter("imageUrl")
fun loadImage(imageView: ImageView, link: String) {
    Glide.with(imageView.context).load(link).into(imageView)
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
