package com.example.todomanager.util

import android.content.Context
import android.view.View
import android.view.inputmethod.InputMethodManager
import androidx.annotation.StringRes
import androidx.fragment.app.Fragment
import com.google.android.material.snackbar.Snackbar

/**
 * Extension function to show a Snackbar with the given message
 */
fun View.showSnackbar(
    message: String,
    duration: Int = Snackbar.LENGTH_SHORT,
    action: String? = null,
    actionListener: (() -> Unit)? = null
) {
    val snackbar = Snackbar.make(this, message, duration)
    action?.let { 
        snackbar.setAction(it) { actionListener?.invoke() }
    }
    snackbar.show()
}

/**
 * Extension function to show a Snackbar with the given string resource
 */
fun View.showSnackbar(
    @StringRes messageRes: Int,
    duration: Int = Snackbar.LENGTH_SHORT,
    action: String? = null,
    actionListener: (() -> Unit)? = null
) {
    val snackbar = Snackbar.make(this, messageRes, duration)
    action?.let { 
        snackbar.setAction(it) { actionListener?.invoke() }
    }
    snackbar.show()
}

/**
 * Extension function to hide the keyboard
 */
fun Fragment.hideKeyboard() {
    view?.let { activity?.hideKeyboard(it) }
}

/**
 * Extension function to hide the keyboard
 */
fun Context.hideKeyboard(view: View) {
    val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
}

/**
 * Extension function to show the keyboard
 */
fun Context.showKeyboard(view: View) {
    val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
    inputMethodManager.showSoftInput(view, InputMethodManager.SHOW_IMPLICIT)
}

/**
 * Extension function to show the keyboard
 */
fun Fragment.showKeyboard() {
    view?.let { activity?.showKeyboard(it) }
}

/**
 * Extension function to show a long toast
 */
fun Context.showLongToast(message: String) {
    android.widget.Toast.makeText(this, message, android.widget.Toast.LENGTH_LONG).show()
}

/**
 * Extension function to show a short toast
 */
fun Context.showShortToast(message: String) {
    android.widget.Toast.makeText(this, message, android.widget.Toast.LENGTH_SHORT).show()
}

/**
 * Extension function to show a long toast from a fragment
 */
fun Fragment.showLongToast(message: String) {
    context?.showLongToast(message)
}

/**
 * Extension function to show a short toast from a fragment
 */
fun Fragment.showShortToast(message: String) {
    context?.showShortToast(message)
}

/**
 * Extension function to show a long toast with a string resource
 */
fun Context.showLongToast(@StringRes messageRes: Int) {
    showLongToast(getString(messageRes))
}

/**
 * Extension function to show a short toast with a string resource
 */
fun Context.showShortToast(@StringRes messageRes: Int) {
    showShortToast(getString(messageRes))
}
