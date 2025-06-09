package com.example.todomanager.util

import android.view.View
import android.view.ViewGroup
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.annotation.AnimRes
import androidx.annotation.DrawableRes
import androidx.core.content.ContextCompat
import androidx.core.view.isVisible
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar

/**
 * Extension function to show a view with animation
 */
fun View.showWithAnimation(
    @AnimRes animResId: Int,
    startDelay: Long = 0,
    onAnimationEnd: (() -> Unit)? = null
) {
    if (visibility == View.VISIBLE) return
    
    val animation = AnimationUtils.loadAnimation(context, animResId).apply {
        this.startOffset = startDelay
        setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {}
            override fun onAnimationRepeat(animation: Animation?) {}
            override fun onAnimationEnd(animation: Animation?) {
                onAnimationEnd?.invoke()
            }
        })
    }
    
    visibility = View.VISIBLE
    startAnimation(animation)
}

/**
 * Extension function to hide a view with animation
 */
fun View.hideWithAnimation(
    @AnimRes animResId: Int,
    startDelay: Long = 0,
    onAnimationEnd: (() -> Unit)? = null
) {
    if (visibility == View.GONE || visibility == View.INVISIBLE) {
        onAnimationEnd?.invoke()
        return
    }
    
    val animation = AnimationUtils.loadAnimation(context, animResId).apply {
        this.startOffset = startDelay
        setAnimationListener(object : Animation.AnimationListener {
            override fun onAnimationStart(animation: Animation?) {}
            override fun onAnimationRepeat(animation: Animation?) {}
            override fun onAnimationEnd(animation: Animation?) {
                visibility = View.GONE
                onAnimationEnd?.invoke()
            }
        })
    }
    
    startAnimation(animation)
}

/**
 * Extension function to set margin for a view
 */
fun View.setMargins(
    left: Int = marginLeft,
    top: Int = marginTop,
    right: Int = marginRight,
    bottom: Int = marginBottom
) {
    if (layoutParams is ViewGroup.MarginLayoutParams) {
        val params = layoutParams as ViewGroup.MarginLayoutParams
        params.setMargins(left, top, right, bottom)
        layoutParams = params
    }
}

/**
 * Extension function to show keyboard and focus on an EditText
 */
fun EditText.showKeyboard() {
    requestFocus()
    val imm = context.getSystemService(android.content.Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.showSoftInput(this, InputMethodManager.SHOW_IMPLICIT)
}

/**
 * Extension function to hide keyboard
 */
fun View.hideKeyboard() {
    val imm = context.getSystemService(android.content.Context.INPUT_METHOD_SERVICE) as InputMethodManager
    imm.hideSoftInputFromWindow(windowToken, 0)
}

/**
 * Extension function to hide keyboard from Fragment
 */
fun Fragment.hideKeyboard() {
    view?.let { activity?.hideKeyboard() }
}

/**
 * Extension function to hide keyboard from Activity
 */
fun android.app.Activity.hideKeyboard() {
    hideKeyboard(currentFocus ?: View(this))
}

/**
 * Extension function to set a click listener with debounce
 */
fun View.setDebouncedClickListener(debounceTime: Long = 600L, action: () -> Unit) {
    setOnClickListener(object : View.OnClickListener {
        private var lastClickTime: Long = 0
        
        override fun onClick(v: View) {
            if (System.currentTimeMillis() - lastClickTime < debounceTime) return
            lastClickTime = System.currentTimeMillis()
            action()
        }
    })
}

/**
 * Extension function to toggle visibility with optional animation
 */
fun View.toggleVisibility(visible: Boolean, useAnimation: Boolean = true) {
    if (visible == isVisible) return
    
    if (useAnimation) {
        if (visible) {
            showWithAnimation(android.R.anim.fade_in)
        } else {
            hideWithAnimation(android.R.anim.fade_out)
        }
    } else {
        visibility = if (visible) View.VISIBLE else View.GONE
    }
}

/**
 * Extension function to set drawable start for TextView/EditText
 */
fun android.widget.TextView.setDrawableStart(@DrawableRes drawableRes: Int) {
    val drawable = if (drawableRes != 0) {
        ContextCompat.getDrawable(context, drawableRes)
    } else {
        null
    }
    
    setCompoundDrawablesWithIntrinsicBounds(
        drawable,
        compoundDrawables[1],
        compoundDrawables[2],
        compoundDrawables[3]
    )
}

/**
 * Extension function to set drawable end for TextView/EditText
 */
fun android.widget.TextView.setDrawableEnd(@DrawableRes drawableRes: Int) {
    val drawable = if (drawableRes != 0) {
        ContextCompat.getDrawable(context, drawableRes)
    } else {
        null
    }
    
    setCompoundDrawablesWithIntrinsicBounds(
        compoundDrawables[0],
        compoundDrawables[1],
        drawable,
        compoundDrawables[3]
    )
}

/**
 * Extension function to show a snackbar with action
 */
fun View.showSnackbar(
    message: String,
    actionText: String? = null,
    action: (() -> Unit)? = null,
    length: Int = Snackbar.LENGTH_LONG
) {
    val snackbar = Snackbar.make(this, message, length)
    actionText?.let {
        snackbar.setAction(it) { action?.invoke() }
    }
    snackbar.show()
}

/**
 * Extension function to get margin values
 */
val View.marginLeft: Int
    get() = (layoutParams as? ViewGroup.MarginLayoutParams)?.leftMargin ?: 0

val View.marginTop: Int
    get() = (layoutParams as? ViewGroup.MarginLayoutParams)?.topMargin ?: 0

val View.marginRight: Int
    get() = (layoutParams as? ViewGroup.MarginLayoutParams)?.rightMargin ?: 0

val View.marginBottom: Int
    get() = (layoutParams as? ViewGroup.MarginLayoutParams)?.bottomMargin ?: 0

/**
 * Extension function to set padding with start/end for RTL support
 */
fun View.setPaddingRelative(
    start: Int = paddingStart,
    top: Int = paddingTop,
    end: Int = paddingEnd,
    bottom: Int = paddingBottom
) {
    setPaddingRelative(start, top, end, bottom)
}
