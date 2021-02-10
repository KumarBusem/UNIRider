package com.uni.rider.common

import android.content.Context
import android.text.TextUtils
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.databinding.BindingAdapter
import androidx.databinding.InverseBindingAdapter
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.github.chrisbanes.photoview.PhotoView
import com.google.android.material.textfield.TextInputEditText
import com.uni.rider.R

/** View extensions */

fun View.show() {
    if (visibility != View.VISIBLE)
        visibility = View.VISIBLE
}

fun View.hide() {
    if (visibility != View.GONE)
        visibility = View.GONE
}

fun View.invisible() {
    if (visibility != View.INVISIBLE)
        visibility = View.INVISIBLE
}

fun View.toggleVisibility() {
    visibility = if (visibility != View.VISIBLE) View.VISIBLE
    else View.GONE
}

fun View.enable() {
    isEnabled = true
    alpha = 1f
}

fun View.disable() {
    isEnabled = false
    alpha = 0.75f
}


fun Context.toast(message: String) {
    Toast.makeText(this, message, Toast.LENGTH_LONG).show()
}

fun RecyclerView.setDivider(context: Context): RecyclerView {
    val itemDecorator = androidx.recyclerview.widget.DividerItemDecoration(context,
            androidx.recyclerview.widget.DividerItemDecoration.VERTICAL
    )
    ContextCompat.getDrawable(context, R.drawable.recycler_divider)
            ?.let { it1 -> itemDecorator.setDrawable(it1) }
    this.addItemDecoration(itemDecorator)
    return this
}


/** Binding Adapters    */

@BindingAdapter("app:enableIf")
fun View.enableIf(enabled: Boolean?) = if (enabled == true) enable() else disable()

@BindingAdapter("app:marqueeEnabled")
fun TextView.enableMarquee(marqueeEnabled: Boolean) {

    if (marqueeEnabled) {
        ellipsize = TextUtils.TruncateAt.MARQUEE
        marqueeRepeatLimit = -1
        setSingleLine()
        isSelected = true
        isFocusable = true
        isFocusableInTouchMode = true
    }
}

@BindingAdapter("android:setFloatText")
fun TextInputEditText.setFloatText(value: Float?) {

    val previousValue = try {
        this.text.toString().toFloat()
    } catch (ex: NumberFormatException) {
        null
    }

    if (previousValue != value) {
        this.setText(value.toString())
    }
}

@InverseBindingAdapter(attribute = "android:setFloatText", event = "android:textAttrChanged")
fun TextInputEditText.getFloatText(): Float? {
    return try {
        this.text.toString().toFloat()
    } catch (ex: NumberFormatException) {
        null
    }
}

@BindingAdapter("android:setIntText")
fun TextInputEditText.setIntText(value: Int?) {
    if (this.text.toString().toIntOrNull() != value) this.setText(value.toString())
}

@InverseBindingAdapter(attribute = "android:setIntText", event = "android:textAttrChanged")
fun TextInputEditText.getIntText(): Int {
    return this.text.toString().toIntOrNull() ?: 0
}


@BindingAdapter("app:setImageFrom")
fun ImageView.setImageFrom(url: String?) {
    if (!url.isNullOrEmpty()) {
        Glide.with(this).load(url).into(this)
    }
}

@BindingAdapter("app:setImageFrom")
fun PhotoView.setImageFrom(url: String?) {
    if (!url.isNullOrEmpty()) {
        Glide.with(this).load(url).into(this)
    }
}

@BindingAdapter("app:setSProjectStatusColor")
fun TextView.setSProjectStatusColor(status: String?) {
    val color = when (status) {
//        Project.STATUS_IN_PROGRESS -> R.color.colorAccent
//        Project.STATUS_ON_HOLD -> R.color.colorOrange
//        Project.STATUS_COMPLETED -> R.color.colorGreen
        else -> R.color.colorGrey500
    }

    setTextColor(ContextCompat.getColor(context, color))
}
